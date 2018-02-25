package ru.nsu.fit.g13205.kushner.utils;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class Coordinates2D {

    private double x;
    private double y;

    public Coordinates2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
