package ru.nsu.fit.g13205.kushner.utils;

import ru.nsu.fit.g13205.kushner.model.Spline;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Konstantin on 26.04.2016.
 */
public class FigureFileInfo {

    private double n, m, k, a, b, c, d;
    private double cX, cY, cZ;
    private double[][] rotateMatrix;
    private ArrayList<Coordinates> splinePoints;
    private Color color;

    public FigureFileInfo(double n, double m, double k, double a, double b, double c, double d, double cX, double cY, double cZ,
                          double[][] rotateMatrix, ArrayList<Coordinates> points, Color color) {
        this.splinePoints = points;
        this.k = k;
        this.n = n;
        this.a = a;
        this.m = m;
        this.b = b;
        this.c = c;
        this.d = d;
        this.cY = cY;
        this.cX = cX;
        this.cZ = cZ;
        this.rotateMatrix = rotateMatrix;
        this.color = color;
    }

    public FigureFileInfo(double cX, double cY, double cZ, double[][] rotateMatrix, ArrayList<Coordinates> points, Color color) {
        this.cY = cY;
        this.cX = cX;
        this.cZ = cZ;
        this.splinePoints = points;
        this.rotateMatrix = rotateMatrix;
        this.color = color;
    }

    public double getN() {
        return n;
    }

    public double getA() {
        return a;
    }

    public double getM() {
        return m;
    }

    public double getC() {
        return c;
    }

    public double getB() {
        return b;
    }

    public double getD() {
        return d;
    }

    public double getcX() {
        return cX;
    }

    public double getcY() {
        return cY;
    }

    public double getcZ() {
        return cZ;
    }

    public double[][] getRotateMatrix() {
        return rotateMatrix;
    }

    public Color getColor() {
        return color;
    }

    public double getK() {
        return k;
    }

    public ArrayList<Coordinates> getSplinePoints() {
        return splinePoints;
    }

    public void setN(double n) {
        this.n = n;
    }

    public void setM(double m) {
        this.m = m;
    }

    public void setK(double k) {
        this.k = k;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setD(double d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "FigureFileInfo{" +
                "n=" + n +
                ", m=" + m +
                ", k=" + k +
                ", a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", cX=" + cX +
                ", cY=" + cY +
                ", cZ=" + cZ +
                ", rotateMatrix=" + Arrays.toString(rotateMatrix) +
                ", splinePoints=" + splinePoints +
                '}';
    }
}
