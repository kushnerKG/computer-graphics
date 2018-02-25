package ru.nsu.fit.g13205.kushner.view.setting;

/**
 * Created by Konstantin on 21.02.2016.
 */
public class EditSetting {

    private int m;
    private int n;
    private int cellSize;
    private int lineWidth;
    private int mode;

    public EditSetting(int m, int n, int cellSize, int lineWidth, int mode) {
        this.m = m;
        this.n = n;
        this.cellSize = cellSize;
        this.mode = mode;
        this.lineWidth = lineWidth;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public int getMode() {
        return mode;
    }
}
