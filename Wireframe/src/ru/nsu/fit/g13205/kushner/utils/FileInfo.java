package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Konstantin on 26.04.2016.
 */
public class FileInfo {
    private double n, m, k, a, b, c, d;
    private double zn, zf, sw, sh;
    private int sceneSize;
    private ArrayList<FigureFileInfo> figures = new ArrayList<FigureFileInfo>();
    private Color backgroundColor;

    private boolean isNewFormat = false;

    private double[][] sceneRotateMatrix;

    public FileInfo(double n, double m, double k, double a, double b, double c, double d, double zn, double zf, double sw,
                    double sh, Color color, double[][] sceneMatrix) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.zn = zn;
        this.zf = zf;
        this.sw = sw;
        this.sh = sh;
        this.backgroundColor = color;
        sceneRotateMatrix = sceneMatrix;
    }

    public void addFigureInfo(FigureFileInfo figureInfo){
        figures.add(figureInfo);
    }

    public void setFigures(ArrayList<FigureFileInfo> f){
        figures = f;
    }

    public double getN() {
        return n;
    }

    public double getK() {
        return k;
    }

    public double getM() {
        return m;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public double getZn() {
        return zn;
    }

    public double getZf() {
        return zf;
    }

    public double getSw() {
        return sw;
    }

    public double getSh() {
        return sh;
    }

    public int getSceneSize() {
        return sceneSize;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public double[][] getSceneRotateMatrix() {
        return sceneRotateMatrix;
    }

    public ArrayList<FigureFileInfo> getFigures() {
        return figures;
    }

    public void setIsNewFormat(boolean value){
        isNewFormat = value;
    }

    public boolean getIsNewFormat(){
        return isNewFormat;
    }

    @Override
    public String toString() {
        String str = "";
        for(FigureFileInfo f: figures){
            str+=f.toString();
            str+="\n";
        }
        return str;
    }
}
