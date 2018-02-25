package ru.nsu.fit.g13205.kushner.raytracing;

import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;
import ru.nsu.fit.g13205.kushner.utils.Vector;

/**
 * Created by Konstantin on 22.05.2016.
 */
public class Ray {
    private Vector rayStart;
    private Vector rayDirection;

    public Ray(Vector rayStart, Vector rayDirection, int i) {
        this.rayStart = rayStart;
        this.rayDirection = rayDirection.getNormalizeVector();
    }

    public Ray(Vector rayStart, Vector rayDirection) {
        this.rayStart = rayStart;
        createRayDirection(rayDirection);
    }

    private void createRayDirection(Vector v){
        double x, y, z;

        double lenK = 0;
        for(int i = 0; i < 3; i++){
            lenK += (v.getValue(i) - rayStart.getValue(i)) * (v.getValue(i) - rayStart.getValue(i));
        }
        lenK = Math.sqrt(lenK);

        x = (v.getValue(0) - rayStart.getValue(0))/lenK;
        y = (v.getValue(1) - rayStart.getValue(1))/lenK;
        z = (v.getValue(2) - rayStart.getValue(2))/lenK;

        rayDirection = new Vector(x, y, z);
    }

    public Vector getRayStart() {
        return rayStart;
    }

    public Vector getRayDirection() {
        return rayDirection;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "rayStart=" + rayStart +
                ", rayDirection=" + rayDirection +
                '}';
    }
}
