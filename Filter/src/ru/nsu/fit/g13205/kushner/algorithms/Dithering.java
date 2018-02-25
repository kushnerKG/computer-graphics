package ru.nsu.fit.g13205.kushner.algorithms;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 17.03.2016.
 */
public class Dithering {

    private static int[][] thresholdMatrix = {{0, 8, 2, 10}, {12, 4, 14, 6}, {3, 11, 1, 9}, {15, 7, 13, 5}};

    public static BufferedImage applyOrderedDithering(BufferedImage image, int red, int green, int blue) {
        int width = image.getWidth();
        int height = image.getHeight();
        double cof = 1d/17d;

        int rows;
        int columns;

        double ratioR = 255d/(red - 1);
        double ratioG = 255d/(green - 1);
        double ratioB = 255d/(blue - 1);

        BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rows = i % 4;
                columns = j % 4;

                int rgb = image.getRGB(j, i);
                int r = (rgb>>16)&0xFF;
                int g = (rgb>>8)&0xFF;
                int b = (rgb)&0xFF;

                double threshold = cof * thresholdMatrix[rows][columns];

                r = (r < (ratioR * threshold)) ? (int) (r - ratioR/2) : (int) (r + ratioR/2);
                g = (g < (ratioG * threshold)) ? (int) (g - ratioG/2) : (int) (g + ratioG/2);
                b = (b < (ratioB * threshold)) ? (int) (b - ratioB/2) : (int) (b + ratioB/2);

                r = r > 255 ? 255 : r;
                g = g > 255 ? 255 : g;
                b = b > 255 ? 255 : b;

                r = r < 0 ? 0 : r;
                g = g < 0 ? 0 : g;
                b = b < 0 ? 0 : b;

                int newR = roundColor(red, r);
                int newG = roundColor(green, g);
                int newB = roundColor(blue, b);

                int newRGB = ((newR&0xFF)<<16) | ((newG&0xFF)<<8) | (newB&0xFF);
                returnImage.setRGB(j, i, newRGB);
            }
        }
        return returnImage;
    }

    public static BufferedImage applyOrderedDitheringFromWiki(BufferedImage image, int red, int green, int blue ){
        int width = image.getWidth();
        int height = image.getHeight();
        double cof = 1d/17d;

        BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int rows;
        int columns;

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                rows = i % 4;
                columns = j % 4;

                double threshold = cof * thresholdMatrix[rows][columns];
                int rgb = image.getRGB(j, i);

                int r = (rgb>>16) & 0xFF;
                int g = (rgb>>8) & 0xFF;
                int b = rgb & 0xFF;

                int oldR = (int) (r + r * threshold);
                int oldG = (int) (g + g * threshold);
                int oldB = (int) (b + b * threshold);

                oldR = oldR > 255 ? 255 : oldR;
                oldG = oldG > 255 ? 255 : oldG;
                oldB = oldB > 255 ? 255 : oldB;

                oldR = oldR < 0 ? 0 : oldR;
                oldG = oldG < 0 ? 0 : oldG;
                oldB = oldB < 0 ? 0 : oldB;

                int newR = roundColor(red, oldR);
                int newG = roundColor(green, oldG);
                int newB = roundColor(blue, oldB);

                int newRGB = ((newR&0xFF)<<16) | ((newG&0xFF)<<8) | (newB&0xFF);
                returnImage.setRGB(j, i, newRGB);

            }
        }


        return returnImage;
    }

    public static BufferedImage applyFloydSteinberg(BufferedImage image, int red, int green, int blue ){
        int oldR;
        int newR;
        int redError;

        int oldG;
        int newG;
        int greenError;

        int oldB;
        int newB;
        int blueError;


        BufferedImage tmpImage = new BufferedImage(image.getWidth() + 2, image.getHeight() + 2, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < image.getHeight(); i++){
            for(int j = 0; j < image.getWidth(); j++){
                tmpImage.setRGB(j + 1, i + 1, image.getRGB(j, i));
            }
        }

        BufferedImage returnImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < image.getHeight(); i++){
            for(int j = 0; j < image.getWidth(); j++){
                int x = j + 1;
                int y = i + 1;
                int rgb = tmpImage.getRGB(x, y);
                oldR = (rgb>>16) & 0xFF;
                oldG = (rgb>>8) & 0xFF;
                oldB = rgb & 0xFF;
                newR = roundColor(red, oldR);
                newG = roundColor(green, oldG);
                newB = roundColor(blue, oldB);
                int newRGB = ((newR&0xFF)<<16) | ((newG&0xFF) <<8) | (newB&0xFF);



                redError = oldR - newR;
                greenError = oldG - newG;
                blueError = oldB - newB;

                tmpImage.setRGB(x + 1, y, getNewRgb(tmpImage.getRGB(x + 1, y), redError, greenError, blueError, 7d/16d));
                tmpImage.setRGB(x - 1, y + 1, getNewRgb(tmpImage.getRGB(x - 1, y + 1), redError, greenError, blueError, 3d/16d));
                tmpImage.setRGB(x, y + 1, getNewRgb(tmpImage.getRGB(x, y + 1), redError, greenError, blueError, 5d/16d));
                tmpImage.setRGB(x + 1, y + 1, getNewRgb(tmpImage.getRGB(x + 1, y + 1), redError, greenError, blueError, 1d/16d));

                tmpImage.setRGB(j, i, newRGB);
            }
        }

        for(int i = 0; i < image.getHeight(); i++){
            for(int j = 0; j < image.getWidth(); j++){
                returnImage.setRGB(j, i, tmpImage.getRGB(j, i));
            }
        }

        return returnImage;
    }

    private static int roundColor(int count, int color){
        int step = (int)(255d/(count-1));
        int start = (color / step);
        start *= step;
        int finish = start + step;

        if((color - start) < (finish - color)){
            return start;
        }else{
            return finish;
        }
    }

    private static int getNewRgb(int oldRgb, int redError, int greenError, int blueError, double ratio){
        double deltaR = redError * ratio;
        double deltaG = greenError * ratio;
        double deltaB = blueError * ratio;
        int oldR = (oldRgb >> 16) & 0xFF;
        int oldG = (oldRgb >> 8) & 0xFF;
        int oldB = oldRgb & 0xFF;
        int newR = (int)(oldR + deltaR) > 255 ? 255 : (int)(oldR + deltaR);
        int newG = (int)(oldG + deltaG) > 255 ? 255 : (int)(oldG + deltaG);
        int newB = (int)(oldB + deltaB) > 255 ? 255 : (int)(oldB + deltaB);

        newR = newR < 0 ? 0 : newR;
        newG = newG < 0 ? 0 : newG;
        newB = newB < 0 ? 0 : newB;

        return ((newR&0xFF)<<16)|((newG&0xFF)<<8)|newB&0xFF;

    }



}
