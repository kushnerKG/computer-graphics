package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * Created by Konstantin on 10.04.2016.
 */
public class Reader {

    private double n, m, k, a, b, c, d;
    private double zn, zf, sh, sw;
    private double[][] rotateMatrix = new double[4][4];
    private Color backgroundColor;
    private ArrayList<FigureFileInfo> figures = new ArrayList<>();

    private final File file;
    private Pattern commentPattern = Pattern.compile("\\s*//.*$"); // регулярное выражение поиска комментариев

    public Reader(File file){
        this.file = file;
    }

    public FileInfo read() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String str = commentPattern.matcher(reader.readLine()).replaceAll("");
            String[] strs = str.split(" +");
            n = Double.valueOf(strs[0]);
            m = Double.valueOf(strs[1]);
            k = Double.valueOf(strs[2]);
            a = Double.valueOf(strs[3]);
            b = Double.valueOf(strs[4]);
            c = Double.valueOf(strs[5]);
            d = Double.valueOf(strs[6]);


            str = commentPattern.matcher(reader.readLine()).replaceAll("");
            strs = str.split(" +");
            zn = Double.valueOf(strs[0]);
            zf = Double.valueOf(strs[1]);
            sw = Double.valueOf(strs[2]);
            sh = Double.valueOf(strs[3]);

            str = commentPattern.matcher(reader.readLine()).replaceAll("");
            strs = str.split(" +");
            rotateMatrix[0][0] = Double.valueOf(strs[0]);
            rotateMatrix[0][1] = Double.valueOf(strs[1]);
            rotateMatrix[0][2] = Double.valueOf(strs[2]);

            str = commentPattern.matcher(reader.readLine()).replaceAll("");
            strs = str.split(" +");
            rotateMatrix[1][0] = Double.valueOf(strs[0]);
            rotateMatrix[1][1] = Double.valueOf(strs[1]);
            rotateMatrix[1][2] = Double.valueOf(strs[2]);

            str = commentPattern.matcher(reader.readLine()).replaceAll("");
            strs = str.split(" +");
            rotateMatrix[2][0] = Double.valueOf(strs[0]);
            rotateMatrix[2][1] = Double.valueOf(strs[1]);
            rotateMatrix[2][2] = Double.valueOf(strs[2]);

            str = commentPattern.matcher(reader.readLine()).replaceAll("");
            strs = str.split(" +");
            int r = Integer.valueOf(strs[0]);
            int g = Integer.valueOf(strs[1]);
            int blue = Integer.valueOf(strs[2]);
            backgroundColor = new Color(r, g, blue);

            FileInfo info = new FileInfo(n, m, k, a, b, c, d, zn, zf, sw, sh, backgroundColor, rotateMatrix);

            str = commentPattern.matcher(reader.readLine()).replaceAll("");
            strs = str.split(" +");
            int number = Integer.valueOf(strs[0]);

            for(int i = 0; i < number; i++){
                figures.add(readFigureRecord(reader));
            }

            for(int i = 0; i < number; i++){
                try {
                    readParametres(reader, figures.get(i));
                    info.setIsNewFormat(true);
                }catch (NullPointerException e){
                    info.setIsNewFormat(false);
                    info.setFigures(figures);
                    figures.get(i).setN(n);
                    figures.get(i).setM(m);
                    figures.get(i).setK(k);
                    figures.get(i).setA(a);
                    figures.get(i).setB(b);
                    figures.get(i).setC(c);
                    figures.get(i).setD(d);
                    //return info;
                }

            }
            info.setFigures(figures);

            return info;
        }
    }

    private void readParametres(BufferedReader reader, FigureFileInfo figure) throws IOException {
        String str = commentPattern.matcher(reader.readLine()).replaceAll("");
        String[] strs = str.split(" +");

        double n = Double.valueOf(strs[0]);
        double m = Double.valueOf(strs[1]);
        double k = Double.valueOf(strs[2]);
        double a = Double.valueOf(strs[3]);
        double b = Double.valueOf(strs[4]);
        double c = Double.valueOf(strs[5]);
        double d = Double.valueOf(strs[6]);

        figure.setN(n);
        figure.setM(m);
        figure.setK(k);
        figure.setA(a);
        figure.setB(b);
        figure.setC(c);
        figure.setD(d);

    }

    private FigureFileInfo readFigureRecord(BufferedReader reader) throws IOException {
        String str = commentPattern.matcher(reader.readLine()).replaceAll("");
        String[] strs = str.split(" +");
        double[][] rotateMatrix = new double[3][3];
        ArrayList<Coordinates> points = new ArrayList<Coordinates>();

        int r = Integer.valueOf(strs[0]);
        int g = Integer.valueOf(strs[1]);
        int b = Integer.valueOf(strs[2]);

        str = commentPattern.matcher(reader.readLine()).replaceAll("");
        strs = str.split(" +");
        double cX = Double.valueOf(strs[0]);
        double cY = Double.valueOf(strs[1]);
        double cZ = Double.valueOf(strs[2]);

        str = commentPattern.matcher(reader.readLine()).replaceAll("");
        strs = str.split(" +");
        rotateMatrix[0][0] = Double.valueOf(strs[0]);
        rotateMatrix[0][1] = Double.valueOf(strs[1]);
        rotateMatrix[0][2] = Double.valueOf(strs[2]);

        str = commentPattern.matcher(reader.readLine()).replaceAll("");
        strs = str.split(" +");
        rotateMatrix[1][0] = Double.valueOf(strs[0]);
        rotateMatrix[1][1] = Double.valueOf(strs[1]);
        rotateMatrix[1][2] = Double.valueOf(strs[2]);

        str = commentPattern.matcher(reader.readLine()).replaceAll("");
        strs = str.split(" +");
        rotateMatrix[2][0] = Double.valueOf(strs[0]);
        rotateMatrix[2][1] = Double.valueOf(strs[1]);
        rotateMatrix[2][2] = Double.valueOf(strs[2]);

        str = commentPattern.matcher(reader.readLine()).replaceAll("");
        strs = str.split(" +");
        int n = Integer.valueOf(strs[0]);
        for(int i = 0; i < n; i++){
            str = commentPattern.matcher(reader.readLine()).replaceAll("");
            strs = str.split(" +");
            double x = Double.valueOf(strs[0]);
            double y = Double.valueOf(strs[1]);
            points.add(new Coordinates(x, y));
        }

        return new FigureFileInfo(cX, cY, cZ, rotateMatrix, points, new Color(r, g, b));
    }
}
