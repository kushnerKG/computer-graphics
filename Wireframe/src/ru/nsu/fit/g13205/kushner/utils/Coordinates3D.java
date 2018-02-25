package ru.nsu.fit.g13205.kushner.utils;

/**
 * Created by Konstantin on 17.04.2016.
 */
public class Coordinates3D {

    private double x;
    private double y;
    private double z;

    private double tX;
    private double tY;
    private double tZ;
    private double tU;

    public Coordinates3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tX = x;
        this.tY = y;
        this.tZ = z;
    }

    public Coordinates3D(double[] coord) {
        this.x = coord[0];
        this.y = coord[1];
        this.z = coord[2];
        this.tX = coord[0];
        this.tY = coord[1];
        this.tZ = coord[2];
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
/*
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
*/
    public void setTransformX(double tX) {
        this.tX = tX;
    }

    public void setTransformY(double tY) {
        this.tY = tY;
    }

    public void setTransformZ(double tZ) {
        this.tZ = tZ;
    }

    public void setTransformU(double tU){
        this.tU = tU;
    }

    public double getTransformX() {
        return tX;
    }

    public double getTransformY() {
        return tY;
    }

    public double getTransformZ() {
        return tZ;
    }

    public double gettTransformU() {
        return tU;
    }

    @Override
    public String toString() {
        return "Coordinates3D{" +
                ", tX=" + tX +
                ", tY=" + tY +
                ", tZ=" + tZ +
                ", tU=" + tU +
                '}';
    }
}
