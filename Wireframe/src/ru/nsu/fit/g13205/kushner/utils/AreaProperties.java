package ru.nsu.fit.g13205.kushner.utils;

/**
 * Created by Konstantin on 26.04.2016.
 */
public class AreaProperties {
    private double zn, zf, sw, sh;

    public AreaProperties(double zn, double zf, double sw, double sh) {
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

    public void setSw(double sw) {
        this.sw = sw;
    }

    public void setZf(double zf) {
        this.zf = zf;
    }

    public void setSh(double sh) {
        this.sh = sh;
    }

    @Override
    public String toString() {
        return "AreaProperties{" +
                "zn=" + zn +
                ", zf=" + zf +
                ", sw=" + sw +
                ", sh=" + sh +
                '}';
    }
}
