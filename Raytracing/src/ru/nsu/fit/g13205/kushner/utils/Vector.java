package ru.nsu.fit.g13205.kushner.utils;

import java.util.Arrays;

/**
 * Created by Konstantin on 27.05.2016.
 */
public class Vector {
    private double[] vector = new double[3];

    public Vector(Coordinates3D coord){
        vector[0] = coord.getX();
        vector[1] = coord.getY();
        vector[2] = coord.getZ();
    }

    public Vector(double x, double y, double z) {
        vector[0] = x;
        vector[1] = y;
        vector[2] = z;
    }

    public Vector(double[] values) {
        vector[0] = values[0];
        vector[1] = values[1];
        vector[2] = values[2];
    }

    public double getValue(int index){
        return vector[index];
    }

    public double getLen(){
        return Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
    }

    public void normalizeVector(){
        double lenK = 0;
        for(int i = 0; i < 3; i++){
            lenK += vector[i] * vector[i];
        }
        lenK = Math.sqrt(lenK);

        for(int i = 0; i < 3; i++){
            vector[i] /= lenK;
        }
    }

    public Vector getNormalizeVector(){
        double lenK = 0;

        for(int i = 0; i < 3; i++){
            lenK += vector[i] * vector[i];
        }
        lenK = Math.sqrt(lenK);

        double[] v = new double[3];

        for(int i = 0; i < 3; i++){
            v[i] = vector[i] / lenK;
        }
        return new Vector(v[0], v[1], v[2]);
    }

    public void multipleOnScalar(double scalar){
        vector[0] *= scalar;
        vector[1] *= scalar;
        vector[2] *= scalar;
    }

    public Vector getMultipleOnScalar(double scalar){
        double[] tmp = new double[3];
        tmp[0] = vector[0] * scalar;
        tmp[1] = vector[1] * scalar;
        tmp[2] = vector[2] * scalar;
        return new Vector(tmp);
    }

    public static Vector additionVector(Vector v1, Vector v2){
        return new Vector(v1.getValue(0) + v2.getValue(0), v1.getValue(1) + v2.getValue(1), v1.getValue(2) + v2.getValue(2));
    }

    public static Vector diffVector(Vector v1, Vector v2){
        return new Vector(v1.getValue(0) - v2.getValue(0), v1.getValue(1) - v2.getValue(1), v1.getValue(2) - v2.getValue(2));
    }

    public static Vector vectorMultiplication(Vector v1, Vector v2){
        double[] result = new double[3];
        result[0] = v1.getValue(2)*v2.getValue(1) - v1.getValue(1)*v2.getValue(2);
        result[1] = v1.getValue(0)*v2.getValue(2) - v1.getValue(2)*v2.getValue(0);
        result[2] = v1.getValue(1)*v2.getValue(0) - v1.getValue(0)*v2.getValue(1);
        /*
        result[0] = a[2]*b[1] - a[1]*b[2];
        result[1] = a[0]*b[2] - a[2]*b[0];
        result[2] = a[1]*b[0] - a[0]*b[1];
         */

        return new Vector(result);
    }

    public static double scalarMultiplication(Vector v1, Vector v2){
        double value = 0;

        for(int i = 0; i < 3; i++){
            value += v1.getValue(i) * v2.getValue(i);
        }

        return value;
    }

    public static double norma(Vector v1, Vector v2){
        double acc = 0;
        for(int i = 0; i < 3; i++){
            acc += (v1.getValue(i)-v2.getValue(i)) * (v1.getValue(i)-v2.getValue(i));
        }

        return Math.sqrt(acc);
    }

    public double[] toArray(){
        double[] returnValue = new double[3];
        returnValue[0] = vector[0];
        returnValue[1] = vector[1];
        returnValue[2] = vector[2];
        return returnValue;
    }

    public Coordinates3D toCoordinate3D(){
        return new Coordinates3D(vector[0], vector[1], vector[2], 1);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "vector=" + Arrays.toString(vector) +
                '}';
    }
}
