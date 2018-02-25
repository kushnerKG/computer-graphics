package ru.nsu.fit.g13205.kushner.algorithms;

import ru.nsu.fit.g13205.kushner.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 11.03.2016.
 */
public class Algorithms {

//кароч тут только для равных сторон
    public static BufferedImage applyRotate(BufferedImage image, int angle){
        int width = image.getWidth();
        int height = image.getHeight();
        double rad = angle * (Math.PI / 180);

        int w = Math.round(Math.round(height * Math.abs(Math.sin(rad)) + width * Math.abs(Math.cos(rad))));
        int h = Math.round(Math.round(width * Math.abs(Math.sin(rad)) + height * Math.abs(Math.cos(rad))));

        w = w > Settings.AREA_WIDTH ? Settings.AREA_WIDTH : w;
        h = h > Settings.AREA_HEIGHT ? Settings.AREA_HEIGHT : h;

        BufferedImage returnImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        int offsetX = image.getWidth() / 2;
        int offsetY = image.getHeight() / 2;


        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                int x1 = x - offsetX;
                int y1 = y - offsetY;
                int newX = Math.round(Math.round (Math.cos(rad) * x1 + Math.sin(rad) * y1));
                int newY = Math.round(Math.round (Math.sin(rad) * (-1) * x1 +  Math.cos(rad) * y1));

                if(newX + offsetX > width || newY + offsetY > height || newX + offsetX < 0 || newY + offsetY < 0){
                    returnImage.setRGB(x, y, Color.WHITE.getRGB());
                    continue;
                }

                if(newX + offsetX == 0 && newY + offsetY == 0){
                    returnImage.setRGB(x, y, image.getRGB(newX + offsetX, newY + offsetY));
                    continue;
                }else if(newX + offsetX == 0){
                    returnImage.setRGB(x, y, image.getRGB(newX + offsetX, newY + offsetY - 1));
                    continue;
                }else if(newY + offsetY == 0){
                    returnImage.setRGB(x, y, image.getRGB(newX + offsetX - 1, newY + offsetY));
                    continue;
                }

                returnImage.setRGB(x, y, image.getRGB(newX + offsetX - 1, newY + offsetY - 1));
            }
        }

        return returnImage;
    }

    public static BufferedImage pixelize(BufferedImage image, int count){

        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage tmpImage = new BufferedImage(width/count, height/count, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < tmpImage.getHeight(); i++){
            for(int j = 0; j < tmpImage.getWidth(); j++){
                int r = 0;
                int g = 0;
                int b = 0;
                for (int m = i * count; m < i * count + count; m++) {
                    for (int n = j * count; n < j * count + count; n++) {
                        r += (image.getRGB(n, m) >> 16) & 0xFF;
                        g += (image.getRGB(n, m) >> 8) & 0xFF;
                        b += image.getRGB(n, m) & 0xFF;
                    }
                }
                r /= count*count;
                g /= count*count;
                b /= count*count;

                int rgb = r<<16 | g<<8 | b;

                tmpImage.setRGB(j, i, rgb);
            }
        }

        return tmpImage;
    }

    public static BufferedImage resizeBilinearGray(BufferedImage image, int newWidth, int newHeight){
        int oldWidth = image.getWidth();
        int oldHeight = image.getHeight();
        BufferedImage returnImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        BufferedImage enlargedImage = new BufferedImage(oldWidth + 1, oldHeight + 1, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < oldHeight; i++){
            for(int j = 0; j < oldWidth; j++){
                enlargedImage.setRGB(j, i, image.getRGB(j,i));
            }
        }

        for (int i = 0; i < oldWidth; i++) {
            enlargedImage.setRGB(i, oldHeight, image.getRGB(i, oldHeight - 1));
        }

        for (int i = 0; i < oldHeight; i++) {
            enlargedImage.setRGB(oldWidth, i, image.getRGB(oldWidth - 1, i));
        }

        enlargedImage.setRGB(oldWidth, oldHeight, image.getRGB(oldWidth - 1, oldHeight - 1));

        double xRatio = (double) oldWidth / (double) newWidth;
        double yRatio = (double) oldHeight / (double) newHeight;

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                double x = xRatio * j;
                double y = yRatio * i;

                int x1 = (int) x;
                int x2 = x1 + 1;
                int y1 = (int) y;
                int y2 = y1 + 1;

                int rgbA = enlargedImage.getRGB(x1, y1);
                int rgbB = enlargedImage.getRGB(x2, y1);
                int rgbC = enlargedImage.getRGB(x1, y2);
                int rgbD = enlargedImage.getRGB(x2, y2);

                int rA = (rgbA>>16) & 0xFF;
                int rB = (rgbB>>16) & 0xFF;
                int rC = (rgbC>>16) & 0xFF;
                int rD = (rgbD>>16) & 0xFF;

                double IR = ((x2 - x) * rA + (x - x1) * rB) * (y2 - y)
                        + ((x2 - x) * rC + (x - x1) * rD) * (y - y1);

                int gA = ((rgbA>>8) & 0xFF);
                int gB = ((rgbB>>8) & 0xFF);
                int gC = ((rgbC>>8) & 0xFF);
                int gD = ((rgbD>>8) & 0xFF);

                double IG = ((x2 - x) * gA + (x - x1) * gB) * (y2 - y)
                        + ((x2 - x) * gC + (x - x1) * gD) * (y - y1);

                int bA = (rgbA & 0xFF);
                int bB = (rgbB & 0xFF);
                int bC = (rgbC & 0xFF);
                int bD = (rgbD & 0xFF);

                double IB = ((x2 - x) * bA + (x - x1) * bB) * (y2 - y)
                        + ((x2 - x) * bC + (x - x1) * bD) * (y - y1);

                returnImage.setRGB(j, i, new Color((int) IR, (int) IG, (int) IB).getRGB());
            }
        }


        return returnImage;
    }


    public static BufferedImage superSampling(BufferedImage image, int newWidth, int newHeight){
        int oWidth = image.getWidth();
        int oHeight = image.getHeight();
        double deltaX = (double)oWidth / (double)newWidth;
        double deltaY = (double)oHeight / (double)newHeight;
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);


        for(int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                double startYArea = (i * deltaY);
                double startXArea = (j * deltaX);
                double finishYArea = ((i + 1) * deltaY);
                double finishXArea = ((j + 1) * deltaX);


                int roundStartYArea = (int)startYArea;
                int roundStartXArea = (int)startXArea;
                int roundFinishYArea = (int)finishYArea;
                int roundFinishXArea = (int)finishXArea;

                if (roundFinishYArea < (i + 1) * deltaY) {
                    roundFinishYArea++;
                }
                if (roundFinishXArea < (j + 1) * deltaX) {
                    roundFinishXArea++;
                }

                int width = roundFinishXArea - roundStartXArea;
                int height = roundFinishYArea - roundStartYArea;
                double[][] points = new double[height][width];

                for(int m = 0; m < height; m++){
                    for(int n = 0; n < width; n++){
                        points[m][n] = 1d;
                    }
                }
                if(roundStartXArea < startXArea){
                    double delta = startXArea - roundStartXArea;

                    for(int m = 0; m < height; m++){
                        points[m][0] -= delta * points[m][0];
                    }
                }
                if(roundFinishXArea > finishXArea){
                    double delta = roundFinishXArea - finishXArea;
                    for(int m = 0; m < height; m++){
                        points[m][width - 1] -= delta * points[m][width - 1];
                    }
                }
                if(roundStartYArea < startYArea){
                    double delta = startYArea - roundStartYArea;
                    for(int m = 0; m < width; m++){
                        points[0][m] -= delta * points[0][m];
                    }
                }
                if(roundFinishYArea > finishYArea){
                    double delta = roundFinishYArea - finishYArea;
                    for(int m = 0; m < width; m++){
                        points[height - 1][m] -= delta * points[height - 1][m];
                    }
                }

                double r = 0;
                double g = 0;
                double b = 0;
                for (int m = roundStartXArea; m < finishXArea; m++) {
                    for (int n = roundStartYArea; n < finishYArea; n++) {
                        try {
                            int RGB = image.getRGB(m, n);
                            Color currentColor = new Color(RGB);
                            double cof = points[n - roundStartYArea][m - roundStartXArea];

                            r += (currentColor.getRed() * cof);
                            g += (currentColor.getGreen() * cof);
                            b += (currentColor.getBlue() * cof);
                        }catch (ArrayIndexOutOfBoundsException ignored){

                        }
                    }
                }

                double s = deltaX * deltaY;
                r = (r / s);
                g = (g / s);
                b = (b / s);
                newImage.setRGB(j, i, ((int)r<<16)|((int)g<<8)|((int)b));

            }
        }
        return newImage;
    }

    public static BufferedImage toBlackWhite(BufferedImage image){
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int rgb = image.getRGB(j, i);
                int tmp = (int) (0.3d * ((rgb >> 16) & 0xFF) + 0.59d * ((rgb >> 8) & 0xFF) + 0.11d * (rgb & 0xFF));
                newImage.setRGB(j, i, (tmp << 16) | (tmp << 8) | (tmp));
            }
        }

        return newImage;
    }

    public static BufferedImage toNegative(BufferedImage image){
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < image.getHeight(); i++){
            for(int j = 0; j < image.getWidth(); j++){
                int rgb = image.getRGB(j, i);
                int r = 255 - (rgb>>16)&0xFF;
                int g = 255 - ((rgb>>8)&0xFF);
                int b = 255 - (rgb&0xFF);
                newImage.setRGB(j, i, r<<16 | g<<8 | b);
            }
        }

        return newImage;
    }

}
