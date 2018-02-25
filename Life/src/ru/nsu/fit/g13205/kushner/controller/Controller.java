package ru.nsu.fit.g13205.kushner.controller;

import ru.nsu.fit.g13205.kushner.model.Field;
import ru.nsu.fit.g13205.kushner.model.LifeModel;
import ru.nsu.fit.g13205.kushner.model.LifeModelListener;
import ru.nsu.fit.g13205.kushner.model.LifeModelObservable;
import ru.nsu.fit.g13205.kushner.view.MainWindow;
import ru.nsu.fit.g13205.kushner.view.setting.EditSetting;
import ru.nsu.fit.g13205.kushner.view.setting.GameSetting;

import java.awt.*;

/**
 * Created by Konstantin on 25.02.2016.
 */
public class Controller {

    private MainWindow mainWindow;
    private LifeModel model;

    public Controller() {
        model = new LifeModel();
        mainWindow = new MainWindow(1000, 600, "Life", this);
        mainWindow.setMinimumSize(new Dimension(800, 600));
        model.subscribe(mainWindow);
        mainWindow.setVisible(true);
    }

    public void handleNewGameSetting(GameSetting setting){
        model.handleNewGameSetting(setting);
    }

    public void handleLoadFile(boolean[][] livingCells){
        model.handleLoadFile(livingCells);
    }

    public void handleChangeStatusCell(int m, int n, boolean status){
        model.handleChangeStatusCell(m, n, status);
    }

    public void handleChangeSize(int m, int n) {
        model.handleChangeSize(m, n);
    }

    public void handleStepAction(){
        model.handleStepAction();
    }

    public void handleStartAction() {
        model.handleStartAction();
    }

    public void handleStopAction(){
        model.handleStopAction();
    }

    public void handleClearField(){
        model.handleClearField();
    }
}
