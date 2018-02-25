package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class WindowPanel extends JPanel {

    private JLabel statusBarLabel;
    private Controller controller;
    private BufferedImage image;

    private Point pressedPoint;
    private boolean rotation = false;
    private boolean pressedControl = false;

    private boolean isUpdatable = true;

    private double znRatio = 0.95;


    public WindowPanel(Controller controller) {
        super();
        this.controller = controller;
        setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350 * 3 + 40, 370));
        this.add(createStatusBar(), BorderLayout.SOUTH);
        this.addMouseListener(mouseClicked());
        this.addMouseMotionListener(mouseMotion());
        this.addMouseWheelListener(mouseWheelListener());
        Toolkit.getDefaultToolkit().addAWTEventListener(keyListener(), AWTEvent.KEY_EVENT_MASK);
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
        controller.setHeightImage(this.getHeight() - 5);
        if(isUpdatable) {
            if (controller != null) {
                try {
                    controller.updateMainImage();
                } catch (NullPointerException ignored) {

                }
            }
        }
        if (image != null) {
            graphics.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        }

    }

    @Override
    public void repaint(){
        super.repaint();
    }

    private AWTEventListener keyListener(){
        return new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
                java.awt.event.KeyEvent kEvt = (java.awt.event.KeyEvent) event;
                if (kEvt.getID() == KeyEvent.KEY_PRESSED) {
                    if(kEvt.getKeyCode() == kEvt.VK_CONTROL){
                        pressedControl = true;
                        return;
                    }

                    if(kEvt.getKeyCode() == kEvt.VK_UP){
                        controller.moveCameraToUp();
                        return;
                    }

                    if(kEvt.getKeyCode() == kEvt.VK_DOWN){
                        controller.moveCameraToDown();
                        return;
                    }

                    if(kEvt.getKeyCode() == kEvt.VK_LEFT){
                        controller.moveCameraToLeft();
                        return;
                    }

                    if(kEvt.getKeyCode() == kEvt.VK_RIGHT){
                        controller.moveCameraToRight();
                    }

                }else if(kEvt.getID() == KeyEvent.KEY_RELEASED) {
                    if(kEvt.getKeyCode() == kEvt.VK_CONTROL){
                        pressedControl = false;
                    }

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
                    double deltaX =  pressedPoint.getX() - x;
                    double deltaY =  y - pressedPoint.getY();
                    //double deltaX =  -pressedPoint.getX() + x;
                    //double deltaY =  -y + pressedPoint.getY();
                    controller.rotate(0, -deltaY * 0.0005d,  deltaX * 0.0005d);

                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        };
    }

    private MouseAdapter mouseWheelListener(){
        return new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                if(pressedControl){
                    if(e.getWheelRotation() > 0){
                        controller.zoom(0.3);
                    }else{
                        controller.zoom(-0.3);
                    }
                    return;
                }

                if(e.getWheelRotation() > 0){
                    controller.changeZnWithWeel(znRatio);
                }else{
                    controller.changeZnWithWeel(1d/znRatio);
                }
            }
        };
    }

    public void updateImage(BufferedImage image){
        this.image = image;
    }

    public JLabel getStatusBarLabel() {
        return statusBarLabel;
    }

    public void setUpdatable(boolean isUpdatable) {
        this.isUpdatable = isUpdatable;
    }

    public void setStatusBarText(String text) {
        statusBarLabel.setText(text);
    }

}
