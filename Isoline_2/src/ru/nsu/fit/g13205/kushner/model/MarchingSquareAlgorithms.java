package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.utils.FileInfo;
import ru.nsu.fit.g13205.kushner.utils.IFunction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Konstantin on 01.04.2016.
 */
public class MarchingSquareAlgorithms {

    public static void drawIsoline(double isolineValue, BufferedImage image, double A, double B, double C, double D,
                                    IFunction function, FileInfo info){
        Graphics graphics = image.getGraphics();
        graphics.setColor(info.getColorBorder());
        double deltaX = (B - A)/(info.getK() - 1);
        double deltaY = (D - C)/(info.getM() - 1);
        double f1;
        double f2;
        double f3;
        double f4;
        for (double x = A; x < B; x += deltaX) {
            for (double y = C; y < D; y += deltaY) {
                Point p1, p2, p3, p4;
                int count;
                ArrayList<Boundary> points = new ArrayList<Boundary>();
                f1 = function.getValue(x, y);//z(i,j)
                f2 = function.getValue(x + deltaX, y);//z(i+1, j)
                f3 = function.getValue(x, y + deltaY);//z(i, j+1)
                f4 = function.getValue(x + deltaX, y + deltaY);//z(i+1, j+1)
                Point  p;

                if((p = xCheck(f1, f2, isolineValue, x, y, A, B, C, D, image.getWidth(), image.getHeight(), info)) != null){
                    points.add(new Boundary(p, Sides.BOTTOM));
                }
                if((p = xCheck(f3, f4, isolineValue, x, y + deltaY, A, B, C, D, image.getWidth(), image.getHeight(), info)) != null){
                    points.add(new Boundary(p, Sides.TOP));
                }
                if((p = yCheck(f1, f3, isolineValue, x, y, A, B, C, D, image.getWidth(), image.getHeight(), info)) != null){
                    points.add(new Boundary(p, Sides.LEFT));
                }
                if((p = yCheck(f2, f4, isolineValue, x + deltaX, y,  A, B, C, D, image.getWidth(), image.getHeight(), info)) != null){
                    points.add(new Boundary(p, Sides.RIGHT));
                }

                count = points.size();
                switch (count) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2: {
                        p1 = points.get(0).getPoint();
                        p2 = points.get(1).getPoint();
                        graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                        break;
                    }
                    case 3: {
                        Boundary b1 = points.get(0);
                        Boundary b2 = points.get(1);
                        Boundary b3 = points.get(2);
                        if(check(b1, b2) || check(b2, b1)){
                            p1 = b1.getPoint();
                            p2 = b2.getPoint();
                            graphics.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
                        }
                        if(check(b1, b3) || check(b3, b1)){
                            p1 = b1.getPoint();
                            p2 = b3.getPoint();
                            graphics.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
                        }
                        if(check(b2, b3) || check(b3, b2)){
                            p1 = b2.getPoint();
                            p2 = b3.getPoint();
                            graphics.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
                        }
                        break;
                    }
                    case 4: {
                        p1 = points.get(0).getPoint();
                        p2 = points.get(1).getPoint();
                        p3 = points.get(2).getPoint();
                        p4 = points.get(3).getPoint();
                        double average = (f1 + f2 + f3 + f4) / 4;

                        if(average >= isolineValue ){
                            if(f3 >= isolineValue && f2 >= isolineValue && f1 < isolineValue && f4 < isolineValue) {
                                graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p3.getX(), (int) p3.getY());
                                graphics.drawLine((int) p2.getX(), (int) p2.getY(), (int) p4.getX(), (int) p4.getY());
                            }else{
                                graphics.drawLine((int) p2.getX(), (int) p2.getY(), (int) p3.getX(), (int) p3.getY());
                                graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p4.getX(), (int) p4.getY());
                            }

                        }else{
                            if(f3 >= isolineValue && f2 >= isolineValue && f1 < isolineValue && f4 < isolineValue) {
                                graphics.drawLine((int) p2.getX(), (int) p2.getY(), (int) p3.getX(), (int) p3.getY());
                                graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p4.getX(), (int) p4.getY());
                            }else{
                                graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p3.getX(), (int) p3.getY());
                                graphics.drawLine((int) p2.getX(), (int) p2.getY(), (int) p4.getX(), (int) p4.getY());
                            }
                        }

                        break;
                    }
                }

            }
        }
    }

    private static boolean check(Boundary b1, Boundary b2){
        if(b1.getSide() == Sides.BOTTOM && b2.getSide() == Sides.RIGHT){
            return true;
        }
        if(b1.getSide() == Sides.RIGHT && b2.getSide() == Sides.TOP){
            return true;
        }
        if(b1.getSide() == Sides.TOP && b2.getSide() == Sides.LEFT){
            return true;
        }
        if(b1.getSide() == Sides.LEFT && b2.getSide() == Sides.BOTTOM){
            return true;
        }
        return false;
    }

    private static Point xCheck(double leftF1, double rightF2, double isolineValue, double xI, double y, double A, double B, double C, double D,
                        int width, int height, FileInfo info){
        double deltaX = (B - A)/(info.getK() - 1);
        double xx;
        double yy;
        if(leftF1 < isolineValue && rightF2 > isolineValue){
            xx = xI + deltaX * (isolineValue - leftF1)/Math.abs(leftF1 - rightF2);
            yy = y;
            return fromCoordinatesToPixel(xx, yy, width, height, A, B, C, D);
        }else if(leftF1 > isolineValue && rightF2 < isolineValue){
            xx = xI + deltaX * (leftF1 - isolineValue)/Math.abs(leftF1 - rightF2);
            yy = y;
            Point point = fromCoordinatesToPixel(xx, yy, width, height, A, B, C, D);
            return point;
        }else{
            return  null;
        }
    }

    private static Point yCheck(double leftF1, double rightF2, double isolineValue, double x, double yJ, double A, double B, double C, double D,
                        int width, int height, FileInfo info){
        double deltaY = (D - C)/(info.getM() - 1);
        double xx;
        double yy;
        if(leftF1 < isolineValue && rightF2 > isolineValue){
            xx = x;
            yy = yJ + deltaY * (isolineValue - leftF1)/Math.abs(leftF1 - rightF2);
            return fromCoordinatesToPixel(xx, yy, width, height, A, B, C, D);
        }else if(leftF1 > isolineValue && rightF2 < isolineValue){
            xx = x;
            yy = yJ + deltaY * (leftF1 - isolineValue)/Math.abs(leftF1 - rightF2);
            Point point = fromCoordinatesToPixel(xx, yy, width, height, A, B, C, D);
            return point;
        }else {
            return  null;
        }
    }

    private  static Point fromCoordinatesToPixel(double x, double y, int width, int height, double A, double B, double C, double D){
        int pX;
        int pY;

        pX = (int) (width * (x - A)/(B - A) + 0.5);
        pY = height -  (int) (height * (y - C)/(D - C) + 0.5);

        return new Point(pX, pY);
    }

}
