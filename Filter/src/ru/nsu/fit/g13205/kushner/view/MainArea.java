package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.algorithms.Algorithms;
import ru.nsu.fit.g13205.kushner.algorithms.Dithering;
import ru.nsu.fit.g13205.kushner.algorithms.Filters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 08.03.2016.
 */
public class MainArea extends JPanel {

    private static final int OFFSET = 10;

    private JLabel statusBarLabel;

    private BufferedImage originalImage;
    private BufferedImage imageA;
    private BufferedImage imageB;
    private BufferedImage imageC;
    private BufferedImage beforePuxelizeImageB;

    private boolean wasChange = false;

    private boolean isPixelizeMode;
    private BufferedImage pixelizeImage;
    private BufferedImage beforePixelizeImageC;
    private int pixelixeCount = 1;

    private boolean isGammaCorrection = false;
    private BufferedImage beforeGammaCorrectionImage;
    private BufferedImage beforePixelizeBeforeGammaCorrection;

    private boolean isRotate = false;
    private BufferedImage beforeRotateImage;
    private BufferedImage beforePixelizeBeforeRotate;

    private double resizeCof = 1;

    private boolean isSelectable = false;

    private boolean isDragged = false;
    private boolean isClicked = false;
    private boolean showImageC = false;


    private Point[] areaImageA = new Point[4];
    private Point[] selectArea = new Point[4];

    private MainWindow parentFrame;


