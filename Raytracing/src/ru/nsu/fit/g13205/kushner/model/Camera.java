package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.figure.Quadrangle;
import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;
import ru.nsu.fit.g13205.kushner.utils.Vector;

import java.util.Set;

/**
 * Created by Konstantin on 15.05.2016.
 */
public class Camera {

    private double[] viewPoint = Settings.DEFAULT_VIEW_POINT;
    private double[] eyePoint = Settings.DEFAULT_EYE_POINT;
    private double[] upVector = Settings.DEFAULT_UP_VECTOR;
    private static final double DELTA = 2d;
    private double[][] matrixOfCamera;

    public Camera() {
    }

    public Camera(double[] viewPoint, double[] eyePoint, double[] upVector ) {
        this.viewPoint = viewPoint;
        this.eyePoint = eyePoint;
        this.upVector = upVector;
        this.matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);
    }

    public void moveCamera(double angleX, double angleY, double angleZ){

        double[][] rotateMatrix = {{1d, 0d, 0d, 0d},
                {0d, 1d, 0d, 0d},
                {0d, 0d, 1d, 0d},
                {0d, 0d, 0d, 1d}};

        double[][] xMatrix = MatrixFactory.getXAxisRotation(angleX);
        double[][] yMatrix = MatrixFactory.getYAxisRotation(angleY);
        double[][] zMatrix = MatrixFactory.getZAxisRotation(angleZ);
        rotateMatrix = MatrixFactory.matrixMultiplication(xMatrix, rotateMatrix);
        rotateMatrix = MatrixFactory.matrixMultiplication(yMatrix, rotateMatrix);
        rotateMatrix = MatrixFactory.matrixMultiplication(zMatrix, rotateMatrix);

        double[][] tmp = MatrixFactory.matrixMultiplication(MatrixFactory.getInvertRotateMatrix(rotateMatrix), MatrixFactory.getMoveMatrix(-viewPoint[0],
                -viewPoint[1], -viewPoint[2]));

        rotateMatrix = MatrixFactory.matrixMultiplication(MatrixFactory.getMoveMatrix(viewPoint[0],
                viewPoint[1], viewPoint[2]), tmp);

        viewPoint = multiplication(rotateMatrix, new double[]{viewPoint[0], viewPoint[1], viewPoint[2], 1d});
        eyePoint = multiplication(rotateMatrix, new double[]{eyePoint[0], eyePoint[1], eyePoint[2], 1d});


        adjustUpVector();
        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);
    }

    private double[] multiplication(double[][] matrix, double[] vector){
        double[] result = new double[4];

        for (int i = 0; i < 4; i++) {
            double sum = 0;
            for (int j = 0; j < 4; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
        }

        return new double[]{result[0], result[1], result[2]};
    }

    private void adjustUpVector(){
        double z[] = new double[3];//вектор взгляда
        z[0] = viewPoint[0] - eyePoint[0];
        z[1] = viewPoint[1] - eyePoint[1];
        z[2] = viewPoint[2] - eyePoint[2];
        MatrixFactory.normalizeVector(z);

        double right[] = MatrixFactory.vectorMultiplication(upVector, z);//вектор в право
        MatrixFactory.normalizeVector(right);

        upVector = MatrixFactory.vectorMultiplication(z, right);
        MatrixFactory.normalizeVector(upVector);
    }

    public void zoom(double ratio){

        double z[] = new double[3];//вектор взгляда
        z[0] = viewPoint[0] - eyePoint[0];
        z[1] = viewPoint[1] - eyePoint[1];
        z[2] = viewPoint[2] - eyePoint[2];
        MatrixFactory.normalizeVector(z);

        z[0] *= ratio;
        z[1] *= ratio;
        z[2] *= ratio;

        eyePoint[0] -= z[0];
        eyePoint[1] -= z[1];
        eyePoint[2] -= z[2];
        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);


    }

    public void moveToRight(){
        eyePoint[1] -= DELTA;
        viewPoint[1] -= DELTA;
        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);
    }

    public void moveToLeft(){
        eyePoint[1] += DELTA;
        viewPoint[1] += DELTA;

        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);
    }

    public void moveToUp(){
        eyePoint[2] += DELTA;
        viewPoint[2] += DELTA;
        adjustUpVector();
        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);
    }

    public void moveToDown(){
        eyePoint[2] -= DELTA;
        viewPoint[2] -= DELTA;
        adjustUpVector();
        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);

    }

    public double[][] getMatrixOfCamera(){
        return this.matrixOfCamera;
    }

    public void initPosition(double boxAbsX, double boxAbsZ, Scene scene){
        Coordinates3D tmp = scene.getCenter();

        viewPoint[0] = tmp.getX();
        viewPoint[1] = tmp.getY();
        viewPoint[2] = tmp.getZ();

        double eyeX = -boxAbsX - boxAbsZ / Math.atan(Settings.DEFAULT_EYE_ANGLE);

        eyePoint[0] = eyeX;
        eyePoint[1] = tmp.getY();
        eyePoint[2] = tmp.getZ();

        upVector = Settings.DEFAULT_UP_VECTOR;

        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);
    }

    public double[] getViewPoint() {
        return viewPoint;
    }

    public double[] getEyePoint() {
        return eyePoint;
    }

    public double[] getUpVector() {
        return upVector;
    }

    public void setViewPoint(Vector viewVector){
        this.viewPoint[0] = viewVector.getValue(0);
        this.viewPoint[1] = viewVector.getValue(1);
        this.viewPoint[2] = viewVector.getValue(2);
        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);
    }

    public void setEyePoint(Vector eyeVector){
        this.eyePoint[0] = eyeVector.getValue(0);
        this.eyePoint[1] = eyeVector.getValue(1);
        this.eyePoint[2] = eyeVector.getValue(2);
        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, upVector);
    }

    public void setUpPoint(Vector upVector){
        this.upVector[0] = upVector.getValue(0);
        this.upVector[1] = upVector.getValue(1);
        this.upVector[2] = upVector.getValue(2);
        matrixOfCamera = MatrixFactory.getMatrixOfCamera(eyePoint, viewPoint, this.upVector);
    }

}
