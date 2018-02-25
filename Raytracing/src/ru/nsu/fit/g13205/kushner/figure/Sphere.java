package ru.nsu.fit.g13205.kushner.figure;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.model.MatrixFactory;
import ru.nsu.fit.g13205.kushner.raytracing.IntersectionInfo;
import ru.nsu.fit.g13205.kushner.raytracing.Ray;
import ru.nsu.fit.g13205.kushner.utils.Coordinates2D;
import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;
import ru.nsu.fit.g13205.kushner.utils.LineSegment;
import ru.nsu.fit.g13205.kushner.utils.Vector;

import java.util.ArrayList;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class Sphere implements Figure {
    //KDr KDg KDb KSr KSg KSb Power

    private final Coordinates3D center;
    private final double radius;
    private final OpticProperties opticProperties;
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();
    private double xMin, xMax, yMin, yMax, zMin, zMax;

    private ArrayList<LineSegment> transformFigure;

    public Sphere(Coordinates3D center, double radius, OpticProperties opticProperties) {
        this.center = center;
        this.radius = radius;
        this.opticProperties = opticProperties;
        calculateLineSegment();
    }

    private void calculateLineSegment(){
        int m = Settings.SPHERE_M;
        double step = Math.PI * 2 / m;

        double cX = center.getX();
        double cY = center.getY();


        ArrayList<Coordinates2D> coordinates2Ds = new ArrayList<>();
        for(double angle = 0; angle <= 2 * Math.PI; angle += step){
            coordinates2Ds.add(new Coordinates2D(radius * Math.cos(angle), radius * Math.sin(angle)));
        }

        ArrayList<ArrayList<Coordinates3D>> xSections = createXSections(coordinates2Ds);
        ArrayList<ArrayList<Coordinates3D>> phSections = createPHSections(xSections);


        for(ArrayList<Coordinates3D> coordinates3Ds: xSections){
            for(int i = 0; i < coordinates3Ds.size() - 1; i++){
                lineSegments.add(new LineSegment(coordinates3Ds.get(i), coordinates3Ds.get(i+1)));
            }
        }

        for(ArrayList<Coordinates3D> coordinates3Ds: phSections){
            for(int i = 0; i < coordinates3Ds.size() - 1; i++){
                lineSegments.add(new LineSegment(coordinates3Ds.get(i), coordinates3Ds.get(i+1)));
            }
        }
    }

    public Coordinates3D getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public OpticProperties getOpticProperties() {
        return opticProperties;
    }

    public ArrayList<LineSegment> getLineSegments() {
        return lineSegments;
    }

    private ArrayList<ArrayList<Coordinates3D>> createXSections(ArrayList<Coordinates2D> coord2D){

        int m = Settings.SPHERE_M;

        double deltaPH = 2 * Math.PI / m;

        double xMin = 0, xMax = 0, yMin = 0, yMax = 0, zMin = 0, zMax = 0;
        boolean set = false;

        ArrayList<ArrayList<Coordinates3D>> xSections = new ArrayList<>();
        for(double ph = 0; ph <= 2 * Math.PI; ph += deltaPH){
            ArrayList<Coordinates3D> coordinates = new ArrayList<>();

            double[][] transformationMatrix = createTransformationMatrix(ph);

            for(Coordinates2D coord: coord2D){
                Coordinates3D tmp = new Coordinates3D(matrixMultiplication(transformationMatrix, new double[]{
                        coord.getY(), coord.getY(), coord.getX(), 1d}));

                coordinates.add(tmp);

                double x = tmp.getX(), y = tmp.getY(), z = tmp.getZ();

                if(!set){
                    xMin = x;
                    xMax = x;
                    yMin = y;
                    yMax = y;
                    zMin = z;
                    zMax = z;

                    set = true;
                }
                xMin = x < xMin ? x : xMin;
                yMin = y < yMin ? y : yMin;
                zMin = z < zMin ? z : zMin;

                xMax = x > xMax ? x : xMax;
                yMax = y > yMax ? y : yMax;
                zMax = z > zMax ? z : zMax;
            }
            xSections.add(coordinates);
        }

        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;

        return xSections;
    }

    private ArrayList<ArrayList<Coordinates3D>> createPHSections(ArrayList<ArrayList<Coordinates3D>> xSections){

        ArrayList<ArrayList<Coordinates3D>> phSections = new ArrayList<>();

        for(int i = 0; i < xSections.get(0).size(); i++){
            ArrayList<Coordinates3D> section = new ArrayList<>();
            for(ArrayList<Coordinates3D> tmp: xSections){
                section.add(new Coordinates3D(tmp.get(i).getX(), tmp.get(i).getY(), tmp.get(i).getZ(), 1));
            }
            phSections.add(section);
        }

        return phSections;
    }


    private double[][] createTransformationMatrix(double ph){
        double[][] transformationMatrix = new double[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                transformationMatrix[i][j] = 0d;
            }
        }

        transformationMatrix[0][0] = Math.cos(ph);
        transformationMatrix[1][1] = Math.sin(ph);
        transformationMatrix[2][2] = -1d;
        transformationMatrix[3][3] = 1d;
        return transformationMatrix;
    }

    private double[] matrixMultiplication(double[][] leftMatrix, double[] rightVector){
        double[] result = new double[rightVector.length];

        for (int i = 0; i < leftMatrix.length; i++){
            double sum = 0;
            for(int j = 0; j < leftMatrix[i].length; j++){
                sum += leftMatrix[i][j] * rightVector[j];
            }
            result[i] = sum;
        }

        return result;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "center=" + center +
                ", radius=" + radius +
                ", opticProperties=" + opticProperties +
                '}';
    }

    @Override
    public IntersectionInfo checkRay(Ray ray) {
        boolean isInner = false;

        Vector c = new Vector(center.getX(), center.getY(), center.getZ());

        Vector OC = Vector.diffVector(c, ray.getRayStart());

        double r = OC.getLen();

        if(r < radius){
            return null;
        }

        double ca = Vector.scalarMultiplication(OC, ray.getRayDirection());

        if(ca < 0){
            return null;
        }

        double D = Vector.scalarMultiplication(OC, OC) - ca * ca;

        double hc = radius * radius - D;

        if(hc < 0){
            return null;
        }

        double t = 0;

        t = ca - Math.sqrt(hc);

        //t = 1;

        Vector intersection = new Vector(ray.getRayStart().getValue(0) + t * ray.getRayDirection().getValue(0),
                ray.getRayStart().getValue(1) + t * ray.getRayDirection().getValue(1),
                ray.getRayStart().getValue(2) + t * ray.getRayDirection().getValue(2));

        double len = Vector.norma(ray.getRayStart(), intersection);

        double nx = (intersection.getValue(0) - c.getValue(0))/radius;
        double ny = (intersection.getValue(1) - c.getValue(1))/radius;
        double nz = (intersection.getValue(2) - c.getValue(2))/radius;

        Vector normal = new Vector(nx, ny, nz);

        return new IntersectionInfo(intersection, opticProperties, normal, len);
    }

    @Override
    public ArrayList<LineSegment> getTransformFigure(double[][] transformMatrix, double shiftX, double shiftY, double shiftZ) {

        double[][] moveMatrix = MatrixFactory.getMoveMatrix(center.getX(), center.getY(), center.getZ());
        transformMatrix = MatrixFactory.matrixMultiplication(transformMatrix, moveMatrix);

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

    public double getxMin() {
        return xMin;
    }

    public double getxMax() {
        return xMax;
    }

    public double getyMin() {
        return yMin;
    }

    public double getyMax() {
        return yMax;
    }

    public double getzMax() {
        return zMax;
    }

    public double getzMin() {
        return zMin;
    }
}
