package ru.nsu.fit.g13205.kushner.utils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class Reader {
    private final File file;


    public Reader(File file){
        this.file = file;
    }

    public FileInfo readFile() throws BadFileFormatException {
        Pattern commentPattern = Pattern.compile("\\s*//.*$"); // регулярное выражение поиска комментариев
        int k = 0, m = 0, nColor = 0;
        ArrayList<Color> colorsLegend = new ArrayList<>();
        Color colorBorder = null;
        String str;
        String[] splitStr;
        try(BufferedReader bf = new BufferedReader(new FileReader(file))){
            str = commentPattern.matcher(bf.readLine()).replaceAll("");
            splitStr = str.split(" +");//больше одного пробела можно
            k = Integer.valueOf(splitStr[0]);
            m = Integer.valueOf(splitStr[1]);
            str = commentPattern.matcher(bf.readLine()).replaceAll("");
            nColor = Integer.valueOf(str);
            for(int i = 0; i < nColor; i++){
                str = commentPattern.matcher(bf.readLine()).replaceAll("");
                splitStr = str.split(" +");
                if(splitStr.length > 3){
                    throw new BadFileFormatException("в строке есть символы, после полезных данных");
                }
                int r = Integer.valueOf(splitStr[0]);
                int g = Integer.valueOf(splitStr[1]);
                int b = Integer.valueOf(splitStr[2]);
                colorsLegend.add(new Color(r, g, b));
            }
            str = commentPattern.matcher(bf.readLine()).replaceAll("");
            splitStr = str.split(" +");
            int r = Integer.valueOf(splitStr[0]);
            int g = Integer.valueOf(splitStr[1]);
            int b = Integer.valueOf(splitStr[2]);
            colorBorder = new Color(r, g, b);
        }catch (NumberFormatException e){
            throw new BadFileFormatException("неверный формат файла: ожидалась цифра, но получен иной символ");
        } catch (IOException ignored) {

        }catch (IndexOutOfBoundsException e){
            throw new BadFileFormatException("неверный формат файла: в строке недостаточно информации");
        }catch (NullPointerException e){
            throw new BadFileFormatException("неверный формат файла: файл закончился раньше, чем ожидалось");
        }
        return new FileInfo(k, m, nColor, colorsLegend, colorBorder);
    }

    private void errorMessage(JFrame owner, String message){
        Object[] options = {"OK"};
        JOptionPane.showOptionDialog(owner,
                String.format("%s", message), "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
}
