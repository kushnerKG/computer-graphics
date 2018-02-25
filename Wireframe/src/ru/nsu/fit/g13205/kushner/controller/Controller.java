package ru.nsu.fit.g13205.kushner.controller;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.model.Model;
import ru.nsu.fit.g13205.kushner.utils.AreaProperties;
import ru.nsu.fit.g13205.kushner.utils.Coordinates;
import ru.nsu.fit.g13205.kushner.utils.FileInfo;
import ru.nsu.fit.g13205.kushner.utils.Properties;
import ru.nsu.fit.g13205.kushner.view.MainWindow;
import ru.nsu.fit.g13205.kushner.view.SplineDialogInterface;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Konstantin on 23.04.2016.
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

    public void handleCloseDialog(){
        model.handleCloseDialog();
    }

    public void handleOpenDialog(SplineDialogInterface dialog){
        model.handleOpenDialog(dialog);
    }

    public void handleRotate(double rX, double rY, double rZ){
        model.handleRotate(rX, rY, rZ);
    }

    public void handleAddFigure(){
        model.handleAddFigure();
    }

    public void handleDeleteFigure(){
        model.handleDeleteFigure();
    }

    public void handleSwitchOnNextFigure(){
        model.handleSwitchOnNextFigure();
    }

    public void handleSwitchOnPrevFigure(){
        model.handleSwitchOnPrevFigure();
    }

    public void handleOkButton(){
        model.handleOkButton();
    }

    public void handleNewBasicPoint(int pX, int pY, Point point){
        model.handleNewBasicPoint(pX, pY, point);
    }

    public void handleDeleteBasicPoint(Point point){
        model.handleDeleteBasicPoint(point);
    }

    public void handleBasicPointDragged(int pX, int pY, Coordinates point){
        model.handleBasicPointDragged(pX, pY, point);
    }

    public void handleUpdateMainImage(Properties properties){}

    public void handleUpdateProperties(Properties properties){
        model.handleUpdateProperties(properties);
    }

    public void handleUpdateColor(Color color){
        model.handleUpdateColor(color);
    }

    public BufferedImage getMainImage(){
        return model.getMainImage();
    }

    public void handleUpdateAreaProperties(AreaProperties areaProperties){
        model.handleUpdateAreaProperties(areaProperties);
    }

    public void handleUpdateZn(double delta){
        model.handleUpdateZn(delta);
    }

    public void handleSave(File file) throws IOException {
        model.handleSave(file);
    }

    public void handleOpenFile(FileInfo fileInfo){
        model.handleOpenFile(fileInfo);
    }

    public void handleReset(){
        model.handleReset();
    }
}
