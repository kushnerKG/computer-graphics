package ru.nsu.fit.g13205.kushner.view.setting;

import ru.nsu.fit.g13205.kushner.SettingApplication;

/**
 * Created by Konstantin on 25.02.2016.
 */
public class GameAreaProperties {
    private int m;
    private int n;
    private int size;
    private int widthBorder;
    private int mode;

    public GameAreaProperties(int m, int n, int size, int mode, int widthBorder) {
        this.m = m;
        this.n = n;
        this.size = size;
        this.mode = mode;
        this.widthBorder = widthBorder;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getSize() {
        return size;
    }

    public int getWidthBorder() {
        return widthBorder;
    }

    public int getMode() {
        return mode;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setWidthBorder(int widthBorder) {
        this.widthBorder = widthBorder;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
