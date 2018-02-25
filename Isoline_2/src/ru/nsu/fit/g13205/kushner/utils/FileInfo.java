package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class FileInfo {
    private int k;
    private int m;
    private final int nLevels;
    private final ArrayList<Color> colorsOfLegend;
    private final Color colorBorder;

    public FileInfo(int k, int m, int nLevels, ArrayList<Color> colorsOfLegend, Color colorBorder) {
        this.k = k;
        this.m = m;
        this.nLevels = nLevels;
        this.colorsOfLegend = colorsOfLegend;
        this.colorBorder = colorBorder;
    }

    public int getK() {
        return k;
    }

    public int getM() {
        return m;
    }

    public int getnLevels() {
        return nLevels;
    }

    public ArrayList<Color> getColorsOfLegend() {
        return colorsOfLegend;
    }

    public Color getColorBorder() {
        return colorBorder;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setM(int m) {
        this.m = m;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "k=" + k +
                ", m=" + m +
                ", nLevels=" + nLevels +
                ", colorsOfLegend=" + colorsOfLegend +
                ", colorBorder=" + colorBorder +
                '}';
    }
}
