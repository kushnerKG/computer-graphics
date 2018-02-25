package ru.nsu.fit.g13205.kushner.model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 24.03.2016.
 */
public interface ModelListener {

    void updateImage(BufferedImage image, Point start, Point finish);

    void updateCoordinates(double x, double y);

}
