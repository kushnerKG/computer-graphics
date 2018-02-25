package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.figure.*;
import ru.nsu.fit.g13205.kushner.raytracing.Render;
import ru.nsu.fit.g13205.kushner.utils.*;

import java.awt.*;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class Scene {

    private final ArrayList<Figure> figures = new ArrayList<>();
    private double xMax, yMax, zMax;
    private double xMin, yMin, zMin;
    private double deltaX, deltaY, deltaZ;
    private double shiftX, shiftY, shiftZ;
    private double zN = Settings.ZN;
    private double zF = Settings.ZF;
    private double sW = Settings.SW;
    private double sH = Settings.SH;
    private double gamma;
    private ArrayList<LightSource> lightSources;
    private Color backgroundColor;
    private int quality;

    private Color ambientColor;

    private int depth = 2;
    private double boxAbsX, boxAbsY, boxAbsZ;
    private double[][] matrixOfCamera;

    private double xMinWithShift, xMaxWithShift, yMinWithShift, yMaxWithShift, zMinWithShift, zMaxWithShift;
    private double[][] inner = {{1d, 0d, 0d, 0d},
            {0d, 1d, 0d, 0d},
            {0d, 0d, 1d, 0d},
            {0d, 0d, 0d, 1d}};

    private ArrayList<LineSegment> outsideBox = new ArrayList<>();
    private ArrayList<LineSegment> rotateOutsideBox = new ArrayList<>();
    private double[][] rotateMatrix = {{1d, 0d, 0d, 0d},
            {0d, 1d, 0d, 0d},
            {0d, 0d, 1d, 0d},
            {0d, 0d, 0d, 1d}};

    private double[][] projMatrix;
    private boolean isActual = true;
    private boolean isInitOutsideBox = false;
    private boolean isSetShift = false;

    private Camera camera;

    public Scene() {
    }

    public Scene(RenderInfo renderFileInfo, SceneFileInfo sceneFileInfo, Camera camera) {
        this.quality = renderFileInfo.getQuality();
        this.depth = renderFileInfo.getDepth();
        this.zN = renderFileInfo.getZn();
        this.zF = renderFileInfo.getZf();
        this.sW = renderFileInfo.getSw();
        this.sH = renderFileInfo.getSh();
        this.backgroundColor = renderFileInfo.getBackgroundColor();
        this.gamma = renderFileInfo.getGamma();
        this.camera = camera;
        for(Box box: sceneFileInfo.getBoxes()){
            figures.add(box);
        }

        for (Sphere sphere: sceneFileInfo.getSpheres()){
            figures.add(sphere);
        }

        for(Triangle triangle: sceneFileInfo.getTriangles()){
            figures.add(triangle);
        }

        for(Quadrangle quadrangle: sceneFileInfo.getQuadrangles()){
            figures.add(quadrangle);
        }
        ambientColor = sceneFileInfo.getAmbientLightColor();
        lightSources = sceneFileInfo.getLightSources();
    }

    public void  rotateScene(double angleX, double angleY, double angleZ){
        double[][] xMatrix = MatrixFactory.getXAxisRotation(angleX);
        double[][] yMatrix = MatrixFactory.getYAxisRotation(angleY);
        double[][] zMatrix = MatrixFactory.getZAxisRotation(angleZ);
        rotateMatrix = MatrixFactory.matrixMultiplication(xMatrix, rotateMatrix);
        rotateMatrix = MatrixFactory.matrixMultiplication(yMatrix, rotateMatrix);
        rotateMatrix = MatrixFactory.matrixMultiplication(zMatrix, rotateMatrix);
    }

    public void drawScene(BufferedImage image, double[][] matrixOfCamera){

        this.matrixOfCamera = matrixOfCamera;

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                image.setRGB(x, y, Color.BLACK.getRGB());
            }
        }

        calculateMinAndMax(false, matrixOfCamera);
        transformFigures();

        double deltaHeight = image.getHeight() / Math.abs(xMax - xMin);
        double deltaWidth = image.getWidth() / Math.abs(yMax - yMin);

        double delta = (deltaWidth > deltaHeight ? deltaWidth : deltaHeight) / 2;

        Graphics graphics = image.getGraphics();

        for(Figure figure: figures){
            ArrayList<LineSegment> lineSegments = figure.getTransformFigure();

            OpticProperties op = figure.getOpticProperties();
            Color color = new Color(op.getKDr(), op.getKDg(), op.getKDb());
            graphics.setColor(color);

            for(int i = 0; i < lineSegments.size(); i++){
                LineSegment lineSegment = lineSegments.get(i);
                Coordinates3D start = lineSegment.getStart();
                Coordinates3D finish = lineSegment.getFinish();

                //if(Clipper.check(start, finish, zN, zF, sW, sH)) {
                    Point p1 = fromCoordinatesToPixel(new Coordinates2D(start.getX(),
                            start.getY()), image.getWidth(), image.getHeight(), delta);
                    Point p2 = fromCoordinatesToPixel(new Coordinates2D(finish.getX(),
                            finish.getY()), image.getWidth(), image.getHeight(), delta);

                    graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                //}
            }

        }
    }

    private Point fromCoordinatesToPixel(Coordinates2D point, int width, int height, double delta){
        double x = point.getX(), y = point.getY();

        double deltaW = width/(sW);
        double deltaH = height/(sH);

        int pX = (int) (width/2 + x*deltaW);
        int pY = (int) (height/2 + deltaH*y);
        return new Point(pX, pY);
    }

    private void transformFigures(){
        for(Figure figure: figures){
            figure.getTransformFigure(createTransformMatrix(), shiftX, shiftY, shiftZ);
        }
    }

    private void calculateShift(){
        double xMax = 0, yMax = 0, zMax = 0, xMin = 0, yMin = 0, zMin = 0;
        double deltaX, deltaY, deltaZ;

        for (Figure figure : figures) {
            figure.getTransformFigure(inner, 0d, 0d, 0d);
        }
        if(figures.size()!=0){
            Figure firstSurface = figures.get(0);
            xMin = firstSurface.getxMin();
            xMax = firstSurface.getxMax();
            yMin = firstSurface.getyMin();
            yMax = firstSurface.getyMax();
            zMin = firstSurface.getzMin();
            zMax = firstSurface.getzMax();
            for(int i = 1; i < figures.size(); i++){
                Figure current = figures.get(i);

                xMin = current.getxMin() < xMin ? current.getxMin() : xMin;
                yMin = current.getyMin() < yMin ? current.getyMin() : yMin;
                zMin = current.getzMin() < zMin ? current.getzMin() : zMin;
                xMax = current.getxMax() > xMax ? current.getxMax() : xMax;
                yMax = current.getyMax() > yMax ? current.getyMax() : yMax;
                zMax = current.getzMax() > zMax ? current.getzMax() : zMax;
            }
        }


        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;

        boxAbsX = xMin;
        boxAbsY = yMax - yMin;
        boxAbsZ = zMax;

        this.deltaX = (xMax - xMin)/2;
        this.deltaY = (yMax - yMin)/2;
        this.deltaZ = (zMax - zMin)/2;
        this.shiftX = xMax - this.deltaX;
        this.shiftY = yMax - this.deltaY;
        this.shiftZ = zMax - this.deltaZ;

        isSetShift = true;

    }

    private void calculateMinAndMax(boolean withShift, double[][] matrix){
        if(!isSetShift){
            calculateShift();
        }

        double xMax = 0, yMax = 0, zMax = 0, xMin = 0, yMin = 0, zMin = 0;
        double deltaX, deltaY, deltaZ;


        if(!withShift) {
            for (Figure figure : figures) {
                figure.getTransformFigure(matrix, 0d, 0d, 0d);
            }

        }else {
            double[][] tmp = MatrixFactory.matrixMultiplication(rotateMatrix, MatrixFactory.getMoveMatrix(-shiftX, -shiftY, -shiftZ));
            tmp = MatrixFactory.matrixMultiplication(MatrixFactory.getMoveMatrix(shiftX, shiftY, shiftZ), tmp);
            tmp = MatrixFactory.matrixMultiplication(matrixOfCamera, tmp);
            for (Figure figure : figures) {
                figure.getTransformFigure(tmp, shiftX, shiftY, shiftZ);
            }
        }
        if(figures.size()!=0){
            Figure firstSurface = figures.get(0);
            xMin = firstSurface.getxMin();
            xMax = firstSurface.getxMax();
            yMin = firstSurface.getyMin();
            yMax = firstSurface.getyMax();
            zMin = firstSurface.getzMin();
            zMax = firstSurface.getzMax();
            for(int i = 1; i < figures.size(); i++){
                Figure current = figures.get(i);

                xMin = current.getxMin() < xMin ? current.getxMin() : xMin;
                yMin = current.getyMin() < yMin ? current.getyMin() : yMin;
                zMin = current.getzMin() < zMin ? current.getzMin() : zMin;
                xMax = current.getxMax() > xMax ? current.getxMax() : xMax;
                yMax = current.getyMax() > yMax ? current.getyMax() : yMax;
                zMax = current.getzMax() > zMax ? current.getzMax() : zMax;
            }


        }

        if(withShift){
            xMinWithShift = xMin;
            xMaxWithShift = xMax;
            yMinWithShift = yMin;
            yMaxWithShift = yMax;
            zMinWithShift = zMin;
            zMaxWithShift = zMax;
        }else {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            this.zMin = zMin;
            this.zMax = zMax;


        }

        this.deltaX = (xMax - xMin)/2;
        this.deltaY = (yMax - yMin)/2;
        this.deltaZ = (zMax - zMin)/2;
        if(!withShift && !isSetShift) {
            this.shiftX = xMax - this.deltaX;
            this.shiftY = yMax - this.deltaY;
            this.shiftZ = zMax - this.deltaZ;
            isSetShift = true;
        }


    }

    public void changeZn(double ratio){
        zN *= ratio;
    }

    public void initPosition(){
        double eyeX = -boxAbsX - boxAbsZ / Math.atan(Settings.DEFAULT_EYE_ANGLE);

        rotateMatrix = new double[][] {{1d, 0d, 0d, 0d},
                {0d, 1d, 0d, 0d},
                {0d, 0d, 1d, 0d},
                {0d, 0d, 0d, 1d}};


        zN = (-boxAbsX - eyeX) / 2;
        zF = boxAbsX - eyeX + (boxAbsX);
    }

    public double getBoxAbsY() {
        return boxAbsY;
    }

    public double getBoxAbsX() {
        return boxAbsX;
    }

    public double getBoxAbsZ() {
        return boxAbsZ;
    }

    public void setProperties(Properties properties) {
        this.zN = properties.getZn();
        this.zF = properties.getZf();
        this.sW = properties.getSw();
        this.sH = properties.getSh();
    }

    public DialogProperties getProperties(){
        return new DialogProperties(backgroundColor, gamma, depth, quality, sW, sH, zN, zF);
    }


    public void renderScene(BufferedImage renderImage, ArrayList<ModelListener> listeners){
        double[][] invertMatrix = MatrixFactory.getInvertRotateMatrix(rotateMatrix);

        double[] eye = this.camera.getEyePoint();
        double[] view = this.camera.getViewPoint();
        double[] up = this.camera.getUpVector();

        up = adjustUpVector(view, eye, up);

        Vector newEye, newView, newUp;

        newUp = new Vector(multiple(invertMatrix, new double[]{up[0], up[1], up[2], 1d}));

        invertMatrix = MatrixFactory.matrixMultiplication(invertMatrix, MatrixFactory.getMoveMatrix(-this.camera.getViewPoint()[0],
                -this.camera.getViewPoint()[1], -this.camera.getViewPoint()[2]));
        invertMatrix = MatrixFactory.matrixMultiplication(MatrixFactory.getMoveMatrix(this.camera.getViewPoint()[0],
                this.camera.getViewPoint()[1], this.camera.getViewPoint()[2]), invertMatrix);

        newView = new Vector(multiple(invertMatrix, new double[]{view[0], view[1], view[2], 1d}));
        newEye = new Vector(multiple(invertMatrix, new double[]{eye[0], eye[1], eye[2], 1d}));

        //newView = new Vector(view);
        //newEye = new Vector(multiple(invertMatrix, eye));
        //newUp = new Vector(adjustUpVector(newView.toArray(), newEye.toArray(), new Vector(up).toArray()));
        newUp = new Vector(adjustUpVector(newView.toArray(), newEye.toArray(), newUp.toArray()));
        Render render = new Render(newUp, newView, newEye, figures, renderImage,
                new Properties(zN, zF, sW, sH), lightSources, ambientColor, depth, backgroundColor, gamma, listeners, quality);

        Thread thread = new Thread(render);

        thread.start();
}

    public double[] multiple(double[][] matrix, double[] vector){
        double[] resultVector = new double[vector.length];
        for(int i = 0; i < 4; i++){
            resultVector[i] = 0;
            for(int j = 0; j < 4; j++){
                resultVector[i] += matrix[i][j] * vector[j];
            }
        }

        return resultVector;
    }

    private double[] adjustUpVector(double[] viewPoint, double[] eyePoint, double[] upVector){
        double z[] = new double[3];//вектор взгляда
        z[0] = viewPoint[0] - eyePoint[0];
        z[1] = viewPoint[1] - eyePoint[1];
        z[2] = viewPoint[2] - eyePoint[2];
        MatrixFactory.normalizeVector(z);

        double right[] = MatrixFactory.vectorMultiplication(upVector, z);//вектор в право
        MatrixFactory.normalizeVector(right);

        upVector = MatrixFactory.vectorMultiplication(z, right);
        MatrixFactory.normalizeVector(upVector);
        return upVector;
    }

    private double[] getNewUp(double[][] invertMatrix, double[] up){
        return multiplication(invertMatrix, up);
    }

    private double[] getNewEye(double[][] invertMatrix, double[] eye){
        return multiplication(invertMatrix, eye);
    }

    private double[] getNewView(double[][] invertMatrix, double[] view){
        return multiplication(invertMatrix, view);
    }



    private double[] multiplication(double[][] matrix, double[] vector){
        double[] result = new double[3];

        for (int i = 0; i < 3; i++) {
            double sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
        }

        return result;
    }

    public Coordinates3D getCenter(){
        calculateMinAndMax(false, rotateMatrix);

        return new Coordinates3D(xMin + deltaX, yMin + deltaY, zMin + deltaZ, 1);
    }

    private double[][] createTransformMatrix(){

        double[][] tmp = MatrixFactory.matrixMultiplication(rotateMatrix, MatrixFactory.getMoveMatrix(-camera.getViewPoint()[0],
                -camera.getViewPoint()[1], -camera.getViewPoint()[2]));
        tmp = MatrixFactory.matrixMultiplication(MatrixFactory.getMoveMatrix(camera.getViewPoint()[0],
                camera.getViewPoint()[1], camera.getViewPoint()[2]), tmp);
        tmp = MatrixFactory.matrixMultiplication(matrixOfCamera, tmp);

        tmp = MatrixFactory.matrixMultiplication(MatrixFactory.getProjMatrix(0, 0, zN, 0), tmp);
        return tmp;
    }

    public void addLightSource(ArrayList<LightSource> lightSources){
        this.lightSources = lightSources;
    }

    public void setAmbientColor(Color color){
        this.ambientColor = color;
    }

    public void setBackGroundColor(Color color){
        this.backgroundColor = color;
    }

    public void setSettingFromDialog(DialogProperties dialogProperties){
        zF = dialogProperties.getZf();
        zN = dialogProperties.getZn();
        sH = dialogProperties.getSh();
        sW = dialogProperties.getSw();

        backgroundColor = dialogProperties.getBackgroundColor();
        depth = dialogProperties.getDepth();
        quality = dialogProperties.getQuality();
        gamma = dialogProperties.getGamma();
    }

    public double getGamma() {
        return gamma;
    }

    public int getQuality() {
        return quality;
    }

    public int getDepth() {
        return depth;
    }

    public double getxMax() {
        return xMax;
    }

    public double getzN() {
        return zN;
    }

    public double getzF() {
        return zF;
    }

    public double getsW() {
        return sW;
    }

    public double getsH() {
        return sH;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setDepth(int depth){
        this.depth = depth;
    }

    public void setQuality(int quality){
        this.quality = quality;
    }

    public void setGamma(double gamma){
        this.gamma = gamma;
    }

    public Vector getViewPoint() {
        double[][] invertMatrix = MatrixFactory.getInvertRotateMatrix(rotateMatrix);

        double[] eye = this.camera.getEyePoint();
        double[] view = this.camera.getViewPoint();
        double[] up = this.camera.getUpVector();

        up = adjustUpVector(view, eye, up);

        Vector newEye, newView, newUp;

        newUp = new Vector(multiple(invertMatrix, new double[]{up[0], up[1], up[2], 1d}));

        invertMatrix = MatrixFactory.matrixMultiplication(invertMatrix, MatrixFactory.getMoveMatrix(-this.camera.getViewPoint()[0],
                -this.camera.getViewPoint()[1], -this.camera.getViewPoint()[2]));
        invertMatrix = MatrixFactory.matrixMultiplication(MatrixFactory.getMoveMatrix(this.camera.getViewPoint()[0],
                this.camera.getViewPoint()[1], this.camera.getViewPoint()[2]), invertMatrix);

        newView = new Vector(multiple(invertMatrix, new double[]{view[0], view[1], view[2], 1d}));
        newEye = new Vector(multiple(invertMatrix, new double[]{eye[0], eye[1], eye[2], 1d}));

        //newView = new Vector(view);
        //newEye = new Vector(multiple(invertMatrix, eye));
        //newUp = new Vector(adjustUpVector(newView.toArray(), newEye.toArray(), new Vector(up).toArray()));
        newUp = new Vector(adjustUpVector(newView.toArray(), newEye.toArray(), newUp.toArray()));
        return newView;
    }

    public Vector getEyePoint() {

        double[][] invertMatrix = MatrixFactory.getInvertRotateMatrix(rotateMatrix);

        double[] eye = this.camera.getEyePoint();
        double[] view = this.camera.getViewPoint();
        double[] up = this.camera.getUpVector();

        up = adjustUpVector(view, eye, up);

        Vector newEye, newView, newUp;

        newUp = new Vector(multiple(invertMatrix, new double[]{up[0], up[1], up[2], 1d}));

        invertMatrix = MatrixFactory.matrixMultiplication(invertMatrix, MatrixFactory.getMoveMatrix(-this.camera.getViewPoint()[0],
                -this.camera.getViewPoint()[1], -this.camera.getViewPoint()[2]));
        invertMatrix = MatrixFactory.matrixMultiplication(MatrixFactory.getMoveMatrix(this.camera.getViewPoint()[0],
                this.camera.getViewPoint()[1], this.camera.getViewPoint()[2]), invertMatrix);

        newView = new Vector(multiple(invertMatrix, new double[]{view[0], view[1], view[2], 1d}));
        newEye = new Vector(multiple(invertMatrix, new double[]{eye[0], eye[1], eye[2], 1d}));

        //newView = new Vector(view);
        //newEye = new Vector(multiple(invertMatrix, eye));
        //newUp = new Vector(adjustUpVector(newView.toArray(), newEye.toArray(), new Vector(up).toArray()));
        newUp = new Vector(adjustUpVector(newView.toArray(), newEye.toArray(), newUp.toArray()));
        return newEye;
    }

    public Vector getUpVector() {

        double[][] invertMatrix = MatrixFactory.getInvertRotateMatrix(rotateMatrix);

        double[] eye = this.camera.getEyePoint();
        double[] view = this.camera.getViewPoint();
        double[] up = this.camera.getUpVector();

        up = adjustUpVector(view, eye, up);

        Vector newEye, newView, newUp;

        newUp = new Vector(multiple(invertMatrix, new double[]{up[0], up[1], up[2], 1d}));

        invertMatrix = MatrixFactory.matrixMultiplication(invertMatrix, MatrixFactory.getMoveMatrix(-this.camera.getViewPoint()[0],
                -this.camera.getViewPoint()[1], -this.camera.getViewPoint()[2]));
        invertMatrix = MatrixFactory.matrixMultiplication(MatrixFactory.getMoveMatrix(this.camera.getViewPoint()[0],
                this.camera.getViewPoint()[1], this.camera.getViewPoint()[2]), invertMatrix);

        newView = new Vector(multiple(invertMatrix, new double[]{view[0], view[1], view[2], 1d}));
        newEye = new Vector(multiple(invertMatrix, new double[]{eye[0], eye[1], eye[2], 1d}));

        //newView = new Vector(view);
        //newEye = new Vector(multiple(invertMatrix, eye));
        //newUp = new Vector(adjustUpVector(newView.toArray(), newEye.toArray(), new Vector(up).toArray()));
        newUp = new Vector(adjustUpVector(newView.toArray(), newEye.toArray(), newUp.toArray()));
        return newUp;

    }

}
