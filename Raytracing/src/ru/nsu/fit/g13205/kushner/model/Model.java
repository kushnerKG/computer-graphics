package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.utils.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class Model implements ModelObservable {

    private ArrayList<ModelListener> listeners = new ArrayList<>();
    private Scene scene;
    private BufferedImage image;

    private int imageHeight = Settings.IMAGE_HEIGHT;

    private SceneFileInfo sceneFileInfo;
    private RenderInfo renderFileInfo;

    private Color backGroundColor = Settings.DEFAULT_BACKGROUND_COLOR;
    private Camera camera;

    private double sW = Settings.SW;
    private double sH = Settings.SH;

    private boolean isRender = false;
    private BufferedImage renderImage;
    Properties properties;

    @Override
    public void handleOpenSceneFileWithRender(SceneFileInfo sceneFileInfo, RenderInfo renderFileInfo) {

        this.renderFileInfo = renderFileInfo;
        this.sceneFileInfo = sceneFileInfo;

        camera = new Camera(renderFileInfo.getViewVector().toArray(), renderFileInfo.getEyeVector().toArray(),
                renderFileInfo.getUpVector().toArray());

        scene = new Scene(renderFileInfo, sceneFileInfo, camera);

        sW = renderFileInfo.getSw();
        sH = renderFileInfo.getSh();
        properties = new Properties(renderFileInfo.getZn(), renderFileInfo.getZf(), renderFileInfo.getSw(), renderFileInfo.getSh());
        backGroundColor = renderFileInfo.getBackgroundColor();

        scene.setBackGroundColor(backGroundColor);
        image = new BufferedImage(imageHeight, (int)(sW/sH)*imageHeight, BufferedImage.TYPE_INT_RGB);

        if(properties!=null){
            scene.setProperties(properties);
        }

        repaintMainPanel();


    }

    @Override
    public void handleNewPropertiesFromDialog(DialogProperties dialogProperties) {
        sW = dialogProperties.getSw();
        sH = dialogProperties.getSh();
        backGroundColor = dialogProperties.getBackgroundColor();

        properties = new Properties(dialogProperties.getZn(), dialogProperties.getZf(), dialogProperties.getSw(), dialogProperties.getSh());

        scene.setSettingFromDialog(dialogProperties);
    }

    @Override
    public void handleSaveRenderFile(File file) throws IOException {

        RenderInfo renderFileInfo = new RenderInfo(scene.getBackgroundColor(), scene.getGamma(), scene.getDepth(), scene.getQuality(),
                new Coordinates3D(scene.getEyePoint().getValue(0), scene.getEyePoint().getValue(1), scene.getEyePoint().getValue(2), 1d),
                new Coordinates3D(scene.getViewPoint().getValue(0), scene.getViewPoint().getValue(1), scene.getViewPoint().getValue(2), 1d),
                new Coordinates3D(scene.getUpVector().getValue(0), scene.getUpVector().getValue(1), scene.getUpVector().getValue(2), 1d),
                scene.getzN(), scene.getzF(), scene.getsW(), scene.getsH());

        RenderFileWriter renderFileWriter = new RenderFileWriter();

        renderFileWriter.write(file, renderFileInfo);

    }

    @Override
    public void handleNewHeight(int height) {
        imageHeight = height;
        if(!isRender){
            image = new BufferedImage((int)(sW/sH * imageHeight), imageHeight, BufferedImage.TYPE_INT_RGB);
            //scene.drawScene(image, camera.getMatrixOfCamera());
            if(scene!=null){
                scene.drawScene(image, camera.getMatrixOfCamera());
            }
        }
    }

    @Override
    public void handleOpenSceneFile(SceneFileInfo sceneFileInfo) {
        this.sceneFileInfo = sceneFileInfo;

        this.renderFileInfo = null;

        RenderInfo renderFileInfo = new RenderInfo(Settings.DEFAULT_BACKGROUND_COLOR, Settings.DEFAULT_GAMMA, Settings.DEFAULT_DEPTH,
                Settings.DEFAULT_QUALITY,
                new Coordinates3D(Settings.DEFAULT_EYE_POINT[0], Settings.DEFAULT_EYE_POINT[1], Settings.DEFAULT_EYE_POINT[2], 1d),
                new Coordinates3D(Settings.DEFAULT_VIEW_POINT[0], Settings.DEFAULT_VIEW_POINT[1], Settings.DEFAULT_VIEW_POINT[2], 1d),
                new Coordinates3D(Settings.DEFAULT_UP_VECTOR[0], Settings.DEFAULT_UP_VECTOR[1], Settings.DEFAULT_UP_VECTOR[2], 1d),
                Settings.ZN, Settings.ZF, Settings.SW, Settings.SH);

        this.renderFileInfo = renderFileInfo;

        camera = new Camera(renderFileInfo.getViewVector().toArray(), renderFileInfo.getEyeVector().toArray(),
                renderFileInfo.getUpVector().toArray());
        scene = new Scene(renderFileInfo, sceneFileInfo, camera);
        scene.setProperties(new Properties(Settings.ZN, Settings.ZF, Settings.SW, Settings.SH));

        scene.setBackGroundColor(backGroundColor);
        image = new BufferedImage(imageHeight, (int)(sW / sH) * imageHeight, BufferedImage.TYPE_INT_RGB);

        scene.setProperties(properties = new Properties(renderFileInfo.getZn(), renderFileInfo.getZf(),
                renderFileInfo.getSw(), renderFileInfo.getSh()));

        scene.drawScene(image, camera.getMatrixOfCamera());
        handlePushOnInitButton();
        repaintMainPanel();
    }



    @Override
    public void handleUpdateMainImage() {//пересчитываем изображение
        if(scene!=null) {
            scene.drawScene(image, camera.getMatrixOfCamera());
            updateImage(image);
        }
    }

    @Override
    public void handleMoveCamera(double xAngle, double yAngle, double zAngle) {
        if(!isRender) {
            //camera.moveCamera(xAngle, yAngle, zAngle);
            scene.rotateScene(xAngle, yAngle, zAngle);
            repaintMainPanel();
        }
    }

    @Override
    public void handleZoomCamera(double delta) {
        camera.zoom(delta);
        repaintMainPanel();
    }

    @Override
    public void handleMoveCameraToRight() {
        camera.moveToRight();
        repaintMainPanel();
    }

    @Override
    public void handleMoveCameraToLeft() {
        camera.moveToLeft();
        repaintMainPanel();
    }

    @Override
    public void handleMoveCameraToUp() {
        camera.moveToUp();
        repaintMainPanel();
    }

    @Override
    public void handleMoveCameraToDown() {
        camera.moveToDown();
        repaintMainPanel();
    }

    @Override
    public void handleChangeZnWithWeel(double ratio) {
        scene.changeZn(ratio);
        repaintMainPanel();
    }

    @Override
    public void handleChangeProperties(Properties properties) {
        scene.setProperties(properties);

        if(sW != properties.getSw() || sH != properties.getSh()){
            sW = properties.getSw();
            sH = properties.getSh();
            image = new BufferedImage( (int) (sW/sH * imageHeight), imageHeight,  BufferedImage.TYPE_INT_RGB);
        }

        repaintMainPanel();
    }

    @Override
    public void handleGetProperties() {
        DialogProperties properties = scene.getProperties();
        for(ModelListener listener: listeners){
            listener.updateProperties(properties);
        }
    }


    @Override
    public void handlePushOnInitButton() {
        if(!isRender) {
            scene.initPosition();
            camera.initPosition(scene.getBoxAbsX(), scene.getBoxAbsZ(), scene);
            repaintMainPanel();
        }
    }

    @Override
    public void handleRenderScene() {
        renderImage = new BufferedImage(image.getWidth(), (int)(sW / sH) * image.getHeight(), BufferedImage.TYPE_INT_RGB);

        isRender = true;
        scene.renderScene(renderImage, listeners);
    }

    @Override
    public void handleShowWireframe() {
        isRender = false;
        repaintMainPanel();
    }

    @Override
    public void handleOpenRenderFile(RenderInfo fileInfo) {

        this.renderFileInfo = fileInfo;
        if(camera!=null) {
            camera.setEyePoint(fileInfo.getEyeVector());
            camera.setViewPoint(fileInfo.getViewVector());
            camera.setUpPoint(fileInfo.getUpVector());

        }
        sW = fileInfo.getSw();
        sH = fileInfo.getSh();
        properties = new Properties(fileInfo.getZn(), fileInfo.getZf(), fileInfo.getSw(), fileInfo.getSh());
        scene.setProperties(properties);
        backGroundColor = fileInfo.getBackgroundColor();
        scene.setBackGroundColor(backGroundColor);
        scene.setDepth(fileInfo.getDepth());
        scene.setQuality(fileInfo.getQuality());
        scene.setGamma(fileInfo.getGamma());
        backGroundColor = fileInfo.getBackgroundColor();

        repaintMainPanel();
    }

    @Override
    public void handleSaveImage(File file) throws IOException {
        BMPWriter bmpWriter = new BMPWriter(file);
        if (isRender) {
            bmpWriter.write(renderImage);
        }else{
            bmpWriter.write(image);
        }
    }



    @Override
    public void subscribe(ModelListener listener) {
        listeners.add(listener);
    }

    private void updateImage(BufferedImage image){
        if(scene!=null) {
            for (ModelListener listener : listeners) {
                if (!isRender) {
                    listener.updateImage(image);
                } else {
                    listener.updateImage(renderImage);
                }
            }
        }
    }

    private void repaintMainPanel(){
        for(ModelListener listener: listeners){
            listener.windowPanelRepaint();
        }
    }
}
