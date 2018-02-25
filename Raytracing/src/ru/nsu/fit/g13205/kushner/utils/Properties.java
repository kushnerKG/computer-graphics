package ru.nsu.fit.g13205.kushner.utils;

/**
 * Created by Konstantin on 16.05.2016.
 */
public class Properties {

    private double zn, zf, sw, sh;

    public Properties(double zn, double zf, double sw, double sh) {
        this.zn = zn;
        this.zf = zf;
        this.sw = sw;
        this.sh = sh;
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

    public void setZn(double zn) {
        this.zn = zn;
    }

    public void setZf(double zf) {
        this.zf = zf;
    }

    public void setSw(double sw) {
        this.sw = sw;
    }

    public void setSh(double sh) {
        this.sh = sh;
    }
}
