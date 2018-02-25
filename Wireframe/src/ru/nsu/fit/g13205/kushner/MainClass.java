package ru.nsu.fit.g13205.kushner;

import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.model.MatrixFactory;

/**
 * Created by Konstantin on 09.04.2016.
 */
public class MainClass {

    public static void main(String[] args) {

        new Controller(Settings.WIDTH_WINDOW, Settings.HEIGHT_WINDOW,
                Settings.MIN_WIDTH_WINDOW, Settings.MIN_HEIGHT_WINDOW);

        double[][] m = MatrixFactory.getInvertMatrixOfCamera(Settings.pCam, Settings.pView, Settings.vUp);
    }

}
