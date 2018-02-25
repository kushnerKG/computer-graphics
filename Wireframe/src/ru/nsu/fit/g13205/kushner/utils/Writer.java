package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Konstantin on 10.04.2016.
 */
public class Writer {
    private final File file;


    public Writer(File file){
        this.file = file;
    }

    public void write(FileInfo info) throws IOException {
        try(FileWriter fileWriter = new FileWriter(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(info.getN()).append(" ").append(info.getM()).append(" ").append(info.getK()).append(" ").append(info.getA()).
                    append(" ").append(info.getB()).append(" ").append(info.getC()).append(" ").append(info.getD()).append("\n");
            fileWriter.write(stringBuilder.toString());

            stringBuilder.setLength(0);
            stringBuilder.append(info.getZn()).append(" ").append(info.getZf()).append(" ").append(info.getSw()).append(" ").
                    append(info.getSh()).append("\n");
            fileWriter.write(stringBuilder.toString());

            stringBuilder.setLength(0);
            double[][] matrix = info.getSceneRotateMatrix();
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    stringBuilder.append(matrix[i][j]).append(" ");
                }
                stringBuilder.append("\n");
                fileWriter.write(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
            Color color = info.getBackgroundColor();
            stringBuilder.append(color.getRed()).append(" ").append(color.getGreen()).append(" ").append(color.getBlue()).append("\n");
            fileWriter.write(stringBuilder.toString());
            stringBuilder.setLength(0);
            stringBuilder.append(info.getFigures().size()).append("\n");
            fileWriter.write(stringBuilder.toString());
            stringBuilder.setLength(0);

            ArrayList<FigureFileInfo> figures = info.getFigures();

            for(FigureFileInfo f: figures){
                doFigureRecord(fileWriter, f);
            }

            for(FigureFileInfo f: figures){
                drawParameters(fileWriter, f);
            }
        }
    }

    private void drawParameters(FileWriter fileWriter, FigureFileInfo info) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(info.getN()).append(" ").append(info.getM()).append(" ").append(info.getK()).append(" ").append(info.getA()).
                append(" ").append(info.getB()).append(" ").append(info.getC()).append(" ").append(info.getD()).append("\n");
        fileWriter.write(stringBuilder.toString());
    }

    private void doFigureRecord(FileWriter fileWriter, FigureFileInfo info) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        Color color = info.getColor();
        stringBuilder.append(color.getRed()).append(" ").append(color.getGreen()).append(" ").append(color.getBlue()).append("\n");
        fileWriter.write(stringBuilder.toString());
        stringBuilder.setLength(0);

        stringBuilder.append(info.getcX()).append(" ").append(info.getcY()).append(" ").append(info.getcZ()).append("\n");
        fileWriter.write(stringBuilder.toString());
        stringBuilder.setLength(0);

        double[][] matrix = info.getRotateMatrix();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                stringBuilder.append(matrix[i][j]).append(" ");
            }
            stringBuilder.append("\n");
            fileWriter.write(stringBuilder.toString());
            stringBuilder.setLength(0);
        }

        ArrayList<Coordinates> arrayList = info.getSplinePoints();

        stringBuilder.append(arrayList.size()).append("\n");
        fileWriter.write(stringBuilder.toString());
        stringBuilder.setLength(0);

        for(Coordinates coordinates: arrayList){
            stringBuilder.append(coordinates.getX()).append(" ").append(coordinates.getY()).append("\n");
            fileWriter.write(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
    }

}
