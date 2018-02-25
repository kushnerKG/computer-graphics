package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.utils.DialogProperties;
import ru.nsu.fit.g13205.kushner.utils.Properties;

import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 14.05.2016.
 */
public interface
ModelListener {

    void updateImage(BufferedImage image);

    void windowPanelRepaint();

    void updateProperties(DialogProperties properties);

    void setUpdatable(boolean isUpdatable);

    void setStatusBarText(String text);
}
