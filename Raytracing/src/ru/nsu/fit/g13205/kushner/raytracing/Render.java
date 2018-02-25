package ru.nsu.fit.g13205.kushner.raytracing;

import ru.nsu.fit.g13205.kushner.figure.Figure;
import ru.nsu.fit.g13205.kushner.figure.OpticProperties;
import ru.nsu.fit.g13205.kushner.model.Model;
import ru.nsu.fit.g13205.kushner.model.ModelListener;
import ru.nsu.fit.g13205.kushner.utils.*;
import ru.nsu.fit.g13205.kushner.utils.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Konstantin on 28.05.2016.
 */
public class Render implements Runnable{

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
    private int prevPercent = 0;

    private int total = 0;

    public Render(Vector up, Vector view, Vector eye, ArrayList<Figure> figures, BufferedImage renderImage,
                  Properties properties, ArrayList<LightSource> lightSources, Color ambientColor, int depth, Color backgroundColor,
                  double gamma, ArrayList<ModelListener> listeners, int quality) {
        this.up = up;
        this.view = view;
        this.eye = eye;
        this.figures = figures;
        this.renderImage = renderImage;
        this.sW = properties.getSw();
        this.sH = properties.getSh();
        this.zN = properties.getZn();
        this.zF = properties.getZf();

        this.lightSources = lightSources;
        this.ambientColor = ambientColor;
        ambientRed = ambientColor.getRed() / 255f;
        ambientGreen = ambientColor.getGreen() / 255f;
        ambientBlue = ambientColor.getBlue() / 255f;
        this.depth = depth;
        this.backGroundColor = backgroundColor;
        this.gamma = gamma;
        this.listeners = listeners;
        this.quality = quality;

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

    public void render() {


        for(ModelListener listener: listeners){
            listener.setUpdatable(false);
        }

        String str = String.format("%d percent", 0);

        for(ModelListener listener: listeners){
            listener.setStatusBarText(str);
        }

        RenderInfo renderInfo = new RenderInfo(backGroundColor, gamma, depth, quality,
                new Coordinates3D(eye.getValue(0), eye.getValue(1), eye.getValue(2), 1d),
                new Coordinates3D(view.getValue(0), view.getValue(1), view.getValue(2), 1d),
                new Coordinates3D(up.getValue(0), up.getValue(1), up.getValue(2), 1d),
                zN, zF, sW, sH);

        if(quality == 0){
            this.intensities = new Intensity[renderImage.getHeight() % 2 == 0 ? renderImage.getHeight() / 2 : renderImage.getHeight() / 2 + 2]
                    [renderImage.getWidth() % 2 == 0 ? renderImage.getWidth() / 2 : renderImage.getWidth() / 2 + 2];
        }else if(quality == 1){
            this.intensities = new Intensity[renderImage.getHeight()][renderImage.getWidth()];
        }else if(quality == 2){
            this.intensities = new Intensity[renderImage.getHeight() * 4][renderImage.getWidth() * 4];
        }

  /*      CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {
            @Override
            public void run() {

                if(quality == 0){
                    roughtRenderImage();
                }else if(quality == 1){
                    normalRenderImage();
                }else if(quality == 2){
                    fineRenderImage();
                }

                for(ModelListener listener: listeners){
                    listener.setUpdatable(true);
                }

                for(ModelListener listener: listeners){
                    listener.windowPanelRepaint();
                }


            }
        });
*/
        int coreQuantity = Runtime.getRuntime().availableProcessors();


        ArrayList<RenderWorker> pull = new ArrayList<>();

        for(int i = 0; i < coreQuantity; i++){
            pull.add(new RenderWorker(renderInfo, lightSources, figures,
                    renderImage, ambientColor, listeners, intensities, null, i, coreQuantity, this));
        }

        for(int i = 0; i < coreQuantity; i++){
            pull.get(i).start();
        }

        for(int i = 0; i < coreQuantity; i++){
            try {
                pull.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(quality == 0){
            roughtRenderImage();
        }else if(quality == 1){
            normalRenderImage();
        }else if(quality == 2){
            try {
                fineRenderImage();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        for(ModelListener listener: listeners){
            listener.setUpdatable(true);
        }

        for(ModelListener listener: listeners){
            listener.windowPanelRepaint();
        }

    }

    public synchronized void processed(int count){
        total += count;


        double percent = (double) total / ((double)renderImage.getHeight() * (double) renderImage.getWidth()) * 100;


        if(percent - 1 >=  prevPercent){

            prevPercent = (int) percent;
            String str = String.format("%d percent", prevPercent);

            for(ModelListener listener: listeners){
                listener.setStatusBarText(str);
            }
        }

    }

    private void normalRenderImage() {
        float max = calculateMaxIntensity();

        float r = (backGroundColor.getRed() / 255f)/ max;
        float g = (backGroundColor.getGreen() / 255f)/ max;
        float b = (backGroundColor.getBlue() / 255f)/ max;

        r = (float) Math.pow(r, 1d/gamma);
        g = (float) Math.pow(g, 1d/gamma);
        b = (float) Math.pow(b, 1d/gamma);

        Color bc = new Color(r, g, b);

        int count = 0;
        for (int y = 0; y < renderImage.getHeight(); y++) {
            for (int x = 0; x < renderImage.getWidth(); x++) {
                float red = (float) Math.pow((intensities[y][x].getRedIntensity() / max), 1d/gamma);
                float green = (float) Math.pow((intensities[y][x].getGreenIntensity() / max), 1d/gamma);
                float blue = (float) Math.pow((intensities[y][x].getBlueIntensity() / max), 1d/gamma);
                renderImage.setRGB(x, y, new Color(red, green, blue).getRGB());
                count++;
            }
        }
    }


    private void roughtRenderImage() {
        float max = calculateMaxIntensity();

        float r = (backGroundColor.getRed() / 255f)/ max;
        float g = (backGroundColor.getGreen() / 255f)/ max;
        float b = (backGroundColor.getBlue() / 255f)/ max;

        r = (float) Math.pow(r, 1d/gamma);
        g = (float) Math.pow(g, 1d/gamma);
        b = (float) Math.pow(b, 1d/gamma);

        Color bc = new Color(r, g, b);

        int count = 0;
        for (int y = 0; y < renderImage.getHeight(); y++) {
            for (int x = 0; x < renderImage.getWidth(); x++) {
                float red = (float) Math.pow((intensities[y/2][x/2].getRedIntensity() / max), 1d/gamma);
                float green = (float) Math.pow((intensities[y/2][x/2].getGreenIntensity() / max), 1d/gamma);
                float blue = (float) Math.pow((intensities[y/2][x/2].getBlueIntensity() / max), 1d/gamma);
                renderImage.setRGB(x, y, new Color(red, green, blue).getRGB());
                count++;
            }
        }
    }


    private void fineRenderImage() {
        float max = calculateMaxIntensity();

        float rb = (backGroundColor.getRed() / 255f)/ max;
        float gb = (backGroundColor.getGreen() / 255f)/ max;
        float bb = (backGroundColor.getBlue() / 255f)/ max;

        rb = (float) Math.pow(rb, 1d/gamma);
        gb = (float) Math.pow(gb, 1d/gamma);
        bb = (float) Math.pow(bb, 1d/gamma);

        Color bc = new Color(rb, gb, bb);

        for (int y = 0; y < renderImage.getHeight(); y++) {
            for (int x = 0; x < renderImage.getWidth(); x++) {
                float r = 0f;
                float g = 0f;
                float b = 0f;

                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        r += intensities[y * 2 + i][x * 2 + j].getRedIntensity();
                        g += intensities[y * 2 + i][x * 2 + j].getGreenIntensity();
                        b += intensities[y * 2 + i][x * 2 + j].getBlueIntensity();
                    }
                }


                r /= 4f;
                g /= 4f;
                b /= 4f;

                float red = (float) Math.pow((r / max), 1d/gamma);
                float green = (float) Math.pow((g / max), 1d/gamma);
                float blue = (float) Math.pow((b / max), 1d/gamma);
                renderImage.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }

    }


    private float calculateMaxIntensity(){
        float max = intensities[0][0].getMaxIntensity();

        for(int i = 0; i < intensities.length; i++){

            for (int j = 0; j < intensities[i].length; j++) {
                if (max < intensities[i][j].getMaxIntensity()) {
                    max = intensities[i][j].getMaxIntensity();
                }
            }

        }

        return max;
    }

    @Override
    public void run() {
        render();
    }
}
