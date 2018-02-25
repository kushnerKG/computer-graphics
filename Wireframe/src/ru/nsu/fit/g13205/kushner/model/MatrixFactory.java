package ru.nsu.fit.g13205.kushner.model;

import java.util.Arrays;

/**
 * Created by Konstantin on 17.04.2016.
 */
public class MatrixFactory {

    private static double[][] getPSPMatrix(double zn){
        double[][] result = new double[4][4];

        for(int i = 0; i < 3; i++){
            result[i][i] = 1d;
        }

        result[3][2] = 1d/zn;

        return result;
    }

    public static double[][] getProjMatrix(double sw, double sh, double zn, double zf){


        double[][] result = new double[4][4];


        result[0][0] = (2d*zn) / sw;
        result[1][1] = (2d*zn) / sh;
        result[2][2] = zf / (zf - zn);
        result[2][3] = (-zn*zf) / (zf - zn);
        result[3][2] = 1d;

        return getPSPMatrix(zn);
        //return result;
        //return MatrixFactory.matrixMultiplication(result, getPSPMatrix(zn));
    }

    public static double[][] getMatrixOfCamera(double[] pCam, double[] pView, double[] vUp){

        double[] kVector = new double[3];
        for(int i = 0; i < 3; i++){
            kVector[i] = pCam[i] - pView[i];
        }
        normalizeVector(kVector);

        double[] iVector = vectorMultiplication(vUp, kVector);
        normalizeVector(iVector);

        double[] jVector = vectorMultiplication(kVector, iVector);

        double[][] M1 = new double[4][4];
        double[][] M2 = new double[4][4];
        for(int i = 0; i < 4; i++){
            M1[i][i] = 1d;
        }
        M1[0][3] = pCam[0];
        M1[1][3] = pCam[1];
        M1[2][3] = pCam[2];

        for(int i = 0; i < 3; i++){
            M2[0][i] = iVector[i];
            M2[1][i] = jVector[i];
            M2[2][i] = kVector[i];
        }
        M2[3][3] = 1d;

        return matrixMultiplication(M2, M1);
    }

    public static double[][] getInvertMatrixOfCamera(double[] pCam, double[] pView, double[] vUp){

        double[] kVector = new double[3];
        for(int i = 0; i < 3; i++){
            kVector[i] = pCam[i] - pView[i];
        }
        normalizeVector(kVector);

        double[] iVector = vectorMultiplication(vUp, kVector);
        normalizeVector(iVector);

        double[] jVector = vectorMultiplication(kVector, iVector);

        double[][] M1 = new double[4][4];
        double[][] M2 = new double[4][4];
        for(int i = 0; i < 4; i++){
            M1[i][i] = 1d;
        }
        M1[0][3] = pCam[0];
        M1[1][3] = pCam[1];
        M1[2][3] = pCam[2];

        for(int i = 0; i < 3; i++){
            M2[i][0] = iVector[i];
            M2[i][1] = jVector[i];
            M2[i][2] = kVector[i];
        }
        M2[3][3] = 1d;

        return matrixMultiplication(M1, M2);
    }

    private static void normalizeVector(double[] v){
        double lenK = 0;
        for(int i = 0; i < 3; i++){
            lenK += v[i] * v[i];
        }
        lenK = Math.sqrt(lenK);

        for(int i = 0; i < 3; i++){
            v[i] /= lenK;
        }
    }

    public static double[] vectorMultiplication(double[] a, double[] b){
        double[] result = new double[a.length];
        result[0] = -a[2]*b[1] + a[1]*b[2];
        result[1] = -a[0]*b[2] + a[2]*b[0];
        result[2] = -a[1]*b[0] + a[0]*b[1];
        return result;
    }

    public static double[][] getCompressionMatrix(double ratio){
        double[][] matrix = new double[4][4];
        matrix[0][0] = 1d/ratio;
        matrix[1][1] = 1d/ratio;
        matrix[2][2] = 1d/ratio;
        matrix[3][3] = 1d;
        return matrix;
    }

    public static double[][] getZAxisRotation(double angle){
        double[][] matrix = new double[4][4];

        matrix[0][0] = Math.cos(angle);
        matrix[0][1] = -1 * Math.sin(angle);
        matrix[1][0] = Math.sin(angle);
        matrix[1][1] = Math.cos(angle);
        matrix[2][2] = 1d;
        matrix[3][3] = 1d;

        return matrix;
    }

    public static double[][] getXAxisRotation(double angle){
        double[][] matrix = new double[4][4];
        matrix[0][0] = 1d;
        matrix[1][1] = Math.cos(angle);
        matrix[1][2] = -1 * Math.sin(angle);
        matrix[2][1] = Math.sin(angle);
        matrix[2][2] = Math.cos(angle);
        matrix[3][3] = 1d;

        return matrix;
    }

    public static double[][] getYAxisRotation(double angle){
        double[][] matrix = new double[4][4];
        matrix[1][1] = 1d;
        matrix[0][0] = Math.cos(angle);
        matrix[0][2] = -1 * Math.sin(angle);
        matrix[2][0] = Math.sin(angle);
        matrix[2][2] = Math.cos(angle);
        matrix[3][3] = 1d;
        return matrix;
    }

    public static double[][] matrixMultiplication(double[][] leftMatrix, double[][] rightMatrix){

        double[][] result = new double[leftMatrix.length][rightMatrix.length];
        for(int i = 0; i < leftMatrix.length; i++){
            for(int j = 0; j < rightMatrix[0].length; j++){
                result[i][j] = 0;
                for(int k = 0; k < rightMatrix.length; k++){
                    result[i][j] += leftMatrix[i][k] * rightMatrix[k][j];
                }
            }
        }

        return result;
    }

    public static double[][] getMoveMatrix(double[][] matrix, double deltaX, double deltaY, double deltaZ){
        double[][] resultMatrix = new double[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                resultMatrix[i][j] = matrix[i][j];
            }
        }
        matrix[0][matrix[0].length - 1] = deltaX;
        matrix[1][matrix[1].length - 1] = deltaY;
        matrix[2][matrix[2].length - 1] = deltaZ;

        return resultMatrix;
    }

    public static double[][] getMoveMatrix(double deltaX, double deltaY, double deltaZ){
        double[][] resultMatrix = new double[4][4];
        resultMatrix[0][0] = 1;
        resultMatrix[1][1] = 1;
        resultMatrix[2][2] = 1;
        resultMatrix[3][3] = 1;

        resultMatrix[0][3] = deltaX;
        resultMatrix[1][3] = deltaY;
        resultMatrix[2][3] = deltaZ;

        return resultMatrix;
    }
}
