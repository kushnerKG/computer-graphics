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
public class Box implements Figure {

    private final Coordinates3D minPoint;
    private final Coordinates3D maxPoint;
    private final OpticProperties opticProperties;

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();
    private double xMin, xMax, yMin, yMax, zMin, zMax;

    private ArrayList<LineSegment> transformFigure;

    public Box(Coordinates3D minPoint, Coordinates3D maxPoint, OpticProperties opticProperties) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.opticProperties = opticProperties;

        calculateLineSegment();
    }

    private void calculateLineSegment(){
        Coordinates3D vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7, vertex8;
        double deltaX, deltaY, deltaZ;

        deltaX = maxPoint.getX() - minPoint.getX();
        deltaY = maxPoint.getY() - minPoint.getY();
        deltaZ = maxPoint.getZ() - maxPoint.getY();

        vertex1 = minPoint;
        vertex2 = new Coordinates3D(vertex1.getX() + deltaX, vertex1.getY(), vertex1.getZ(), 1);
        vertex3 = new Coordinates3D(vertex1.getX(), vertex1.getY() + deltaY, vertex1.getZ(), 1);
        vertex4 = new Coordinates3D(vertex1.getX() + deltaX, vertex1.getY() + deltaY, vertex1.getZ(), 1);
        lineSegments.addAll(separateLine(vertex1, vertex2));
        lineSegments.addAll(separateLine(vertex1, vertex3));
        lineSegments.addAll(separateLine(vertex2, vertex4));
        lineSegments.addAll(separateLine(vertex3, vertex4));

        vertex5 = maxPoint;
        vertex6 = new Coordinates3D(vertex5.getX() - deltaX, vertex5.getY(), vertex5.getZ(), 1);
        vertex7 = new Coordinates3D(vertex5.getX(), vertex5.getY() - deltaY, vertex5.getZ(), 1);
        vertex8 = new Coordinates3D(vertex5.getX() - deltaX, vertex5.getY() - deltaY, vertex5.getZ(), 1);

        lineSegments.addAll(separateLine(vertex5, vertex6));
        lineSegments.addAll(separateLine(vertex5, vertex7));
        lineSegments.addAll(separateLine(vertex8, vertex6));
        lineSegments.addAll(separateLine(vertex8, vertex7));

        lineSegments.addAll(separateLine(vertex1, vertex8));
        lineSegments.addAll(separateLine(vertex2, vertex7));
        lineSegments.addAll(separateLine(vertex3, vertex6));
        lineSegments.addAll(separateLine(vertex4, vertex5));

    }

    private ArrayList<LineSegment> separateLine(Coordinates3D p1, Coordinates3D p2){
        ArrayList<LineSegment> lineSegments = new ArrayList<>();
        int m = Settings.BOX_M;
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

    public Coordinates3D getMinPoint() {
        return minPoint;
    }

    public Coordinates3D getMaxPoint() {
        return maxPoint;
    }

    public OpticProperties getOpticProperties() {
        return opticProperties;
    }

    @Override
    public String toString() {
        return "Box{" +
                "minPoint=" + minPoint +
                ", maxPoint=" + maxPoint +
                ", opticProperties=" + opticProperties +
                '}';
    }

    @Override
    public IntersectionInfo checkRay(Ray ray) {

        double t1, t2;

        double tNear = Double.NEGATIVE_INFINITY;
        double tFar = Double.POSITIVE_INFINITY;

        if(ray.getRayDirection().getValue(0) == 0){
            double x = ray.getRayStart().getValue(0);
            if(x < minPoint.getX() || x > maxPoint.getX()){
                return null;
            }
        }

        t1 = (minPoint.getX() - ray.getRayStart().getValue(0)) / ray.getRayDirection().getValue(0);
        t2 = (maxPoint.getX() - ray.getRayStart().getValue(0)) / ray.getRayDirection().getValue(0);

        if(t2 < t1){
            double tmp;
            tmp = t1;
            t1 = t2;
            t2 = tmp;
        }

        if(t1 > tNear){
            tNear = t1;
        }

        if(t2 < tFar){
            tFar = t2;
        }

        if(tNear > tFar){
            return null;
        }

        if(tFar < 0){
            return null;
        }


        ///////////////////////////////

        if(ray.getRayDirection().getValue(1) == 0){
            double x = ray.getRayStart().getValue(1);
            if(x < minPoint.getY() || x > maxPoint.getY()){
                return null;
            }
        }

        t1 = (minPoint.getY() - ray.getRayStart().getValue(1)) / ray.getRayDirection().getValue(1);
        t2 = (maxPoint.getY() - ray.getRayStart().getValue(1)) / ray.getRayDirection().getValue(1);

        if(t2 < t1){
            double tmp;
            tmp = t1;
            t1 = t2;
            t2 = tmp;
        }

        if(t1 > tNear){
            tNear = t1;
        }

        if(t2 < tFar){
            tFar = t2;
        }

        if(tNear > tFar){
            return null;
        }

        if(tFar < 0){
            return null;
        }
//////////////////////////


        if(ray.getRayDirection().getValue(2) == 0){
            double x = ray.getRayStart().getValue(2);
            if(x < minPoint.getZ() || x > maxPoint.getZ()){
                return null;
            }
        }

        t1 = (minPoint.getZ() - ray.getRayStart().getValue(2)) / ray.getRayDirection().getValue(2);
        t2 = (maxPoint.getZ() - ray.getRayStart().getValue(2)) / ray.getRayDirection().getValue(2);

        if(t2 < t1){
            double tmp;
            tmp = t1;
            t1 = t2;
            t2 = tmp;
        }

        if(t1 > tNear){
            tNear = t1;
        }

        if(t2 < tFar){
            tFar = t2;
        }

        if(tNear > tFar){
            return null;
        }

        if(tFar < 0){
            return null;
        }

        if(tNear<0){
            return null;
        }

        double t = tNear;


        Vector intersection = new Vector(ray.getRayStart().getValue(0) + t * ray.getRayDirection().getValue(0),
                ray.getRayStart().getValue(1) + t * ray.getRayDirection().getValue(1),
                ray.getRayStart().getValue(2) + t * ray.getRayDirection().getValue(2));

        double len = Vector.norma(ray.getRayStart(), intersection);

        return new IntersectionInfo(intersection, opticProperties, calculateNormal(intersection), len);

    }

    private Vector calculateNormal(Vector intersection){
        double x = intersection.getValue(0);
        double y = intersection.getValue(1);
        double z = intersection.getValue(2);

        double xMin = minPoint.getX();
        double yMin = minPoint.getY();
        double zMin = minPoint.getZ();

        double xMax = maxPoint.getX();
        double yMax = maxPoint.getY();
        double zMax = maxPoint.getZ();

        if(Math.abs(x - xMin) < 0.0000001){
            return new Vector(-1d, 0, 0);
        }

        if(Math.abs(x - xMax) < 0.00000001){
            return new Vector(1d, 0, 0);
        }
//////////////////////////
        if(Math.abs(y - yMin) < 0.0000001){
            return new Vector(0, -1d, 0);
        }

        if(Math.abs(y - yMax) < 0.00000001){
            return new Vector(0, 1d, 0);
        }
//////////////////////////
        if(Math.abs(z - zMin) < 0.0000001){
            return new Vector(0, 0, -1d);
        }

        if(Math.abs(z - zMax) < 0.00000001){
            return new Vector(0, 0, 1d);
        }
        return null;
    }


    @Override
    public ArrayList<LineSegment> getTransformFigure(double[][] transformMatrix, double shiftX, double shiftY, double shiftZ) {



        transformFigure = new ArrayList<>();
        //transformMatrix = MatrixFactory.matrixMultiplication(
        //        transformMatrix, MatrixFactory.getMoveMatrix(-shiftX, -shiftY, -shiftZ));


        double xMin = 0, xMax = 0, yMin = 0, yMax = 0, zMin = 0, zMax = 0;

        boolean set = false;

        double x1 = 0, y1 = 0, z1 = 0, u1 = 0;

        for(LineSegment ls: lineSegments){
            Coordinates3D p1 = ls.getStart();
            Coordinates3D p2 = ls.getFinish();

            double[][] v1 = MatrixFactory.matrixMultiplication(transformMatrix,
                    new double[][]{{p1.getX()}, {p1.getY()},{p1.getZ()},{1d}});

            double[][] v2 = MatrixFactory.matrixMultiplication(transformMatrix,
                    new double[][]{{p2.getX()}, {p2.getY()},{p2.getZ()},{1d}});

            x1 = v1[0][0]/(v1[3][0]);
            y1 = v1[1][0]/(v1[3][0]);
            z1 = v1[2][0];
            u1 = v1[3][0];

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
                zMax = z1
                ;
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
