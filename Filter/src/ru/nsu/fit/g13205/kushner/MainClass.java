package ru.nsu.fit.g13205.kushner;


import ru.nsu.fit.g13205.kushner.view.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Konstantin on 08.03.2016.
 */
public class MainClass {

    public static void main(String[] args) {
        JFrame mainWindow = new MainWindow(1200, 600, "Filter");
        mainWindow.setMinimumSize(new Dimension(800, 600));
        mainWindow.setVisible(true);
    }

}
