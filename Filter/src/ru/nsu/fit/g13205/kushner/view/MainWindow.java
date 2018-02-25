package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.algorithms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Konstantin on 08.03.2016.
 */
public class MainWindow extends MyMainFrame {

    private MainArea mainArea;
    private boolean isSelectImageB = false;
    private JScrollPane scrollPane;
    private boolean isSelectedMode = false;
    private boolean unPixelizeMode = true;

    private JCheckBoxMenuItem pixelizeItem;
    private JCheckBoxMenuItem selectItem;
    private JMenuItem desaturateItem;
    private JMenuItem negativeItem;
    private JMenuItem saveItem;
    private JMenuItem newDocumentItem;
    private JMenuItem blurItem;
    private JMenuItem sharpenItem;
    private JMenuItem embossItem;
    private JMenuItem medianFilterItem;
    private JMenuItem watercolorItem;
    private JMenuItem robertsItem;
    private JMenuItem sobelItem;
    private JMenuItem gammaCorrectionItem;
    private JMenuItem copyRightItem;
    private JMenuItem copyLeftItem;
    private JMenuItem floydSteinbergItem;
    private JMenuItem orderedDitheringItem;
    private JMenuItem rotateItem;
    private JMenuItem x2Item;

    ////////////////////////
    private JToggleButton pixelizeButton;
    private JToggleButton selectButton;
    private JButton desaturateButton;
    private JButton negativeButton;
    private JButton saveButton;
    private JButton newDocumentButton;
    private JButton blurButton;
    private JButton sharpenButton;
    private JButton embossButton;
    private JButton medianFilterButton;
    private JButton watercolorButton;
    private JButton robertsButton;
    private JButton sobelButton;
    private JButton gammaCorrectionButton;
    private JButton copyRightButton;
    private JButton copyLeftButton;
    private JButton floydSteinbergButton;
    private JButton orderedDitheringButton;
    private JButton rotateButton;
    private JButton x2Button;

    ////


