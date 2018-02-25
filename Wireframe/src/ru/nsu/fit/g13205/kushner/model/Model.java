package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.utils.*;
import ru.nsu.fit.g13205.kushner.view.SplineDialogInterface;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Konstantin on 20.04.2016.
 */
public class Model implements ModelObservable{
    private ArrayList<ModelListener> listeners = new ArrayList<>();
    private SplineDialogInterface dialog;
    private BufferedImage dialogImage = new BufferedImage(Settings.DIALOG_IMAGE_WIDTH, Settings.DIALOG_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private BufferedImage image;
    private Surface currentSurface;
    private int numberOfFigure;
    private double[][] matrixOfCamera;
    private double[][] projMatrix;
    private Scene scene;

    private Color backgroundColor = Settings.BACKGROUND_COLOR;

    private AreaProperties areaProperties = Settings.getDefaultAreaProperties();

    private boolean isOpenDialog = false;
    private boolean isModificate = false;
    private boolean isSwitch = false;
    private boolean openedFile = false;

    public Model() {
        matrixOfCamera = MatrixFactory.getMatrixOfCamera(Settings.pCam, Settings.pView, Settings.vUp);
        scene = new Scene(matrixOfCamera);
    }

    public void handleReset(){
        double[][] newRotation = new double[4][4];
        for(int i = 0; i < 4; i++){
            newRotation[i][i] = 1d;
        }
        scene.setRotMatrix(newRotation);
        doRepaint();
    }

    public void handleOpenFile(FileInfo fileInfo){
        if(fileInfo.getIsNewFormat()){
            scene = new Scene(matrixOfCamera);
            double[][] sceneMatrix = new double[4][4];
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    sceneMatrix[i][j] = fileInfo.getSceneRotateMatrix()[i][j];
                }
            }
            sceneMatrix[3][3] = 1d;

            scene.setRotMatrix(sceneMatrix);


            double sw = fileInfo.getSw();
            double sh = fileInfo.getSh();
            double zn = fileInfo.getZn();
            double zf = fileInfo.getZf();

            projMatrix = MatrixFactory.getProjMatrix(sw, sh, zn, zf);


            ArrayList<FigureFileInfo> figures = fileInfo.getFigures();
            for(FigureFileInfo figure: figures){
                Spline spline = Algorithms.drawSpline(dialogImage, figure.getSplinePoints());
                Properties properties = new Properties((int)figure.getN(), (int)figure.getM(), (int)figure.getK(),
                        figure.getA(), figure.getB(), figure.getC(), figure.getD(), figure.getColor(), figure.getcX(), figure.getcY(), figure.getcZ());
                double[][] rotationMatrix = new double[4][4];
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        rotationMatrix[i][j] = figure.getRotateMatrix()[i][j];
                    }
                }
                rotationMatrix[3][3] = 1d;
                Surface surface = new Surface(spline, properties, rotationMatrix, matrixOfCamera);
                scene.addSurface(surface);

            }
            backgroundColor = fileInfo.getBackgroundColor();
            scene.updateProjMatrix(projMatrix);
            fileInfo.getSceneRotateMatrix();
        }else{
            scene = new Scene(matrixOfCamera);
            double[][] sceneMatrix = new double[4][4];
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    sceneMatrix[i][j] = fileInfo.getSceneRotateMatrix()[i][j];
                }
            }
            sceneMatrix[3][3] = 1d;

            scene.setRotMatrix(sceneMatrix);
            int n = (int) fileInfo.getN();
            int m = (int) fileInfo.getM();
            int k = (int) fileInfo.getK();
            double a = fileInfo.getA();
            double b = fileInfo.getB();
            double c = fileInfo.getC();
            double d = fileInfo.getD();

            double sw = fileInfo.getSw();
            double sh = fileInfo.getSh();
            double zn = fileInfo.getZn();
            double zf = fileInfo.getZf();

            projMatrix = MatrixFactory.getProjMatrix(sw, sh, zn, zf);

            ArrayList<FigureFileInfo> figures = fileInfo.getFigures();
            try {
                for (FigureFileInfo figure : figures) {
                    Spline spline = Algorithms.drawSpline(dialogImage, figure.getSplinePoints());
                    Properties properties = new Properties((int)figure.getN(), (int)figure.getM(), (int)figure.getK(),
                            figure.getA(), figure.getB(), figure.getC(), figure.getD(), figure.getColor(), figure.getcX(), figure.getcY(), figure.getcZ());
                    double[][] rotationMatrix = new double[4][4];
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            rotationMatrix[i][j] = figure.getRotateMatrix()[i][j];
                        }
                    }
                    rotationMatrix[3][3] = 1d;
                    Surface surface = new Surface(spline, properties, rotationMatrix, matrixOfCamera);
                    scene.addSurface(surface);

                }
                backgroundColor = fileInfo.getBackgroundColor();
                scene.updateProjMatrix(projMatrix);
                fileInfo.getSceneRotateMatrix();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        openedFile = true;
        doRepaint();
    }


    public void handleSave(File file) throws IOException {
        FileInfo info = createFileInfo();
        Writer fileWriter = new Writer(file);
        fileWriter.write(info);
    }

    private FileInfo createFileInfo(){
        Properties p = Settings.getDefaultProperties();
        FileInfo fileInfo = new FileInfo(p.getN(), p.getM(), p.getK(), p.getA(), p.getB(), p.getC(), p.getD(),
                areaProperties.getZn(), areaProperties.getZf(), areaProperties.getSw(), areaProperties.getSh(), backgroundColor, scene.getRoateMatrix());

        for(int i = 0; i < scene.size(); i++){
            Surface tmp = scene.getSurface(i);
            Properties properties = tmp.getProperties();
            FigureFileInfo figureInfo = new FigureFileInfo(properties.getN(), properties.getM(), properties.getK(), properties.getA(),
                    properties.getB(), properties.getC(), properties.getD(), properties.getcX(), properties.getcY(), properties.getcZ(),
                    tmp.getRotateMatrix(), tmp.getSpline().getCoordinatesPoints(), properties.getColor());

            fileInfo.addFigureInfo(figureInfo);
        }
        return fileInfo;
    }

    public void handleUpdateZn(double delta){
        areaProperties.setZn(areaProperties.getZn() + delta);

        projMatrix = MatrixFactory.getProjMatrix(areaProperties.getSw(), areaProperties.getSh(),
                areaProperties.getZn(), areaProperties.getZf());
        scene.updateProjMatrix(projMatrix);
        doRepaint();
        updateDialogValue(currentSurface.getProperties());
    }

    public void handleUpdateAreaProperties(AreaProperties areaProperties){
        this.areaProperties = areaProperties;
        projMatrix = MatrixFactory.getProjMatrix(areaProperties.getSw(), areaProperties.getSh(),
                areaProperties.getZn(), areaProperties.getZf());
        scene.updateProjMatrix(projMatrix);
        doRepaint();
        image = null;
    }

    public void handleCloseDialog(){
        isSwitch = true;
        isOpenDialog = false;
    }


    public void handleOkButton(){
        isSwitch = true;
        isOpenDialog = false;
    }


    public void handleOpenDialog(SplineDialogInterface dialog){
        try {
            AreaProperties tmp = Settings.getDefaultAreaProperties();
            projMatrix = MatrixFactory.getProjMatrix(tmp.getSw(), tmp.getSh(), tmp.getZn(), tmp.getZf());
            scene.updateProjMatrix(projMatrix);
            this.dialog = dialog;

            if (scene == null  || scene.size() == 0) {
                handleShowDefaultFunction();
                scene.addSurface(currentSurface);

            } else {
                currentSurface = scene.getSurface(0);
                Algorithms.drawSpline(dialogImage, currentSurface.getSpline());

                dialog.updateImage(dialogImage, currentSurface.getSpline());
                updateDialogValue(currentSurface.getProperties());
                doRepaint();
            }
            numberOfFigure = 0;
            updateDialogValue(currentSurface.getProperties());
            isOpenDialog = true;
            isSwitch = false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleRotate(double rX, double rY, double rZ){
        if(isOpenDialog) {
            currentSurface.rotateSurface(rX, rY, rZ);
            doRepaint();
        }else{
            scene.rotateScene(rX, rY, rZ);
            doRepaint();
        }
    }

    public void handleAddFigure(){
        handleShowDefaultFunction();
        scene.addSurface(currentSurface);
        numberOfFigure = scene.size() - 1;
        updateDialogValue(currentSurface.getProperties());
    }

    public void handleDeleteFigure(){
        scene.removeSurface(numberOfFigure);
        isModificate = true;
        if(scene.size()!=0){
            currentSurface = scene.getSurface(0);
        }
        doRepaint();
    }

    public void handleSwitchOnNextFigure(){
        if(scene.size() > numberOfFigure + 1){
            isSwitch = true;
            currentSurface = scene.getSurface(numberOfFigure+1);
            numberOfFigure++;
            updateDialogValue(currentSurface.getProperties());
            clearDialogImage();
            Algorithms.drawSpline(dialogImage, currentSurface.getSpline().getCoordinatesPoints());
            dialog.updateImage(dialogImage, currentSurface.getSpline());
            isSwitch = false;
        }
    }

    public void handleSwitchOnPrevFigure(){
        if(numberOfFigure - 1 >= 0){
            isSwitch = true;
            currentSurface = scene.getSurface(numberOfFigure-1);
            numberOfFigure--;
            updateDialogValue(currentSurface.getProperties());
            clearDialogImage();
            Algorithms.drawSpline(dialogImage, currentSurface.getSpline().getCoordinatesPoints());
            dialog.updateImage(dialogImage, currentSurface.getSpline());
            isSwitch = false;
        }
    }


    private void handleShowDefaultFunction(){
        clearDialogImage();
        ArrayList<Coordinates> points = new ArrayList<Coordinates>();
        points.add(Settings.P1);
        points.add(Settings.P2);
        points.add(Settings.P3);
        points.add(Settings.P4);
        points.add(Settings.P5);
        points.add(Settings.P6);
        points.add(Settings.P7);
        points.add(Settings.P8);

        Spline spline = Algorithms.drawSpline(dialogImage, points);
        currentSurface = new Surface(spline, Settings.getDefaultProperties(), matrixOfCamera);
        dialog.updateImage(dialogImage, spline);
        doRepaint();
    }

    public void handleUpdateProperties(Properties properties){
        if(!isSwitch) {
            scene.removeSurface(numberOfFigure);
            currentSurface = new Surface(currentSurface.getSpline(), properties, currentSurface.getRotateMatrix(), matrixOfCamera);
            scene.addSurface(currentSurface, numberOfFigure);
            doRepaint();
        }
    }

    public void handleUpdateColor(Color color){
        if(!isSwitch) {
            currentSurface.setColor(color);
            currentSurface.getProperties().setColor(color);
            doRepaint();
        }
    }

    public void handleNewBasicPoint(int pX, int pY, Point _point){
        int index = currentSurface.indexPointOfSpline(_point);
        currentSurface.addPointsOfSpline(fromPixelToCoordinates(pX, pY), index);

        Spline newSpline = Algorithms.drawSpline(dialogImage, currentSurface.getSplinePoints());
        scene.removeSurface(numberOfFigure);
        currentSurface = new Surface(newSpline, currentSurface.getProperties(), currentSurface.getRotateMatrix(), matrixOfCamera);
        scene.addSurface(currentSurface, numberOfFigure);
        dialog.updateImage(dialogImage, currentSurface.getSpline());
        doRepaint();
    }

    public void handleDeleteBasicPoint(Point _point){
        currentSurface.removePointOfSpline(_point);
        Spline newSpline = Algorithms.drawSpline(dialogImage, currentSurface.getSplinePoints());
        scene.removeSurface(numberOfFigure);
        currentSurface = new Surface(newSpline, currentSurface.getProperties(), currentSurface.getRotateMatrix(), matrixOfCamera);
        scene.addSurface(currentSurface, numberOfFigure);
        dialog.updateImage(dialogImage, currentSurface.getSpline());
        doRepaint();
    }

    public void handleBasicPointDragged(int pX, int pY, Coordinates point){
        int index = currentSurface.indexPointOfSpline(point);
        currentSurface.removePointOfSpline(point);
        currentSurface.addPointsOfSpline(fromPixelToCoordinates(pX, pY), index);
        Spline newSpline = Algorithms.drawSpline(dialogImage, currentSurface.getSplinePoints());
        scene.removeSurface(numberOfFigure);
        currentSurface = new Surface(newSpline, currentSurface.getProperties(), currentSurface.getRotateMatrix(), matrixOfCamera);
        scene.addSurface(currentSurface, numberOfFigure);
        dialog.updateImageAndDraggedPoint(dialogImage, currentSurface.getSpline(), currentSurface.getSplinePoints().get(index));
        doRepaint();
    }

    public BufferedImage getMainImage(){


        if(currentSurface!=null && isModificate) {
            drawScene();
        }
        if(openedFile){
            drawScene();
            openedFile = false;
        }

        if(isModificate){
            drawScene();
        }
        return image;
    }

    //////////////////////////////////////////

    private void updateDialogValue(Properties properties){
        projMatrix = MatrixFactory.getProjMatrix(areaProperties.getSw(), areaProperties.getSh(), areaProperties.getZn(), areaProperties.getZf());
        scene.updateProjMatrix(projMatrix);
        dialog.updateProperties(properties, areaProperties, numberOfFigure);

    }


    private Coordinates fromPixelToCoordinates(int pX, int pY){
        double x, y, delta = currentSurface.getSplineDelta();
        int width = dialogImage.getWidth();
        int height = dialogImage.getHeight();

        x = (pX > width/2) ? (double)(pX - width/2)/ delta : (double)(width/2 - pX) / delta * (-1);
        y = (pY < height/2) ? (double)(height/2 - pY) / delta : (double)(pY - height/2) / delta * (-1);

        return new Coordinates(x, y);
    }


    private Point fromCoordToPixel(Coordinates point, int width, int height, double delta){
        double x = point.getX(), y = point.getY();
        int pX = (int) (width/2 + x*delta);
        int pY = (int) (height/2 - delta*y);
        return new Point(pX, pY);
    }

    private double calculateDelta(double xMax, double yMax){
        double deltaX = image.getWidth() / (2*(xMax));
        double deltaY = image.getHeight() / (2*(yMax));

        double delta = deltaX < deltaY ? deltaX : deltaY;//теперь маштабирование будеи идти по более плотной оси
        return Math.round(delta);
    }

    private void drawScene(){
        isModificate = false;
        if(image == null) {
            double ratio = areaProperties.getSw()/areaProperties.getSh();
            image = new BufferedImage((int) (ratio * Settings.MIN_SIZE_IMAGE), (int) Settings.MIN_SIZE_IMAGE, BufferedImage.TYPE_INT_RGB);
            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
                    image.setRGB(x, y, backgroundColor.getRGB());
                }
            }
        }else {
            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
                    image.setRGB(x, y, backgroundColor.getRGB());
                }
            }
        }

        if(scene != null) {
            for (int i = 0; i < scene.size(); i++) {
                scene.getSurface(i).calculateMinAndMax(scene.getRoateMatrix());
            }

            scene.calculateMinAndMax();
        }

        double delta = calculateDelta(scene.getAbsX()/2, scene.getAbsY()/2) - 30;
        double shiftX = scene.getShiftX();
        double shiftY = scene.getShiftY();
        double shiftZ = scene.getShiftZ();
        double compressionRatio = scene.getCompressionRatio();

        delta = image.getWidth()/2.7 > image.getHeight()/2.7 ? image.getWidth()/2.7: image.getHeight()/2.7;


        if(scene != null) {
            for (int i = 0; i < scene.size(); i++) {
                    drawRotationFigure(image, scene.getSurface(i).getWireframe(scene.getCompressionRatio(),
                        scene.getRoateMatrix(), shiftX, shiftY, shiftZ),
                        scene.getSurface(i).getProperties(), delta);
            }
            ArrayList<ArrayList<Coordinates3D>> box = scene.getRotatesBox();

            Graphics g = image.getGraphics();
            g.setColor(Settings.BOX_COLOR);

            for(int i = 0; i < box.size(); i++){
                for(int j = 0; j < box.get(i).size() - 1; j++){
                    Coordinates3D c1 = box.get(i).get(j);
                    Coordinates3D c2 = box.get(i).get(j+1);

                    if(Clipper.cubCheck(c1, areaProperties) && Clipper.cubCheck(c2, areaProperties)) {
                        Point p1 = fromCoordToPixel(new Coordinates(box.get(i).get(j).getTransformX(),
                                box.get(i).get(j).getTransformY()), image.getWidth(), image.getHeight(), delta);
                        Point p2 = fromCoordToPixel(new Coordinates(box.get(i).get(j + 1).getTransformX(),
                                box.get(i).get(j + 1).getTransformY()), image.getWidth(), image.getHeight(), delta);
                        g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                    }
                }
            }
            ArrayList<Coordinates3D> axis = scene.getRotateAxis();
            Point O = fromCoordToPixel(new Coordinates(0, 0), image.getWidth(), image.getHeight(), delta);
            g.setColor(Settings.X_COLOR);
            Point p = fromCoordToPixel(new Coordinates(axis.get(0).getTransformX(),
                    axis.get(0).getTransformY()), image.getWidth(), image.getHeight(), delta);
            g.drawLine((int) O.getX(), (int) O.getY(), (int) p.getX(), (int) p.getY());

            g.setColor(Settings.Y_COLOR);
            p = fromCoordToPixel(new Coordinates(axis.get(1).getTransformX(),
                    axis.get(1).getTransformY()), image.getWidth(), image.getHeight(), delta);
            g.drawLine((int) O.getX(), (int) O.getY(), (int) p.getX(), (int) p.getY());

            g.setColor(Settings.Z_COLOR);
            p = fromCoordToPixel(new Coordinates(axis.get(2).getTransformX(),
                    axis.get(2).getTransformY()), image.getWidth(), image.getHeight(), delta);
            g.drawLine((int) O.getX(), (int) O.getY(), (int) p.getX(), (int) p.getY());
        }
    }

    private void doRepaint(){
        isModificate = true;
        for (ModelListener listener : listeners) {
            listener.WindowPanelRepaint();
        }
    }

    private void drawRotationFigure(BufferedImage image, WireFrame wireFrame, Properties properties, double delta){
        Graphics graphics = image.getGraphics();
        graphics.setColor(properties.getColor());

        ArrayList<ArrayList<Coordinates3D>> xSections = wireFrame.getxSections();
        ArrayList<ArrayList<Coordinates3D>> phSections = wireFrame.getPhSections();

        for(int i = 0; i < xSections.size(); i += properties.getK()){//b сплайн смешенный на угол относительно начального
            for(int j = 0; j < xSections.get(i).size() - 1; j++){
                Coordinates3D coord1 = xSections.get(i).get(j);
                Coordinates3D coord2 = xSections.get(i).get(j + 1);

                if(Clipper.check(coord1, areaProperties) && Clipper.check(coord2, areaProperties)) {

                     Point p1 = fromCoordToPixel(new Coordinates(xSections.get(i).get(j).getTransformX(),
                            xSections.get(i).get(j).getTransformY()), image.getWidth(), image.getHeight(), delta);
                    Point p2 = fromCoordToPixel(new Coordinates(xSections.get(i).get(j + 1).getTransformX(),
                            xSections.get(i).get(j + 1).getTransformY()), image.getWidth(), image.getHeight(), delta);
                    graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                }else{
                }
            }
        }

        for(int i = 0; i < phSections.size(); i+= properties.getK()){
            try {
                try {
                    for (int j = 0; j < phSections.get(i).size() - 1; j++) {
                        Coordinates3D coord1 = phSections.get(i).get(j);
                        Coordinates3D coord2 = phSections.get(i).get(j + 1);

                        if (Clipper.check(coord1, areaProperties) && Clipper.check(coord2, areaProperties)) {
                            Point p1 = fromCoordToPixel(new Coordinates(phSections.get(i).get(j).getTransformX(),
                                    phSections.get(i).get(j).getTransformY()), image.getWidth(), image.getHeight(), delta);
                            Point p2 = fromCoordToPixel(new Coordinates(phSections.get(i).get(j + 1).getTransformX(),
                                    phSections.get(i).get(j + 1).getTransformY()), image.getWidth(), image.getHeight(), delta);
                            graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                        }
                    }
                }catch (Exception v){
                    v.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();

            }
        }

        Point p1 = fromCoordToPixel(new Coordinates(wireFrame.getAxis().get(0).get(0).getTransformX(),
                wireFrame.getAxis().get(0).get(0).getTransformY()), image.getWidth(), image.getHeight(), delta);

        Point p2 = fromCoordToPixel(new Coordinates(wireFrame.getAxis().get(0).get(1).getTransformX(),
                wireFrame.getAxis().get(0).get(1).getTransformY()), image.getWidth(), image.getHeight(), delta);

        graphics.setColor(Settings.X_COLOR);
        graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());

        p1 = fromCoordToPixel(new Coordinates(wireFrame.getAxis().get(1).get(0).getTransformX(),
                wireFrame.getAxis().get(1).get(0).getTransformY()), image.getWidth(), image.getHeight(), delta);

        p2 = fromCoordToPixel(new Coordinates(wireFrame.getAxis().get(1).get(1).getTransformX(),
                wireFrame.getAxis().get(1).get(1).getTransformY()), image.getWidth(), image.getHeight(), delta);

        graphics.setColor(Settings.Y_COLOR);
        graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());

        p1 = fromCoordToPixel(new Coordinates(wireFrame.getAxis().get(2).get(0).getTransformX(),
                wireFrame.getAxis().get(2).get(0).getTransformY()), image.getWidth(), image.getHeight(), delta);

        p2 = fromCoordToPixel(new Coordinates(wireFrame.getAxis().get(2).get(1).getTransformX(),
                wireFrame.getAxis().get(2).get(1).getTransformY()), image.getWidth(), image.getHeight(), delta);

        graphics.setColor(Settings.Z_COLOR);
        graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
    }

    private void clearDialogImage(){
        if(dialogImage == null) {
            dialogImage = new BufferedImage(Settings.DIALOG_IMAGE_WIDTH, Settings.DIALOG_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        }else{
            for(int y = 0; y < dialogImage.getHeight(); y++){
                for(int x = 0; x < dialogImage.getWidth(); x++){
                    dialogImage.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
    }

    @Override
    public void subscribe(ModelListener listener) {
        listeners.add(listener);
    }
}
