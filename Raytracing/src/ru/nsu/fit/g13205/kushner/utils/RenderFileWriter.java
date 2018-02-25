package ru.nsu.fit.g13205.kushner.utils;

import java.awt.*;
import java.io.*;

/**
 * Created by Konstantin on 31.05.2016.
 */
public class RenderFileWriter {

    private static final String ROUGH_IDENTIFIER = "ROUGHT";
    private static final String NORMAL_IDENTIFIER = "NORMAL";
    private static final String FINE_IDENTIFIER = "FINE";

    public void write(File file, RenderInfo renderFileInfo) throws IOException {

        Color backgroundColor = renderFileInfo.getBackgroundColor();
        double gamma = renderFileInfo.getGamma();
        int depth = renderFileInfo.getDepth();
        int quality = renderFileInfo.getQuality(); //rough – грубое(0), normal – среднее(1), fine – высокое(2)

        Vector eyePoint = renderFileInfo.getEyeVector();
        Vector viewPoint = renderFileInfo.getViewVector();
        Vector upVector = renderFileInfo.getUpVector();


        double zn = renderFileInfo.getZn();
        double zf = renderFileInfo.getZf();
        double sw = renderFileInfo.getSw();
        double sh = renderFileInfo.getSh();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(backgroundColor.getRed() + " " + backgroundColor.getGreen() + " " + backgroundColor.getBlue() + "\n");
            writer.write(String.valueOf(gamma) + "\n");
            writer.write(String.valueOf(depth) + "\n");

            if(quality == 0) {
                writer.write(String.valueOf(ROUGH_IDENTIFIER) + "\n\n");
            }else if (quality == 1){
                writer.write(String.valueOf(NORMAL_IDENTIFIER) + "\n\n");
            }else{
                writer.write(String.valueOf(FINE_IDENTIFIER) + "\n\n");
            }

            writer.write(eyePoint.getValue(0) + " " + eyePoint.getValue(1) + " " + eyePoint.getValue(2) + "\n");
            writer.write(viewPoint.getValue(0) + " " + viewPoint.getValue(1) + " " + viewPoint.getValue(2) + "\n");
            writer.write(upVector.getValue(0) + " " + upVector.getValue(1) + " " + upVector.getValue(2) + "\n");
            writer.write(zn + " " + zf + "\n");
            writer.write(sw + " " +sh + "\n");
        }
    }

}
