package ru.nsu.fit.g13205.kushner.algorithms;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Konstantin on 14.03.2016.
 */
public class BMPWriter {
    private static final int MAGIC_NUMBER = 0x424D;// or 0x4D42
    private File file;

    public BMPWriter(File file) {
        this.file = file;
    }

    public void write(BufferedImage image) throws IOException {
        try(DataOutputStream inputStream = new DataOutputStream(new FileOutputStream(file))) {
            byte[] fileHeader = createFileHeader(image);
            inputStream.write(fileHeader);
            byte[] mapHeader = createMapHeader(image);
            inputStream.write(mapHeader);
            byte[] pixelArea = createPixelArea(image);
            inputStream.write(pixelArea);
        }

    }

    private void bufferCopy(byte[] src, byte[] dest, int start, int number){
        for(int i = 0; i < number; i++){
            dest[i + start] = src[i];
        }
    }

    private byte[] createPixelArea(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        int lineLength = width * 3;
        if(lineLength % 4 != 0){
            lineLength += (4 - lineLength % 4);
        }
        int pixelSize = lineLength * height;
        byte[] pixelBuffer = new byte[pixelSize];


        for (int i = height; i >= 1; i--) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i - 1);
                byte red = (byte) ((rgb>>16)&0xFF);
                byte green = (byte) ((rgb>>8)&0xFF);
                byte blue = (byte) ((rgb)&0xFF);
                pixelBuffer[(height - i) * lineLength + 3 * j] = blue;
                pixelBuffer[(height - i) * lineLength + 3 * j + 1] = green;
                pixelBuffer[(height - i) * lineLength + 3 * j + 2] = red;
            }
        }
        return pixelBuffer;
    }

    private byte[] createMapHeader(BufferedImage image){
        byte[] mapHeader = new byte[40];
        byte[] intBuf = new byte[4];
        byte[] shortBuf = new byte[2];
        int width = image.getWidth();
        int height = image.getHeight();
        littleEndianUnIntToByte(40, intBuf);
        bufferCopy(intBuf, mapHeader, 0, 4);

        littleEndianIntToByte(width, intBuf);
        bufferCopy(intBuf, mapHeader, 4, 4);

        littleEndianIntToByte(height, intBuf);
        bufferCopy(intBuf, mapHeader, 8, 4);

        littleEndianUnShortToByte(0, shortBuf);
        bufferCopy(shortBuf, mapHeader, 12, 2);

        littleEndianUnShortToByte(24, shortBuf);
        bufferCopy(shortBuf, mapHeader, 14, 2);

        littleEndianUnIntToByte(0, intBuf);
        bufferCopy(intBuf, mapHeader, 16, 4);

        littleEndianUnIntToByte(0, intBuf);
        bufferCopy(intBuf, mapHeader, 20, 4);

        littleEndianIntToByte(0, intBuf);
        bufferCopy(intBuf, mapHeader, 24, 4);
        bufferCopy(intBuf, mapHeader, 28, 4);
        bufferCopy(intBuf, mapHeader, 32, 4);
        bufferCopy(intBuf, mapHeader, 36, 4);

        return mapHeader;
    }

    private byte[] createFileHeader(BufferedImage image){
        byte[] fileHeader = new byte[14];
        byte[] intBuf = new byte[4];
        byte[] shortBuf = new byte[2];
        int width = image.getWidth();
        int height = image.getHeight();
        fileHeader[0] = (MAGIC_NUMBER>>8) & 0xFF;
        fileHeader[1] = (MAGIC_NUMBER) & 0xFF;
        int lineLength;
        if(image.getWidth() % 4 == 0){
            lineLength = width;
        }else{
            lineLength = width + (4 - width % 4);
        }
        int fileSize = 54 + lineLength * 3 * height ;
        littleEndianUnIntToByte(fileSize, intBuf);
        bufferCopy(intBuf, fileHeader, 2, 4);

        littleEndianUnShortToByte(0, shortBuf);
        bufferCopy(intBuf, fileHeader, 6, 2);
        bufferCopy(intBuf, fileHeader, 8, 2);

        littleEndianUnIntToByte(54, intBuf);
        bufferCopy(intBuf, fileHeader, 10, 4);

        return fileHeader;
    }

    private void littleEndianUnIntToByte(int value, byte[] buf){//little endian
        buf[0] = (byte) (value&0xFF);
        buf[1] = (byte) ((value>>8)&0xFF);
        buf[2] = (byte) ((value>>16)&0xFF);
        buf[3] = (byte) ((value>>24)&0xFF);
    }
    private void littleEndianIntToByte(int value, byte[] buf){
        buf[0] = (byte) (value&0xFF);
        buf[1] = (byte) ((value>>8)&0xFF);
        buf[2] = (byte) ((value>>16)&0xFF);
        buf[3] = (byte) ((value>>24)&0xFF);
    }
    private void littleEndianUnShortToByte(int value, byte[] buf){//little endian
        buf[0] = (byte) (value&0xFF);
        buf[1] = (byte) ((value>>8)&0xFF);
    }
}
