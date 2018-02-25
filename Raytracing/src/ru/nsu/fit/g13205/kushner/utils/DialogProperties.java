package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;

/**
 * Created by Konstantin on 31.05.2016.
 */
public class DialogProperties {

    private final Color backgroundColor;
    private final double gamma;
    private final int depth;
    private final int quality;
    private final double sw;
    private final double sh;
    private final double zn;
    private final double zf;

    public DialogProperties(Color backgroundColor, double gamma, int depth, int quality, double sw, double sh, double zn, double zf) {
        this.backgroundColor = backgroundColor;
        this.gamma = gamma;
        this.depth = depth;
        this.quality = quality;
        this.sw = sw;
        this.sh = sh;
        this.zn = zn;
        this.zf = zf;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public double getGamma() {
        return gamma;
    }

    public int getQuality() {
        return quality;
    }

    public int getDepth() {
        return depth;
    }

    public double getSw() {
        return sw;
    }

    public double getZn() {
        return zn;
    }

    public double getSh() {
        return sh;
    }

    public double getZf() {
        return zf;
    }

    @Override
    public String toString() {
        return "DialogProperties{" +
                "backgroundColor=" + backgroundColor +
                ", gamma=" + gamma +
                ", depth=" + depth +
                ", quality=" + quality +
                ", sw=" + sw +
                ", sh=" + sh +
                ", zn=" + zn +
                ", zf=" + zf +
                '}';
    }
}
