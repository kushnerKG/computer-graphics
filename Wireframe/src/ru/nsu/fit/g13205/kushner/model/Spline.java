package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.utils.Coordinates;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Konstantin on 14.04.2016.
 */
public class Spline {

    private ArrayList<Coordinates> coordinatesPoints;
    private ArrayList<Point> pixelPoints;
    private ArrayList<Point> middlePoints;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private double shift;
    private double delta;
    private double len;


    public Spline(ArrayList<Coordinates> coordinatesPoints, ArrayList<Point> pixelPoints, ArrayList<Point> middlePoints,
                  double xMin, double xMax, double yMin, double yMax, double delta, double len) {
        this.coordinatesPoints = coordinatesPoints;
        this.pixelPoints = pixelPoints;
        this.middlePoints = middlePoints;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        shift = xMax - (xMax - xMin) / 2;
        this.delta = delta;
        this.len = len;
    }

    public ArrayList<Coordinates> getCoordinatesPoints() {
        return coordinatesPoints;
    }

    public ArrayList<Point> getPixelPoints() {
        return pixelPoints;
    }

    public ArrayList<Point> getMiddlePoints() {
        return middlePoints;
    }

    public double getxMin() {
        return xMin;
    }

    public double getxMax() {
        return xMax;
    }

    public double getyMin() {
        return yMin;
    }

    public double getyMax() {
        return yMax;
    }

    public double getDelta() {
        return delta;
    }

    public double getLen() {
        return len;
    }

    public Coordinates getRealPoint(Point point){
        int index = pixelPoints.indexOf(point);
        return coordinatesPoints.get(index);
    }



    public int getIndexNewBasicPoint(Point point){
        return middlePoints.indexOf(point) + 1;
    }

    public int indexOfBasicPoint(Point point){
        return pixelPoints.indexOf(point);
    }

    public double getShift() {
        return shift;
    }
}
