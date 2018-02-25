package ru.nsu.fit.g13205.kushner.raytracing;

import ru.nsu.fit.g13205.kushner.figure.Figure;
import ru.nsu.fit.g13205.kushner.figure.OpticProperties;
import ru.nsu.fit.g13205.kushner.model.ModelListener;
import ru.nsu.fit.g13205.kushner.utils.*;
import ru.nsu.fit.g13205.kushner.utils.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Konstantin on 01.06.2016.
 */
public class RenderWorker extends Thread {

    private Vector up;
    private Vector view;
    private Vector eye;
    private ArrayList<Figure> figures;
    private ArrayList<LightSource> lightSources;
    private BufferedImage renderImage;
    private double sW, sH;
    private double zN, zF;
    private Color ambientColor;
    private float ambientRed;
    private float ambientGreen;
    private float ambientBlue;
    private int depth;
    private Color backGroundColor;
    private double gamma;

    private Intensity[][] intensities;
    private ArrayList<ModelListener> listeners;
    private int quality;

    private Vector start;
    private Vector right;
    private CyclicBarrier barrier;
    private int number;
    private int coreQuantity;
    private Render render;

    public RenderWorker(RenderInfo renderInfo, ArrayList<LightSource> lightSources, ArrayList<Figure> figures,
                        BufferedImage renderImage, Color ambientColor, ArrayList<ModelListener> listeners,
                        Intensity[][] intensities, CyclicBarrier barrier, int number, int coreQuantity, Render render) {

        this.render = render;

        this.coreQuantity = coreQuantity;
        this.number = number;
        this.barrier = barrier;
        this.intensities = intensities;
        this.up = renderInfo.getUpVector();
        this.view = renderInfo.getViewVector();
        this.eye = renderInfo.getEyeVector();
        this.figures = figures;
        this.renderImage = renderImage;
        this.sW = renderInfo.getSw();
        this.sH = renderInfo.getSh();
        this.zN = renderInfo.getZn();
        this.zF = renderInfo.getZf();

        this.lightSources = lightSources;
        this.ambientColor = ambientColor;
        ambientRed = ambientColor.getRed() / 255f;
        ambientGreen = ambientColor.getGreen() / 255f;
        ambientBlue = ambientColor.getBlue() / 255f;
        this.depth = renderInfo.getDepth();
        this.backGroundColor = renderInfo.getBackgroundColor();
        this.gamma = renderInfo.getGamma();
        this.listeners = listeners;
        this.quality = renderInfo.getQuality();

        Vector upSq = new Vector(this.up.getValue(0), this.up.getValue(1), this.up.getValue(2));
        Vector view1 = new Vector(this.view.getValue(0), this.view.getValue(1), this.view.getValue(2));
        Vector eye1 = new Vector(this.eye.getValue(0), this.eye.getValue(1), this.eye.getValue(2));

        Vector direction = Vector.diffVector(view1, eye1);
        direction.normalizeVector();
        upSq.normalizeVector();

        right = Vector.vectorMultiplication(direction, upSq);
        right.normalizeVector();


        Vector center = new Vector(eye1.getValue(0) + direction.getValue(0) * zN, eye1.getValue(1) + direction.getValue(1) * zN,
                eye1.getValue(2) + direction.getValue(2) * zN);

        Vector shift = Vector.additionVector(up.getMultipleOnScalar(sH/2), right.getMultipleOnScalar(sW/2));
        start = Vector.additionVector(center, shift);
    }

    @Override
    public void run()	//Этот метод будет выполнен в побочном потоке
    {
        render();
    }

    public Vector convert(ru.nsu.fit.g13205.kushner.utils.Point point, Vector start, Vector up, Vector right, int width, int height, float sw, float sh){

        float deltaW = sw / (float) width;
        float deltaH = sh / (float) height;

        Vector r = right.getMultipleOnScalar(-deltaW * point.getX());
        Vector u = up.getMultipleOnScalar(-deltaH * point.getY());

        return Vector.additionVector(start, Vector.additionVector(r, u));
    }


