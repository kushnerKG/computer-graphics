package ru.nsu.fit.g13205.kushner.raytracing;

import ru.nsu.fit.g13205.kushner.figure.OpticProperties;
import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;
import ru.nsu.fit.g13205.kushner.utils.Vector;

import java.util.ArrayList;

/**
 * Created by Konstantin on 23.05.2016.
 */
public class IntersectionInfo {
    private Vector intersectionPoints;
    private OpticProperties opticProperties;
    private double len;
    private Vector normal;

    public IntersectionInfo(Vector intersectionPoints, OpticProperties opticProperties, Vector normal, double len) {
        this.intersectionPoints = intersectionPoints;
        this.opticProperties = opticProperties;
        this.len = len;
        this.normal = normal;
    }

    public Vector getIntersectionPoints() {
        return intersectionPoints;
    }

    public OpticProperties getOpticProperties() {
        return opticProperties;
    }

    public double getLen() {
        return len;
    }

    public Vector getNormal() {
        return normal;
    }

    @Override
    public String toString() {
        return "IntersectionInfo{" +
                "intersectionPoints=" + intersectionPoints +
                '}';
    }
}
