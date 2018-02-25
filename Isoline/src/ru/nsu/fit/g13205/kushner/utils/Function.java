package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class Function {

    public final static double A = -2d;
    public final static double B = 2d;
    public final static double C = -2d;
    public final static double D = 2d;

    public static double getValue(double x, double y){
        new Color(220, 159, 128);
        //x3+y3+z3=24
        //return 2d*x*x - y*y;
        //
        //return Math.pow(x*x*x + y*y*y - 24, 1d/3d);
        return Math.exp(-x*x - y*y)*x*y;
    }

    public static double getValueLegendFunction(double y){
        return y;
    }

}
