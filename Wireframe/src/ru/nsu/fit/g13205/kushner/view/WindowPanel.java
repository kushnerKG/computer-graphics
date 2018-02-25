package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 10.04.2016.
 */
public class WindowPanel extends JPanel {

    private JLabel statusBarLabel;
    private BufferedImage image;
    private Controller controller;

    private Point pressedPoint;
    private Point releasedPoint;
    private boolean rotation = false;
    private int count = 0;

    public WindowPanel(Controller controller) {
        super();
        this.controller = controller;
        setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350 * 3 + 40, 370));
        this.addMouseListener(mouseClicked());
        this.addMouseMotionListener(mouseMotion());
        this.add(createStatusBar(), BorderLayout.SOUTH);
        this.addMouseWheelListener(mouseWheelListener());

    }

    private JPanel createStatusBar(){

        JPanel tmpPanel = new JPanel();
        tmpPanel.setBackground(new Color(134, 151, 255));
        tmpPanel.setLayout(new BorderLayout());

        statusBarLabel = new JLabel("Status bar");

        tmpPanel.add(statusBarLabel, BorderLayout.WEST);

        return tmpPanel;
    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        if(controller!=null) {
            image = controller.getMainImage();
        }
        if(image!=null){
            graphics.drawImage(image, 10,10, image.getWidth(), image.getHeight(), null);
        }

    }



    @Override
    public void repaint(){
        super.repaint();
    }

    private MouseAdapter mouseWheelListener(){
        return new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                if(e.getWheelRotation() > 0){
                    controller.handleUpdateZn(0.3);
                }else{
                    controller.handleUpdateZn(-0.3);
                }
            }
        };
    }

    private MouseAdapter mouseClicked(){

        return new MouseAdapter() {


            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressedPoint = new Point(e.getX(), e.getY());
                rotation = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                rotation = false;
            }
        };
    }

    private MouseAdapter mouseMotion(){

        return new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(rotation) {
                    int x = e.getX();
                    int y = e.getY();
                    //double deltaX =  pressedPoint.getX() - x;
                    //double deltaY =  y-pressedPoint.getY();
                    double deltaX =  -pressedPoint.getX() + x;
                    double deltaY =  -y + pressedPoint.getY();
                    controller.handleRotate(deltaY * 0.0005d, -deltaX * 0.0005d, 0);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        };
    }


    public JLabel getStatusBarLabel() {
        return statusBarLabel;
    }

    public void updateImage(BufferedImage image) {
        this.image = image;
    }
}
