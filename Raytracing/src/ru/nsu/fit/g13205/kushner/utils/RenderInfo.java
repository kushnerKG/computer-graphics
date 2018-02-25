package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class RenderInfo {

    private final Color backgroundColor;
    private final double gamma;
    private final int depth;
    private final int quality; //rough – грубое(0), normal – среднее(1), fine – высокое(2)
    private final Coordinates3D eyePoint;
    private final Coordinates3D viewPoint;
    private final Coordinates3D upVector;
    private final double zn;
    private final double zf;
    private final double sw;
    private final double sh;

    public static final int QUALITY_ROUGH = 0;
    public static final int QUALITY_NORMAL = 1;
    public static final int QUALITY_FINE = 2;

    public RenderInfo(Color backgroundColor, double gamma, int depth, int quality, Coordinates3D eyePoint,
                      Coordinates3D viewPoint, Coordinates3D upVector, double zn, double zf, double sw, double sh) {
        this.backgroundColor = backgroundColor;
        this.gamma = gamma;
        this.depth = depth;
        this.quality = quality;
        this.eyePoint = eyePoint;
        this.viewPoint = viewPoint;
        this.upVector = upVector;
        this.zn = zn;
        this.zf = zf;
        this.sw = sw;
        this.sh = sh;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public double getGamma() {
        return gamma;
    }

    public int getDepth() {
        return depth;
    }

    public Vector getEyeVector() {
        return new Vector(eyePoint.getX(), eyePoint.getY(), eyePoint.getZ());
    }

    public Vector getViewVector() {
        return new Vector(viewPoint.getX(), viewPoint.getY(), viewPoint.getZ());
    }

    public Vector getUpVector() {
        return new Vector(upVector.getX(), upVector.getY(), upVector.getZ());
    }

    public double getZn() {
        return zn;
    }

    public double getSw() {
        return sw;
    }

    public double getZf() {
        return zf;
    }

    public double getSh() {
        return sh;
    }

    public int getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return "RenderInfo{" +
                "backgroundColor=" + backgroundColor +
                ", gamma=" + gamma +
                ", depth=" + depth +
                ", quality=" + quality +
                ", eyePoint=" + eyePoint +
                ", viewPoint=" + viewPoint +
                ", upVector=" + upVector +
                ", zn=" + zn +
                ", zf=" + zf +
                ", sw=" + sw +
                ", sh=" + sh +
                '}';
    }
}
