package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;

/**
 * Created by Konstantin on 15.04.2016.
 */
public class Properties {
    private int n, m, k;
    private double a, b, c, d, cX, cY, cZ;

    private Color color;

    public Properties(int n, int m, int k, double a, double b, double c, double d, Color color, double cX, double cY, double cZ) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.b = b;
        this.a = a;
        this.c = c;
        this.d = d;
        this.color = color;
        this.cX = cX;
        this.cY = cY;
        this.cZ = cZ;
    }

    public int getN() {
        return n;
    }

    public double getD() {
        return d;
    }

    public double getC() {
        return c;
    }

    public double getB() {
        return b;
    }

    public double getA() {
        return a;
    }

    public int getK() {
        return k;
    }

    public int getM() {
        return m;
    }

    public Color getColor() {
        return color;
    }

    public double getcX() {
        return cX;
    }

    public double getcZ() {
        return cZ;
    }

    public double getcY() {
        return cY;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setcX(double cX) {
        this.cX = cX;
    }

    public void setcY(double cY) {
        this.cY = cY;
    }

    public void setcZ(double cZ) {
        this.cZ = cZ;
    }

    @Override
    public String toString() {
        return "Properties{" +
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
                ", color=" + color +
                '}';
    }
}
