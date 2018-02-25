package ru.nsu.fit.g13205.kushner.model;

import java.awt.*;

/**
 * Created by Konstantin on 01.04.2016.
 */
public class Boundary {
    private final Sides side;
    private final Point point;

    public Boundary(Point point, Sides side) {
        this.side = side;
        this.point = point;
    }

    public Sides getSide() {
        return side;
    }

    public Point getPoint() {
        return point;
    }
}
