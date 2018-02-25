package ru.nsu.fit.g13205.kushner.model;

import com.sun.org.apache.xpath.internal.SourceTree;
import ru.nsu.fit.g13205.kushner.utils.Coordinates;
import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;
import ru.nsu.fit.g13205.kushner.utils.Properties;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Konstantin on 20.04.2016.
 */
public class Surface {//все что касается поверхности вращения(точки, точки описывающие образующую кривую,)

    private ArrayList<ArrayList<Coordinates3D>> xSections = new ArrayList<>();
    private ArrayList<ArrayList<Coordinates3D>> phSections = new ArrayList<>();
    private ArrayList<ArrayList<Coordinates3D>> axis = new ArrayList<>();

    private Spline spline;
    private boolean isActual = false;

    private ArrayList<Coordinates> splinePoints;//которые задает пользователь


    private Properties properties;
    private double[][] rotateMatrix = {{1d, 0d, 0d, 0d}, {0d, 1d, 0d, 0d}, {0d, 0d, 1d, 0d}, {0d, 0d, 0d, 1d}};
    private double[][] matrixOfCamera;
    private double[][] projMatrix;

    private double xMin;
    private double xMax;
    double yMin;
    private double yMax;
    double zMin;
    private double zMax;
    private double xMinOfSpline, xMaxOfSpline, yMinOfSpline, yMaxOfSpline;

    private double shift;

    public Surface(Spline spline, Properties properties, double[][] matrixOfCamera) {
        this.spline = spline;
        this.splinePoints = spline.getCoordinatesPoints();
        this.properties = properties;
        ApproximatedSpline approximatedSpline = Algorithms.calculateSplinePoints(spline, properties);
        this.xMinOfSpline = approximatedSpline.getxMin();
        this.xMaxOfSpline = approximatedSpline.getxMax();
        this.yMinOfSpline = approximatedSpline.getyMin();
        this.yMaxOfSpline = approximatedSpline.getyMax();
        this.shift = spline.getShift();
        this.matrixOfCamera= matrixOfCamera;
        createXSections(approximatedSpline.getBasicPoint(), properties);
        createPHSections(properties.getK());
        createAxis();

        isActual = true;
    }

    public Surface(Spline spline, Properties properties, double[][] rotationMatrix, double[][] matrixOfCamera) {
        this.spline = spline;
        this.splinePoints = spline.getCoordinatesPoints();
        this.properties = properties;
        ApproximatedSpline approximatedSpline = Algorithms.calculateSplinePoints(spline, properties);
        this.xMinOfSpline = approximatedSpline.getxMin();
        this.xMaxOfSpline = approximatedSpline.getxMax();
        this.yMinOfSpline = approximatedSpline.getyMin();
        this.yMaxOfSpline = approximatedSpline.getyMax();
        this.rotateMatrix = rotationMatrix;
        this.shift = spline.getShift();
        this.matrixOfCamera = matrixOfCamera;
        createXSections(approximatedSpline.getBasicPoint(), properties);
        createPHSections(properties.getK());
        createAxis();
        isActual = false;
    }

    public void updateProjMatrix(double[][] projMatrix){
        this.projMatrix = projMatrix;
    }

    public int indexPointOfSpline(Point point){
        return spline.getIndexNewBasicPoint(point);
    }

    public int indexPointOfSpline(Coordinates point){
        return spline.getCoordinatesPoints().indexOf(point);
    }

    public void addPointsOfSpline(Coordinates point, int index){
        spline.getCoordinatesPoints().add(index, point);
    }

    public void removePointOfSpline(Point point){
        spline.getCoordinatesPoints().remove(spline.indexOfBasicPoint(point));
    }

    public void removePointOfSpline(Coordinates point){
        spline.getCoordinatesPoints().remove(point);
    }

