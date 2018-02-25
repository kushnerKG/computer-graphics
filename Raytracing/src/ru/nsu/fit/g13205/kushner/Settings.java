package ru.nsu.fit.g13205.kushner;

import java.awt.*;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class Settings {
    public static final String ABOUT_MESSAGE = "© Kushner Konstantin, NSU FIT 13205, 2016";
    public static String PROJECT_NAME = "Raytracing";

    public static String SCENE_EXTENSION = "scene";
    public static String RENDER_EXTENSION = "render";
    public static String IMAGE_EXTENSION = "bmp";

    public static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    public static final double DEFAULT_GAMMA = 1d;
    public static final int DEFAULT_DEPTH = 1;
    public static final int DEFAULT_QUALITY = 1;

    public static final int SPHERE_M = 30;
    public static final int TRIANGLE_M = 5;
    public static final int QUADRANGLE_M = 5;
    public static final int BOX_M = 5;
    public static final int OUTSIDE_BOX_M = 10;

    public static final double[] DEFAULT_UP_VECTOR = {0d, 0d, 1d};
    public static final double DEFAULT_EYE_ANGLE = Math.PI/6;
    public static final double[] DEFAULT_VIEW_POINT = {0, 0,0};
    public static final double[] DEFAULT_EYE_POINT = {-20, 0, 0};

    public static final double ZN = 15;
    public static final double ZF = 30;
    public static final double SW = 20;
    public static final double SH = 20;

    public static final int IMAGE_HEIGHT = 400;

    public static final Color X_COLOR = new Color(255, 0, 0);
    public static final Color Y_COLOR = new Color(0, 255, 0);
    public static final Color Z_COLOR = new Color(0, 0, 255);

    public static final Color BOX_COLOR = new Color(255, 255, 255);

    public final static int WIDTH_WINDOW = 1200;
    public final static int HEIGHT_WINDOW = 500;
    public final static int MIN_WIDTH_WINDOW = 600;
    public final static int MIN_HEIGHT_WINDOW = 500;
}