    public MainArea(MainWindow parentFrame) {
        super();
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350 * 3 + 40, 370));
        this.addMouseListener(mouseClicked());
        this.addMouseMotionListener(mouseMotion());
        this.add(createStatusBar(), BorderLayout.SOUTH);
    }

    private MouseAdapter mouseClicked(){
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(isSelectable){
                    isClicked = true;
                    calculateSelectArea(e.getX(), e.getY());
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    if ((e.getX() >= (int) areaImageA[0].getX()) && (e.getX() <= (int) areaImageA[1].getX())
                            && (e.getY() >= OFFSET) && (e.getY() <= (int) areaImageA[3].getY())) {
                        isDragged = true;
                    }
                }catch (NullPointerException ignored){
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                isDragged = false;
            }
        };
    }



    private MouseAdapter mouseMotion(){
        return new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(isSelectable){
                    calculateSelectArea(e.getX(), e.getY());
                    repaint();
                }

            }
        };
    }

    private void calculateSelectArea(int x, int y){
        if(((x >= (int)areaImageA[0].getX()) && (x <= (int)areaImageA[1].getX())
                && (y >= OFFSET) && (y <= (int)areaImageA[3].getY())) || isDragged){
            int widthRect = (int)(Settings.AREA_WIDTH / resizeCof);
            int heightRect = (int)(Settings.AREA_HEIGHT / resizeCof);
            int xLeftBorder = OFFSET;
            int yLeftBorder = OFFSET;
            int xRightBorder = (int) areaImageA[2].getX();
            int yRightBorder = (int) areaImageA[2].getY();

            if((x - widthRect / 2) < xLeftBorder){
                xRightBorder = xLeftBorder + widthRect;
            }else if((x + widthRect / 2) > xRightBorder ){
                xLeftBorder = xRightBorder - widthRect;
            } else{
                xLeftBorder = x - widthRect / 2;
                xRightBorder = x + widthRect / 2;
            }

            if((y - heightRect / 2) < yLeftBorder){
                yRightBorder = yLeftBorder + heightRect;
            }else if((y + heightRect / 2) > yRightBorder){
                yLeftBorder = yRightBorder - heightRect;
            }else{
                yLeftBorder = y - heightRect / 2;
                yRightBorder = y + heightRect / 2;
            }

            selectArea[0] = new Point(xLeftBorder, yLeftBorder);
            selectArea[1] = new Point(xRightBorder, yLeftBorder);
            selectArea[2] = new Point(xRightBorder, yRightBorder);
            selectArea[3] = new Point(xLeftBorder, yRightBorder);
        }

        if(imageA.getWidth() < Settings.AREA_WIDTH && imageA.getHeight() < Settings.AREA_HEIGHT){
            selectArea[0] = new Point(OFFSET, OFFSET);
            selectArea[1] = new Point(OFFSET + imageA.getWidth(), OFFSET);
            selectArea[2] = new Point(imageA.getWidth() + OFFSET, imageA.getHeight() + OFFSET);
            selectArea[3] = new Point(OFFSET, imageA.getHeight() + OFFSET);
        }
    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        graphics.drawRect(9, 9, 351, 351);
        graphics.drawRect(369, 9, 351, 351);
        graphics.drawRect(729, 9, 351, 351);

        if(imageA != null && originalImage!=null){
            graphics.drawImage(imageA, OFFSET, OFFSET, this);
            areaImageA[0] = new Point(OFFSET, OFFSET);
            areaImageA[1] = new Point(OFFSET + imageA.getWidth(), OFFSET);
            areaImageA[2] = new Point(OFFSET + imageA.getWidth(), OFFSET + imageA.getHeight());
            areaImageA[3] = new Point(OFFSET, OFFSET + imageA.getHeight());
            if(isSelectable) {
                float[] dashl = {2, 2};
                BasicStroke pen = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10, dashl, 0);
                Graphics2D g2 = (Graphics2D) graphics;
                g2.setStroke(pen);
                g2.setColor(new Color(255, 255, 255));

                g2.drawLine((int)selectArea[0].getX(), (int)selectArea[0].getY(), (int)selectArea[1].getX(), (int)selectArea[1].getY());
                g2.drawLine((int)selectArea[1].getX(), (int)selectArea[1].getY(), (int)selectArea[2].getX(), (int)selectArea[2].getY());
                g2.drawLine((int)selectArea[2].getX(), (int)selectArea[2].getY(), (int)selectArea[3].getX(), (int)selectArea[3].getY());
                g2.drawLine((int)selectArea[0].getX(), (int)selectArea[0].getY(), (int)selectArea[3].getX(), (int)selectArea[3].getY());
                if(selectArea[0].getX() > 0 && selectArea[0].getY() > 0 && (isDragged || isClicked)) {
                    imageB = createImageB();
                    parentFrame.setSelectImageB(true);
                    isClicked = false;
                }
            }
        }

        if(imageB != null){
            if(isPixelizeMode) {
                graphics.drawImage(upPixelizeImage(imageB, pixelixeCount), 370, 10, this);
            }else {
                graphics.drawImage(imageB, 370, 10, this);
            }
            parentFrame.setSelectImageB(true);
        }

        if(showImageC){
            graphics.drawImage(imageC, 730, 10, this);
            parentFrame.setSelectImageC(true);
        }

    }

    @Override
    public void repaint(){
        super.repaint();
    }

    public BufferedImage createImageB(){

        int xStart = (int) (selectArea[0].getX() - OFFSET);
        int yStart = (int) (selectArea[0].getY() - OFFSET);

        xStart = (int)(xStart * resizeCof);
        yStart = (int)(yStart * resizeCof);

        if(xStart + Settings.AREA_WIDTH > originalImage.getWidth() && !(originalImage.getWidth() < Settings.AREA_WIDTH)){
            xStart--;
        }
        if(yStart + Settings.AREA_HEIGHT > originalImage.getHeight() && !(originalImage.getHeight() < Settings.AREA_HEIGHT)){
            yStart--;
        }

        int width = 0;
        int height = 0;

        if(originalImage.getWidth() < Settings.AREA_WIDTH){
            width = originalImage.getWidth();
        }else{
            width = Settings.AREA_WIDTH;
        }

        if(originalImage.getHeight() < Settings.AREA_HEIGHT){
            height = originalImage.getHeight();
        }else{
            height = Settings.AREA_HEIGHT;
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        try {
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    image.setRGB(j, i, originalImage.getRGB(xStart + j, yStart + i));
                }
            }
        }catch (IndexOutOfBoundsException e){
           //e.printStackTrace();
        }

        if(isPixelizeMode){
            beforePuxelizeImageB = image;
            image = Algorithms.pixelize(image, pixelixeCount);
        }

        return image;
    }

    public void setImage(BufferedImage originalImage, BufferedImage imageA, double resizeCof){
        this.originalImage = originalImage;
        this.imageA = imageA;
        this.resizeCof = resizeCof;
        setSelect(false);
        imageB = null;
        parentFrame.setSelectImageB(false);
        if(showImageC){
            showImageC = false;
        }
    }

    private JPanel createStatusBar(){
        Box bottomBox = Box.createHorizontalBox();

        JPanel tmpPanel = new JPanel();
        tmpPanel.setBackground(new Color(134, 151, 255));
        tmpPanel.setLayout(new BorderLayout());

        statusBarLabel = new JLabel("Status bar");

        bottomBox.add(statusBarLabel);
        bottomBox.add(Box.createHorizontalGlue());
        tmpPanel.add(bottomBox, BorderLayout.WEST);

        return tmpPanel;
    }

    public JLabel getStatusBar(){
        return statusBarLabel;
    }

    public void setSelect(boolean value){
        selectArea[0] = new Point(0,0);
        selectArea[1] = new Point(0,0);
        selectArea[2] = new Point(0,0);
        selectArea[3] = new Point(0,0);

        isSelectable = value;
    }

    public void showDesaturate(){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Algorithms.toBlackWhite(imageB);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Algorithms.toBlackWhite(beforePuxelizeImageB);
        }else{
            imageC = Algorithms.toBlackWhite(imageB);
        }
        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showNegative(){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Algorithms.toNegative(imageB);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Algorithms.toNegative(beforePuxelizeImageB);
        }else{
            imageC = Algorithms.toNegative(imageB);
        }
        wasChange = true;

        showImageC = true;
        repaint();
    }

    public BufferedImage getImageC(){
        return imageC;
    }

    public void showBlur(){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Filters.applyBlurFilter(imageB);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Filters.applyBlurFilter(beforePuxelizeImageB);
        }else{
            imageC = Filters.applyBlurFilter(imageB);
        }

        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showSharpen(){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Filters.applySharpenFilter(imageB);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Filters.applySharpenFilter(beforePuxelizeImageB);
        }else{
            imageC = Filters.applySharpenFilter(imageB);
        }

        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showEmboss(){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Filters.applyEmbossFilter(imageB);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Filters.applyEmbossFilter(beforePuxelizeImageB);
        }else{
            imageC = Filters.applyEmbossFilter(imageB);
        }

        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showWaterColor(){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Filters.applyWatercolorFilter(imageB);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Filters.applyWatercolorFilter(beforePuxelizeImageB);
        }else{
            imageC = Filters.applyWatercolorFilter(imageB);
        }

        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showMedianFilter(){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Filters.applyMedianFilter(imageB);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Filters.applyMedianFilter(beforePuxelizeImageB);
        }else{
            imageC = Filters.applyMedianFilter(imageB);
        }

        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showRobertsFilter(int threshold){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Filters.applyRobertsFilter(imageB, threshold);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Filters.applyRobertsFilter(beforePuxelizeImageB, threshold);
        }else{
            imageC = Filters.applyRobertsFilter(imageB, threshold);
        }

        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showSobelFilter(int threshold){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Filters.applySobelFilter(imageB, threshold);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Filters.applySobelFilter(beforePuxelizeImageB, threshold);
        }else{
            imageC = Filters.applySobelFilter(imageB, threshold);
        }

        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showGammaCorrection(double gamma){
        if(isPixelizeMode) {
            if(isGammaCorrection){
                BufferedImage tmpImage = Filters.applyGammaCorrectionFilter(imageB, gamma);
                imageC = upPixelizeImage(tmpImage, pixelixeCount);
            }else{
                beforeGammaCorrectionImage = imageC;

                BufferedImage tmpImage = Filters.applyGammaCorrectionFilter(imageB, gamma);
                imageC = upPixelizeImage(tmpImage, pixelixeCount);


                beforePixelizeBeforeGammaCorrection = Filters.applyGammaCorrectionFilter(beforePuxelizeImageB, gamma);
                isGammaCorrection = true;
            }

            BufferedImage tmpImage = Filters.applyGammaCorrectionFilter(imageB, gamma);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            //beforePixelizeImageC = Filters.applyGammaCorrectionFilter(beforePuxelizeImageB, gamma);
        }else{
            if(isGammaCorrection) {
                imageC = Filters.applyGammaCorrectionFilter(imageB, gamma);
            }else{
                beforeGammaCorrectionImage = imageC;
                imageC = Filters.applyGammaCorrectionFilter(imageB, gamma);
                isGammaCorrection = true;
            }
        }

        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void setIsGammaCorrection(boolean val){
        if(!val){
            beforePixelizeImageC = beforePixelizeBeforeGammaCorrection;
        }
        isGammaCorrection = val;
    }

    public void cancelGammaCorrection(){
        imageC = beforeGammaCorrectionImage;
        isGammaCorrection = false;
        repaint();
    }

    public void copyRight(){
        if(imageB != null){
            imageC = imageB;
            showImageC = true;
            wasChange = true;
            repaint();
        }
    }

    public void copyLeft(){
        if(showImageC){
            imageB = imageC;
            repaint();
        }
    }

    public void showX2(){
        try {
            if(!isPixelizeMode) {
                BufferedImage tmp = Algorithms.resizeBilinearGray(imageB, imageB.getWidth() * 2, imageB.getHeight() * 2);

                int w = tmp.getWidth() > Settings.AREA_WIDTH ? Settings.AREA_WIDTH : tmp.getWidth();
                int h = tmp.getHeight() > Settings.AREA_HEIGHT ? Settings.AREA_HEIGHT : tmp.getHeight();

                imageC = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                for (int i = 0; i < h; i++) {
                    for (int j = 0; j < w; j++) {
                        imageC.setRGB(j, i, tmp.getRGB((int) (tmp.getWidth() / 2) - (int) w / 2 + j, (int) tmp.getHeight() / 2 - (int) w / 2 + i));
                    }
                }
            }else{

                BufferedImage tmp = Algorithms.resizeBilinearGray(imageB, imageB.getWidth() * 2, imageB.getHeight() * 2);


                /////////
                BufferedImage tmpBeforePixelizeImageC = Algorithms.resizeBilinearGray(beforePuxelizeImageB,
                        beforePuxelizeImageB.getWidth() * 2, beforePuxelizeImageB.getHeight() * 2);

                int w1 = tmpBeforePixelizeImageC.getWidth() > Settings.AREA_WIDTH ? Settings.AREA_WIDTH : tmpBeforePixelizeImageC.getWidth();
                int h1 = tmpBeforePixelizeImageC.getHeight() > Settings.AREA_HEIGHT ? Settings.AREA_HEIGHT : tmpBeforePixelizeImageC.getHeight();

                beforePixelizeImageC = new BufferedImage(w1, h1, BufferedImage.TYPE_INT_RGB);

                for (int i = 0; i < h1; i++) {
                    for (int j = 0; j < w1; j++) {
                        beforePixelizeImageC.setRGB(j, i, tmpBeforePixelizeImageC.getRGB((int) (tmpBeforePixelizeImageC.getWidth() / 2) - (int) w1 / 2 + j,
                                (int) tmpBeforePixelizeImageC.getHeight() / 2 - (int) w1 / 2 + i));
                    }
                }
                ///////////////

                tmp = Algorithms.resizeBilinearGray(imageB, imageB.getWidth() * 2, imageB.getHeight() * 2);

                tmp = upPixelizeImageForX2(tmp, pixelixeCount);

                int w = tmp.getWidth() > Settings.AREA_WIDTH ? Settings.AREA_WIDTH : tmp.getWidth();
                int h = tmp.getHeight() > Settings.AREA_HEIGHT ? Settings.AREA_HEIGHT : tmp.getHeight();

                imageC = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                for (int i = 0; i < h; i++) {
                    for (int j = 0; j < w; j++) {
                        imageC.setRGB(j, i, tmp.getRGB((int) (tmp.getWidth() / 2) - (int) w / 2 + j, (int) tmp.getHeight() / 2 - (int) w / 2 + i));
                    }
                }

            }
            showImageC = true;
            wasChange = true;
            repaint();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showPixelize(int count){
        isPixelizeMode = true;
        beforePuxelizeImageB = imageB;
        imageB = Algorithms.pixelize(imageB, count);
        pixelixeCount = count;

        repaint();
    }

    public void unShowPixelize(){//скрыть pixelize, отобразить старое изображение
        isPixelizeMode = false;
        if(imageB != null) {
            imageB = beforePuxelizeImageB;
        }
        if(showImageC){
            imageC = beforePixelizeImageC;
        }
        repaint();
    }

    public void showFloydDithering(int red, int green, int blue){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Dithering.applyFloydSteinberg(imageB, red, green, blue);

            imageC = upPixelizeImage(tmpImage, pixelixeCount);

            beforePixelizeImageC = Dithering.applyFloydSteinberg(beforePuxelizeImageB, red, green, blue);
        }else{
            imageC = Dithering.applyFloydSteinberg(imageB, red, green, blue);
        }

        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showOrderedDithering(int red, int green, int blue){
        if(isPixelizeMode) {
            BufferedImage tmpImage = Dithering.applyOrderedDithering(imageB, red, green, blue);
            imageC = upPixelizeImage(tmpImage, pixelixeCount);
            beforePixelizeImageC = Dithering.applyOrderedDithering(beforePuxelizeImageB, red, green, blue);
        }else{

            imageC = Dithering.applyOrderedDithering(imageB, red, green, blue);
        }



        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void showRotateImage(int angle){
        if(isPixelizeMode) {
            if(isRotate) {
                BufferedImage tmpImage = Algorithms.applyRotate(imageB, angle);
                imageC = upPixelizeImage(tmpImage, pixelixeCount);
                //beforePixelizeImageC = Algorithms.applyRotate(beforePuxelizeImageB, angle);
            }else{
                beforeRotateImage = imageC;
                BufferedImage tmpImage = Algorithms.applyRotate(imageB, angle);
                imageC = upPixelizeImage(tmpImage, pixelixeCount);
                beforePixelizeBeforeRotate = Algorithms.applyRotate(beforePuxelizeImageB, angle);

                isRotate = true;
            }
        }else{
            if(isRotate) {
                imageC = Algorithms.applyRotate(imageB, angle);
            }else{
                beforeRotateImage = imageC;
                imageC = Algorithms.applyRotate(imageB, angle);
                isRotate = true;
            }
        }
        wasChange = true;
        showImageC = true;
        repaint();
    }

    public void cancelRotateImage(){

        imageC = beforeRotateImage;
        //beforePixelizeImageC = beforePixelizeBeforeRotate;

        isRotate = false;
        repaint();
    }

    public void setRotate(boolean val){
        if(!val){
            beforePixelizeImageC = beforePixelizeBeforeRotate;
        }
        isRotate = val;
    }


    private BufferedImage upPixelizeImageForX2(BufferedImage pixelizeImage, int count){
        int width = pixelizeImage.getWidth() * count;
        int height = pixelizeImage.getHeight() * count;

        BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int rgb = pixelizeImage.getRGB(j / count, i / count);
                returnImage.setRGB(j, i, rgb);
            }
        }

        width = width > Settings.AREA_WIDTH ? Settings.AREA_WIDTH : width;
        height = height > Settings.AREA_HEIGHT ? Settings.AREA_HEIGHT : height;

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int rgb = pixelizeImage.getRGB(j / count, i / count);
                returnImage.setRGB(j, i, rgb);
            }
        }
        return returnImage;
    }

    private BufferedImage upPixelizeImage(BufferedImage pixelizeImage, int count){
        int width = pixelizeImage.getWidth() * count;
        int height = pixelizeImage.getHeight() * count;
        width = width > Settings.AREA_WIDTH ? Settings.AREA_WIDTH : width;
        height = height > Settings.AREA_HEIGHT ? Settings.AREA_HEIGHT : height;

        BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int rgb = pixelizeImage.getRGB(j / count, i / count);
                returnImage.setRGB(j, i, rgb);
            }
        }
        return returnImage;
    }

    public void clear(){
        showImageC = false;
        imageA = null;
        imageB = null;
        imageC = null;
        beforePuxelizeImageB = null;
        beforePixelizeImageC = null;

        repaint();
    }

    public void setWasChange(boolean b){
        wasChange = b;
    }

    public boolean getWasChange(){
        return wasChange;
    }
}
