package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.model.Spline;
import ru.nsu.fit.g13205.kushner.utils.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 15.04.2016.
 */
public class ImagePanel extends JPanel {

    private BufferedImage image;
    private Spline spline;
    private Controller controller;
    private Coordinates draggedPoint;
    private CreatingSplineDialog creatingSplineDialog;

    private boolean isPressed = false;

    public ImagePanel(Controller controller, CreatingSplineDialog creatingSplineDialog) {
        super();
        this.creatingSplineDialog = creatingSplineDialog;
        this.controller = controller;
        this.addMouseListener(mouseClicked());
        this.addMouseMotionListener(mouseMotion());
        this.setPreferredSize(new Dimension(Settings.DIALOG_IMAGE_WIDTH, Settings.DIALOG_IMAGE_HEIGHT));
    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        if(image!=null){
            graphics.drawImage(image, 0, 0, this);
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
                if (e.getButton() == MouseEvent.BUTTON3){
                    int pX = e.getX();
                    int pY = e.getY();
                    for(Point tmp: spline.getPixelPoints()){
                        int deltaX = (int)Math.abs(pX - tmp.getX());
                        int deltaY = (int)Math.abs(pY - tmp.getY());
                        if(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) < Math.pow(Settings.BASIC_POINT_SIZE, 2)){
                            controller.handleDeleteBasicPoint(tmp);
                            break;
                        }
                    }
                    creatingSplineDialog.updateMainImage();
                }else{
                    int pX = e.getX();
                    int pY = e.getY();

                    for(Point tmp: spline.getMiddlePoints()){
                        int deltaX = (int)Math.abs(pX - tmp.getX());
                        int deltaY = (int)Math.abs(pY - tmp.getY());
                        if(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) < Math.pow(Settings.MIDDLE_POINT_SIZE, 2)){
                            controller.handleNewBasicPoint(pX, pY, tmp);
                            break;
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

                super.mousePressed(e);

                int pX = e.getX();
                int pY = e.getY();

                for(Point tmp: spline.getPixelPoints()){
                    int deltaX = (int)Math.abs(pX - tmp.getX());
                    int deltaY = (int)Math.abs(pY - tmp.getY());
                    if(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) < Math.pow(Settings.BASIC_POINT_SIZE, 2)){
                        isPressed = true;
                        draggedPoint = spline.getRealPoint(tmp);
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                isPressed = false;
                draggedPoint = null;
            }
        };
    }

    private MouseAdapter mouseMotion(){

        return new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                if(image != null && isPressed) {
                    controller.handleBasicPointDragged(e.getX(), e.getY(), draggedPoint);
                    creatingSplineDialog.updateMainImage();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        };
    }

    public void updateImage(BufferedImage image, Spline spline){
        this.image = image;
        this.spline = spline;
        repaint();
    }

    public void updateImageAndDraggedPoint(BufferedImage image, Spline spline, Coordinates point) {
        this.image = image;
        this.spline = spline;
        this.draggedPoint = point;
        repaint();
    }
}
