package ru.nsu.fit.g13205.kushner.model;

/**
 * Created by Konstantin on 27.02.2016.
 */
public class Impact implements Cloneable{
    private double value;
    private boolean isLife;
    private boolean prevLiveStatus;

    public Impact(boolean isLife, double value) {
        this.isLife = isLife;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public boolean isLife() {
        return isLife;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setLife(boolean life) {
        prevLiveStatus = isLife;
        isLife = life;
    }

    public boolean getPrevLiveStatus() {
        return prevLiveStatus;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Impact obj = (Impact) super.clone();
        obj.setLife(isLife);
        obj.setValue(value);
        return obj;
    }

    @Override
    public String toString() {
        return "Impact{" +
                "isLife=" + isLife +
                ", value=" + value +
                '}';
    }
}
