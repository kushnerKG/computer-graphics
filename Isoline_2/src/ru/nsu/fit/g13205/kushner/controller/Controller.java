package ru.nsu.fit.g13205.kushner.controller;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.model.Model;
import ru.nsu.fit.g13205.kushner.utils.FileInfo;
import ru.nsu.fit.g13205.kushner.view.MainWindow;

import java.awt.*;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class Controller {
    Model model;
    MainWindow mainWindow;

    public Controller(int width, int height, int minWidth, int minHeight) {
        mainWindow = new MainWindow(width, height, Settings.PROJECT_NAME, this);
        model = new Model(mainWindow.getMainArea());
        mainWindow.setMinimumSize(new Dimension(minWidth, minHeight));
        model.subscribe(mainWindow);
        mainWindow.setVisible(true);
        model.handleNewFieldSize(mainWindow.getMainArea().getWidth(), mainWindow.getMainArea().height());
    }

    public void handleOpenFile(FileInfo info){
        model.handleFileOpen(info);
    }

    public void handleNewFieldSize(int width, int height){
        model.handleNewFieldSize(width, height);
    }

    public void handleCoordinates(int pixelX, int pixelY){
        model.handleCoordinates(pixelX, pixelY);
    }

    public void handleSelectDrawGrid(){
        model.handleSelectDrawGrid();
    }

    public void handleUnSelectDrawGrid(){
        model.handleUnSelectDrawGrid();
    }

    public void handleSelectShowIsoline(){
        model.handleSelectShowIsoline();
    }

    public void handleUnSelectDrawIsoline(){
        model.handleUnSelectDrawIsoline();
    }

    public void handleClickOnMap(int pX, int pY){
        model.handleClickOnMap(pX, pY);
    }

    public void handleNewSetting(FileInfo info, double a, double b, double c, double d){
        model.handleNewSetting(info, a, b, c, d);
    }

    public void handleSwitchOnInteractiveMode(){
        model.handleSwitchOnInteractiveMode();
    }

    public void handleSwitchOffInteractiveMode(){
        model.handleSwitchOffInteractiveMode();
    }

    public void handleDragged(int pX, int pY){
        model.handleDragged(pX, pY);
    }

    public void handleUnDragged(){
        model.handleUnDragged();
    }

    public void handleDeleteIsoline(){
        model.handleDeleteIsoline();
    }

    public void handleSwitchOnInterpolation(){
        model.handleSwitchOnInterpolation();
    }

    public void handleSwitchOffInterpolation(){
        model.handleSwitchOffInterpolation();
    }

    public void handleNewDocument(){
        model.handleNewDocument();
    }

    public void handleShowMap(){
        model.handleShowMap();
    }

    public void handleUnShowMap(){
        model.handleUnShowMap();
    }
}
