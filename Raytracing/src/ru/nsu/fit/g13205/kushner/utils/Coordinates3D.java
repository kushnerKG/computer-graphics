package ru.nsu.fit.g13205.kushner.utils;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class Coordinates3D {
    private double x;
    private double y;
    private double z;
    private double u;

    public Coordinates3D(double x, double y, double z, double u) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
    }

    public Coordinates3D(double[] coord) {
        this.x = coord[0];
        this.y = coord[1];
        this.z = coord[2];
        this.u = coord[3];
    }

    public double getZ() {
        return z;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public double getU() {
        return u;
    }

    @Override
    public String toString() {
        return "Coordinates3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", u=" + u +
                '}';
    }
}