    private void createAxis(){
        ArrayList<Coordinates3D> xAxis = new ArrayList<>();
        xAxis.add(new Coordinates3D(0, 0, 0));
        xAxis.add(new Coordinates3D(xMax, 0, 0));

        ArrayList<Coordinates3D> yAxis = new ArrayList<>();
        yAxis.add(new Coordinates3D(0, 0, 0));
        yAxis.add(new Coordinates3D(0, yMax, 0));

        ArrayList<Coordinates3D> zAxis = new ArrayList<>();
        zAxis.add(new Coordinates3D(0, 0, 0));
        zAxis.add(new Coordinates3D(0, 0, zMax));

        axis.add(xAxis);
        axis.add(yAxis);
        axis.add(zAxis);
    }
    //b сплайн смешенный на угол относительно начального
    private void createXSections(ArrayList<Coordinates> coord2D, Properties properties){
        double c = properties.getC(), d = properties.getD();

        int m = properties.getM();

        double deltaPH = (d - c) / ((m) * properties.getK());

        double xMin = 0, xMax = 0, yMin = 0, yMax = 0, zMin = 0, zMax = 0;
        boolean set = false;

        for(double ph = c; ph <= d; ph += deltaPH){
            ArrayList<Coordinates3D> coordinates = new ArrayList<>();

            double[][] transformationMatrix = createTransformationMatrix(ph);
            double shiftMatrix[][] = MatrixFactory.getMoveMatrix(0,0,shift);
            transformationMatrix = MatrixFactory.matrixMultiplication(shiftMatrix, transformationMatrix);

            for(Coordinates coord: coord2D){
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

        if(xSections.size() != (m) * properties.getK()+1){
            ArrayList<Coordinates3D> coordinates = new ArrayList<>();

            double[][] transformationMatrix = createTransformationMatrix(d);
            double shiftMatrix[][] = MatrixFactory.getMoveMatrix(0,0,shift);
            transformationMatrix = MatrixFactory.matrixMultiplication(shiftMatrix, transformationMatrix);

            for(Coordinates coord: coord2D){
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
    }

    //продольные дуги
    private void createPHSections(int k){
        for(int i = 0; i < xSections.get(0).size(); i++){
            ArrayList<Coordinates3D> section = new ArrayList<>();
            for(ArrayList<Coordinates3D> tmp: xSections){
                section.add(tmp.get(i));
            }
            phSections.add(section);
        }
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


    public void rotateSurface(double angleX, double angleY, double angleZ){
        double[][] xMatrix = MatrixFactory.getXAxisRotation(angleX);
        double[][] yMatrix = MatrixFactory.getYAxisRotation(angleY);
        double[][] zMatrix = MatrixFactory.getZAxisRotation(angleZ);
        rotateMatrix = MatrixFactory.matrixMultiplication(xMatrix, rotateMatrix);
        rotateMatrix = MatrixFactory.matrixMultiplication(yMatrix, rotateMatrix);
        rotateMatrix = MatrixFactory.matrixMultiplication(zMatrix, rotateMatrix);
        isActual = false;
    }

    public void calculateMinAndMax(double[][] sceneMatrix){
        if(true) {

            double[][] moveMatrix = MatrixFactory.getMoveMatrix(properties.getcX(), properties.getcY(), properties.getcZ());
            double[][] transformMatrix = MatrixFactory.matrixMultiplication(moveMatrix, rotateMatrix);

            rotate(transformMatrix, false);
        }
    }

    public WireFrame getWireframe(double delta, double[][] sceneMatrix, double shiftX, double shiftY, double shiftZ){
        if(true){
            double[][] moveMatrix = MatrixFactory.getMoveMatrix(properties.getcX(), properties.getcY(), properties.getcZ());
            double[][] transformMatrix = MatrixFactory.matrixMultiplication(moveMatrix, rotateMatrix);
            transformMatrix = MatrixFactory.matrixMultiplication(
                    MatrixFactory.getMoveMatrix(-shiftX, -shiftY, -shiftZ), transformMatrix);
            transformMatrix = MatrixFactory.matrixMultiplication(MatrixFactory.getCompressionMatrix(delta), transformMatrix);
            transformMatrix = MatrixFactory.matrixMultiplication(sceneMatrix, transformMatrix);
            transformMatrix = MatrixFactory.matrixMultiplication(matrixOfCamera, transformMatrix);
            transformMatrix = MatrixFactory.matrixMultiplication(projMatrix, transformMatrix);

            rotate(transformMatrix, true);
            rotateAxis(axis, transformMatrix);
            isActual = true;
            return new WireFrame(xSections, phSections, axis, xMax, yMax,  zMax);
        }else{
            return new WireFrame(xSections, phSections, axis, xMax, yMax, zMax);
        }
    }


    private void rotate(double[][] transformMatrix, boolean flag){
        double xMin = 0, xMax = 0, yMin = 0, yMax = 0, zMin = 0, zMax = 0;

        boolean set = false;
        for(ArrayList<Coordinates3D> tmp: xSections){
            for(Coordinates3D coord: tmp){
                double[][] v = MatrixFactory.matrixMultiplication(transformMatrix,
                        new double[][]{{coord.getX()}, {coord.getY()},{coord.getZ()},{1d}});
                double ratio;
                if(flag){
                    ratio = (v[3][0]);
                }else{
                    ratio = 1;
                }
                coord.setTransformX(v[0][0]/(ratio));
                coord.setTransformY(v[1][0]/(ratio));
                coord.setTransformZ(v[2][0]);
                coord.setTransformU(v[3][0]);
                //System.out.println(coord.getTransformX() + " " + coord.getTransformY() + " " + coord.getTransformZ() +" " + coord.gettTransformU());

                double x = coord.getTransformX(), y = coord.getTransformY(), z = coord.getTransformZ();
                if(!set){
                    xMin = x;
                    yMin = y;
                    zMin = z;
                    xMax = x;
                    yMax = y;
                    zMax = z;
                    set = true;
                }
                xMax = x > xMax ? x : xMax;
                yMax = y > yMax ? y : yMax;
                zMax = z > zMax ? z : zMax;
                xMin = x < xMin ? x : xMin;
                yMin = y < yMin ? y : yMin;
                zMin = z < zMin ? z : zMin;
            }
        }

        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }




    private void rotateAxis(ArrayList<ArrayList<Coordinates3D>> section, double[][] matrix){
        for(ArrayList<Coordinates3D> tmp: section){
            for(Coordinates3D coord: tmp){
                double[][] v = MatrixFactory.matrixMultiplication(matrix,
                        new double[][]{{coord.getX()}, {coord.getY()},{coord.getZ()},{1d}});

                coord.setTransformX(v[0][0]/(v[3][0]));
                coord.setTransformY(v[1][0]/(v[3][0]));
                coord.setTransformZ(v[2][0]);

            }
        }
    }

    public double getxMin(){
        return xMin;
    }

    public double getyMin(){
        return yMin;
    }

    public double getzMin(){
        return zMin;
    }

    public double getxMax() {
        return xMax;
    }

    public double getyMax() {
        return yMax;
    }

    public double getzMax() {
        return zMax;
    }

    public double getxMinOfSpline() {
        return xMinOfSpline;
    }

    public double getxMaxOfSpline() {
        return xMaxOfSpline;
    }

    public double getyMinOfSpline() {
        return yMinOfSpline;
    }

    public double getyMaxOfSpline() {
        return yMaxOfSpline;
    }

    public ArrayList<Coordinates> getSplinePoints() {
        return spline.getCoordinatesPoints();
    }

    public Spline getSpline(){
        return spline;
    }

    public Properties getProperties(){
        return properties;
    }

    public double[][] getRotateMatrix() {
        return rotateMatrix;
    }

    public double getSplineDelta(){
        return  spline.getDelta();
    }

    public void setColor(Color color){
        properties.setColor(color);
    }

    public void setActual(boolean value){
        isActual = value;
    }

    @Override
    public String toString() {
        return "Surface{" +
                "zMax=" + zMax +
                ", zMin=" + zMin +
                ", yMax=" + yMax +
                ", yMin=" + yMin +
                ", xMax=" + xMax +
                ", xMin=" + xMin +
                '}';
    }
}
