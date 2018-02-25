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

    public static IFunction getFunction(){
        return new IFunction() {
            @Override
            public double getValue(double x, double y) {
                return 2d*x*x - y*y;
            }
        };
    }

    public static IFunction getLegendFunction(){
        return new IFunction(){
            @Override
            public double getValue(double x, double y) {
                return y;
            }
        };
    }

}
