package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.view.setting.EditSetting;
import ru.nsu.fit.g13205.kushner.view.setting.GameSetting;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Konstantin on 27.02.2016.
 */
public class Field {

    private Impact[][] field;
    private GameSetting setting;
    private int m;
    private int n;

    public Field(GameSetting setting, int m, int n) {
        this.setting = setting;
        this.m = m;
        this.n = n;
        field = new Impact[n][m];

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                field[i][j] = new Impact(false, 0.0d);
            }
        }

    }

    public void doStep(){
        calculateLiveCell();
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                field[i][j].setValue(0.0d);
            }
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(i % 2 != 0 && j == m - 1) {
                    continue;
                }
                if (field[i][j].isLife()) {
                    handleChangeStatusCell(j, i, true);
                }

            }
        }
    }

    public void handleNewGameSetting(GameSetting setting){
        this.setting = setting;
        Impact[][] tmp = new Impact[n][m];

        for(int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tmp[i][j] = new Impact(field[i][j].isLife(), 0.0d);
                //tmp[i][j].setLife();
                field[i][j].setValue(0.0d);
                field[i][j].setLife(false);
            }
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(j == m - 1 && i % 2 != 0){
                    continue;
                }
                if(tmp[i][j].isLife()){
                    handleChangeStatusCell(j, i, true);
                }
            }
        }
    }

    public void handleChangeSize(int _m, int _n){
        Impact[][] tmp = new Impact[n][m];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                tmp[i][j] = new Impact(field[i][j].isLife(), field[i][j].getValue());
            }
        }
        field = new Impact[_n][_m];

        for(int i = 0; i < _n; i++){
            for(int j = 0; j < _m; j++){
                field[i][j] = new Impact(false, 0.0d);
            }
        }

        if(m > _m){
            m = _m;
        }
        if(n > _n){
            n = _n;
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(i % 2 != 0 && j == m - 1) {
                    continue;
                }
                if (tmp[i][j].isLife()) {
                    handleChangeStatusCell(j, i, true);
                }

            }
        }
        m = _m;
        n = _n;
        //calculateLiveCell();
    }

    public void handleChangeStatusCell(int m, int n, boolean status){
        field[n][m].setLife(status);
        setLiveCell(n, m, status);
    }

    public void handleLoadFile(boolean[][] livingCells){

        Impact[][] tmp = new Impact[n][m];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(i % 2 != 0 && j == m - 1){
                    continue;
                }
                tmp[i][j] = new Impact(field[i][j].isLife(), 0.0d);
            }
        }

        n = livingCells.length;
        m = livingCells[0].length;

        field = new Impact[n][m];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                field[i][j] = new Impact(false, 0);
            }
        }
        for(int i = 0; i < tmp.length; i++){
            for(int j = 0; j < tmp[i].length; j++){
                if(tmp[i][j].isLife()){
                    handleChangeStatusCell(j, i, true);
                }
            }
        }
    }

    private void firstInit(boolean[][] livingCells) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (livingCells[i][j]) {
                    field[i][j].setLife(true);
                    field[i][j].setValue(0);
                } else {
                    field[i][j].setLife(false);
                    field[i][j].setValue(0);
                }
            }
        }
        calculateImpact();
    }

    private void calculateImpact(){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(field[i][j].isLife()){
                    setLiveCell(i, j, true);
                }
            }
        }
    }

    private void calculateLiveCell(){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double tmp = field[i][j].getValue();
                if(!field[i][j].isLife() && setting.getBirthBegin() <= tmp
                        && tmp <= setting.getBirthEnd()){
                    field[i][j].setLife(true);
                }

                if(field[i][j].isLife() && tmp < setting.getLifeBegin()){
                    field[i][j].setLife(false);
                }

                if(field[i][j].isLife() && tmp > setting.getLifeEnd()){
                    field[i][j].setLife(false);
                }
            }
        }


    }

    private void setLiveCell(int i, int j, boolean status){

        double fstImpact = setting.getFSTImpact();
        double secondImpact = setting.getSNDImpact();
        int delta = 0;
        if(i % 2 == 0){
            delta = 1;
        }
        if(status){
            update(i - 1, j - delta, fstImpact);
            update(i - 1, j + 1 - delta, fstImpact);
            update(i, j - 1, fstImpact);
            update(i, j + 1, fstImpact);
            update(i + 1, j - delta, fstImpact);
            update(i + 1, j + 1 - delta, fstImpact);

            update(i - 2, j, secondImpact);
            update(i - 1, j - 1 - delta, secondImpact);
            update(i - 1, j + 2 - delta, secondImpact);
            update(i + 1, j - 1 - delta, secondImpact);
            update(i + 1, j + 2 - delta, secondImpact);
            update(i + 2, j, secondImpact);

        }else{
            update(i - 1, j - delta, (-1d) * fstImpact);
            update(i - 1, j + 1 - delta, (-1d) * fstImpact);
            update(i, j - 1, (-1d) * fstImpact);
            update(i, j + 1, (-1d) * fstImpact);
            update(i + 1, j - delta, (-1d) * fstImpact);
            update(i + 1, j + 1 - delta, (-1d) * fstImpact);

            update(i - 2, j, (-1) * secondImpact);
            update(i - 1, j - 1 - delta, (-1d) * secondImpact);
            update(i - 1, j + 2 - delta, (-1d) * secondImpact);
            update(i + 1, j - 1 - delta, (-1d) * secondImpact);
            update(i + 1, j + 2 - delta, (-1d) * secondImpact);
            update(i + 2, j, (-1) * secondImpact);
        }
    }

    private void update(int i, int j, double value){
        try{
            double tmp = field[i][j].getValue() + value;
            tmp = new BigDecimal(tmp).setScale(1, RoundingMode.HALF_UP).doubleValue();
            field[i][j].setValue(tmp);
        }catch (IndexOutOfBoundsException ignored){
            //ignored.printStackTrace();
        }
    }



    public Impact[][] doSnapshot(){
        Impact[][] tmp = new Impact[n][m];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                try {
                    tmp[i][j] = (Impact) field[i][j].clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        return tmp;
    }

    public Impact[][] getField() {
        return field;
    }
}
