package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.utils.Function;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class MainArea extends JPanel {

    private JLabel statusBarLabel;
    private JLabel coordinateLabel;

    Pattern pattern = Pattern.compile(",?0*$");

    private MainWindow parentFrame;
    private Controller controller;
    private BufferedImage image;
    private int startXMap = 0;
    private int startYMap = 0;
    private int finishXMap = 0;
    private int finishYMap = 0;

    public MainArea(MainWindow parentFrame, Controller controller) {
        super();
        this.parentFrame = parentFrame;
        this.controller = controller;
        setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350 * 3 + 40, 370));
        this.addMouseListener(mouseClicked());
        this.addMouseMotionListener(mouseMotion());
        this.add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createStatusBar(){

        JPanel tmpPanel = new JPanel();
        tmpPanel.setBackground(new Color(134, 151, 255));
        tmpPanel.setLayout(new BorderLayout());

        statusBarLabel = new JLabel("Status bar");

        coordinateLabel = new JLabel("");

        tmpPanel.add(coordinateLabel, BorderLayout.EAST);
        tmpPanel.add(statusBarLabel, BorderLayout.WEST);

        return tmpPanel;
    }

    public JLabel getStatusBar(){
        return statusBarLabel;
    }


    public int height() {
        return this.getHeight() - 13;
    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        if(image!=null){
            graphics.drawImage(image, Settings.OFFSET_X, Settings.OFFSET_Y, this);
        }
    }

    @Override
    public void repaint(){
        super.repaint();
    }

    private MouseAdapter mouseClicked(){

        return new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();

                if(x > startXMap && x < finishXMap && y > startYMap && y < finishYMap){
                    controller.handleClickOnMap(x, y);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        };
    }

    private MouseAdapter mouseMotion(){

        return new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                int x = e.getX();
                int y = e.getY();

                if(x > startXMap && x < finishXMap && y > startYMap && y < finishYMap){
                    controller.handleCoordinates(x, y);
                }else{
                    coordinateLabel.setText("");
                }
            }
        };
    }

    public void updateImage(BufferedImage image, Point start, Point finish){
        startXMap = (int) start.getX();
        startYMap = (int) start.getY();
        finishXMap = (int) finish.getX();
        finishYMap = (int) finish.getY();

        this.image = image;
        repaint();
    }

    public void updateCoordinates(double x, double y) {
        String zStr = String.format("%.4f", Function.getValue(x, y));
        String xStr = String.format("%.4f", x);
        String yStr = String.format("%.4f", y);
        xStr = pattern.matcher(xStr).replaceAll("");
        yStr = pattern.matcher(yStr).replaceAll("");
        String str = String.format("x = %-8s y = %-8s%n z = %-8s%n", xStr, yStr, zStr);
        coordinateLabel.setText(str);
    }
}
