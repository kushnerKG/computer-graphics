package ru.nsu.fit.g13205.kushner.utils;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class LineSegment {

    private final Coordinates3D start;
    private final Coordinates3D finish;

    public LineSegment(Coordinates3D start, Coordinates3D finish) {
        this.start = start;
        this.finish = finish;
    }

    public Coordinates3D getStart() {
        return start;
    }

    public Coordinates3D getFinish() {
        return finish;
    }
}
