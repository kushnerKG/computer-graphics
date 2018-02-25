package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.utils.Coordinates;

import java.util.ArrayList;

/**
 * Created by Konstantin on 15.04.2016.
 */
public class ApproximatedSpline {//хранить точки, которые разбивают сплайн на n*k точек по длине

    private ArrayList<Coordinates> basicPoint;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private Spline spline;

    public ApproximatedSpline(Spline spline, ArrayList<Coordinates> basicPoint, double xMin, double xMax, double yMin, double yMax) {
        this.basicPoint = basicPoint;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.spline = spline;
    }

    public ArrayList<Coordinates> getBasicPoint() {
        return basicPoint;
    }

    public ArrayList<Coordinates> getSplinePoints(){
        return spline.getCoordinatesPoints();
    }

    public double getxMin() {
        return xMin;
    }

    public double getxMax() {
        return xMax;
    }

    public double getyMax() {
        return yMax;
    }

    public double getyMin() {
        return yMin;
    }
}
