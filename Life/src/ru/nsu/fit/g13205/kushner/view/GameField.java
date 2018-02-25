package ru.nsu.fit.g13205.kushner.view;


/**
 * Created by Konstantin on 26.02.2016.
 */
public class GameField {

    private boolean[][] coloredCells;
    private int m;
    private int n;

    public GameField(int m, int n) {
        this.m = m;
        this.n = n;

        coloredCells = new boolean[n][m];
    }

    public void updateSize(int m, int n){
        setM(m);
        setN(n);
    }

    private void setM(int m) {
        boolean[][] x = coloredCells;

        int tmpM;
        if(this.m < m){
            tmpM = this.m;
        }else{
            tmpM = m;
        }
        this.m = m;
        coloredCells = new boolean[n][m];

        for(int i = 0; i < n; i++){
            for(int j = 0; j < tmpM; j++){
                coloredCells[i][j] = x[i][j];
            }
        }
    }

    private void setN(int n) {
        boolean[][] x = coloredCells;

        int tmpN;
        if(this.n < n){
            tmpN = this.n;
        }else{
            tmpN = n;
        }
        this.n = n;
        coloredCells = new boolean[n][m];

        for(int i = 0; i < tmpN; i++){
            for(int j = 0; j < m; j++){
                coloredCells[i][j] = x[i][j];
            }
        }
    }

    public void setColoredCell(int m, int n, boolean status){
        coloredCells[n][m] = status;
    }

    public void setColoredCell(int m, int n, int currentMode){
        if(currentMode == 1){
            if(!coloredCells[n][m]){
                coloredCells[n][m] = true;
            }
        }else{
            coloredCells[n][m] = !coloredCells[n][m];
        }
    }

    public void clearGameField(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                coloredCells[i][j] = false;
            }
        }
    }

    public boolean getColoredCell(int n, int m) {
        return coloredCells[n][m];
    }
}