    public MainWindow(int width, int height, String windowTitle) {
        super(width, height, windowTitle);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onExit();
            }
        });
        setLocationRelativeTo(null);

        mainArea = new MainArea(this);
        try {
            createMenuBar();
            createToolBar();
            initMenu();
            selectButton.setEnabled(false);
            copyLeftButton.setEnabled(false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        scrollPane = new JScrollPane(mainArea);
        this.add(scrollPane);
    }



    @Override
    public void paintComponents(Graphics g){
        super.paintComponents(g);
        if(scrollPane != null) {
            scrollPane.updateUI();
        }
    }

    @Override
    public void repaint(){
        super.repaint();
        if (scrollPane != null) {
            scrollPane.updateUI();
        }

    }

    private void createToolBar(){
        addToolBarButton("File/Open", "resources/openFile.gif", mainArea.getStatusBar());
        newDocumentButton = addToolBarButton("File/New Document", "resources/newFile.gif", mainArea.getStatusBar());
        saveButton = addToolBarButton("File/Save as", "resources/saveIcon.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        selectButton = addToolBarToggleButton("Edit/Select", "resources/select.gif", mainArea.getStatusBar());
        pixelizeButton = addToolBarToggleButton("Edit/Pixelize", "resources/Pixelize.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        desaturateButton = addToolBarButton("Edit/Desaturate", "resources/blackAndWhite.gif", mainArea.getStatusBar());
        negativeButton = addToolBarButton("Edit/Negative", "resources/negative.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        blurButton = addToolBarButton("Edit/Blur", "resources/blur.gif", mainArea.getStatusBar());
        sharpenButton = addToolBarButton("Edit/Sharpen", "resources/sharpen.gif", mainArea.getStatusBar());
        embossButton = addToolBarButton("Edit/Emboss", "resources/stamping.gif", mainArea.getStatusBar());
        medianFilterButton = addToolBarButton("Edit/MedianFilter", "resources/medians.gif", mainArea.getStatusBar());
        watercolorButton = addToolBarButton("Edit/Watercolor", "resources/palette.gif", mainArea.getStatusBar());
        gammaCorrectionButton = addToolBarButton("Edit/GammaCorrection", "resources/gamma.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        robertsButton = addToolBarButton("Edit/Roberts", "resources/roberts.gif", mainArea.getStatusBar());
        sobelButton = addToolBarButton("Edit/Sobel", "resources/sobel.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        floydSteinbergButton = addToolBarButton("Edit/Floyd-Steinberg", "resources/floyd.gif", mainArea.getStatusBar());
        orderedDitheringButton = addToolBarButton("Edit/OrderedDithering", "resources/ordered.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        x2Button = addToolBarButton("Edit/X2", "resources/zoom.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        rotateButton = addToolBarButton("Edit/Rotate", "resources/rotate.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        copyLeftButton = addToolBarButton("Edit/CopyLeft", "resources/copyLeft.gif", mainArea.getStatusBar());
        copyRightButton = addToolBarButton("Edit/CopyRight", "resources/copyRight.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        addToolBarButton("Help/About", "resources/about.gif", mainArea.getStatusBar());

        addToolBarSeparator();
        addToolBarButton("File/Exit", "resources/exit.gif", mainArea.getStatusBar());
    }

    private void createMenuBar() throws NoSuchMethodException {
        addSubMenu("File", 0);
        addMenuItem("File/Open", "Open a file", 0, "onOpenFile",  mainArea.getStatusBar());
        addMenuItem("File/New Document", "Вернуть все в начальное состояние", 0, "onNewDocument",  mainArea.getStatusBar());
        addMenuItem("File/Save as", "Save the file", 0, "onSaveFile",  mainArea.getStatusBar());
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit", 0, "onExit",  mainArea.getStatusBar());

        addSubMenu("Edit", 0);

        addCheckBoxMenuItem("Edit/Select", "Select", 0, "onSelect",  mainArea.getStatusBar());
        addCheckBoxMenuItem("Edit/Pixelize", "Select", 0, "onPixelize",  mainArea.getStatusBar());
        addMenuSeparator("Edit");

        addMenuItem("Edit/Desaturate", "Получить черно-белое изображение", 0, "onDesaturate", mainArea.getStatusBar());
        addMenuItem("Edit/Negative", "Получить негатив", 0, "onNegative", mainArea.getStatusBar());
        addMenuSeparator("Edit");
        addMenuItem("Edit/Blur", "Получить размытое изображение", 0, "onBlur", mainArea.getStatusBar());
        addMenuItem("Edit/Sharpen", "Увеличить резкость", 0, "onSharpen", mainArea.getStatusBar());
        addMenuItem("Edit/Emboss", "Тиснение", 0, "onEmboss", mainArea.getStatusBar());
        addMenuItem("Edit/MedianFilter", "Медианный фильтр сглаживания", 0, "onMedianFilter", mainArea.getStatusBar());
        addMenuItem("Edit/Watercolor", "Акварелизация", 0, "onWaterColor", mainArea.getStatusBar());
        addMenuItem("Edit/GammaCorrection", "Гамма коррекция", 0, "onGammaCorrection", mainArea.getStatusBar());
        addMenuSeparator("Edit");
        addMenuItem("Edit/Roberts", "Выделение границ методом робертса", 0, "onRoberts", mainArea.getStatusBar());
        addMenuItem("Edit/Sobel", "Выделение границ методом собеля", 0, "onSobel", mainArea.getStatusBar());
        addMenuSeparator("Edit");
        addMenuItem("Edit/Floyd-Steinberg", "Применение алгоритма дизеринга Флойда-Стейнберга", 0, "onFloydSteinberg", mainArea.getStatusBar());
        addMenuItem("Edit/OrderedDithering", "Применение алгоритма порядкового дизеринга", 0, "onOrderedDithering", mainArea.getStatusBar());
        addMenuSeparator("Edit");
        addMenuItem("Edit/X2", "Увеличить изображение в два раза", 0, "onX2", mainArea.getStatusBar());
        addMenuSeparator("Edit");
        addMenuItem("Edit/Rotate", "Повернуть изображение", 0, "onRotate", mainArea.getStatusBar());
        addMenuSeparator("Edit");

        addMenuItem("Edit/CopyRight", "Скопировать из области B в область C", 0, "onCopyRight", mainArea.getStatusBar());
        addMenuItem("Edit/CopyLeft", "Скопировать из области C в область B", 0, "onCopyLeft", mainArea.getStatusBar());

        addSubMenu("Help", 0);
        addMenuItem("Help/About", "о авторе", 0, "onAbout", mainArea.getStatusBar());

        selectItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Select");
        pixelizeItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Pixelize");

        desaturateItem = (JMenuItem) super.getMenuElement("Edit/Desaturate");
        negativeItem = (JMenuItem) super.getMenuElement("Edit/Negative");
        saveItem = (JMenuItem) super.getMenuElement("File/Save as");
        newDocumentItem = (JMenuItem) super.getMenuElement("File/New Document");

        blurItem = (JMenuItem) super.getMenuElement("Edit/Blur");
        sharpenItem = (JMenuItem) super.getMenuElement("Edit/Sharpen");
        embossItem = (JMenuItem) super.getMenuElement("Edit/Emboss");
        medianFilterItem = (JMenuItem) super.getMenuElement("Edit/MedianFilter");
        watercolorItem = (JMenuItem) super.getMenuElement("Edit/Watercolor");
        robertsItem = (JMenuItem) super.getMenuElement("Edit/Roberts");
        sobelItem = (JMenuItem) super.getMenuElement("Edit/Sobel");
        gammaCorrectionItem = (JMenuItem) super.getMenuElement("Edit/GammaCorrection");
        copyLeftItem = (JMenuItem) super.getMenuElement("Edit/CopyLeft");
        copyRightItem = (JMenuItem) super.getMenuElement("Edit/CopyRight");
        x2Item = (JMenuItem) super.getMenuElement("Edit/X2");
        floydSteinbergItem = (JMenuItem) super.getMenuElement("Edit/Floyd-Steinberg");
        orderedDitheringItem = (JMenuItem) super.getMenuElement("Edit/OrderedDithering");
        rotateItem = (JMenuItem) super.getMenuElement("Edit/Rotate");


        selectItem.setEnabled(false);

        copyLeftItem.setEnabled(false);

    }


    public void onRotate(){

        RotateDialog dialog = new RotateDialog("angle", this, -180, 180, 0, mainArea);

        dialog.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mainArea.showRotateImage(dialog.getAngle());
                }catch (NumberFormatException exp){
                    return;
                }
            }
        });

        dialog.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.cancelRotateImage();
                dialog.setVisible(false);
            }
        });
        dialog.setVisible(true);

    }

    public void onOrderedDithering(){
        DitheringDialog dialog = new DitheringDialog("Dithering", this, 2, 360);

        dialog.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.showOrderedDithering(dialog.getRed(), dialog.getGreen(), dialog.getBlue());
            }
        });

        dialog.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        dialog.setVisible(true);
    }

    public void onNewDocument(){
        mainArea.clear();
    }

    public void onFloydSteinberg(){

        DitheringDialog dialog = new DitheringDialog("Dithering", this, 2, 360);

        dialog.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.showFloydDithering(dialog.getRed(), dialog.getGreen(), dialog.getBlue());
            }
        });

        dialog.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        dialog.setVisible(true);
    }

    public void onPixelize(){
        if(unPixelizeMode) {

            pixelizeItem.setSelected(true);
            pixelizeButton.setSelected(true);
            unPixelizeMode = false;

            PixelizeDialog dialog = new PixelizeDialog(this);

            dialog.getOkButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int value = dialog.getValue();
                    if (value != 1) {
                        mainArea.showPixelize(value);
                        dialog.setVisible(false);
                    }

                }
            });

            dialog.getCancelButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            });
            dialog.setVisible(true);
        }else{
            pixelizeItem.setSelected(false);
            pixelizeButton.setSelected(false);
            unPixelizeMode = true;
            mainArea.unShowPixelize();
        }
    }

    public void onCopyRight(){
        mainArea.copyRight();
    }

    public void onCopyLeft(){
        mainArea.copyLeft();
    }

    public void onX2(){
        mainArea.showX2();
    }

    public void onSaveFile(){
        File currentFile = this.getSaveFileName(Settings.EXTENSION, "Bmp file");
        BufferedImage image = mainArea.getImageC();
        try {
            BMPWriter bmpW = new BMPWriter(currentFile);
            bmpW.write(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRoberts(){

        FindEdgeDialog dialog = new FindEdgeDialog("Edge treshold", this, 0, 360, 11);


        dialog.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int threshold = dialog.getThreshold();
                    if (threshold < 0 || threshold > 360) {
                        return;
                    }
                }catch (NumberFormatException exp){
                    return;
                }
                mainArea.showRobertsFilter(dialog.getThreshold());
            }
        });

        dialog.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.getOwner().setVisible(false);
            }
        });
        dialog.setVisible(true);
    }

    public void onSobel(){
        FindEdgeDialog dialog = new FindEdgeDialog("Edge treshold", this, 0, 360, 11);

        dialog.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int threshold = dialog.getThreshold();
                    if (threshold < 0 || threshold > 360) {
                        return;
                    }
                }catch (NumberFormatException exp){
                    return;
                }

                mainArea.showSobelFilter(dialog.getThreshold());
            }
        });

        dialog.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.getOwner().setVisible(false);
            }
        });
        dialog.setVisible(true);
    }

    public void onGammaCorrection(){
        GammaCorrectionDialog dialog = new GammaCorrectionDialog(this, mainArea);

        dialog.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mainArea.showGammaCorrection(dialog.getGamma());
                }catch (NumberFormatException exp){}
            }
        });

        dialog.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.cancelGammaCorrection();
                dialog.setVisible(false);
            }
        });
        dialog.setVisible(true);
    }

    public void onMedianFilter(){
        mainArea.showMedianFilter();
    }

    public void onWaterColor(){
        mainArea.showWaterColor();
    }

    public void onEmboss(){
        mainArea.showEmboss();
    }

    public void onSharpen(){
        mainArea.showSharpen();
    }

    public void onBlur(){
        mainArea.showBlur();
    }

    public void onNegative(){
        mainArea.showNegative();
    }

    public void onDesaturate(){
        mainArea.showDesaturate();
    }

    public void onSelect(){

        if(isSelectedMode){
            isSelectedMode = false;
            selectItem.setSelected(false);
            selectButton.setSelected(false);
            mainArea.setSelect(false);
            mainArea.repaint();
        }else{
            isSelectedMode = true;
            selectItem.setSelected(true);
            selectButton.setSelected(true);
            mainArea.setSelect(true);
        }
    }

    public void onExit(){
        System.exit(0);
    }

    public void onOpenFile(){
        File file = this.getOpenFileName(Settings.EXTENSION, "Text file");
        try {
            BMPReader reader = new BMPReader(file);
            BufferedImage originalImage = reader.read();
            int width  = originalImage.getWidth();
            int height = originalImage.getHeight();
            double cof = 1;
            if(width >= Settings.AREA_WIDTH && height >= Settings.AREA_HEIGHT){
                double deltaWidth = (double)width / (double)Settings.AREA_WIDTH;
                double deltaHeight = (double) height / (double)Settings.AREA_HEIGHT;

                if(deltaWidth > deltaHeight){
                    cof = deltaWidth;
                }else{
                    cof = deltaHeight;
                }
            }else if(width >= Settings.AREA_WIDTH && height < Settings.AREA_HEIGHT){
                cof = (double)width / (double)Settings.AREA_WIDTH;
            }else if(width < Settings.AREA_WIDTH && height >= Settings.AREA_HEIGHT){
                cof = (double) height / (double)Settings.AREA_HEIGHT;
            }
            BufferedImage imageA = Algorithms.superSampling(originalImage, (int)((double)width / cof), (int)((double)height / cof));
            mainArea.setImage(originalImage, imageA, cof);
            selectItem.setEnabled(true);
            copyLeftItem.setEnabled(false);
            selectButton.setEnabled(true);
            copyLeftButton.setEnabled(false);
            selectItem.setSelected(false);
            mainArea.setSelect(false);
            mainArea.repaint();
        } catch (NullPointerException ignored){

        }catch (BadFormatException exp){
            Object[] options = {"OK"};
            JOptionPane.showOptionDialog(this,
                    exp.getMessage(), "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onAbout(){
        JOptionPane.showMessageDialog(this, Settings.ABOUT_MESSAGE);
    }

    public void setSelectImageB(boolean isSelectImageB){
        this.isSelectImageB = isSelectImageB;
        initMenu();
    }

    public void setSelectImageC(boolean isSelectImageC){
        copyLeftItem.setEnabled(isSelectImageC);
        copyLeftButton.setEnabled(isSelectImageC);
    }

    private void initMenu(){
        pixelizeItem.setEnabled(isSelectImageB);

        desaturateItem.setEnabled(isSelectImageB);
        negativeItem.setEnabled(isSelectImageB);
        blurItem.setEnabled(isSelectImageB);
        sharpenItem.setEnabled(isSelectImageB);
        embossItem.setEnabled(isSelectImageB);
        medianFilterItem.setEnabled(isSelectImageB);
        watercolorItem.setEnabled(isSelectImageB);
        robertsItem.setEnabled(isSelectImageB);
        sobelItem.setEnabled(isSelectImageB);
        gammaCorrectionItem.setEnabled(isSelectImageB);
        copyRightItem.setEnabled(isSelectImageB);
        x2Item.setEnabled(isSelectImageB);
        floydSteinbergItem.setEnabled(isSelectImageB);
        orderedDitheringItem.setEnabled(isSelectImageB);
        rotateItem.setEnabled(isSelectImageB);

        saveItem.setEnabled(mainArea.getWasChange());

        pixelizeButton.setEnabled(isSelectImageB);
        desaturateButton.setEnabled(isSelectImageB);
        negativeButton.setEnabled(isSelectImageB);
        newDocumentButton.setEnabled(isSelectImageB);
        blurButton.setEnabled(isSelectImageB);
        sharpenButton.setEnabled(isSelectImageB);
        embossButton.setEnabled(isSelectImageB);
        medianFilterButton.setEnabled(isSelectImageB);
        watercolorButton.setEnabled(isSelectImageB);
        robertsButton.setEnabled(isSelectImageB);
        sobelButton.setEnabled(isSelectImageB);
        gammaCorrectionButton.setEnabled(isSelectImageB);
        copyRightButton.setEnabled(isSelectImageB);
        x2Item.setEnabled(isSelectImageB);
        floydSteinbergButton.setEnabled(isSelectImageB);
        orderedDitheringButton.setEnabled(isSelectImageB);
        rotateButton.setEnabled(isSelectImageB);
        x2Button.setEnabled(isSelectImageB);

        saveButton.setEnabled(mainArea.getWasChange());
    }


}
