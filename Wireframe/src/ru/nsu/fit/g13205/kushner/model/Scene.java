package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;

import java.util.ArrayList;

/**
 * Created by Konstantin on 23.04.2016.
 */
public class Scene {//сначала вызвать посчитать мин и макс а потом возвращать их
    private ArrayList<Surface> surfaces = new ArrayList<>();
    private double xMax, yMax, zMax;
    private double xMin, yMin, zMin;
    private double deltaX, deltaY, deltaZ;
    private double shiftX, shiftY, shiftZ;
    private double compressionRatio;
    private double maxXOfScene;
    private double maxYOfScene;
    private double[][] rotateMatrix = {{1d, 0d, 0d, 0d},
            {0d, 1d, 0d, 0d},
            {0d, 0d, 1d, 0d},
            {0d, 0d, 0d, 1d}};

    private double[][] matrixOfCamera;

    private double[][] projMatrix;


    private ArrayList<ArrayList<Coordinates3D>> box = new ArrayList<>();
    private ArrayList<Coordinates3D> axis = new ArrayList<>();

    public Scene(double[][] matrixOfCamera) {
        this.matrixOfCamera = matrixOfCamera;
        createBox();
        createAxis();
    }

    public void updateProjMatrix(double[][] projMatrix){
        this.projMatrix = projMatrix;
        for(Surface surface: surfaces){
            surface.updateProjMatrix(projMatrix);
        }
    }

    public void createAxis(){
        axis.add(new Coordinates3D(1, 0, 0));
        axis.add(new Coordinates3D(0, 1, 0));
        axis.add(new Coordinates3D(0, 0, 1));
    }

    public void createBox(){
        ArrayList<Coordinates3D> tmp = new ArrayList<Coordinates3D>();
        tmp.add(new Coordinates3D(-1d, -1d, -1d));
        tmp.add(new Coordinates3D(-0.6d, -1d, -1d));
        tmp.add(new Coordinates3D(-0.2d, -1d, -1d));
        tmp.add(new Coordinates3D(0.2d, -1d, -1d));
        tmp.add(new Coordinates3D(0.6d, -1d, -1d));


        tmp.add(new Coordinates3D(1d, -1d, -1d));
        tmp.add(new Coordinates3D(1d, -0.6d, -1d));
        tmp.add(new Coordinates3D(1d, -0.2d, -1d));
        tmp.add(new Coordinates3D(1d, 0.2d, -1d));
        tmp.add(new Coordinates3D(1d, 0.6d, -1d));


        tmp.add(new Coordinates3D(1d, 1d, -1d));
        tmp.add(new Coordinates3D(0.6d, 1d, -1d));
        tmp.add(new Coordinates3D(0.2d, 1d, -1d));
        tmp.add(new Coordinates3D(-0.2d, 1d, -1d));
        tmp.add(new Coordinates3D(-0.6d, 1d, -1d));

        tmp.add(new Coordinates3D(-1d, 1d, -1d));

        tmp.add(new Coordinates3D(-1d, 0.6d, -1d));
        tmp.add(new Coordinates3D(-1d, 0.2d, -1d));
        tmp.add(new Coordinates3D(-1d, -0.2d, -1d));
        tmp.add(new Coordinates3D(-1d, -0.61d, -1d));

        tmp.add(new Coordinates3D(-1d, -1d, -1d));
        box.add(tmp);
/////////////////////////////////////////
        tmp.add(new Coordinates3D(-1d, -1d, 1d));

        tmp.add(new Coordinates3D(-0.6d, -1d, 1d));
        tmp.add(new Coordinates3D(-0.2d, -1d, 1d));
        tmp.add(new Coordinates3D(0.2d, -1d, 1d));
        tmp.add(new Coordinates3D(0.6d, -1d, 1d));


        tmp.add(new Coordinates3D(1d, -1d, 1d));

        tmp.add(new Coordinates3D(1d, -0.6d, 1d));
        tmp.add(new Coordinates3D(1d, -0.2d, 1d));
        tmp.add(new Coordinates3D(1d, 0.2d, 1d));
        tmp.add(new Coordinates3D(1d, 0.6d, 1d));

        tmp.add(new Coordinates3D(1d, 1d, 1d));

        tmp.add(new Coordinates3D(0.6d, 1d, 1d));
        tmp.add(new Coordinates3D(0.2d, 1d, 1d));
        tmp.add(new Coordinates3D(-0.2d, 1d, 1d));
        tmp.add(new Coordinates3D(-0.6d, 1d, 1d));


        tmp.add(new Coordinates3D(-1d, 1d, 1d));

        tmp.add(new Coordinates3D(-1d, 0.6d, 1d));
        tmp.add(new Coordinates3D(-1d, 0.2d, 1d));
        tmp.add(new Coordinates3D(-1d, -0.2d, 1d));
        tmp.add(new Coordinates3D(-1d, -0.6d, 1d));

        tmp.add(new Coordinates3D(-1d, -1d, 1d));
        box.add(tmp);
///////////////////////////////////
        tmp = new ArrayList<>();
        tmp.add(new Coordinates3D(-1, -1, -1));

        tmp.add(new Coordinates3D(-1, -1, -0.6));
        tmp.add(new Coordinates3D(-1, -1, -0.2));
        tmp.add(new Coordinates3D(-1, -1, 0.2));
        tmp.add(new Coordinates3D(-1, -1, 0.6));

        tmp.add(new Coordinates3D(-1, -1, 1));
        box.add(tmp);
/////////////////////////////////
        tmp = new ArrayList<>();
        tmp.add(new Coordinates3D(-1, 1, -1));

        tmp.add(new Coordinates3D(-1, 1, -0.6));
        tmp.add(new Coordinates3D(-1, 1, -0.2));
        tmp.add(new Coordinates3D(-1, 1, 0.2));
        tmp.add(new Coordinates3D(-1, 1, 0.6));

        tmp.add(new Coordinates3D(-1, 1, 1));
        box.add(tmp);
////////////////
        tmp = new ArrayList<>();
        tmp.add(new Coordinates3D(1, -1, -1));

        tmp.add(new Coordinates3D(1, -1, -0.6));
        tmp.add(new Coordinates3D(1, -1, -0.2));
        tmp.add(new Coordinates3D(1, -1, 0.2));
        tmp.add(new Coordinates3D(1, -1, 0.6));

        tmp.add(new Coordinates3D(1, -1, 1));
        box.add(tmp);
////////////////////
        tmp = new ArrayList<>();
        tmp.add(new Coordinates3D(1, 1, -1));

        tmp.add(new Coordinates3D(1, 1, -0.6));
        tmp.add(new Coordinates3D(1, 1, -0.2));
        tmp.add(new Coordinates3D(1, 1, 0.2));
        tmp.add(new Coordinates3D(1, 1, 0.6));

        tmp.add(new Coordinates3D(1, 1, 1));
        box.add(tmp);

    }