    public void render() {

        if(quality == 0){
            roughtRender();
        }else if(quality == 1){
            normalRender();
        }else if(quality == 2){
            fineRender();
        }

    }


    private void roughtRender(){

        for (int y = number; y < intensities.length; y+=coreQuantity) {
            for (int x = 0; x < intensities[y].length; x++) {

                Ray ray = new Ray(eye, convert(new Point((x + 1) *2, (y + 1) *2), start, up.getNormalizeVector(), right,
                        renderImage.getWidth(), renderImage.getHeight(), (float)sW, (float)sH));

                intensities[y][x] = tracing(ray, depth - 1);

                if(intensities[y][x].getRedIntensity() == 0d && intensities[y][x].getBlueIntensity() == 0 &&
                        intensities[y][x].getGreenIntensity() == 0d){
                    float r = (backGroundColor.getRed() / 255f);
                    float g = (backGroundColor.getGreen() / 255f);
                    float b = (backGroundColor.getBlue() / 255f);

                    intensities[y][x].addIntensity(new Intensity(r,g,b));
                }
            }
            render.processed(renderImage.getWidth() * 2);
        }

    }


    private void normalRender(){
        int width = renderImage.getWidth();
        int height = renderImage.getHeight();



        try {
            for (int y = number; y < renderImage.getHeight(); y+=coreQuantity) {
                for (int x = 0; x < renderImage.getWidth(); x++) {

                    Ray ray = new Ray(eye, convert(new Point(x, y), start, up.getNormalizeVector(), right,
                            width, height, (float)sW, (float)sH));

                    intensities[y][x] = tracing(ray, depth - 1);

                    if(intensities[y][x].getRedIntensity() == 0d && intensities[y][x].getBlueIntensity() == 0 &&
                            intensities[y][x].getGreenIntensity() == 0d){
                        float r = (backGroundColor.getRed() / 255f);
                        float g = (backGroundColor.getGreen() / 255f);
                        float b = (backGroundColor.getBlue() / 255f);

                        intensities[y][x].addIntensity(new Intensity(r,g,b));
                    }
                }
                render.processed(renderImage.getWidth());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void fineRender(){

        for (int y = number; y < intensities.length; y+=coreQuantity) {
            for (int x = 0; x < intensities[y].length; x++) {

                Ray ray = new Ray(eye, convert(new Point(0.25f + 0.5f * x, 0.25f + 0.5f * y), start, up.getNormalizeVector(), right,
                        renderImage.getWidth(), renderImage.getHeight(), (float)sW, (float)sH));

                intensities[y][x] = tracing(ray, depth - 1);

                if(intensities[y][x].getRedIntensity() == 0d && intensities[y][x].getBlueIntensity() == 0 &&
                        intensities[y][x].getGreenIntensity() == 0d){

                    float r = (backGroundColor.getRed() / 255f);
                    float g = (backGroundColor.getGreen() / 255f);
                    float b = (backGroundColor.getBlue() / 255f);

                    intensities[y][x].addIntensity(new Intensity(r,g,b));
                }
            }
            if(y%4 == 0) {
                render.processed(renderImage.getWidth());
            }
        }


        /*try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }*/

    }




    private Intensity tracing(Ray ray, int depth){
        ArrayList<IntersectionInfo> intersections = new ArrayList<>();

        for (Figure figure : figures) {
            IntersectionInfo c;
            if ((c = figure.checkRay(ray)) != null) {
                intersections.add(c);
            }
        }

        if (intersections.size() > 0) {
            IntersectionInfo hit = intersections.get(0);

            for (IntersectionInfo intersectionInfo : intersections) {
                if (intersectionInfo.getLen() < hit.getLen()) {
                    hit = intersectionInfo;
                }
            }

            OpticProperties op = hit.getOpticProperties();

            Vector normal = hit.getNormal();
            Intensity totalIntensity = new Intensity(0f, 0f, 0f);
            Intensity ambientIntensity = new Intensity(op.getKDr() * ambientRed, op.getKDg() * ambientGreen, op.getKDb() * ambientBlue);
            totalIntensity.addIntensity(ambientIntensity);

            for(LightSource lightSource: lightSources){

                if(!checkLightSource(lightSource, hit.getIntersectionPoints())){
                    Ray directionOnLightSource = new Ray(hit.getIntersectionPoints(),lightSource.getVectorOfLightSource());

                    float redLS = lightSource.getColorOfLightSource().getRed()/255f;
                    float greenLS = lightSource.getColorOfLightSource().getGreen()/255f;
                    float blueLS = lightSource.getColorOfLightSource().getBlue()/255f;
                    float dRatio = (float)Vector.scalarMultiplication(normal, directionOnLightSource.getRayDirection());
                    float len = (float) Vector.norma(hit.getIntersectionPoints(), lightSource.getVectorOfLightSource());
                    float fatt = fatt(len);

                    directionOnLightSource = new Ray(hit.getIntersectionPoints(),lightSource.getVectorOfLightSource());
                    Ray directionOnEye = new Ray(hit.getIntersectionPoints(), ray.getRayStart());

                    Vector H = Vector.additionVector(directionOnLightSource.getRayDirection(), directionOnEye.getRayDirection());
                    H.normalizeVector();

                    float sRatio = (float)Vector.scalarMultiplication(normal, H);

                    if(sRatio < 0 || dRatio < 0) {
                        continue;
                    }

                    sRatio = (float) Math.pow(sRatio, op.getPower());

                    Intensity in = new Intensity(fatt * redLS * (dRatio * op.getKDr() + sRatio * op.getKSr()),
                            fatt * greenLS * (dRatio * op.getKDg() + sRatio * op.getKSg()),
                            fatt * blueLS * (dRatio * op.getKDb() + sRatio * op.getKSb()));

                    totalIntensity.addIntensity(in);
                }
            }

            if(depth > 0){

                Intensity tmp = tracing(createReflectedRay(normal, hit.getIntersectionPoints(),
                        new Ray(hit.getIntersectionPoints(), ray.getRayStart())), depth - 1);

                tmp.mul(op.getKSr(), op.getKSg(), op.getKSb());

                totalIntensity.addIntensity(tmp);
            }

            return totalIntensity;
        }
        return new Intensity(0f, 0f, 0f);
    }

    private Ray createReflectedRay(Vector normal, Vector hit, Ray lRay){

        normal = new Vector(normal.getValue(0), normal.getValue(1), normal.getValue(2));
        Ray reflectedRay;
        double tmp = 2 * Vector.scalarMultiplication(normal, lRay.getRayDirection());
        normal.multipleOnScalar(tmp);
        Vector reflected = Vector.diffVector(normal, lRay.getRayDirection());
        reflectedRay = new Ray(new Vector(hit.getValue(0), hit.getValue(1), hit.getValue(2)), reflected, 1);

        return reflectedRay;
    }

    private boolean checkLightSource(LightSource lightSource, Vector hit){

        Ray ray = new Ray(lightSource.getVectorOfLightSource(), hit);
        IntersectionInfo c;

        for (Figure figure : figures) {
            if ((c = figure.checkRay(ray)) != null) {
                Vector inter = c.getIntersectionPoints();

                double len1 = Vector.norma(lightSource.getVectorOfLightSource(), inter);
                double len2 = Vector.norma(lightSource.getVectorOfLightSource(), hit);
                if(len1 < len2){
                    if(Math.abs(inter.getValue(0) - hit.getValue(0)) < 0.000001 && Math.abs(inter.getValue(1) - hit.getValue(1)) < 0.000001 &&
                            Math.abs(inter.getValue(2) - hit.getValue(2)) < 0.000001){

                    }else {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private float fatt(float len){
        return 1f/(1+len);
    }


    private float calculateMaxIntensity(){
        float max = intensities[0][0].getMaxIntensity();

        for(int i = 0; i < intensities.length; i++){
            for(int j = 0; j < intensities[i].length; j++){
                if(max < intensities[i][j].getMaxIntensity()) {
                    max = intensities[i][j].getMaxIntensity();
                }
            }
        }

        return max;
    }


}
