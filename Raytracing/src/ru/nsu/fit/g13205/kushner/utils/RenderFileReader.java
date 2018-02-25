package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class RenderFileReader {

    //rough – грубое(0), normal – среднее(1), fine – высокое(2)
    private static final String ROUGH_IDENTIFIER = "ROUGHT";
    private static final String NORMAL_IDENTIFIER = "NORMAL";
    private static final String FINE_IDENTIFIER = "FINE";

    private Pattern commentPattern = Pattern.compile("\\s*//.*$"); // регулярное выражение поиска комментариев

    public RenderInfo read(File file) throws IOException {

        Color backgroundColor;
        double gamma;
        int depth;
        int quality = 0; //rough – грубое(0), normal – среднее(1), fine – высокое(2)
        Coordinates3D eyePoint;
        Coordinates3D viewPoint;
        Coordinates3D upVector;
        double zn, zf, sw, sh;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String str = readNextLine(reader);
            String[] separateLine = str.split(" +");
            backgroundColor = new Color(Integer.valueOf(separateLine[0]), Integer.valueOf(separateLine[1]),
                    Integer.valueOf(separateLine[2]));

            str = readNextLine(reader);
            gamma = Double.valueOf(str);

            str = readNextLine(reader);
            depth = Integer.valueOf(str);

            str = readNextLine(reader);
            switch (str){//по хороошему нужно проверять данные на валидность
                case ROUGH_IDENTIFIER:
                    quality = RenderInfo.QUALITY_ROUGH;
                    break;
                case NORMAL_IDENTIFIER:
                    quality = RenderInfo.QUALITY_NORMAL;
                    break;
                case FINE_IDENTIFIER:
                    quality = RenderInfo.QUALITY_FINE;
                    break;
            }

            eyePoint = readCoordinates(reader);
            viewPoint = readCoordinates(reader);
            upVector = readCoordinates(reader);

            str = readNextLine(reader);
            separateLine = str.split(" +");
            zn = Double.valueOf(separateLine[0]);
            zf = Double.valueOf(separateLine[1]);


            str = readNextLine(reader);
            separateLine = str.split(" +");
            sw = Double.valueOf(separateLine[0]);
            sh = Double.valueOf(separateLine[1]);

        }
        return new RenderInfo(backgroundColor, gamma, depth, quality, eyePoint, viewPoint, upVector, zn, zf, sw, sh);
    }

    private Coordinates3D readCoordinates(BufferedReader reader) throws IOException {
        String str = readNextLine(reader);
        String[] separateLine = str.split(" +");
        return new Coordinates3D(Double.valueOf(separateLine[0]), Double.valueOf(separateLine[1]),
                Double.valueOf(separateLine[2]), 1);
    }

    private String readNextLine(BufferedReader reader) throws IOException {
        String str = commentPattern.matcher(reader.readLine()).replaceAll("");

        while(str.length()==0){
            str = commentPattern.matcher(reader.readLine()).replaceAll("");
        }

        return str;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("./FIT_Group_Surname_Raytracing_Data/test.render");
        RenderFileReader renderFileReader = new RenderFileReader();
        System.out.println(renderFileReader.read(file));
    }
}
