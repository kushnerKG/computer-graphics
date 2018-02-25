package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.model.Spline;
import ru.nsu.fit.g13205.kushner.utils.AreaProperties;
import ru.nsu.fit.g13205.kushner.utils.Coordinates;
import ru.nsu.fit.g13205.kushner.utils.Properties;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 15.04.2016.
 */
public interface SplineDialogInterface {

    void updateImage(BufferedImage image, Spline spline);

    void updateImageAndDraggedPoint(BufferedImage image, Spline spline, Coordinates point);

    void setMaxFigure(int number, Color color);

    void setCurrentNumberFigure(int number, Color color);

    //void setFigureNumber(int number);

    void updateProperties(Properties properties, AreaProperties areaProperties, int number);
}
