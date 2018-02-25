package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.utils.Coordinates;
import ru.nsu.fit.g13205.kushner.utils.Properties;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Konstantin on 14.04.2016.
 */
public class Algorithms {

    private static double[][] MS = {{-1d/6d, 3d/6d, -3d/6d, 1d/6d},
            {3d/6d, -6d/6d, 3d/6d, 0},
            {-3d/6d, 0, 3d/6d, 0},
            {1d/6d, 4d/6d, 1d/6d, 0}};

    public static Spline drawSpline(BufferedImage image, ArrayList<Coordinates> points){
        double xMin = points.get(0).getX(), yMin = points.get(0).getY();
        double xMax = xMin, yMax = yMin;
        int width = image.getWidth(), height = image.getHeight();
        int blackRGB = Color.BLACK.getRGB();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                image.setRGB(x, y, blackRGB);
            }
        }

        for(Coordinates point: points){
            double x = point.getX();
            double y = point.getY();
            xMin = x < xMin ? x : xMin;
            xMax = x > xMax ? x : xMax;
            yMin = y < yMin ? y : yMin;
            yMax = y > yMax ? y : yMax;
        }
        double deltaX = width / (2 * (xMax - xMin + 1));
        double deltaY = height / (2 * (yMax - yMin + 1));

        double delta = deltaX < deltaY ? deltaX : deltaY;//теперь маштабирование будеи идти по более плотной оси
        delta = Math.round(delta);

        drawAxis(image, delta);
        ArrayList<Point> middlePoint = drawBasisPoint(image, points, delta);
        return drawSpline(image, points, delta, middlePoint);
    }

    public static ApproximatedSpline calculateSplinePoints(Spline spline, Properties properties){

        double xMin = 0, xMax = 0, yMin = 0, yMax = 0;
        boolean setXMin = false, setXMax = false, setYMin = false, setYMax = false;
        double startLen = properties.getA() * spline.getLen();
        double stopLen = properties.getB() * spline.getLen();

        int number = properties.getN();
        double deltaLen = (stopLen - startLen) / (number * properties.getK());
        ArrayList<Coordinates> points = spline.getCoordinatesPoints();
        int size = points.size();
        ArrayList<Coordinates> modelCoordinates = new ArrayList<Coordinates>();

        double len = 0;
        double currentBreakPoint = startLen;
        double valueX = 0, valueY = 0;

        for(int i = 1; i < size - 2; i++){
            Coordinates[] GS = new Coordinates[]{points.get(i - 1), points.get(i), points.get(i + 1), points.get(i + 2)};
            Coordinates[] MGS = new Coordinates[4];
            for(int m = 0; m < 4; m++){
                double X = 0, Y = 0;
                for(int n = 0; n < 4; n++){
                    X += MS[m][n] * GS[n].getX();
                    Y += MS[m][n] * GS[n].getY();
                }
                MGS[m] = new Coordinates(X, Y);
            }
            valueX = 0;
            valueY = 0;
            double  nextX, nextY;
            for(double t = 0; t < 1; t+=0.001d){
                valueX = MGS[0].getX() * Math.pow(t, 3) + MGS[1].getX() * Math.pow(t, 2) + MGS[2].getX() * Math.pow(t, 1) + MGS[3].getX();
                valueY = MGS[0].getY() * Math.pow(t, 3) + MGS[1].getY() * Math.pow(t, 2) + MGS[2].getY() * Math.pow(t, 1) + MGS[3].getY();
                nextX = MGS[0].getX() * Math.pow(t + 0.001d, 3) + MGS[1].getX() * Math.pow(t + 0.001d, 2)
                        + MGS[2].getX() * Math.pow(t + 0.001d, 1) + MGS[3].getX();
                nextY = MGS[0].getY() * Math.pow(t + 0.001d, 3) + MGS[1].getY() * Math.pow(t + 0.001d, 2)
                        + MGS[2].getY() * Math.pow(t + 0.001d, 1) + MGS[3].getY();

                len += Math.sqrt(Math.pow(nextX - valueX, 2) + Math.pow(nextY - valueY, 2));

                if(len >= currentBreakPoint){
                    modelCoordinates.add(new Coordinates(valueX, valueY));
                    currentBreakPoint += deltaLen;

                }

                if(len > startLen){
                    if(!setXMin && !setXMax && !setYMin && !setYMax){
                        xMin = xMax = valueX;
                        yMin = yMax = valueY;
                        setXMin = setXMax = setYMin = setYMax = true;
                    }

                    xMin = valueX < xMin ? valueX : xMin;
                    xMax = valueX > xMax ? valueX : xMax;
                    yMin = valueY < yMin ? valueY : yMin;
                    yMax = valueY > yMax ? valueY : yMax;
                }

                if(len > stopLen){
                    break;
                }
            }
        }
        ApproximatedSpline a = new ApproximatedSpline(spline, modelCoordinates, xMin, xMax, yMin, yMax);
        if(modelCoordinates.size() < (properties.getN()) * properties.getK() + 1) {
            modelCoordinates.add(new Coordinates(valueX, valueY));
        }
        return new ApproximatedSpline(spline, modelCoordinates, xMin, xMax, yMin, yMax);
    }

    private static Spline drawSpline(BufferedImage image, ArrayList<Coordinates> points, double delta, ArrayList<Point> middlePoint){
        int size = points.size();
        int width = image.getWidth(), height = image.getHeight();
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(255, 0, 0));
        double xMin = 0, xMax = 0;
        double yMin = 0, yMax = 0;
        boolean set  = false;
        boolean setXMin = false, setXMax = false, setYMin = false, setYMax = false;
        double len = 0;
        for(int i = 1; i < size - 2; i++){
            Coordinates[] GS = new Coordinates[]{points.get(i - 1), points.get(i), points.get(i + 1), points.get(i + 2)};
            Coordinates[] MGS = new Coordinates[4];
            for(int m = 0; m < 4; m++){
                double X = 0, Y = 0;
                for(int n = 0; n < 4; n++){
                    X += MS[m][n] * GS[n].getX();
                    Y += MS[m][n] * GS[n].getY();
                }
                MGS[m] = new Coordinates(X, Y);
            }
            double valueX = 0, valueY = 0, nextX, nextY;
            for(double t = 0; t < 1; t+=0.001d){
                valueX = MGS[0].getX() * Math.pow(t, 3) + MGS[1].getX() * Math.pow(t, 2) + MGS[2].getX() * Math.pow(t, 1) + MGS[3].getX();
                valueY = MGS[0].getY() * Math.pow(t, 3) + MGS[1].getY() * Math.pow(t, 2) + MGS[2].getY() * Math.pow(t, 1) + MGS[3].getY();
                Point point = fromCoordToPixel(new Coordinates(valueX, valueY), width, height, delta);
                nextX = MGS[0].getX() * Math.pow(t + 0.001d, 3) + MGS[1].getX() * Math.pow(t + 0.001d, 2)
                        + MGS[2].getX() * Math.pow(t + 0.001d, 1) + MGS[3].getX();
                nextY = MGS[0].getY() * Math.pow(t + 0.001d, 3) + MGS[1].getY() * Math.pow(t + 0.001d, 2)
                        + MGS[2].getY() * Math.pow(t + 0.001d, 1) + MGS[3].getY();
                if(!set){
                    xMin = xMax = valueX;
                    yMin = yMax = valueY;
                    set = true;
                }

                yMin = valueY < yMin ? valueY : yMin;
                yMax = valueY > yMax ? valueY : yMax;

                xMin = valueX < xMin ? valueX : xMin;
                xMax = valueX > xMax ? valueX : xMax;

                len += Math.sqrt(Math.pow(nextX - valueX, 2) + Math.pow(nextY - valueY, 2));
                image.setRGB((int)point.getX(), (int)point.getY(), new Color(255, 0, 0).getRGB());
            }
        }
        ArrayList<Point> pixelPoint = new ArrayList<Point>();

        for (Coordinates point: points){
            pixelPoint.add(new Point(fromCoordToPixel(point, width, height, delta)));
        }


        return new Spline(points, pixelPoint, middlePoint, xMin, xMax, yMin, yMax, delta, len);
    }

    public static void drawSpline(BufferedImage image, Spline spline){
        ArrayList<Coordinates> points = spline.getCoordinatesPoints();
        double delta = spline.getDelta();
        int size = points.size();
        int width = image.getWidth(), height = image.getHeight();
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(255, 0, 0));
        double xMin = 0, xMax = 0;
        double yMin = 0, yMax = 0;
        boolean set = false;
        double len = 0;
        for(int i = 1; i < size - 2; i++){
            Coordinates[] GS = new Coordinates[]{points.get(i - 1), points.get(i), points.get(i + 1), points.get(i + 2)};
            Coordinates[] MGS = new Coordinates[4];
            for(int m = 0; m < 4; m++){
                double X = 0, Y = 0;
                for(int n = 0; n < 4; n++){
                    X += MS[m][n] * GS[n].getX();
                    Y += MS[m][n] * GS[n].getY();
                }
                MGS[m] = new Coordinates(X, Y);
            }
            double valueX = 0, valueY = 0, nextX, nextY;
            for(double t = 0; t < 1; t+=0.001d){
                valueX = MGS[0].getX() * Math.pow(t, 3) + MGS[1].getX() * Math.pow(t, 2) + MGS[2].getX() * Math.pow(t, 1) + MGS[3].getX();
                valueY = MGS[0].getY() * Math.pow(t, 3) + MGS[1].getY() * Math.pow(t, 2) + MGS[2].getY() * Math.pow(t, 1) + MGS[3].getY();
                Point point = fromCoordToPixel(new Coordinates(valueX, valueY), width, height, delta);
                nextX = MGS[0].getX() * Math.pow(t + 0.001d, 3) + MGS[1].getX() * Math.pow(t + 0.001d, 2)
                        + MGS[2].getX() * Math.pow(t + 0.001d, 1) + MGS[3].getX();
                nextY = MGS[0].getY() * Math.pow(t + 0.001d, 3) + MGS[1].getY() * Math.pow(t + 0.001d, 2)
                        + MGS[2].getY() * Math.pow(t + 0.001d, 1) + MGS[3].getY();
                if(!set){
                    xMin = xMax = valueX;
                    yMin = yMax = valueY;
                    set = true;
                }

                yMin = valueY < yMin ? valueY : yMin;
                yMax = valueY > yMax ? valueY : yMax;

                xMin = valueX < xMin ? valueX : xMin;
                xMax = valueX > xMax ? valueX : xMax;

                len += Math.sqrt(Math.pow(nextX - valueX, 2) + Math.pow(nextY - valueY, 2));
                image.setRGB((int)point.getX(), (int)point.getY(), new Color(255, 0, 0).getRGB());
            }

        }
        ArrayList<Point> pixelPoint = new ArrayList<Point>();

        for (Coordinates point: points){
            pixelPoint.add(new Point(fromCoordToPixel(point, width, height, delta)));
        }

    }

    private static ArrayList<Point> drawBasisPoint(BufferedImage image, ArrayList<Coordinates> points, double delta){
        int width = image.getWidth(), height = image.getHeight();
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(0, 255, 0));

        ArrayList<Point> middlePoints = new ArrayList<Point>();

        for(int i = 0; i < points.size() - 1; i++){
            Point leftPoint = fromCoordToPixel(points.get(i), width, height, delta);
            Point rightPoint = fromCoordToPixel(points.get(i + 1), width, height, delta);
            int middleX = (int) (Math.abs(rightPoint.getX() - leftPoint.getX())/2);
            middleX = (int) (leftPoint.getX() < rightPoint.getX()? leftPoint.getX() + middleX : rightPoint.getX() + middleX);
            int middleY = (int) (Math.abs(rightPoint.getY() - leftPoint.getY())/2);
            middleY = (int) (leftPoint.getY() < rightPoint.getY() ? leftPoint.getY() + middleY : rightPoint.getY() + middleY);
            graphics.drawOval((int)leftPoint.getX() - Settings.BASIC_POINT_SIZE/2, (int)leftPoint.getY() - Settings.BASIC_POINT_SIZE/2,
                    Settings.BASIC_POINT_SIZE, Settings.BASIC_POINT_SIZE);

            graphics.drawOval(middleX - Settings.MIDDLE_POINT_SIZE /2, middleY - Settings.MIDDLE_POINT_SIZE /2,
                    Settings.MIDDLE_POINT_SIZE, Settings.MIDDLE_POINT_SIZE);
            middlePoints.add(new Point(middleX, middleY));
            graphics.drawLine((int)leftPoint.getX(), (int)leftPoint.getY(), (int)rightPoint.getX(), (int)rightPoint.getY());
        }
        Point tmpPoint = fromCoordToPixel(points.get(points.size() - 1), width, height, delta);
        graphics.drawOval((int)tmpPoint.getX() - Settings.BASIC_POINT_SIZE/2, (int)tmpPoint.getY() - Settings.BASIC_POINT_SIZE/2,
                Settings.BASIC_POINT_SIZE, Settings.BASIC_POINT_SIZE);

        return middlePoints;
    }

    private static void drawAxis(BufferedImage image, double delta){
        Graphics graphics = image.getGraphics();
        int width = image.getWidth(), height = image.getHeight();

        graphics.setColor(new Color(255, 255, 255));
        graphics.drawLine(0, height/2, width, height/2);
        graphics.drawLine(width/2, 0, width/2, height);

        for (int i = width/2; i < width; i += delta) {
            graphics.drawLine(i, height / 2 - Settings.TICKS_SIZE, i, height / 2 + Settings.TICKS_SIZE);
        }
        for (int i = width/2; i > 0; i -= delta) {
            graphics.drawLine(i, height / 2 - Settings.TICKS_SIZE, i, height / 2 + Settings.TICKS_SIZE);
        }
        for (int i = height/2; i < height; i += delta) {
            graphics.drawLine(width / 2 - Settings.TICKS_SIZE, i, width / 2 + Settings.TICKS_SIZE, i);
        }
        for (int i = height/2; i > 0; i -= delta) {
            graphics.drawLine(width / 2 - Settings.TICKS_SIZE, i, width / 2 + Settings.TICKS_SIZE, i);
        }
    }

    private static Point fromCoordToPixel(Coordinates point, int width, int height, double delta){
        double x = point.getX(), y = point.getY();
        int pX = (int) (width/2 + x*delta);
        int pY = (int) (height/2 - delta*y);
        return new Point(pX, pY);
    }
}
