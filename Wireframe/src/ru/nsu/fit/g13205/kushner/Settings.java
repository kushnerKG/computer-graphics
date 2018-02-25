package ru.nsu.fit.g13205.kushner;

import javafx.scene.layout.Background;
import ru.nsu.fit.g13205.kushner.utils.AreaProperties;
import ru.nsu.fit.g13205.kushner.utils.Coordinates;
import ru.nsu.fit.g13205.kushner.utils.Properties;

import java.awt.*;

/**
 * Created by Konstantin on 10.04.2016.
 */
public class Settings {

    public static final String ABOUT_MESSAGE = "© Kushner Konstantin, NSU FIT 13205, 2016";
    public final static String EXTENSION = "txt";

    public final static Color BACKGROUND_COLOR = new Color(0,0,0);

    public static String PROJECT_NAME = "Surface";

    public static double[] pCam = {0, 0, -10};
    public static double[] pView = {0, 0, 10};
    public static double[] vUp = {0, 10, 0};

    public static final Color X_COLOR = new Color(255, 0, 0);
    public static final Color Y_COLOR = new Color(0, 255, 0);
    public static final Color Z_COLOR = new Color(0, 0, 255);

    public static final Color BOX_COLOR = new Color(255, 255, 255);

    public static final int TICKS_SIZE = 2;
    public static final int BASIC_POINT_SIZE = 8;
    public static final int MIDDLE_POINT_SIZE = 4;

    public static final int DIALOG_IMAGE_WIDTH = 550;
    public static final int DIALOG_IMAGE_HEIGHT = 350;

    public final static int WIDTH_WINDOW = 1200;
    public final static int HEIGHT_WINDOW = 650;
    public final static int MIN_WIDTH_WINDOW = 600;
    public final static int MIN_HEIGHT_WINDOW = 400;


    public final static Coordinates P1 = new Coordinates(-12,1);
    public final static Coordinates P2 = new Coordinates(-1,2);
    public final static Coordinates P3 = new Coordinates(4,3);
    public final static Coordinates P4 = new Coordinates(5.5,1);
    public final static Coordinates P5 = new Coordinates(7.7,2);
    public final static Coordinates P6 = new Coordinates(9,4);
    public final static Coordinates P7 = new Coordinates(10,-7);
    public final static Coordinates P8 = new Coordinates(12,2);

    public final static double DEFAULT_ZN = 8;
    public final static double DEFAULT_ZF = 23;
    public final static double DEFAULT_SW = 5;
    public final static double DEFAULT_SH = 5;
    public final static double MIN_SIZE_IMAGE = 550;

    public final static double ROTATION_X_AXIS = 0;
    public final static double ROTATION_Y_AXIS = 0;
    public final static double ROTATION_Z_AXIS = 0;

    public static Properties getDefaultProperties(){
        return new Properties(10, 10, 5, 0d, 1d, 0d, 6.28, new Color(255, 255, 0), 0, 0, 0);
    }

    public static AreaProperties getDefaultAreaProperties(){
        return new AreaProperties(DEFAULT_ZN, DEFAULT_ZF, DEFAULT_SW, DEFAULT_SH);
    }

}
