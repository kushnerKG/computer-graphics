package ru.nsu.fit.g13205.kushner.figure;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class OpticProperties {
    //KDr KDg KDb KSr KSg KSb Power

    private final float KDr;
    private final float KDg;
    private final float KDb;
    private final float KSr;
    private final float KSg;
    private final float KSb;
    private final float power;

    public OpticProperties(float KDr, float KDg, float KDb, float KSr, float KSg, float KSb, float power) {
        this.KDr = KDr;
        this.KDg = KDg;
        this.KDb = KDb;
        this.KSr = KSr;
        this.KSg = KSg;
        this.KSb = KSb;
        this.power = power;
    }

    public float getKDr() {
        return KDr;
    }

    public float getPower() {
        return power;
    }

    public float getKSb() {
        return KSb;
    }

    public float getKSg() {
        return KSg;
    }

    public float getKSr() {
        return KSr;
    }

    public float getKDb() {
        return KDb;
    }

    public float getKDg() {
        return KDg;
    }

    @Override
    public String toString() {
        return "OpticProperties{" +
                "KDr=" + KDr +
                ", KDg=" + KDg +
                ", KDb=" + KDb +
                ", KSr=" + KSr +
                ", KSg=" + KSg +
                ", KSb=" + KSb +
                ", power=" + power +
                '}';
    }
}