    public void  rotateScene(double angleX, double angleY, double angleZ){
        double[][] xMatrix = MatrixFactory.getXAxisRotation(angleX);
        double[][] yMatrix = MatrixFactory.getYAxisRotation(angleY);
        double[][] zMatrix = MatrixFactory.getZAxisRotation(angleZ);
        rotateMatrix = MatrixFactory.matrixMultiplication(xMatrix, rotateMatrix);
        rotateMatrix = MatrixFactory.matrixMultiplication(yMatrix, rotateMatrix);
        rotateMatrix = MatrixFactory.matrixMultiplication(zMatrix, rotateMatrix);

        for(Surface surface: surfaces){
            surface.setActual(false);
        }
    }

    public ArrayList<ArrayList<Coordinates3D>> getRotatesBox(){
        double[][] matrix = MatrixFactory.matrixMultiplication(matrixOfCamera, rotateMatrix);

        matrix = MatrixFactory.matrixMultiplication(projMatrix, matrix);

        boolean set = false;
        for(ArrayList<Coordinates3D> tmp: box){
            for(Coordinates3D coord: tmp){
                double[][] v = MatrixFactory.matrixMultiplication(matrix,
                        new double[][]{{coord.getX()}, {coord.getY()},{coord.getZ()},{1d}});


                coord.setTransformX(v[0][0]/(v[3][0]));
                coord.setTransformY(v[1][0]/(v[3][0]));
                coord.setTransformZ(v[2][0]);
                coord.setTransformU(v[3][0]);

                if(!set){
                    maxXOfScene = coord.getTransformX();
                    maxYOfScene = coord.getTransformY();
                    set = true;
                }else{
                    double x = coord.getTransformX();
                    double y = coord.getTransformY();
                    maxXOfScene = x > maxXOfScene ? x : maxXOfScene;
                    maxYOfScene = y > maxYOfScene ? y : maxYOfScene;
                }


                //System.out.println(coord.getTransformZ() + " " + coord.gettTransformU());
            }
        }
        return box;
    }

