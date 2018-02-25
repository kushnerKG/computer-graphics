package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.utils.FileInfo;
import ru.nsu.fit.g13205.kushner.utils.Function;
import ru.nsu.fit.g13205.kushner.utils.IFunction;
import ru.nsu.fit.g13205.kushner.view.MainArea;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class Model implements ModelObservable {

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
    public Pattern pattern = Pattern.compile(",?0*$");

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
    private BufferedImage tmpImage;
    private BufferedImage isolineLegendImage;

    private boolean showGrid = false;
    private boolean showMap = false;
    private boolean showLegend = false;
    private boolean showIsoline = false;
    private boolean enableInterpolation = false;

    private boolean interactiveMode = false;

    private double lastDraggedIsoline = 0;
    private boolean isDragged = false;
    private ArrayList<Double> drawnIsolines = new ArrayList<Double>();

    public Model(MainArea mainArea) {
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
        Graphics imageGraphics = image.getGraphics();


        if(showLegend){
            if(!enableInterpolation) {
                createLegendImage();
            }else {
                createLegendImage();
                interpolateLegend();
                imageGraphics.drawImage(legendImage, offsetX + widthMap + offsetLegendX, offsetY, null);
            }
        }

        if(showMap){
            if(!enableInterpolation) {
                createMapImage();
            }else {
                mapImage = new BufferedImage(widthMap, heightMap, BufferedImage.TYPE_INT_ARGB);
                interpolateMap();
                imageGraphics.drawImage(mapImage, offsetX, offsetY, null);
            }
        }

        if(showGrid){
            createGridImage();
        }

        if(showIsoline){
            isolineImage = new BufferedImage(widthMap, heightMap, BufferedImage.TYPE_INT_ARGB);
            for(double tmp: drawnIsolines){
                MarchingSquareAlgorithms.drawIsoline(tmp, isolineImage, A, B, C, D, Function.getFunction(), info);
            }
            isolineLegendImage = new BufferedImage(widthLegend / 3, heightLegend, BufferedImage.TYPE_INT_ARGB);

            for (double isolineValue = minZ; isolineValue < maxZ; isolineValue += (maxZ - minZ) / (info.getColorsOfLegend().size())) {
                MarchingSquareAlgorithms.drawIsoline(isolineValue, isolineLegendImage, minZ, maxZ, minZ, maxZ,
                        Function.getLegendFunction(), info);
            }

            legendImage.getGraphics().drawImage(isolineLegendImage, offsetX + widthMap + offsetLegendX, offsetY, null);
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
            try {
                g.drawImage(legendImage, offsetX + widthMap + offsetLegendX, offsetY, null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(showGrid){
            g.drawImage(gridImage, offsetX, offsetY, null);
        }

        if(showIsoline){
            g.drawImage(isolineImage, offsetX, offsetY, null);
            g.drawImage(isolineLegendImage, offsetX + widthMap + offsetLegendX, offsetY, null);
        }
        if(isDragged) {
            g.drawImage(tmpImage, offsetX, offsetY, null);
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
        calculateMinAndMax();
        createLegendImage();
        createMapImage();
        isPrinted = true;
        showMap = true;
        showLegend = true;
        if(showGrid){
            createGridImage();
        }
        if(showIsoline){
            isolineImage = new BufferedImage(isolineImage.getWidth(), isolineImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for(double tmp: drawnIsolines){
                MarchingSquareAlgorithms.drawIsoline(tmp, isolineImage, A, B, C, D, Function.getFunction(), info);
            }
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
                value = Function.getFunction().getValue(point.getX(), point.getY());
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
            double value = Function.getLegendFunction().getValue(0, y);
            for(int pX = 0; pX < widthLegend/3; pX++){
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

        Graphics g = legendImage.getGraphics();
        g.setColor(new Color(0, 0, 0));
        int offset = widthLegend/3;
        int delta = heightLegend / colorLegend.size();
        int currentH = delta + 5;
        double currentValue = maxZ - deltaZ;
        String zStr;
        for(int i = 0; i < colorLegend.size() - 1; i++){
            zStr = String.format(" %.4f", currentValue);
            zStr = pattern.matcher(zStr).replaceAll("");
            g.drawString(zStr, offset + 1, currentH);
            currentValue -= deltaZ;
            currentH += delta;
        }

        g.drawString(pattern.matcher(String.format(" %.4f", maxZ)).replaceAll(""), offset + 1, 11);
        g.drawString(pattern.matcher(String.format(" %.4f", minZ)).replaceAll(""), offset + 1, heightLegend - 4);
    }

    private void calculateMinAndMax(){
        RealPoint point = fromPixelToCoordinates(0, 0);
        minZ = Function.getFunction().getValue(point.getX(), point.getY());
        maxZ = minZ;
        int width = widthMap;
        int height = heightMap;
        double value;
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                point = fromPixelToCoordinates(x, y);
                value = Function.getFunction().getValue(point.getX(), point.getY());
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
        double deltaX = (double)(widthMap + 1) / ((double)info.getK() - 1);
        double deltaY = (double)(heightMap + 1) / ((double)info.getM() - 1);
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

    public void handleSelectShowIsoline(){

        int isolineCount = info.getColorsOfLegend().size();
        if(isolineImage == null) {
            isolineImage = new BufferedImage(widthMap, heightMap, BufferedImage.TYPE_INT_ARGB);
            for (double isolineValue = minZ; isolineValue < maxZ; isolineValue += (maxZ - minZ) / isolineCount) {
                drawnIsolines.add(isolineValue);
                MarchingSquareAlgorithms.drawIsoline(isolineValue, isolineImage, A, B, C, D, Function.getFunction(), info);
            }
        }

        if(isolineLegendImage == null){
            isolineLegendImage = new BufferedImage(widthLegend / 3, heightLegend, BufferedImage.TYPE_INT_ARGB);
            for (double isolineValue = minZ; isolineValue < maxZ; isolineValue += (maxZ - minZ) / isolineCount) {
                MarchingSquareAlgorithms.drawIsoline(isolineValue, isolineLegendImage, minZ, maxZ, minZ, maxZ,
                        Function.getLegendFunction(), info);
            }
        }
        showIsoline = true;
        rendering();
    }

    public void handleClickOnMap(int pX, int pY){
        try {

            if (showIsoline && interactiveMode) {
                RealPoint point = fromPixelToCoordinates(pX - offsetX, pY - offsetY);
                double isoLineValue = Function.getFunction().getValue(point.getX(), point.getY());
                drawnIsolines.add(isoLineValue);
                MarchingSquareAlgorithms.drawIsoline(isoLineValue, isolineImage, A, B, C, D, Function.getFunction(), info);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        rendering();
    }


    public void handleUnSelectDrawIsoline(){
        showIsoline = false;
        rendering();
    }

    public void handleSwitchOnInteractiveMode(){
        interactiveMode = true;
    }

    public void handleSwitchOffInteractiveMode(){
        interactiveMode = false;
    }

    public void handleDragged(int pX, int pY){
        if(!(showIsoline && interactiveMode)){
            return;
        }
        isDragged = true;
        if(tmpImage == null || (tmpImage.getWidth() != isolineImage.getWidth() && tmpImage.getHeight() != isolineImage.getHeight())){
            tmpImage = new BufferedImage(isolineImage.getWidth(), isolineImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }else{
            for(int y = 0; y < isolineImage.getHeight(); y++){
                for(int x = 0; x < isolineImage.getWidth(); x++){
                    tmpImage.setRGB(x, y, new Color(255, 255, 255, 0).getRGB());
                }
            }
        }
        RealPoint point = fromPixelToCoordinates(pX - offsetX, pY - offsetY);
        double isoLineValue = Function.getFunction().getValue(point.getX(), point.getY());
        MarchingSquareAlgorithms.drawIsoline(isoLineValue, tmpImage, A, B, C, D, Function.getFunction(), info);
        lastDraggedIsoline = isoLineValue;
        rendering();
    }

    public void handleUnDragged(){
        if(!(showIsoline && interactiveMode)){
            return;
        }
        isDragged = false;
        MarchingSquareAlgorithms.drawIsoline(lastDraggedIsoline, isolineImage, A, B, C, D, Function.getFunction(), info);
        drawnIsolines.add(lastDraggedIsoline);
        rendering();
    }

    public void handleDeleteIsoline(){
        isolineImage = null;
        showIsoline = false;
        rendering();
    }

    public void handleSwitchOnInterpolation(){
        enableInterpolation = true;
        interpolateLegend();
        interpolateMap();
        rendering();
    }

    private void interpolateMap(){
        IFunction function = Function.getFunction();
        RealPoint point;
        for(int y = 0; y < mapImage.getHeight(); y++){
            for(int x = 0; x < mapImage.getWidth(); x++){
                point = fromPixelToCoordinates(x , y);
                double value = function.getValue(point.getX(), point.getY());

                value = value -  minZ;//теперь больше нуля
                try {
                    int rgb = legendImage.getRGB(1, heightLegend - 1 - (int) (heightLegend * (Math.abs(value) / (maxZ - minZ))));
                    mapImage.setRGB(x, y, rgb);
                }catch (Exception e){
                    if((heightLegend - 1 - (int) (heightLegend * (Math.abs(value) / (maxZ - minZ)))) == -1){
                        int rgb = legendImage.getRGB(1, 1);
                        mapImage.setRGB(x, y, rgb);
                    }
                }
            }
        }
    }

    private void interpolateLegend(){
        int quantity = info.getColorsOfLegend().size();
        int deltaY = heightLegend / quantity;
        int startY = deltaY / 2;

        int width = widthLegend/3;
        for(int i = 0; i < quantity - 1; i++){
            int finishY = startY + deltaY;
            int rgb1 = legendImage.getRGB(1, startY);
            int rgb2 = legendImage.getRGB(1, finishY);
            int r1, g1, b1, r2, g2, b2, r, g, b;
            r1 = (rgb1>>16) & 0xFF;
            g1 = (rgb1>>8) & 0xFF;
            b1 = rgb1 & 0xFF;

            r2 = (rgb2>>16) & 0xFF;
            g2 = (rgb2>>8) & 0xFF;
            b2 = rgb2 & 0xFF;

            for(int y = startY; y < finishY; y++){
                r = r1*(finishY - y)/ deltaY + r2*(y - startY)/deltaY;
                g = g1*(finishY - y)/ deltaY + g2*(y - startY)/deltaY;
                b = b1*(finishY - y)/ deltaY + b2*(y - startY)/deltaY;
                int rgb = (0xFF<<24)|((r&0xFF)<<16) | ((g&0xFF)<<8) | (b&0xFF);
                for(int x = 0; x < width; x++){
                    legendImage.setRGB(x, y, rgb);
                }
            }
            startY += deltaY;
        }

    }

    public void handleSwitchOffInterpolation(){
        enableInterpolation = false;
        createLegendImage();
        createMapImage();
        rendering();
    }

    public void handleNewDocument(){
        mapImage = null;
        gridImage = null;
        legendImage = null;
        isolineImage = null;
        tmpImage = null;
        isolineLegendImage = null;

        showGrid = false;
        showMap = false;
        showLegend = false;
        showIsoline = false;
        enableInterpolation = false;
        interactiveMode = false;

        rendering();

    }

    @Override
    public void subscribe(ModelListener listener){
        listeners.add(listener);
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

    public void handleShowMap(){
        showMap = true;
        rendering();
    }

    public void handleUnShowMap(){
        showMap = false;
        rendering();
    }
}
