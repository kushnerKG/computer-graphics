package ru.nsu.fit.g13205.kushner.graphics;

import ru.nsu.fit.g13205.kushner.SettingApplication;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by Konstantin on 15.02.2016.
 */
public class MyGraphics {

    public static void drawBresenhamLine(int x0, int y0, int x1, int y1, Graphics g, BufferedImage image, Color targetColor){
        int x, y, dx, dy, upX, upY, error;
        dx = x1 - x0;
        dy = y1 - y0;

        upX = (int)Math.signum(dx);
        upY = (int)Math.signum(dy);

        dx = Math.abs(dx);
        dy = Math.abs(dy);

        x = x0;
        y = y0;
        image.setRGB(x, y, targetColor.getRGB());

        if(dx > dy){
            error = 0;
            for (int i = 0; i < dx; i++){
                error += dy;
                if (2 * error >= dx) {
                    error -= dx;
                    x += upX;
                    y += upY;
                }
                else {
                    x += upX;
                }
                image.setRGB(x, y, targetColor.getRGB());
            }
        }else{
            error = 0;
            for (int i = 0; i < dy; i++){
                error += dx;
                if (2 * error >= dy) {
                    error -= dy;
                    x += upX;
                    y += upY;
                }
                else {
                    y += upY;
                }
                image.setRGB(x, y, targetColor.getRGB());
            }
        }
    }

    public static void spanFill(Point seedPoint, Color targetColor, BufferedImage bufferedImage, int maxWeight, int maxHeight){
        Stack<Point> stack = new Stack<Point>();
        stack.push(seedPoint);
        while(!stack.isEmpty()){
            Point currentPoint = stack.pop();
            int xMin = currentPoint.getX();
            int xMax = currentPoint.getX();
            int y = currentPoint.getY();
            while (bufferedImage.getRGB(xMax, y) != SettingApplication.BORDER_COLOR.getRGB() && xMax < maxWeight - 1){
                bufferedImage.setRGB(xMax, y, targetColor.getRGB());
                xMax++;
            }
            xMax--;
            //bufferedImage.setRGB(xMax, y, SettingApplication.ANOTHER_COLOR.getRGB());
            //xMax--;

            while(bufferedImage.getRGB(xMin, y) != SettingApplication.BORDER_COLOR.getRGB() && xMin > 0){
                bufferedImage.setRGB(xMin, y, targetColor.getRGB());
                xMin--;
            }
            xMin++;
            //bufferedImage.setRGB(xMin, y, SettingApplication.ANOTHER_COLOR.getRGB());
            //xMin++;
            if(y - 1 >= 0) {
                for (int i = xMax; i >= xMin; i--) {
                    int flag = 0;
                    while ((bufferedImage.getRGB(i, y - 1) !=  SettingApplication.BORDER_COLOR.getRGB()) && (i >= xMin) &&
                            bufferedImage.getRGB(i, y - 1) != targetColor.getRGB()){
                        i--;
                        flag = 1;
                    }
                    if (flag == 1 && i >= xMin - 1) {
                        i++;
                        stack.push(new Point(i, y - 1));
                    }
                }
            }

            if(y + 1 < maxHeight) {
                for (int i = xMax; i >= xMin; i--) {
                    int flag = 0;
                    while ((bufferedImage.getRGB(i, y + 1) != SettingApplication.BORDER_COLOR.getRGB()) && (i >= xMin) &&
                            bufferedImage.getRGB(i, y + 1) != targetColor.getRGB() ){
                        i--;
                        flag = 1;
                    }
                    if (flag == 1 && i >= xMin - 1) {
                        i++;
                        stack.push(new Point(i, y + 1));
                    }
                }
            }
        }

    }
}
