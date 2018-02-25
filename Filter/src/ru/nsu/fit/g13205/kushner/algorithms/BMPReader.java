package ru.nsu.fit.g13205.kushner.algorithms;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Konstantin on 14.03.2016.
 */
public class BMPReader {
    private static final int MAGIC_NUMBER = 0x424D;// or 0x4D42
    private File file;

    public BMPReader(File file) {
        this.file = file;
    }


    public BufferedImage read() throws IOException, BadFormatException {
        byte[] intBuf = new byte[4];
        byte[] shortBuf = new byte[2];
        BufferedImage image;

        try(DataInputStream inputStream = new DataInputStream(new FileInputStream(file))) {
            parseFileHeader(inputStream);
            inputStream.read(intBuf);
            long bcSize = toLittleEndianUnsignedInt(intBuf);
            int width = 0;
            int height = 0;
            if (bcSize == 12) {
                byte[] mapHeader = new byte[(int) (bcSize - 4)];
                inputStream.read(mapHeader);
                copy(mapHeader, shortBuf, 0, 1);
                width = toLittleEndianUnsignedShort(shortBuf);

                copy(mapHeader, shortBuf, 2, 3);
                height = toLittleEndianUnsignedShort(shortBuf);

                copy(mapHeader, shortBuf, 10, 11);
                int bitCount = toLittleEndianUnsignedShort(shortBuf);
                if (bitCount != 24) {
                    throw new BadFormatException("файл bmp, но битность не 24");
                }
            } else if (bcSize == 40 || bcSize == 108 || bcSize == 124) {
                byte[] mapHeader = new byte[(int) (bcSize - 4)];
                inputStream.read(mapHeader);

                copy(mapHeader, intBuf, 0, 3);
                width = toLittleEndianSignedInt(intBuf);

                copy(mapHeader, intBuf, 4, 7);
                height = toLittleEndianSignedInt(intBuf);

                copy(mapHeader, shortBuf, 10, 11);
                int bitCount = toLittleEndianUnsignedShort(shortBuf);
                if (bitCount != 24) {
                    throw new BadFormatException("файл bmp, но битность не 24");
                }

                copy(mapHeader, intBuf, 12, 15);
                long compression = toLittleEndianUnsignedInt(intBuf);
                if (compression != 0) {
                    throw new BadFormatException("файл был сжат");
                }

                copy(mapHeader, intBuf, 16, 19);
            } else {
                throw new BadFormatException("на вход программе подан не подходящий файл");
            }
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int lengthLine;
            if ((width * 3) % 4 != 0) {
                lengthLine = (width * 3 + (4 - (width * 3) % 4));
            } else {
                lengthLine = width * 3;
            }
            byte[] colorBuffer = new byte[lengthLine * height];
            inputStream.read(colorBuffer);

            if (height >= 0) {
                for (int i = height; i >= 1; i--) {
                    for (int j = 0; j < width; j++) {
                        try {
                            int blue = toUnsignedByte(colorBuffer[(height - i) * lengthLine + 3 * j]);
                            int green = toUnsignedByte(colorBuffer[(height - i) * lengthLine + 3 * j + 1]);
                            int red = toUnsignedByte(colorBuffer[(height - i) * lengthLine + 3 * j + 2]);
                            image.setRGB(j, i - 1, (red << 16) | (green << 8) | (blue));
                        }catch (Exception e){

                        }
                    }
                }
            } else {
                for (int i = 0; i < Math.abs(height); i++) {
                    for (int j = 0; j < width; j++) {
                        int blue = toUnsignedByte(colorBuffer[i * lengthLine + 3 * j]);
                        int green = toUnsignedByte(colorBuffer[i * lengthLine + 3 * j + 1]);
                        int red = toUnsignedByte(colorBuffer[i * lengthLine + 3 * j + 2]);
                        image.setRGB(j, i, (red << 16) | (green << 8) | (blue));
                    }
                }
            }
        }
        return image;
    }

    private void parseFileHeader(DataInputStream iStream) throws IOException, BadFormatException {
        byte[] fileHeader = new byte[14];
        iStream.read(fileHeader);

        if((toUnsignedByte(fileHeader[0]) != ((MAGIC_NUMBER>>8) & 0xFF)) && (toUnsignedByte(fileHeader[1]) != (MAGIC_NUMBER & 0xFF))){
            throw new BadFormatException("не bmp файл");
        }

        byte intBuf[] = new byte[4];
        copy(fileHeader, intBuf, 2, 5);
    }

    private void copy(byte[] src, byte[] dst, int start, int finish){
        for(int i = start; i < finish; i++){
            dst[i - start] = src[i];
        }
    }

    private long toLittleEndianUnsignedInt(byte[] buf){//little endian
        return ((long) ((0x000000FF & ((int)buf[3])) << 24
                | (0x000000FF & ((int)buf[2])) << 16
                | (0x000000FF & ((int)buf[1])) << 8
                | (0x000000FF & ((int)buf[0]))))
                & 0x00000000FFFFFFFFL;
    }

    private int toUnsignedByte(byte b){
        return  0xFF & ((int)b);
    }

    private int toLittleEndianSignedInt(byte[] buf){
        return ((0x000000FF & ((int)buf[3])) << 24
                | (0x000000FF & ((int)buf[2])) << 16
                | (0x000000FF & ((int)buf[1])) << 8
                | (0x000000FF & ((int)buf[0])));
    }

    private int toLittleEndianUnsignedShort(byte[] buf){
        return ((0x000000FF & ((int)buf[1])) << 8
                | (0x000000FF & ((int)buf[0])));
    }

}