    public ArrayList<Coordinates3D> getRotateAxis(){
        double[][] matrix = MatrixFactory.matrixMultiplication(matrixOfCamera, rotateMatrix);
        matrix = MatrixFactory.matrixMultiplication(projMatrix, matrix);
        for(Coordinates3D coord: axis){

            double[][] v = MatrixFactory.matrixMultiplication(matrix,
                    new double[][]{{coord.getX()}, {coord.getY()},{coord.getZ()},{1d}});

            coord.setTransformX(v[0][0]/(v[3][0]));
            coord.setTransformY(v[1][0]/(v[3][0]));
            coord.setTransformZ(v[2][0]/(v[3][0]));
            coord.setTransformZ(v[3][0]);
        }
        return axis;
    }

    public double[][] getRoateMatrix(){
        return rotateMatrix;
    }

    public void addSurface(Surface surface){
        surfaces.add(surfaces.size(), surface);
        surface.updateProjMatrix(projMatrix);
    }

    public void addSurface(Surface surface, int index){
        surfaces.add(index, surface);
        surface.updateProjMatrix(projMatrix);
    }

    public Surface removeSurface(int number){
        return surfaces.remove(number);
    }

    public Surface getSurface(int number){
        return surfaces.get(number);
    }

    public boolean contains(Surface surface){
        return surfaces.contains(surface);
    }

    public int size(){
        return surfaces.size();
    }

    public void calculateMinAndMax(){

        if(surfaces.size()!=0){
            Surface firstSurface = surfaces.get(0);
            xMin = firstSurface.getxMin();
            xMax = firstSurface.getxMax();
            yMin = firstSurface.getyMin();
            yMax = firstSurface.getyMax();
            zMin = firstSurface.getzMin();
            zMax = firstSurface.getzMax();
            for(int i = 1; i < surfaces.size(); i++){
                Surface current = surfaces.get(i);

                xMin = current.getxMin() < xMin ? current.getxMin() : xMin;
                yMin = current.getyMin() < yMin ? current.getyMin() : yMin;
                zMin = current.getzMin() < zMin ? current.getzMin() : zMin;
                xMax = current.getxMax() > xMax ? current.getxMax() : xMax;
                yMax = current.getyMax() > yMax ? current.getyMax() : yMax;
                zMax = current.getzMax() > zMax ? current.getzMax() : zMax;
            }
        }
        double xAbs = Math.abs(xMin) > Math.abs(xMax) ? Math.abs(xMin) : Math.abs(xMax);
        double yAbs = Math.abs(yMin) > Math.abs(yMax) ? Math.abs(yMin) : Math.abs(yMax);
        double zAbs = Math.abs(zMin) > Math.abs(zMax) ? Math.abs(zMin) : Math.abs(zMax);

        deltaX = (xMax - xMin)/2;
        deltaY = (yMax - yMin)/2;
        deltaZ = (zMax - zMin)/2;

        shiftX = xMax - deltaX;
        shiftY = yMax - deltaY;
        shiftZ = zMax - deltaZ;

        compressionRatio = deltaX > deltaY ? deltaX : deltaY;
        compressionRatio = deltaZ > compressionRatio ? deltaZ : compressionRatio;
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

    public double getyMin() {
        return yMin;
    }

    public double getxMin() {
        return xMin;
    }

    public double getzMin() {
        return zMin;
    }

    public double getAbsX(){
        return Math.abs(xMin) > Math.abs(xMax) ?  Math.abs(xMin) : Math.abs(xMax);
    }

    public double getAbsY(){
        return Math.abs(yMin) > Math.abs(yMax) ? Math.abs(yMin) : Math.abs(yMax);
    }

    public double getAbsZ(){
        return Math.abs(zMin) > Math.abs(zMax) ? Math.abs(zMin) : Math.abs(zMax);
    }

    public double getCompressionRatio(){
        return compressionRatio;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public double getShiftX() {
        return shiftX;
    }

    public double getShiftY() {
        return shiftY;
    }

    public double getShiftZ() {
        return shiftZ;
    }

    public void setRotMatrix(double[][] matrix){
        rotateMatrix = matrix;
        for(Surface surface: surfaces){
            surface.setActual(false);
        }
    }

    public double getMaxXOfScene() {
        return maxXOfScene;
    }

    public double getMaxYOfScene() {
        return maxYOfScene;
    }
}
