package ru.nsu.fit.g13205.kushner.controller;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.model.Model;
import ru.nsu.fit.g13205.kushner.utils.DialogProperties;
import ru.nsu.fit.g13205.kushner.utils.Properties;
import ru.nsu.fit.g13205.kushner.utils.RenderInfo;
import ru.nsu.fit.g13205.kushner.utils.SceneFileInfo;
import ru.nsu.fit.g13205.kushner.view.MainWindow;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class Controller {
    Model model;
    MainWindow mainWindow;

    public Controller(int width, int height, int minWidth, int minHeight) {
        mainWindow = new MainWindow(width, height, Settings.PROJECT_NAME, this);
        model = new Model();
        mainWindow.setMinimumSize(new Dimension(minWidth, minHeight));
        model.subscribe(mainWindow);
        mainWindow.setVisible(true);
    }

    public void openSceneFile(SceneFileInfo fileInfo){
        model.handleOpenSceneFile(fileInfo);
    }

    public void updateMainImage(){
        model.handleUpdateMainImage();
    }

    public void rotate(double xAngle, double yAngle, double zAngle){
        model.handleMoveCamera(xAngle, yAngle, zAngle);
    }

    public void zoom(double ratio){
        model.handleZoomCamera(ratio);
    }

    public void moveCameraToLeft(){
        model.handleMoveCameraToLeft();
    }

    public void moveCameraToRight(){
        model.handleMoveCameraToRight();
    }

    public void moveCameraToUp(){
        model.handleMoveCameraToUp();
    }

    public void moveCameraToDown(){
        model.handleMoveCameraToDown();
    }

    public void changeZnWithWeel(double ratio){
        model.handleChangeZnWithWeel(ratio);
    }

    public void changeProperties(Properties properties){
        model.handleChangeProperties(properties);
    }

    public void pushOnInitButton(){
        model.handlePushOnInitButton();
    }

    public void getProperties(){
        model.handleGetProperties();
    }

    public void renderScene(){
        model.handleRenderScene();
    }

    public void showWireframe(){
        model.handleShowWireframe();
    }

    public void openRenderFile(RenderInfo fileInfo){
        model.handleOpenRenderFile(fileInfo);
    }

    public void saveImage(File file) throws IOException {
        model.handleSaveImage(file);
    }

    public void openSceneFileWithRender(SceneFileInfo sceneFileInfo, RenderInfo renderFileInfo){
        model.handleOpenSceneFileWithRender(sceneFileInfo, renderFileInfo);
    }

    public void newPropertiesFromDialog(DialogProperties properties){
        model.handleNewPropertiesFromDialog(properties);
    }

    public void saveRenderFile(File file) throws IOException {
        model.handleSaveRenderFile(file);
    }

    public void setHeightImage(int height){
        model.handleNewHeight(height);
    }

}

