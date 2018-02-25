package ru.nsu.fit.g13205.kushner.figure;

import ru.nsu.fit.g13205.kushner.raytracing.IntersectionInfo;
import ru.nsu.fit.g13205.kushner.raytracing.Ray;
import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;
import ru.nsu.fit.g13205.kushner.utils.LineSegment;

import java.util.ArrayList;

/**
 * Created by Konstantin on 14.05.2016.
 */
public interface Figure {

    IntersectionInfo checkRay(Ray ray);

    ArrayList<LineSegment> getTransformFigure(double[][] rotateMatrix, double shiftX, double shiftY, double shiftZ);

    ArrayList<LineSegment> getTransformFigure();

    OpticProperties getOpticProperties();

    double getxMin();

    double getxMax();

    double getyMin();

    double getyMax() ;

    double getzMax();

    double getzMin();
}
