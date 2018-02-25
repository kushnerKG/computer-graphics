package ru.nsu.fit.g13205.kushner.utils;

import ru.nsu.fit.g13205.kushner.figure.*;

import java.awt.*;
import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class SceneFileReader {

    private static final String SPHERE_IDENTIFIER = "SPHERE";
    private static final String BOX_IDENTIFIER = "BOX";
    private static final String TRIANGLE_IDENTIFIER = "TRIANGLE";
    private static final String QUADRANGLE_IDENTIFIER = "QUADRANGLE";

    private Pattern commentPattern = Pattern.compile("\\s*//.*$"); // регулярное выражение поиска комментариев
    private Pattern spacePattern = Pattern.compile(" +");

    public SceneFileInfo read(File file) throws IOException {

        SceneFileInfo sceneFileInfo = new SceneFileInfo();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String str = readNextLine(reader);
            String[] separateLine = str.split(" +");

            int Ar = Integer.valueOf(separateLine[0]), Ag = Integer.valueOf(separateLine[1]),
                    Ab = Integer.valueOf(separateLine[2]);

            sceneFileInfo.setAmbientLightColor(new Color(Ar, Ag, Ab));

            str = readNextLine(reader);

            int numberLight = Integer.valueOf(str);

            for(int i = 0; i < numberLight; i++){
                sceneFileInfo.addLightSource(readLightSource(reader));
            }

            while((str = reader.readLine()) != null){
                str = commentPattern.matcher(str).replaceAll("");
                str = spacePattern.matcher(str).replaceAll("");
                if(str.length() ==0){
                    continue;
                }
                switch (str){
                    case SPHERE_IDENTIFIER:
                        sceneFileInfo.addSphere(readSphere(reader));
                        break;
                    case BOX_IDENTIFIER:
                        sceneFileInfo.addBox(readBox(reader));
                        break;
                    case TRIANGLE_IDENTIFIER:
                        sceneFileInfo.addTriangle(readTriangle(reader));
                        break;
                    case QUADRANGLE_IDENTIFIER:
                        sceneFileInfo.addQuadrangle(readQuadrangle(reader));
                        break;
                }
            }

        }

        return sceneFileInfo;
    }

    private Sphere readSphere(BufferedReader reader) throws IOException {
        String str = readNextLine(reader);

        String[] separateLine = str.split(" +");
        double CX = Double.valueOf(separateLine[0]), CY= Double.valueOf(separateLine[1]), CZ = Double.valueOf(separateLine[2]);

        str = readNextLine(reader);
        double radius = Double.valueOf(str);

        OpticProperties opticProperties = readOpticProperties(reader);

        return new Sphere(new Coordinates3D(CX, CY, CZ, 1), radius, opticProperties);
    }

    private Box readBox(BufferedReader reader) throws IOException {
        String str = readNextLine(reader);

        String[] separateLine = str.split(" +");

        double x, y, z;

        x = Double.valueOf(separateLine[0]);
        y = Double.valueOf(separateLine[1]);
        z = Double.valueOf(separateLine[2]);
        Coordinates3D minPoint = new Coordinates3D(x, y, z, 1);

        str = readNextLine(reader);
        separateLine = str.split(" +");
        x = Double.valueOf(separateLine[0]);
        y = Double.valueOf(separateLine[1]);
        z = Double.valueOf(separateLine[2]);
        Coordinates3D maxPoint = new Coordinates3D(x, y, z, 1);

        OpticProperties opticProperties = readOpticProperties(reader);

        return new Box(minPoint, maxPoint, opticProperties);
    }

    private Triangle readTriangle(BufferedReader reader) throws IOException {
        Coordinates3D vertex1, vertex2, vertex3;
        double x, y, z;

        vertex1 = readCoordinates(reader);
        vertex2 = readCoordinates(reader);
        vertex3 = readCoordinates(reader);

        OpticProperties opticProperties = readOpticProperties(reader);

        return new Triangle(vertex1, vertex2, vertex3, opticProperties);
    }

    private Quadrangle readQuadrangle(BufferedReader reader) throws IOException {
        Coordinates3D vertex1, vertex2, vertex3, vertex4;

        vertex1 = readCoordinates(reader);
        vertex2 = readCoordinates(reader);
        vertex3 = readCoordinates(reader);
        vertex4 = readCoordinates(reader);

        OpticProperties opticProperties = readOpticProperties(reader);

        return new Quadrangle(vertex1, vertex2, vertex3, vertex4, opticProperties);
    }

    private Coordinates3D readCoordinates(BufferedReader reader) throws IOException {
        double x, y, z;

        String str = readNextLine(reader);
        String[] separateLine = str.split(" +");
        x = Double.valueOf(separateLine[0]);
        y = Double.valueOf(separateLine[1]);
        z = Double.valueOf(separateLine[2]);
        return new Coordinates3D(x, y, z, 1);
    }

    private OpticProperties readOpticProperties(BufferedReader reader) throws IOException {
        String str = readNextLine(reader);

        String[] separateLine = str.split(" +");

        float KDr, KDg, KDb, KSr, KSg, KSb, power;
        KDr = Float.valueOf(separateLine[0]);
        KDg = Float.valueOf(separateLine[1]);
        KDb = Float.valueOf(separateLine[2]);

        KSr = Float.valueOf(separateLine[3]);
        KSg = Float.valueOf(separateLine[4]);
        KSb = Float.valueOf(separateLine[5]);

        power = Float.valueOf(separateLine[6]);

        return new OpticProperties(KDr, KDg, KDb, KSr, KSg, KSb, power);
    }

    private LightSource readLightSource(BufferedReader reader) throws IOException {


        String str = commentPattern.matcher(reader.readLine()).replaceAll("");
        while(str.length()==0){
            str = commentPattern.matcher(reader.readLine()).replaceAll("");
        }

        String[] separateLine = str.split(" +");

        double LX = Double.valueOf(separateLine[0]);
        double LY = Double.valueOf(separateLine[1]);
        double LZ = Double.valueOf(separateLine[2]);

        int LR = Integer.valueOf(separateLine[3]);
        int LG = Integer.valueOf(separateLine[4]);
        int LB = Integer.valueOf(separateLine[5]);

        return new LightSource(new Vector(LX, LY, LZ), new Color(LR, LG, LB));
    }

    private String readNextLine(BufferedReader reader) throws IOException {
        String str = commentPattern.matcher(reader.readLine()).replaceAll("");

        while(str.length()==0){
            str = commentPattern.matcher(reader.readLine()).replaceAll("");
        }

        return str;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("./FIT_Group_Surname_Raytracing_Data/test.scene");
        SceneFileReader sceneFileReader = new SceneFileReader();
        SceneFileInfo fileInfo = sceneFileReader.read(file);
        System.out.println(fileInfo);
    }
}
