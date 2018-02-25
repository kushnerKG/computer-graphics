package ru.nsu.fit.g13205.kushner.figure;

import com.sun.deploy.config.VerboseDefaultConfig;
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
public class Triangle implements Figure {

    private final Coordinates3D coordinatesOfVertex1;
    private final Coordinates3D coordinatesOfVertex2;
    private final Coordinates3D coordinatesOfVertex3;
    private final OpticProperties opticProperties;

    private final Vector normal;
    private final double D;

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();
    private double xMin, xMax, yMin, yMax, zMin, zMax;


    private ArrayList<LineSegment> transformFigure;

    private final Vector projVertex1;
    private final Vector projVertex2;
    private final Vector projVertex3;
    private final int mainCoord;

    public Triangle(Coordinates3D coordinatesOfFirstVertex, Coordinates3D coordinatesOfSecondVertex,
                    Coordinates3D coordinatesOfThirdVertex, OpticProperties opticProperties) {
        this.coordinatesOfVertex1 = coordinatesOfFirstVertex;
        this.coordinatesOfVertex2 = coordinatesOfSecondVertex;
        this.coordinatesOfVertex3 = coordinatesOfThirdVertex;
        this.opticProperties = opticProperties;

        this.normal = calculateNormal();
        normal.multipleOnScalar(-1d);
        D = calculateD(normal);

        if((Math.abs(normal.getValue(0)) >= Math.abs(normal.getValue(1))) &&
                (Math.abs(normal.getValue(0)) >= Math.abs(normal.getValue(2)))){
            projVertex1 = new Vector(coordinatesOfVertex1.getY(), coordinatesOfVertex1.getZ(), 0d);
            projVertex2 = new Vector(coordinatesOfVertex2.getY(), coordinatesOfVertex2.getZ(), 0d);
            projVertex3 = new Vector(coordinatesOfVertex3.getY(), coordinatesOfVertex3.getZ(), 0d);
            mainCoord = 0;
        }else if((Math.abs(normal.getValue(1)) >= Math.abs(normal.getValue(0))) &&
                (Math.abs(normal.getValue(1)) >= Math.abs(normal.getValue(2)))){
            projVertex1 = new Vector(coordinatesOfVertex1.getX(), coordinatesOfVertex1.getZ(), 0d);
            projVertex2 = new Vector(coordinatesOfVertex2.getX(), coordinatesOfVertex2.getZ(), 0d);
            projVertex3 = new Vector(coordinatesOfVertex3.getX(), coordinatesOfVertex3.getZ(), 0d);
            mainCoord = 1;
        }else{
            projVertex1 = new Vector(coordinatesOfVertex1.getX(), coordinatesOfVertex1.getY(), 0d);
            projVertex2 = new Vector(coordinatesOfVertex2.getX(), coordinatesOfVertex2.getY(), 0d);
            projVertex3 = new Vector(coordinatesOfVertex3.getX(), coordinatesOfVertex3.getY(), 0d);
            mainCoord = 2;
        }
        calculateLineSegment();
    }

    private void calculateLineSegment(){
        lineSegments.addAll(separateLine(coordinatesOfVertex1, coordinatesOfVertex2));
        lineSegments.addAll(separateLine(coordinatesOfVertex2, coordinatesOfVertex3));
        lineSegments.addAll(separateLine(coordinatesOfVertex3, coordinatesOfVertex1));
    }

    private ArrayList<LineSegment> separateLine(Coordinates3D p1, Coordinates3D p2){
        ArrayList<LineSegment> lineSegments = new ArrayList<>();
        int m = Settings.TRIANGLE_M;
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

    public Coordinates3D getCoordinatesOfVertex1() {
        return coordinatesOfVertex1;
    }

    public Coordinates3D getCoordinatesOfVertex2() {
        return coordinatesOfVertex2;
    }

    public Coordinates3D getCoordinatesOfVertex3() {
        return coordinatesOfVertex3;
    }

    public OpticProperties getOpticProperties() {
        return opticProperties;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "coordinatesOfVertex1=" + coordinatesOfVertex1 +
                ", coordinatesOfVertex2=" + coordinatesOfVertex2 +
                ", coordinatesOfVertex3=" + coordinatesOfVertex3 +
                ", opticProperties=" + opticProperties +
                '}';
    }

    @Override
    public IntersectionInfo checkRay(Ray ray) {

        double D = this.D;

        double vD = Vector.scalarMultiplication(normal, ray.getRayDirection());
        if(vD >= 0){
            return null;
        }

        double v0 = -Vector.scalarMultiplication(normal, ray.getRayStart()) - D;
        double t = v0 / vD;
        if(t < 0){
            return null;
        }

        Vector intersection = new Vector(ray.getRayStart().getValue(0) + t * ray.getRayDirection().getValue(0),
                ray.getRayStart().getValue(1) + t * ray.getRayDirection().getValue(1),
                ray.getRayStart().getValue(2) + t * ray.getRayDirection().getValue(2));

        Vector sepIntersection = null;

        if(mainCoord == 0){
            sepIntersection = new Vector(intersection.getValue(1), intersection.getValue(2), 0d);
        }else if(mainCoord == 1){
            sepIntersection = new Vector(intersection.getValue(0), intersection.getValue(2), 0d);
        }else{
            sepIntersection = new Vector(intersection.getValue(0), intersection.getValue(1), 0d);
        }

        double s = calculateSquare(projVertex1, projVertex2, projVertex3);

        double alpha = calculateSquare(sepIntersection, projVertex2, projVertex3) / s;
        double betta = calculateSquare(projVertex1, sepIntersection, projVertex3) / s;
        double gamma = calculateSquare(projVertex1, projVertex2, sepIntersection) / s;

        if(Math.abs(alpha + betta + gamma - 1d) < 0.0000000001d && alpha >= 0 && betta >= 0 && gamma >= 0 &&
                alpha <= 1 && betta <= 1 && gamma <=1) {
            double len = Vector.norma(ray.getRayStart(), intersection);
            return new IntersectionInfo(intersection, opticProperties, normal, len);
        }else{
            return null;
        }
    }

    private double calculateSquare(Vector a, Vector b, Vector c){
        return 0.5*((b.getValue(0) - a.getValue(0)) * (c.getValue(1) - a.getValue(1)) -
                (c.getValue(0) - a.getValue(0))*(b.getValue(1) - a.getValue(1)));
    }

    private double calculateD(Vector normal){

        double A = normal.getValue(0);
        double B = normal.getValue(1);
        double C = normal.getValue(2);

        return -A * coordinatesOfVertex1.getX() - B * coordinatesOfVertex1.getY() - C * coordinatesOfVertex1.getZ();
    }

    private Vector calculateNormal(){
        Vector v1 = new Vector(coordinatesOfVertex2.getX() - coordinatesOfVertex1.getX(),
                coordinatesOfVertex2.getY() - coordinatesOfVertex1.getY(), coordinatesOfVertex2.getZ() - coordinatesOfVertex1.getZ());

        Vector v2 = new Vector(coordinatesOfVertex3.getX() - coordinatesOfVertex1.getX(),
                coordinatesOfVertex3.getY() - coordinatesOfVertex1.getY(), coordinatesOfVertex3.getZ() - coordinatesOfVertex1.getZ());
        Vector v = Vector.vectorMultiplication(v1, v2);
        v.normalizeVector();

        return v;
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
