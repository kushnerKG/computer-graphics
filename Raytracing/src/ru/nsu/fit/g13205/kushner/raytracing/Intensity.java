package ru.nsu.fit.g13205.kushner.raytracing;

/**
 * Created by Konstantin on 28.05.2016.
 */
public class Intensity {

    private float rIntensity = 0f;
    private float gIntensity = 0f;
    private float bIntensity = 0f;


    public Intensity(float rIntensity, float gIntensity, float bIntensity) {
        this.rIntensity = rIntensity;
        this.gIntensity = gIntensity;
        this.bIntensity = bIntensity;
    }

    public void addRedIntensity(float i){
        rIntensity += i;
    }

    public void addGreenIntensity(float i){
        gIntensity += i;
    }

    public void addBlueIntensity(float i){
        bIntensity += i;
    }

    public void addIntensity(Intensity intensity){
        rIntensity += intensity.getRedIntensity();
        gIntensity += intensity.getGreenIntensity();
        bIntensity += intensity.getBlueIntensity();
    }

    public float getRedIntensity() {
        return rIntensity;
    }

    public float getBlueIntensity() {
        return bIntensity;
    }

    public float getGreenIntensity() {
        return gIntensity;
    }

    public float getMaxIntensity(){
        float max = rIntensity;

        max = gIntensity > max ? gIntensity : max;
        max = bIntensity > max ? bIntensity : max;

        return max;
    }

    public void mul(float r, float b, float g){
        rIntensity *= r;
        bIntensity *= b;
        gIntensity *= g;
    }

    @Override
    public String toString() {
        return "Intensity{" +
                "rIntensity=" + rIntensity +
                ", gIntensity=" + gIntensity +
                ", bIntensity=" + bIntensity +
                '}';
    }
}
