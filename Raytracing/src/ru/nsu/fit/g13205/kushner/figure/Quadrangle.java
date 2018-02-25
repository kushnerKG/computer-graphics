package ru.nsu.fit.g13205.kushner.figure;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.model.MatrixFactory;
import ru.nsu.fit.g13205.kushner.raytracing.IntersectionInfo;
import ru.nsu.fit.g13205.kushner.raytracing.Ray;
import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;
import ru.nsu.fit.g13205.kushner.utils.LineSegment;
import ru.nsu.fit.g13205.kushner.utils.Vector;

import java.util.ArrayList;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class Quadrangle implements Figure {

    private final Coordinates3D vertex1;
    private final Coordinates3D vertex2;
    private final Coordinates3D vertex3;
    private final Coordinates3D vertex4;
    private final OpticProperties opticProperties;

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();
    private double xMin, xMax, yMin, yMax, zMin, zMax;

    private ArrayList<LineSegment> transformFigure;
    private Triangle triangle1;
    private Triangle triangle2;
    private Triangle triangle3;

    public Quadrangle(Coordinates3D vertex1, Coordinates3D vertex2, Coordinates3D vertex3,
                      Coordinates3D vertex4, OpticProperties opticProperties) {

        /*

         public Triangle(Coordinates3D coordinatesOfFirstVertex, Coordinates3D coordinatesOfSecondVertex,
                    Coordinates3D coordinatesOfThirdVertex, OpticProperties opticProperties) {

         */
        triangle1 = new Triangle(vertex1, vertex2, vertex4, opticProperties);
        triangle2 = new Triangle(vertex4, vertex2, vertex3, opticProperties);

        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.vertex4 = vertex4;
        this.opticProperties = opticProperties;



        calculateLineSegment();
    }


    private void calculateLineSegment(){
        lineSegments.addAll(separateLine(vertex1, vertex2));
        lineSegments.addAll(separateLine(vertex2, vertex3));
        lineSegments.addAll(separateLine(vertex3, vertex4));
        lineSegments.addAll(separateLine(vertex4, vertex1));
    }

    private ArrayList<LineSegment> separateLine(Coordinates3D p1, Coordinates3D p2){
        ArrayList<LineSegment> lineSegments = new ArrayList<>();
        int m = Settings.QUADRANGLE_M;
        double deltaX = (p2.getX() - p1.getX()) / m;
        double deltaY = (p2.getY() - p1.getY()) / m;
        double deltaZ = (p2.getZ() - p1.getZ()) / m;

        for(int i = 0; i < m; i++){
            Coordinates3D p12 = new Coordinates3D(p1.getX() + i * deltaX, p1.getY() + i * deltaY, p1.getZ() + i * deltaZ, 1d);
            Coordinates3D p22 = new Coordinates3D(p1.getX() + (i + 1) * deltaX, p1.getY() + (i + 1) * deltaY, p1.getZ()
                    + (i + 1) * deltaZ, 1d);

            lineSegments.add(new LineSegment(p12, p22));
        }

        return lineSegments;
    }


    public Coordinates3D getVertex1() {
        return vertex1;
    }

    public OpticProperties getOpticProperties() {
        return opticProperties;
    }

    public Coordinates3D getVertex2() {
        return vertex2;
    }

    public Coordinates3D getVertex3() {
        return vertex3;
    }

    public Coordinates3D getVertex4() {
        return vertex4;
    }

    @Override
    public String toString() {
        return "Quadrangle{" +
                "vertex1=" + vertex1 +
                ", vertex2=" + vertex2 +
                ", vertex3=" + vertex3 +
                ", vertex4=" + vertex4 +
                ", opticProperties=" + opticProperties +
                '}';
    }

    @Override
    public IntersectionInfo checkRay(Ray ray) {

        IntersectionInfo c1 = triangle1.checkRay(ray);
        IntersectionInfo c2 = triangle2.checkRay(ray);

        if(c1!=null){
            return c1;
        }else if(c2 != null){
            return c2;
        }else {
            return null;
        }
    }



    @Override
    public ArrayList<LineSegment> getTransformFigure(double[][] transformMatrix, double shiftX, double shiftY, double shiftZ) {
        transformFigure = new ArrayList<>();

        double xMin = 0, xMax = 0, yMin = 0, yMax = 0, zMin = 0, zMax = 0;

        boolean set = false;

        for(LineSegment ls: lineSegments){
            Coordinates3D p1 = ls.getStart();
            Coordinates3D p2 = ls.getFinish();

            double[][] v1 = MatrixFactory.matrixMultiplication(transformMatrix,
                    new double[][]{{p1.getX()}, {p1.getY()},{p1.getZ()},{1d}});

            double[][] v2 = MatrixFactory.matrixMultiplication(transformMatrix,
                    new double[][]{{p2.getX()}, {p2.getY()},{p2.getZ()},{1d}});

            double x1 = v1[0][0]/(v1[3][0]);
            double y1 = v1[1][0]/(v1[3][0]);
            double z1 = v1[2][0];
            double u1 = v1[3][0];

            double x2 = v2[0][0]/(v2[3][0]);
            double y2 = v2[1][0]/(v2[3][0]);
            double z2 = v2[2][0];
            double u2 = v2[3][0];

            if(!set){
                xMin = x1;
                yMin = y1;
                zMin = z1;
                xMax = x1;
                yMax = y1;
                zMax = z1;
                set = true;
            }

            xMax = x1 > xMax ? x1 : xMax;
            yMax = y1 > yMax ? y1 : yMax;
            zMax = z1 > zMax ? z1 : zMax;
            xMin = x1 < xMin ? x1 : xMin;
            yMin = y1 < yMin ? y1 : yMin;
            zMin = z1 < zMin ? z1 : zMin;

            xMax = x2 > xMax ? x2 : xMax;
            yMax = y2 > yMax ? y2 : yMax;
            zMax = z2 > zMax ? z2 : zMax;
            xMin = x2 < xMin ? x2 : xMin;
            yMin = y2 < yMin ? y2 : yMin;
            zMin = z2 < zMin ? z2 : zMin;

            transformFigure.add(new LineSegment(new Coordinates3D(x1, y1, z1, u1), new Coordinates3D(x2, y2, z2, u2)));

        }

        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;

        return transformFigure;
    }

    @Override
    public ArrayList<LineSegment> getTransformFigure() {
        return transformFigure;
    }

    @Override
    public double getxMin() {
        return xMin;
    }

    @Override
    public double getxMax() {
        return xMax;
    }

    @Override
    public double getyMin() {
        return yMin;
    }

    @Override
    public double getyMax() {
        return yMax;
    }

    @Override
    public double getzMax() {
        return zMax;
    }

    @Override
    public double getzMin() {
        return zMin;
    }
}
