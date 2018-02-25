package ru.nsu.fit.g13205.kushner.algorithms;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by Konstantin on 14.03.2016.
 */
public class Filters {

    //public final static double[][] blurMatrix = {{0, 1d/6d, 0}, {1d/6d, 2d/6d, 1d/6d}, {0, 1d/6d, 0}};

    public final static double[][] blurMatrix = {{1d/74d, 2d/74d, 3d/74d, 2d/74d, 1d/74d},
                                                    {2d/74d, 4d/74d, 5d/74d, 4d/74d, 2d/74d}, {3d/74d, 5d/74d, 6d/74d, 5d/74d, 3d/74d},
                                                    {2d/74d, 4d/74d, 5d/74d, 4d/74d, 2d/74d}, {1d/74d, 2d/74d, 3d/74d, 2d/74d, 1d/74d}};

    public final static double[][] sharpenMatrix = {{0, -1d, 0}, {-1d, 5d, -1d}, {0, -1d, 0}};
    public final static double[][] embossMatrix = {{0, 1d, 0}, {-1d, 0d, 1d}, {0, -1d, 0}};

    public static BufferedImage applyGammaCorrectionFilter(BufferedImage image, double gamma){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int rgb = image.getRGB(j, i);
                double r = (rgb>>16) & 0xFF;
                double g = (rgb>>8) & 0xFF;
                double b = rgb & 0xFF;

                r = 255d * Math.pow(r / 255d, 1d/gamma);
                g = 255d * Math.pow(g / 255d, 1d/gamma);
                b = 255d * Math.pow(b / 255d, 1d/gamma);

                rgb = (int)r<<16|(int)g<<8|(int)b;
                returnImage.setRGB(j, i, rgb);
            }
        }

        return returnImage;
    }

    public static BufferedImage applyBlurFilter(BufferedImage image){
        return applyFilterMatrix(image, blurMatrix, 0);
    }

    public static BufferedImage applySharpenFilter(BufferedImage image){
        return applyFilterMatrix(image, sharpenMatrix, 0);
    }

    public static BufferedImage applyRobertsFilter(BufferedImage image, int threshold){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage enlargedImage = new BufferedImage(width + 1, height + 1, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                enlargedImage.setRGB(j, i, image.getRGB(j, i));
            }
        }

        for(int i = 0; i < height; i++){
            enlargedImage.setRGB(width, i, image.getRGB(width - 1, i));
        }
        for(int i = 0; i < width; i++){
            enlargedImage.setRGB(i, height, image.getRGB(i, height - 1));
        }
        enlargedImage.setRGB(width, height, image.getRGB(width - 1, height - 1));
        enlargedImage = Algorithms.toBlackWhite(enlargedImage);
        int Gx;
        int Gy;
        double G;
        int colorWhite = Color.WHITE.getRGB();
        int colorBlack = Color.BLACK.getRGB();
        for(int i = 1; i < height; i++){
            for(int j = 1; j < width; j++){
                Gx = (enlargedImage.getRGB(j + 1, i + 1)&0xFF) - (enlargedImage.getRGB(j, i)&0xFF);
                Gy = (enlargedImage.getRGB(j, i + 1)&0xFF) - (enlargedImage.getRGB(j + 1, i)&0xFF);
                G = Math.sqrt(Gx * Gx + Gy * Gy);
                if(G > threshold){
                    returnImage.setRGB(j, i, colorWhite);
                }else{
                    returnImage.setRGB(j, i, colorBlack);
                }
            }
        }

        return returnImage;
    }

    public static BufferedImage applySobelFilter(BufferedImage image, int threshold){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage greyImage = Algorithms.toBlackWhite(image);
        BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage enlargedImage = getEnlargedImage(greyImage, 1, 1);

        int Sx;
        int Sy;
        double S;
        int whiteRgb = Color.WHITE.getRGB();
        int blackRgb = Color.BLACK.getRGB();

        try {
            for (int i = 1; i < height + 1; i++) {
                for (int j = 1; j < width; j++) {
                    Sx = -(enlargedImage.getRGB(j - 1, i - 1) & 0xFF) + (enlargedImage.getRGB(j + 1, i - 1) & 0xFF) -
                            (enlargedImage.getRGB(j - 1, i) & 0xFF) * 2 + (enlargedImage.getRGB(j + 1, i) & 0xFF) * 2 -
                            (enlargedImage.getRGB(j - 1, i + 1) & 0xFF) + (enlargedImage.getRGB(j + 1, i + 1) & 0xFF);

                    Sy = -(enlargedImage.getRGB(j - 1, i - 1) & 0xFF) - (enlargedImage.getRGB(j, i - 1) & 0xFF) * 2 -
                            (enlargedImage.getRGB(j + 1, i - 1) & 0xFF) +
                            (enlargedImage.getRGB(j - 1, i + 1) & 0xFF) + (enlargedImage.getRGB(j, i + 1) & 0xFF) * 2 +
                            (enlargedImage.getRGB(j + 1, i + 1) & 0xFF);
                    S = Math.sqrt(0.25d * 0.25d * Sx * Sx + 0.25d * 0.25d * Sy * Sy);
                    if (S > threshold) {
                        returnImage.setRGB(j - 1, i - 1, whiteRgb);
                    } else {
                        returnImage.setRGB(j - 1, i - 1, blackRgb);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return returnImage;
    }

    public static BufferedImage applyWatercolorFilter(BufferedImage image){
        try {
            return applyFilterMatrix(applyMedianFilter(image), sharpenMatrix, 0);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage applyMedianFilter(BufferedImage image){
        int widthDelta = 2;
        int heightDelta = 2;

        BufferedImage enlargedImage = getEnlargedImage(image, widthDelta, heightDelta);
        BufferedImage returnImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);


        for(int i = 0; i < image.getHeight(); i++){
            for(int j = 0; j < image.getWidth(); j++){
                int rgb = getMiddleRGB(enlargedImage, widthDelta, heightDelta, j + widthDelta, i + heightDelta);
                int r = rgb>>16;
                int g = (rgb>>8)&0xFF;
                int b = (rgb)&0xFf;
                returnImage.setRGB(j, i, rgb);
            }
        }

        return returnImage;
    }

    private static int getMiddleRGB(BufferedImage image, int widthDelta, int heightDelta, int x, int y){
        class Sort implements Comparator<Integer> {
            public int compare(Integer a, Integer b) {
                return a - b;
            }
        }
        LinkedList<Integer> rSet = new LinkedList<Integer>();
        LinkedList<Integer> gSet = new LinkedList<Integer>();
        LinkedList<Integer> bSet = new LinkedList<Integer>();

        for(int i = y - heightDelta; i < y + heightDelta + 1; i++){
            for(int j = x - widthDelta; j < x + widthDelta + 1; j++){
                int rgb = image.getRGB(j, i);
                int r = (rgb>>16)&0xFF;
                int g = (rgb>>8)&0xFF;
                int b = (rgb)&0xFF;
                rSet.add(r);
                gSet.add(g);
                bSet.add(b);
            }
        }

        Collections.sort(rSet, new Sort());
        Collections.sort(gSet, new Sort());
        Collections.sort(bSet, new Sort());

        return (rSet.get(13)<<16) | (gSet.get(13)<<8) | (bSet.get(13));
    }

    public static BufferedImage applyEmbossFilter(BufferedImage image){
        BufferedImage embossImage = applyFilterMatrix(image, embossMatrix, 128);
        embossImage = Algorithms.toBlackWhite(embossImage);
/*        for(int i = 0; i < embossImage.getHeight(); i++){
            for(int j = 0; j < embossImage.getWidth(); j++){
                int rgb = embossImage.getRGB(j, i);
                int r = (rgb>>16)&0xFF;
                int g = (rgb>>8)&0xFF;
                int b = rgb&0xFF;

                r = (r+128)>255 ? 255 : (r+128);
                g = (g+128)>255 ? 255 : (g+128);
                b = (b+128)>255 ? 255 : (b+128);

                embossImage.setRGB(j, i, (r << 16) | (g << 8) | b);
            }
        }*/
        return embossImage;
    }

    private static BufferedImage applyFilterMatrix(BufferedImage image, double[][] matrix, int offset){
        int deltaWidth = (matrix[0].length/2);
        int deltaHeight = (matrix.length/2);
        BufferedImage enlargedImage = getEnlargedImage(image, deltaWidth, deltaHeight);
        BufferedImage newImage = new BufferedImage(enlargedImage.getWidth(), enlargedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage returnImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        try{
            for(int i = deltaHeight; i < enlargedImage.getHeight() - deltaHeight; i++){
                for(int j = deltaWidth; j < enlargedImage.getWidth() - deltaWidth; j++){
                    double r = 0;
                    double g = 0;
                    double b = 0;
                    for(int m = 0; m < matrix.length; m++){//height
                        for(int n = 0; n < matrix[m].length; n++){//width
                            int rgb = enlargedImage.getRGB(j - deltaWidth + n , i - deltaHeight + m);
                            r += ((rgb>>16)&0xFF) * matrix[m][n];
                            g += ((rgb>>8)&0xFF) * matrix[m][n];
                            b += ((rgb)&0xFF) * matrix[m][n];
                        }
                    }

                    r = (r+offset)>255 ? 255 : (r+offset);
                    g = (g+offset)>255 ? 255 : (g+offset);
                    b = (b+offset)>255 ? 255 : (b+offset);

                    r = r<0 ? 0 : r;
                    g = g<0 ? 0 : g;
                    b = b<0 ? 0 : b;

                    newImage.setRGB(j, i, (int)r<<16|(int)g<<8|(int)b );
                }
            }


            for(int i = 0; i < returnImage.getHeight(); i++){
                for(int j = 0; j < returnImage.getWidth(); j++){
                    returnImage.setRGB(j, i, newImage.getRGB(j + deltaWidth, i + deltaHeight));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnImage;
    }

    public static BufferedImage getEnlargedImage(BufferedImage image, int deltaWidth, int deltaHeight){
        BufferedImage enlargedImage = new BufferedImage(image.getWidth() + deltaWidth*2, image.getHeight() + deltaHeight*2,
                BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < enlargedImage.getHeight(); i++) {
            if (i >= deltaHeight && i < enlargedImage.getHeight() - deltaHeight) {

                for (int j = 0; j < enlargedImage.getWidth(); j++) {
                    if (j >= deltaWidth && j < enlargedImage.getWidth() - deltaWidth) {
                        enlargedImage.setRGB(j, i, image.getRGB(j - deltaWidth, i - deltaHeight));
                    } else {
                        if (j < deltaWidth) {
                            enlargedImage.setRGB(j, i, image.getRGB(0, i - deltaHeight));
                        } else {
                            enlargedImage.setRGB(j, i, image.getRGB(image.getWidth() - 1, i - deltaHeight));
                        }
                    }
                }
            } else {
                if (i < deltaHeight) {
                    for (int j = 0; j < enlargedImage.getWidth(); j++) {
                        if (j >= deltaWidth && j < enlargedImage.getWidth() - deltaWidth) {
                            enlargedImage.setRGB(j, i, image.getRGB(j - deltaWidth, 0));
                        } else {
                            if (j < deltaWidth) {
                                enlargedImage.setRGB(j, i, image.getRGB(0, 0));
                            } else {
                                enlargedImage.setRGB(j, i, image.getRGB(image.getWidth() - 1, 0));
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < enlargedImage.getWidth(); j++) {
                        if (j >= deltaWidth && j < enlargedImage.getWidth() - deltaWidth) {
                            enlargedImage.setRGB(j, i, image.getRGB(j - deltaWidth, image.getHeight() - 1));
                        } else {
                            if (j < deltaWidth) {
                                enlargedImage.setRGB(j, i, image.getRGB(0, image.getHeight() - 1));
                            } else {
                                enlargedImage.setRGB(j, i, image.getRGB(image.getWidth() - 1, image.getHeight() - 1));
                            }
                        }
                    }
                }

            }
        }

        return enlargedImage;
    }
}
