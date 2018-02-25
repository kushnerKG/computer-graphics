package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.SettingApplication;
import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.graphics.MyGraphics;
import ru.nsu.fit.g13205.kushner.graphics.Point;
import ru.nsu.fit.g13205.kushner.model.Impact;
import ru.nsu.fit.g13205.kushner.view.setting.GameAreaProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Konstantin on 21.02.2016.
 */
public class GameArea extends JPanel {

    private Controller controller;

    private int delta;
    private int hWithDelta;
    private int widthInPixel;
    private int heightInPixel;

    private GameAreaProperties properties;
    private Point activeCell =  new Point(-1, -1);
    private Point[][] centres;
    private GameField gameField;
    private Impact[][] prevImpacts;

    private BufferedImage bufferedImage;

    private boolean impactStatus = false;
    private MouseAdapter clickedAdapter;
    private MouseAdapter motionAdapter;


    public GameArea(GameAreaProperties properties, Controller controller) {
        super();
        setLayout(null);
        setBackground(SettingApplication.FONT_COLOR);

        this.properties = properties;
        this.controller = controller;

        gameField = new GameField(properties.getM(), properties.getN());

        setPreferredSize(new Dimension(widthInPixel, heightInPixel));

        clickedAdapter = mouseClicked();
        this.addMouseListener(clickedAdapter);

        motionAdapter = mouseMotion();
        this.addMouseMotionListener(motionAdapter);

        update();
        repaint();
    }

    public void lockPanel(){
        this.removeMouseListener(clickedAdapter);
        this.removeMouseMotionListener(motionAdapter);
    }

    public void unLockPanel(){
        this.addMouseListener(clickedAdapter);
        this.addMouseMotionListener(motionAdapter);
    }

