package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;

import java.util.ArrayList;

/**
 * Created by Konstantin on 20.04.2016.
 */
public class WireFrame {//тут непосредственно повернутое и перемещенное изображение в точку (cx, cy, cz) для отображения в мире
    //этот обьект возвращает Surface когда необходимо перерисовать изображение

    private final ArrayList<ArrayList<Coordinates3D>> xSections;
    private final ArrayList<ArrayList<Coordinates3D>> phSections;
    private final ArrayList<ArrayList<Coordinates3D>> axis;
    private final double xMax, yMax, zMax;

    public WireFrame(ArrayList<ArrayList<Coordinates3D>> xSections, ArrayList<ArrayList<Coordinates3D>> phSections,
                     ArrayList<ArrayList<Coordinates3D>> axis, double xMax, double yMax, double zMax) {
        this.xSections = xSections;
        this.phSections = phSections;
        this.axis = axis;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    public ArrayList<ArrayList<Coordinates3D>> getxSections() {
        return xSections;
    }

    public ArrayList<ArrayList<Coordinates3D>> getPhSections() {
        return phSections;
    }

    public ArrayList<ArrayList<Coordinates3D>> getAxis() {
        return axis;
    }

    public double getxMax() {
        return xMax;
    }

    public double getyMax() {
        return yMax;
    }

    public double getzMax() {
        return zMax;
    }
}
