package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.SettingApplication;
import ru.nsu.fit.g13205.kushner.view.GameArea;
import ru.nsu.fit.g13205.kushner.view.setting.EditSetting;
import ru.nsu.fit.g13205.kushner.view.setting.GameSetting;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Konstantin on 25.02.2016.
 */
public class LifeModel implements LifeModelObservable{

    private ArrayList<LifeModelListener> listeners = new ArrayList<LifeModelListener>();
    private GameSetting gameSetting = new GameSetting(SettingApplication.LIFE_BEGIN, SettingApplication.LIFE_END,
            SettingApplication.BIRTH_BEGIN, SettingApplication.BIRTH_END, SettingApplication.FST_IMPACT, SettingApplication.SND_IMPACT);
    private int m = SettingApplication.M_DEFAULT_VALUE;
    private int n = SettingApplication.N_DEFAULT_VALUE;
    private Field field = new Field(gameSetting, SettingApplication.M_DEFAULT_VALUE, SettingApplication.N_DEFAULT_VALUE);

    public LifeModel() {
    }

    public void handleNewGameSetting(GameSetting setting){
        gameSetting = setting;
        field.handleNewGameSetting(setting);
        for(LifeModelListener listener: listeners){
            listener.updateImpact(field.doSnapshot());
        }
    }

    public void handleLoadFile(boolean[][] livingCells){
        field.handleLoadFile(livingCells);
        for(LifeModelListener listener: listeners){
            listener.updateImpact(field.doSnapshot());
        }
    }

    public void handleChangeStatusCell(int m, int n, boolean status){
        try {
            field.handleChangeStatusCell(m, n, status);
        }catch (Exception e){
            e.printStackTrace();
        }
        for(LifeModelListener listener: listeners){
            listener.updateImpact(field.doSnapshot());
        }
    }

    public void handleChangeSize(int m, int n) {
        this.m = m;
        this.n = n;
        field.handleChangeSize(m, n);
        for(LifeModelListener listener: listeners){
            listener.updateImpact(field.doSnapshot());
        }
    }

    public void handleStepAction(){

        field.doStep();
        for(LifeModelListener listener: listeners){
            listener.updateImpact(field.doSnapshot());
        }
    }

    public void handleStartAction() {

    }

    public void handleStopAction(){

    }

    public void handleClearField(){
        field = new Field(gameSetting, m, n);
        for(LifeModelListener listener: listeners){
            listener.updateImpact(field.doSnapshot());
        }
    }

    @Override
    public void subscribe(LifeModelListener listener){
        listeners.add(listener);
    }

}
