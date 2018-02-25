package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class LightSource {
    //LX LY LZ LR LG LB // LX, LY, LZ – положение источника, LR, LG, LB – цвет источника
    // в пространстве RGB в диапазоне 0..255

    private final Vector coordinatesOfLightSource;
    private final Color colorOfLightSource;

    public LightSource(Vector coordinatesOfLightSource, Color colorOfLightSource) {
        this.coordinatesOfLightSource = coordinatesOfLightSource;
        this.colorOfLightSource = colorOfLightSource;
    }

    public Vector getVectorOfLightSource() {
        return coordinatesOfLightSource;
    }

    public Color getColorOfLightSource() {
        return colorOfLightSource;
    }

    @Override
    public String toString() {
        return "LightSource{" +
                "coordinatesOfLightSource=" + coordinatesOfLightSource +
                ", colorOfLightSource=" + colorOfLightSource +
                '}';
    }
}
