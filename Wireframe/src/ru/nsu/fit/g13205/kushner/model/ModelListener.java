package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.utils.Coordinates;

import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 10.04.2016.
 */
public interface ModelListener {

    void updateImage(BufferedImage image, Spline spline);

    void updateImageAndDraggedPoint(BufferedImage image, Spline spline, Coordinates point);

    public void WindowPanelRepaint();
}
