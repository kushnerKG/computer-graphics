package ru.nsu.fit.g13205.kushner.utils;

import ru.nsu.fit.g13205.kushner.figure.Box;
import ru.nsu.fit.g13205.kushner.figure.Quadrangle;
import ru.nsu.fit.g13205.kushner.figure.Sphere;
import ru.nsu.fit.g13205.kushner.figure.Triangle;

import java.awt.*;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class SceneFileInfo {

    private Color ambientLightColor;
    private final ArrayList<LightSource> lightSources = new ArrayList<>();
    private final ArrayList<Box> boxes = new ArrayList<>();
    private final ArrayList<Sphere> spheres = new ArrayList<>();
    private final ArrayList<Quadrangle> quadrangles = new ArrayList<>();
    private final ArrayList<Triangle> triangles = new ArrayList<>();

    public void setAmbientLightColor(Color color){
        ambientLightColor = color;
    }

    public void addLightSource(LightSource lightSource){
        lightSources.add(lightSource);
    }

    public void addBox(Box box){
        boxes.add(box);
    }

    public void addSphere(Sphere sphere){
        spheres.add(sphere);
    }

    public void addQuadrangle(Quadrangle quadrangle){
        quadrangles.add(quadrangle);
    }

    public void addTriangle(Triangle triangle){
        triangles.add(triangle);
    }

    public int getLightSourceNumber(){
        return lightSources.size();
    }

    public Color getAmbientLightColor() {
        return ambientLightColor;
    }

    public ArrayList<LightSource> getLightSources() {
        return lightSources;
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }

    public ArrayList<Quadrangle> getQuadrangles() {
        return quadrangles;
    }

    public ArrayList<Sphere> getSpheres() {
        return spheres;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("AMBIENT ").append(ambientLightColor).append("\n");

        stringBuilder.append("LIGHT SOURCE::::").append("\n");
        for(LightSource source: lightSources){
            stringBuilder.append(source).append("\n");
        }
        stringBuilder.append("\n\n");

        stringBuilder.append("BOXES:::::").append("\n");
        for(Box box: boxes){
            stringBuilder.append(box).append("\n");
        }
        stringBuilder.append("\n\n");

        stringBuilder.append("SPHERE:::::").append("\n");
        for(Sphere sphere: spheres){
            stringBuilder.append(sphere).append("\n");
        }
        stringBuilder.append("\n\n");

        stringBuilder.append("TRIANGLE:::::").append("\n");
        for(Triangle triangle: triangles){
            stringBuilder.append(triangle).append("\n");
        }
        stringBuilder.append("\n\n");

        stringBuilder.append("Quadrangle::::").append("\n");
        for(Quadrangle quadrangle: quadrangles){
            stringBuilder.append(quadrangle).append("\n");
        }

        return stringBuilder.toString();
    }
}
