package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.utils.FileInfo;
import ru.nsu.fit.g13205.kushner.utils.Function;
import ru.nsu.fit.g13205.kushner.view.MainArea;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class Model {

    private ArrayList<ModelListener> listeners = new ArrayList<>();
    private FileInfo info;
    private double minZ;
    private double maxZ;
    private double A = Function.A;
    private double B = Function.B;
    private double C = Function.C;
    private double D = Function.D;
    private int offsetLegendX = Settings.OFFSET_LEGEND_X;
    private int offsetY = Settings.OFFSET_LEGEND_Y;
    private int widthLegend = Settings.WIDTH_LEGEND;
    private int heightLegend = Settings.HEIGHT_LEGEND;
    private int widthImage = Settings.WIDTH_IMAGE;
    private int heightImage = Settings.HEIGHT_IMAGE;
    private int widthMap = Settings.WIDTH_MAP;
    private int heightMap = Settings.HEIGHT_MAP;
    private int offsetX = 0;

    public final double ratioWidthMap = 0.8;
    public final double ratioHeightMap = 0.95d;
    public final double ratioHeightLegend = 0.95d;
    public final double ratioWidthLegend = 0.1;
    public final double ratioOffsetLegend = 0.05;
    public final double ratioOffsetX = 0.025d;
    public final double ratioOffsetY = 0.025d;

    public boolean isPrinted;

    private BufferedImage image;
    private BufferedImage mapImage;
    private BufferedImage gridImage;
    private BufferedImage legendImage;
    private BufferedImage isolineImage;

    public boolean showGrid = false;
    public boolean showMap = false;
    public boolean showLegend = false;
    public boolean showIsoline = false;

    public boolean interactiveMode = false;

    private MainArea mainArea;

    public Model(MainArea mainArea) {
        this.mainArea = mainArea;
        calculateMinAndMax();
    }

    public void handleNewFieldSize(int width, int height){
        widthImage = width;
        heightImage = height;
        offsetLegendX = (int) (ratioOffsetLegend * width);
        offsetY = (int) (ratioOffsetY*height);
        widthLegend = (int) (ratioWidthLegend * width);
        heightLegend = (int) (ratioHeightLegend * height);
        widthMap = (int) (ratioWidthMap * width);
        heightMap = (int) (ratioHeightMap * height);

        offsetX = (int)(ratioOffsetX * width);

        image = new BufferedImage(widthImage, heightImage, BufferedImage.TYPE_INT_ARGB);

        if(showGrid){
            createGridImage();
        }
        if(showMap){
            createMapImage();
        }
        if(showLegend){
            createLegendImage();
        }

        rendering();

    }

    public void rendering(){
        Graphics g = image.getGraphics();
        int rgb = new Color(255, 255, 255, 0).getRGB();
        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                image.setRGB(x, y, rgb);
            }
        }
        g.setColor(new Color(0, 0, 0));
        g.drawLine(offsetX - 1, offsetY - 1, offsetX - 1, offsetY + heightMap);
        g.drawLine(offsetX + widthMap, offsetY - 1, offsetX + widthMap, offsetY + heightMap);
        g.drawLine(offsetX - 1, offsetY - 1, offsetX + widthMap, offsetY - 1);
        g.drawLine(offsetX - 1, offsetY + heightMap, offsetX + widthMap, offsetY + heightMap);

        g.drawLine(widthMap + offsetLegendX + offsetX - 1, offsetY - 1, widthMap + offsetLegendX + offsetX - 1, offsetY + heightLegend);
        g.drawLine(widthMap + offsetLegendX + offsetX - 1, offsetY - 1, widthMap + offsetLegendX + offsetX + widthLegend, offsetY - 1);
        g.drawLine(widthMap + offsetLegendX + offsetX + widthLegend, offsetY - 1,
                widthMap + offsetLegendX + offsetX + widthLegend, offsetY + heightLegend);
        g.drawLine(widthMap + offsetLegendX + offsetX - 1, offsetY + heightLegend, widthMap + offsetLegendX + offsetX + widthLegend, offsetY + heightLegend);

        if(showMap){
            g.drawImage(mapImage, offsetX, offsetY, null);
            g.drawImage(legendImage, offsetX + widthMap + offsetLegendX, offsetY, null);
        }

        if(showGrid){
            g.drawImage(gridImage, offsetX, offsetY, null);
        }

        if(showIsoline){
            g.drawImage(isolineImage, offsetX, offsetY, null);
        }

        for (ModelListener listener : listeners) {
            int xStart, xFinish, yStart, yFinish;
            xStart = offsetX;
            xFinish = offsetX + widthMap;
            yStart = offsetY;
            yFinish = offsetY + heightMap;
            listener.updateImage(image, new java.awt.Point(xStart, yStart), new java.awt.Point(xFinish, yFinish));
        }


    }

    public void handleNewSetting(FileInfo info, double a, double b, double c, double d){
        A = a;
        B = b;
        C = c;
        D = d;
        this.info = info;
        createLegendImage();
        createMapImage();
        isPrinted = true;
        showMap = true;
        showLegend = true;
        if(showGrid){
            createGridImage();
        }
        if(showIsoline){
            handleSelectDrawIsoline();
        }
        rendering();
    }


    public void handleFileOpen(FileInfo info){

        this.info = info;

        try {
            createLegendImage();
            createMapImage();
        }catch (Exception e){
            e.printStackTrace();
        }

        isPrinted = true;
        showMap = true;
        showLegend = true;
        rendering();
    }

    private void createMapImage(){
        mapImage = new BufferedImage(widthMap, heightMap, BufferedImage.TYPE_INT_ARGB);
        RealPoint point;
        double value = 0;
        double deltaZ = (maxZ - minZ) / info.getColorsOfLegend().size();
        for(int y = 0; y < heightMap; y++){
            for(int x = 0; x < widthMap; x++){

                point = fromPixelToCoordinates(x, y);
                value = Function.getValue(point.getX(), point.getY());
                int level = (int) ((value - minZ) / deltaZ);
                if (level == info.getColorsOfLegend().size()) {
                    level--;
                }
                mapImage.setRGB(x, y, info.getColorsOfLegend().get(info.getColorsOfLegend().size() - level - 1).getRGB());

            }
        }
    }

    private void createLegendImage(){

        legendImage = new BufferedImage(widthLegend, heightLegend, BufferedImage.TYPE_INT_ARGB);
        ArrayList<Color> colorLegend = info.getColorsOfLegend();

        double deltaY = (maxZ - minZ) / heightLegend;
        double deltaZ = (maxZ - minZ) / colorLegend.size();
        for(int pY = 0; pY < heightLegend; pY++){
            double y = (minZ + (heightLegend - pY ) * deltaY);
            double value = Function.getValueLegendFunction(y);
            for(int pX = 0; pX < widthLegend; pX++){
                try {

                    int level = (int) ((value - minZ) / deltaZ);
                    if (level == info.getColorsOfLegend().size()) {
                        level--;
                    }
                    legendImage.setRGB(pX, pY, colorLegend.get(colorLegend.size() - level - 1).getRGB());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void calculateMinAndMax(){
        RealPoint point = fromPixelToCoordinates(0, 0);
        minZ = Function.getValue(point.getX(), point.getY());
        maxZ = minZ;
        int width = widthMap;
        int height = heightMap;
        double value;
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                point = fromPixelToCoordinates(x, y);
                value = Function.getValue(point.getX(), point.getY());
                minZ = value < minZ ? value : minZ;
                maxZ = value > maxZ ? value : maxZ;
            }
        }
    }

    private RealPoint fromPixelToCoordinates(int pX, int pY){

        double x = (B - A)*pX/widthMap + A;
        double y = (D - C)*(heightMap - pY)/heightMap + C;

        return new RealPoint(x, y);
    }



    public void subscribe(ModelListener listener){
        listeners.add(listener);
    }



    public void handleCoordinates(int pixelX, int pixelY){
        if(isPrinted) {
            double deltaX = (B - A) / (double) widthMap;
            double deltaY = (D - C) / (double) heightMap;
            double x = A + (pixelX - offsetX) * deltaX;
            double y = C + (heightMap - pixelY + offsetY) * deltaY;


            for (ModelListener listener : listeners) {

                listener.updateCoordinates(x, y);
            }
        }
    }

    public void handleSelectDrawGrid(){
        createGridImage();
        showGrid = true;
        rendering();
    }

    private void createGridImage(){
        gridImage = new BufferedImage(widthMap, heightMap, BufferedImage.TYPE_INT_ARGB);
        float[] dashl = {1, 4};
        Graphics2D gridImageGraphics = (Graphics2D)gridImage.getGraphics();
        BasicStroke pen = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10, dashl, 0);
        gridImageGraphics.setStroke(pen);
        double deltaX = (double)(widthMap + 1) / (double)info.getK();
        double deltaY = (double)(heightMap + 1) / (double)info.getM();
        Color color = new Color(0, 0, 0, 255);

        gridImageGraphics.setColor(color);

        for (double x = (int)deltaX; x < widthMap; x += deltaX) {
            gridImageGraphics.drawLine((int)x, 0, (int)x, heightMap);
        }

        gridImageGraphics.setColor(new Color(255, 255, 255, 255));

        for (double y = (int)deltaY; y < heightMap; y += deltaY) {
            gridImageGraphics.drawLine(0, (int)y, widthMap, (int)y);
        }
    }

    public void handleUnSelectDrawGrid(){
        showGrid = false;
        rendering();
    }


    public void handleSelectDrawIsoline(){
        isolineImage = new BufferedImage(widthMap, heightMap, BufferedImage.TYPE_INT_ARGB);
        int isolineCount = info.getColorsOfLegend().size();
        try {
            for (double isolineValue = minZ; isolineValue < maxZ; isolineValue += (maxZ - minZ) / isolineCount) {
                drawIsoline(isolineValue);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        showIsoline = true;
        rendering();
    }

    public void handleClickOnMap(int pX, int pY){
        try {

            if (showIsoline && interactiveMode) {
                RealPoint point = fromPixelToCoordinates(pX - offsetX, pY - offsetY);
                double isoLineValue = Function.getValue(point.getX(), point.getY());
                drawIsoline(isoLineValue);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        rendering();
    }

    private void drawIsoline(double isolineValue){
        Graphics graphics = isolineImage.getGraphics();
        graphics.setColor(info.getColorBorder());
        double deltaX = (B - A)/info.getK();
        double deltaY = (D - C)/info.getM();
        double f1;
        double f2;
        double f3;
        double f4;

        int g = 0;
        for (double x = A; x < B; x += deltaX) {
            for (double y = C; y < D; y += deltaY) {
                Point p1, p2, p3, p4;
                int count;
                ArrayList<Point> points = new ArrayList<Point>();
                f1 = Function.getValue(x, y);//z(i,j)
                f2 = Function.getValue(x + deltaX, y);//z(i+1, j)
                f3 = Function.getValue(x, y + deltaY);//z(i, j+1)
                f4 = Function.getValue(x + deltaX, y + deltaY);//z(i+1, j+1)
                Point  p;

                if((p = xCheck(f1, f2, isolineValue, x, y)) != null){
                    points.add(p);
                }
                if((p = xCheck(f3, f4, isolineValue, x, y + deltaY)) != null){
                    points.add(p);
                }
                if((p = yCheck(f1, f3, isolineValue, x, y)) != null){
                    points.add(p);
                }
                if((p = yCheck(f2, f4, isolineValue, x + deltaX, y)) != null){
                    points.add(p);
                }

                count = points.size();
                switch (count) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2: {

                        p1 = points.get(0);
                        p2 = points.get(1);
                        graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                        break;
                    }
                    case 3: {
                        break;
                    }
                    case 4: {
                        p1 = points.get(0);
                        p2 = points.get(1);
                        p3 = points.get(2);
                        p4 = points.get(3);
                        graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                        //    graphics.drawLine((int) p2.getX(), (int) p2.getY(), (int) p3.getX(), (int) p3.getY());
                        graphics.drawLine((int) p3.getX(), (int) p3.getY(), (int) p4.getX(), (int) p4.getY());
                        // graphics.drawLine((int) p4.getX(), (int) p4.getY(), (int) p1.getX(), (int) p1.getY());

                        break;
                    }
                }

            }
        }
    }

    public Point xCheck(double leftF1, double rightF2, double isolineValue, double xI, double y){
        double deltaX = (B - A)/info.getK();
        double xx;
        double yy;
        if(leftF1 < isolineValue && rightF2 > isolineValue){
            xx = xI + deltaX * (isolineValue - leftF1)/Math.abs(leftF1 - rightF2);
            yy = y;

            return fromCoordinatesToPixel(xx, yy);
        }else if(leftF1 > isolineValue && rightF2 < isolineValue){
            xx = xI + deltaX * (leftF1 - isolineValue)/Math.abs(leftF1 - rightF2);
            yy = y;

            Point point = fromCoordinatesToPixel(xx, yy);
            return point;
        }else{
            return  null;
        }
    }

    public Point yCheck(double leftF1, double rightF2, double isolineValue, double x, double yJ){
        double deltaY = (D - C)/info.getM();
        double xx;
        double yy;
        if(leftF1 < isolineValue && rightF2 > isolineValue){
            xx = x;
            yy = yJ + deltaY * (isolineValue - leftF1)/Math.abs(leftF1 - rightF2);
            return fromCoordinatesToPixel(xx, yy);
        }else if(leftF1 > isolineValue && rightF2 < isolineValue){
            xx = x;
            yy = yJ + deltaY * (leftF1 - isolineValue)/Math.abs(leftF1 - rightF2);
            Point point = fromCoordinatesToPixel(xx, yy);
            return point;
        }else{
            return  null;
        }
    }

    private Point fromCoordinatesToPixel(double x, double y){
        int pX;
        int pY;

        pX = (int) (widthMap * (x - A)/(B - A) + 0.5);
        pY = heightMap -  (int) (heightMap * (y - C)/(D - C) + 0.5);

        return new Point(pX, pY);
    }

    public void handleUnSelectDrawIsoline(){
        showIsoline = false;
        rendering();
    }

    private class RealPoint {
        private final double x, y;

        public RealPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    public void handleSwitchOnInteractiveMode(){
        interactiveMode = true;
    }

    public void handleSwitchOffInteractiveMode(){
        interactiveMode = false;
    }
}
