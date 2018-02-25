package ru.nsu.fit.g13205.kushner;

import ru.nsu.fit.g13205.kushner.controller.Controller;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class MainClass {

    public static void main(String[] args) {
        new Controller(Settings.WIDTH_WINDOW, Settings.HEIGHT_WINDOW,
                Settings.MIN_WIDTH_WINDOW, Settings.MIN_HEIGHT_WINDOW);
    }

}