    private MouseAdapter mouseClicked(){
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onField(new Point(e.getX(), e.getY()));
            }
        };
    }

    private MouseAdapter mouseMotion(){
        return new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (properties.getMode() == 1) {
                    inDragged(new Point(e.getX(), e.getY()));
                }
                if (properties.getMode() == 2) {
                    inDragged(new Point(e.getX(), e.getY()));
                }
            }
        };
    }

    private void drawImpact(Impact[][] impacts){
        Graphics g = bufferedImage.getGraphics();
        Color prevColor = g.getColor();
        Font prevFont = g.getFont();
        g.setFont(SettingApplication.IMPACT_FONT);
        g.setColor(SettingApplication.IMPACT_COLOR);
        for (int i = 0; i < impacts.length; i++) {
            for (int j = 0; j < impacts[i].length; j++) {
                if (i % 2 != 0 && j == 0) {
                    continue;
                }
                if(i % 2 == 0) {
                    double value = impacts[i][j].getValue();
                    if(value - (int)value == 0.0d){
                        int shift = String.format("%.0f", value).length();
                        String showString = String.format("%.0f", value);
                        g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                        continue;
                    }
                    int shift = String.format("%.1f", value).length();
                    String showString = String.format("%.1f", value);
                    g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                }else{
                    double value = impacts[i][j - 1].getValue();

                    if(value - (int)value == 0){
                        int shift = String.format("%.0f", value).length();
                        String showString = String.format("%.0f", value);
                        g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                        continue;
                    }
                    int shift = String.format("%.1f", value).length();
                    String showString = String.format("%.1f", value);
                    g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                }
            }
        }
        prevImpacts = impacts.clone();
        g.setColor(prevColor);
        g.setFont(prevFont);
    }

    private void clearImpact(){
        Graphics g = bufferedImage.getGraphics();
        Color prevColor = g.getColor();
        Font prevFont = g.getFont();
        g.setFont(SettingApplication.IMPACT_FONT);
        try {
            if (prevImpacts != null) {
                for (int i = 0; i < properties.getN(); i++) {
                    for (int j = 0; j < properties.getM(); j++) {
                        if (i % 2 != 0 && j == 0) {
                            continue;
                        }
                        if (i % 2 == 0) {
                            if (gameField.getColoredCell(i, j)) {
                                double value = prevImpacts[i][j].getValue();
                                g.setColor(SettingApplication.CLICKED_COLOR);
                                if(value - (int)value == 0){
                                    String showString = String.format("%.0f", value);
                                    int shift = showString.length();
                                    g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                                    continue;
                                }
                                String showString = String.format("%.1f", value);
                                int shift = showString.length();
                                g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                            } else {
                                double value = prevImpacts[i][j].getValue();
                                g.setColor(SettingApplication.UN_CLICKED_COLOR);
                                if(value - (int)value == 0){
                                    String showString = String.format("%.0f", value);
                                    int shift = showString.length();
                                    g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                                    continue;
                                }
                                String showString = String.format("%.1f", value);
                                int shift = showString.length();
                                g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                            }
                        } else {
                            if (gameField.getColoredCell(i, j - 1)) {
                                double value = prevImpacts[i][j - 1].getValue();
                                g.setColor(SettingApplication.CLICKED_COLOR);
                                if(value - (int)value == 0){
                                    String showString = String.format("%.0f", value);
                                    int shift = showString.length();
                                    g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                                    continue;
                                }
                                String showString = String.format("%.1f", value);
                                int shift = showString.length();
                                g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                            } else {
                                double value = prevImpacts[i][j - 1].getValue();
                                g.setColor(SettingApplication.UN_CLICKED_COLOR);
                                if(value - (int)value == 0){
                                    String showString = String.format("%.0f", value);
                                    int shift = showString.length();
                                    g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                                    continue;
                                }
                                String showString = String.format("%.1f", value);
                                int shift = showString.length();
                                g.drawString(showString, centres[i][j].getX() - shift * SettingApplication.FONT_WIDTH, centres[i][j].getY());
                            }
                        }
                    }
                }
            }
        }catch (ArrayIndexOutOfBoundsException e){
        }
        g.setColor(prevColor);
        g.setFont(prevFont);
    }

    public void inDragged(Point point){
        try {
            if (getRGB(point) != SettingApplication.BORDER_COLOR.getRGB() && getRGB(point) != SettingApplication.FONT_COLOR.getRGB()) {
                Point owner = calculateOwner(point);
                int n = owner.getY();
                int m = owner.getX();


                if (n != activeCell.getY() || m != activeCell.getX()) {
                    boolean prevStatus = gameField.getColoredCell(n, m);
                    gameField.setColoredCell(m, n, properties.getMode());
                    if(gameField.getColoredCell(n, m) != prevStatus){
                        changeColor(n, m, point);
                        controller.handleChangeStatusCell(m, n, gameField.getColoredCell(n, m));
                    }
                    activeCell = owner;
                }
            }
        }catch (IndexOutOfBoundsException ignored){
        }

    }


    private  void onField(Point point){

        try {
            if (getRGB(point) != SettingApplication.BORDER_COLOR.getRGB() && getRGB(point) != SettingApplication.FONT_COLOR.getRGB()) {
                Point owner = calculateOwner(point);
                int n = owner.getY();
                int m = owner.getX();
                boolean prevStatus = gameField.getColoredCell(n, m);
                gameField.setColoredCell(m, n, properties.getMode());

                if(gameField.getColoredCell(n, m) != prevStatus){
                    changeColor(n, m, point);
                    controller.handleChangeStatusCell(m, n, gameField.getColoredCell(n, m));

                }
            }
        }catch (IndexOutOfBoundsException | NullPointerException ignored){
        }
    }

    private void changeColor(int n, int m, Point point){
        if(n % 2 != 1) {
            if (gameField.getColoredCell(n, m)) {
                MyGraphics.spanFill(centres[n][m], SettingApplication.CLICKED_COLOR, bufferedImage, widthInPixel, heightInPixel);
                repaint();
            } else {
                MyGraphics.spanFill(centres[n][m], SettingApplication.UN_CLICKED_COLOR, bufferedImage, widthInPixel, heightInPixel);
                repaint();
            }
        }else{
            if (gameField.getColoredCell(n, m)) {
                MyGraphics.spanFill(centres[n][m + 1], SettingApplication.CLICKED_COLOR, bufferedImage, widthInPixel, heightInPixel);
                repaint();
            } else {
                MyGraphics.spanFill(centres[n][m + 1], SettingApplication.UN_CLICKED_COLOR, bufferedImage, widthInPixel, heightInPixel);
                repaint();
            }
        }
    }

    public void update(){
        initField();
        setPreferredSize(new Dimension(widthInPixel, heightInPixel));
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(new Color(255, 255, 255));
        graphics.fillRect(0, 0, widthInPixel, heightInPixel);
        graphics.setColor(SettingApplication.BORDER_COLOR);
        printArea(graphics, bufferedImage);
    }

    @Override
    public void repaint(){
        super.repaint();
    }

    private void initField(){
        int widthInCeil = properties.getM();
        int heightInCeil = properties.getN();

        int lineWidthInPixel = properties.getWidthBorder();
        int ceilSize = properties.getSize();
        int h = Math.round(Math.round(Math.sqrt(3) / 2 * ceilSize));
        this.delta = Math.round(lineWidthInPixel/2);
        this.hWithDelta = Math.round(Math.round(Math.sqrt(3) / 2 * (ceilSize + delta)));



        this.widthInPixel = Math.round((2 * (h + delta) + lineWidthInPixel) * widthInCeil + lineWidthInPixel);
        this.heightInPixel = (int)Math.round(2 * delta + ceilSize + (1.5 * (ceilSize + delta)) * heightInCeil);
        this.bufferedImage = new BufferedImage(widthInPixel, heightInPixel, BufferedImage.TYPE_INT_RGB);
    }

    private int getRGB(Point point) throws java.lang.ArrayIndexOutOfBoundsException{

        return bufferedImage.getRGB(point.getX(), point.getY());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage,0,0, this);
    }

    private Point calculateOwner(Point point){
        int n, m;
        int x = point.getX();
        int y = point.getY();
        double minDistance = calculateDistance(x, y, centres[0][0].getX(), centres[0][0].getY());
        n = 0;
        m = 0;
        for(int i = 0; i < properties.getN(); i++){
            for(int j = 0; j < properties.getM(); j++){
                if (i == 0 && j == 0) {
                    continue;
                }

                if(i % 2 == 0) {
                    double tmp = calculateDistance(x, y, centres[i][j].getX(), centres[i][j].getY());
                    if (tmp < minDistance) {
                        n = i;
                        m = j;
                        minDistance = tmp;
                    }
                }else {
                    try {
                        double tmp = calculateDistance(x, y, centres[i][j + 1].getX(), centres[i][j + 1].getY());
                        if (tmp < minDistance) {
                            n = i;
                            m = j;
                            minDistance = tmp;
                        }
                    }catch (IndexOutOfBoundsException ignored){

                    }
                }
            }
        }

        return new Point(m, n);
    }

    private double calculateDistance(int x0, int y0, int x1, int y1){
        return  Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1)*(y0 - y1));
    }

    private void printArea(Graphics g, BufferedImage image){
        this.centres = calculateCentre();
        for(int i = 0; i < properties.getN(); i++){
            for(int j = 0; j < properties.getM(); j++){
                if(i % 2 != 0 && j ==0){
                    continue;
                }
                int ceilSize = properties.getSize();
                if(i % 2 != 0 && i > 0 && i < properties.getN() - 1){
                    drawLine(new Point(centres[i - 1][j - 1].getX(), centres[i - 1][j - 1].getY() + ceilSize + delta),
                            new Point(centres[i + 1][j - 1].getX(), centres[i + 1][j - 1].getY() - ceilSize - delta), image);
                    drawLine(new Point(centres[i - 1][j].getX(), centres[i - 1][j].getY() + ceilSize + delta),
                            new Point(centres[i + 1][j].getX(), centres[i + 1][j].getY() - ceilSize - delta), image);
                }else if(i % 2 != 0 && i > 0 && i == properties.getN() - 1){
                    Point[] tmp = getPoints(centres[i][j], ceilSize + delta);
                    drawLine(tmp[0], tmp[1], image);
                    drawLine(tmp[1], tmp[2], image);
                    drawLine(tmp[2], tmp[3], image);
                    drawLine(tmp[5], tmp[0], image);
                } else{
                    drawHexagon(getPoints(centres[i][j], ceilSize + delta), g, image);
                }
            }
        }

        for(int i = 0; i < properties.getN(); i++){
            for (int j = 0; j < properties.getM(); j++){
                if(i % 2 == 0){
                    if(gameField.getColoredCell(i, j)){
                        MyGraphics.spanFill(centres[i][j], SettingApplication.CLICKED_COLOR, bufferedImage, widthInPixel, heightInPixel);
                    }else{
                        MyGraphics.spanFill(centres[i][j], SettingApplication.UN_CLICKED_COLOR, bufferedImage, widthInPixel, heightInPixel);
                    }
                }else{
                    try{
                        if(gameField.getColoredCell(i, j)){
                            MyGraphics.spanFill(centres[i][j + 1], SettingApplication.CLICKED_COLOR, bufferedImage, widthInPixel, heightInPixel);
                        }else{
                            MyGraphics.spanFill(centres[i][j + 1], SettingApplication.UN_CLICKED_COLOR, bufferedImage, widthInPixel, heightInPixel);

                        }
                    }
                    catch (IndexOutOfBoundsException ignored){}
                }
            }
        }
    }

    private void drawLine(Point p1, Point p2, BufferedImage image){
        if(properties.getWidthBorder() == 1){
            MyGraphics.drawBresenhamLine(p1.getX(), p1.getY(), p2.getX(),
                    p2.getY(), image.getGraphics(), image, SettingApplication.BORDER_COLOR);
        }else{
            Graphics2D g2 = (Graphics2D) image.getGraphics();
            g2.setStroke(new BasicStroke(properties.getWidthBorder(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Color prevColor = g2.getColor();
            g2.setColor(SettingApplication.BORDER_COLOR);
            g2.draw(new Line2D.Float(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
            g2.setColor(prevColor);
        }
    }

    private void drawHexagon(Point[] points, Graphics g, BufferedImage image){
        if(properties.getWidthBorder() == 1){
            for(int i = 1; i < points.length; i++){
                MyGraphics.drawBresenhamLine(points[i - 1].getX(), points[i - 1].getY(), points[i].getX(), points[i].getY(), g,
                        image, SettingApplication.BORDER_COLOR);
            }
            MyGraphics.drawBresenhamLine(points[points.length - 1].getX(), points[points.length - 1].getY(), points[0].getX(),
                    points[0].getY(), g, image, SettingApplication.BORDER_COLOR);
        }else{
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(properties.getWidthBorder(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for(int i = 1; i < points.length; i++){
                g2.draw(new Line2D.Float(points[i - 1].getX(), points[i - 1].getY(), points[i].getX(), points[i].getY()));
            }
            g2.draw(new Line2D.Float(points[points.length - 1].getX(), points[points.length - 1].getY(), points[0].getX(), points[0].getY()));
        }
    }

    private Point[] getPoints(Point centre, int radius){
        double deg, rad;
        int x0 = centre.getX();
        int y0 = centre.getY();
        int x1, y1;

        Point[] points = new Point[6];

        for(int i = 0; i < 6; i++){
            deg = 60 * i + 30;
            rad = Math.PI / 180 * deg;
            x1 = Math.round(Math.round(x0 + radius * Math.cos(rad)));
            y1 = Math.round(Math.round(y0 + radius * Math.sin(rad)));
            points[i] = new Point(x1, y1);
        }
        return points;
    }

    private Point[][] calculateCentre(){
        int startX;
        int startY = 2*delta + properties.getSize();
        Point[][] centres = new Point[properties.getN()][properties.getM()];

        for(int i = 0; i < properties.getN(); i++){
            if(i % 2 == 0){
                startX = hWithDelta + delta;
            }else{
                startX = delta;
            }
            for(int j = 0; j < properties.getM(); j++){
                if(((i % 2) != 0) &&  (j == 0)){
                    continue;
                }
                centres[i][j] = new Point(startX + j * (2 * hWithDelta),
                         Math.round(Math.round(startY + (1.5 * (properties.getSize() + delta)) * i )));
            }
        }

        return centres;
    }

    public void clearField(){
        gameField.clearGameField();
        controller.handleClearField();
    }

    public void setColoredCell(int m, int n){
        gameField.setColoredCell(m, n, properties.getMode());
    }

    public void saveFieldToFile(File file) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(file);
        writeSaveFile(out);
        out.close();
    }

    private void writeSaveFile(PrintWriter out){

        out.println(properties.getM() + " " + properties.getN());
        out.println(properties.getWidthBorder());
        out.println(properties.getSize());
        int count = 0;
        for(int i = 0; i < properties.getN(); i++){
            for(int j = 0; j < properties.getM(); j++){
                if(gameField.getColoredCell(i, j)){
                    count++;
                }
            }
        }
        out.println(count);
        for(int i = 0; i < properties.getN(); i++){
            for(int j = 0; j < properties.getM(); j++){
                if(gameField.getColoredCell(i, j)){
                    out.println(j + " " + i);
                }
            }
        }
    }

    public void updateSize(){
        gameField.updateSize(properties.getM(), properties.getN());
    }

    public void updateImpact(Impact[][] impacts){
        try {
            if(impactStatus){
                clearImpact();
            }
            for (int i = 0; i < properties.getN(); i++) {
                for (int j = 0; j < properties.getM(); j++){
                    if (i % 2 != 0 && j == 0) {
                        continue;
                    }
                    if (i % 2 != 0) {
                        if (impacts[i][j - 1].isLife() != gameField.getColoredCell(i, j - 1)) {
                            gameField.setColoredCell(j - 1, i, impacts[i][j - 1].isLife());
                            changeColor(i, j - 1, centres[i][j]);
                        }
                    } else {
                        if (impacts[i][j].isLife() != gameField.getColoredCell(i, j)) {
                            gameField.setColoredCell(j, i, impacts[i][j].isLife());
                            changeColor(i, j, centres[i][j]);
                        }
                    }
                }

            }
            if(impactStatus) {//тут возможна ошибка
                drawImpact(impacts);
                repaint();
            }else{
                prevImpacts = impacts;
                repaint();
            }
        }catch (Exception e){
        }

    }

    public void updateStatusImpact(boolean status){
        impactStatus = status;
        if(status){
            if(prevImpacts == null){
                Impact[][] stub = new Impact[properties.getN()][properties.getM()];
                for(int i = 0; i < properties.getN(); i++){
                    for(int j = 0; j < properties.getM(); j++){
                        stub[i][j] = new Impact(false, 0.00d);
                    }
                }
                drawImpact(stub.clone());
                repaint();
            }else{
                drawImpact(prevImpacts);
                repaint();
            }
        }else{
            clearImpact();
            repaint();
        }
    }

    public void updateSize(Impact[][] impacts) {

    }
}
