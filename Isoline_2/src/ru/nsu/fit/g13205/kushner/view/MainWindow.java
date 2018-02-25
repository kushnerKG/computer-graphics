package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.model.ModelListener;
import ru.nsu.fit.g13205.kushner.utils.BadFileFormatException;
import ru.nsu.fit.g13205.kushner.utils.FileInfo;
import ru.nsu.fit.g13205.kushner.utils.Function;
import ru.nsu.fit.g13205.kushner.utils.Reader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class MainWindow extends MyMainFrame implements ModelListener {

    private MainArea mainArea;

    private JMenuItem newDocumentItem;
    private JMenuItem settingsItem;
    private JCheckBoxMenuItem gridItem;
    private JCheckBoxMenuItem isolineItem;
    private JCheckBoxMenuItem interactiveModeItem;
    private JMenuItem clearIsolineItem;
    private JCheckBoxMenuItem interpolationItem;
    private JCheckBoxMenuItem showMapItem;

    private JButton newDocumentButton;
    private JButton settingsButton;
    private JButton clearIsolineButton;
    private JToggleButton gridButton;
    private JToggleButton isolineButton;
    private JToggleButton interactiveModeButton;
    private JToggleButton interpolationButton;
    private JToggleButton showMapButton;

    private boolean includeGridMode = false;
    private boolean includeIsolineMode = false;
    private boolean includeInteractiveMode = false;
    private boolean enableInterpolationMode = false;
    private boolean enableShowMapMode = false;

    private boolean openFile = false;

    private Controller controller;

    private FileInfo info;

    private double A = Function.A;
    private double B = Function.B;
    private double C = Function.C;
    private double D = Function.D;

    public MainWindow(int width, int height, String windowTitle, Controller controller) {
        super(width, height, windowTitle);
        this.controller = controller;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onExit();
            }
        });
        setLocationRelativeTo(null);

        mainArea = new MainArea(this, controller);
        try {
            createMenuBar();
            createToolBar();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.add(mainArea);
        this.addComponentListener(resizeListener());
        initMenu();
    }

    private ComponentAdapter resizeListener(){
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                controller.handleNewFieldSize(mainArea.getWidth(), mainArea.height());
            }
        };
    }

    private void createToolBar(){
        addToolBarButton("File/Open", "resources/openFile.gif", mainArea.getStatusBar());
        newDocumentButton = addToolBarButton("File/New Document", "resources/newFile.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        gridButton = addToolBarToggleButton("Edit/Draw grid", "resources/grid.gif", mainArea.getStatusBar());
        isolineButton = addToolBarToggleButton("Edit/Show isoline", "resources/isoline.gif", mainArea.getStatusBar());
        interactiveModeButton = addToolBarToggleButton("Edit/Interactive mode", "resources/interactiveMode.gif", mainArea.getStatusBar());
        clearIsolineButton = addToolBarButton("Edit/Delete isolines", "resources/clearIsoline.gif", mainArea.getStatusBar());
        interpolationButton = addToolBarToggleButton("Edit/Interpolation", "resources/interpolation.gif", mainArea.getStatusBar());
        settingsButton = addToolBarButton("Edit/Settings", "resources/settings.gif", mainArea.getStatusBar());
        showMapButton = addToolBarToggleButton("Edit/Map", "resources/map.gif", mainArea.getStatusBar());

        addToolBarSeparator();
        addToolBarButton("File/Exit", "resources/exit.gif", mainArea.getStatusBar());
    }

    private void createMenuBar() throws NoSuchMethodException {
        addSubMenu("File", 0);
        addMenuItem("File/Open", "Open a file", 0, "onOpenFile",  mainArea.getStatusBar());
        addMenuItem("File/New Document", "Вернуть все в начальное состояние", 0, "onNewDocument",  mainArea.getStatusBar());
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit", 0, "onExit",  mainArea.getStatusBar());

        addSubMenu("Edit", 0);
        addCheckBoxMenuItem("Edit/Draw grid", "Draw grid", 0, "onGrid",  mainArea.getStatusBar());
        addCheckBoxMenuItem("Edit/Show isoline", "Show isoline", 0, "onShowIsoline",  mainArea.getStatusBar());
        addCheckBoxMenuItem("Edit/Interactive mode", "Включить интерактивный режим рисования изолиний", 0, "onInteractiveMode",  mainArea.getStatusBar());
        addMenuItem("Edit/Delete isolines", "удалить изолинии", 0, "onDeleteIsoline",  mainArea.getStatusBar());
        addCheckBoxMenuItem("Edit/Interpolation", "включить интерполяцию", 0, "onInterpolation",  mainArea.getStatusBar());
        addCheckBoxMenuItem("Edit/Map", "показать карту", 0, "onShowMap",  mainArea.getStatusBar());
        addMenuItem("Edit/Settings", "Settings", 0, "onSettings",  mainArea.getStatusBar());

        addSubMenu("Help", 0);
        addMenuItem("Help/About", "о авторе", 0, "onAbout", mainArea.getStatusBar());

        newDocumentItem = (JMenuItem) super.getMenuElement("File/New Document");
        gridItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Draw grid");
        isolineItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Show isoline");
        interactiveModeItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Interactive mode");
        settingsItem = (JMenuItem) super.getMenuElement("Edit/Settings");
        clearIsolineItem = (JMenuItem) super.getMenuElement("Edit/Delete isolines");
        interpolationItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Map");

        showMapItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Interpolation");
    }

    public void onOpenFile(){
        File file = this.getOpenFileName(Settings.EXTENSION, "Text file");
        Reader reader = new Reader(file);
        try {
            controller.handleNewFieldSize(mainArea.getWidth(), mainArea.height());
            info = reader.readFile();
            controller.handleOpenFile(info);
            openFile = true;
            initMenu();
        } catch (BadFileFormatException e) {
            errorMessage(this, e.getMessage());
        } catch (Exception e){
        //    e.printStackTrace();
        }

        showMapButton.setSelected(true);
        showMapItem.setSelected(true);
        enableShowMapMode = true;
    }


    public void onNewDocument(){
        controller.handleNewDocument();
        openFile = false;
        initMenu();
    }

    public void onExit(){
        System.exit(0);
    }

    public void onGrid(){
        if(includeGridMode) {
            gridItem.setSelected(false);
            gridButton.setSelected(false);
            includeGridMode = false;
            controller.handleUnSelectDrawGrid();
        }else{
            gridItem.setSelected(true);
            gridButton.setSelected(true);
            includeGridMode = true;
            controller.handleSelectDrawGrid();
        }
    }

    public void onShowMap(){
        try {
            if (enableShowMapMode) {
                showMapButton.setSelected(false);
                showMapItem.setSelected(false);
                enableShowMapMode = false;
                controller.handleUnShowMap();
            } else {
                showMapButton.setSelected(true);
                showMapItem.setSelected(true);
                enableShowMapMode = true;
                controller.handleShowMap();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onShowIsoline(){

        if(includeIsolineMode) {
            isolineItem.setSelected(false);
            isolineButton.setSelected(false);
            includeIsolineMode = false;
            controller.handleUnSelectDrawIsoline();
        }else{
            isolineItem.setSelected(true);
            isolineButton.setSelected(true);
            includeIsolineMode = true;
            controller.handleSelectShowIsoline();
        }
    }

    public void onInteractiveMode(){
        if(includeInteractiveMode) {
            interactiveModeButton.setSelected(false);
            interactiveModeItem.setSelected(false);
            includeInteractiveMode = false;
            controller.handleSwitchOffInteractiveMode();
        }else{
            interactiveModeButton.setSelected(true);
            interactiveModeItem.setSelected(true);
            includeInteractiveMode = true;
            controller.handleSwitchOnInteractiveMode();
        }
    }

    public void onDeleteIsoline(){
        controller.handleDeleteIsoline();
        includeIsolineMode = false;
        includeInteractiveMode = false;

        interactiveModeItem.setSelected(false);
        interactiveModeButton.setSelected(false);

        isolineItem.setSelected(false);
        isolineButton.setSelected(false);
    }

    public void onInterpolation(){
        if(enableInterpolationMode) {
            interpolationButton.setSelected(false);
            interactiveModeItem.setSelected(false);
            enableInterpolationMode = false;
            controller.handleSwitchOffInterpolation();
        }else{
            interpolationButton.setSelected(true);
            interactiveModeItem.setSelected(true);
            enableInterpolationMode = true;
            controller.handleSwitchOnInterpolation();
        }
    }

    public void onSettings(){

        SettingsDialog dialog = new SettingsDialog("Settings", this, info.getK(), info.getM(), A, B, C, D);

        dialog.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A = dialog.getA();
                B = dialog.getB();
                C = dialog.getC();
                D = dialog.getD();

                info.setK(dialog.getK());
                info.setM(dialog.getM());

                controller.handleNewSetting(info, A, B, C, D);
                dialog.setVisible(false);
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

    public void onAbout(){
        JOptionPane.showMessageDialog(this, Settings.ABOUT_MESSAGE);
    }

    private void errorMessage(JFrame owner, String message){
        Object[] options = {"OK"};
        JOptionPane.showOptionDialog(owner,
                String.format("%s", message), "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    }

    public MainArea getMainArea(){
        return mainArea;
    }

    public void initMenu(){
        newDocumentButton.setEnabled(openFile);
        settingsButton.setEnabled(openFile);
        gridButton.setEnabled(openFile);
        isolineButton.setEnabled(openFile);
        interactiveModeButton.setEnabled(openFile);
        clearIsolineButton.setEnabled(openFile);
        interpolationButton.setEnabled(openFile);

        newDocumentItem.setEnabled(openFile);
        settingsItem.setEnabled(openFile);
        gridItem.setEnabled(openFile);
        isolineItem.setEnabled(openFile);
        interactiveModeItem.setEnabled(openFile);
        clearIsolineItem.setEnabled(openFile);
        interpolationItem.setEnabled(openFile);
    }

    @Override
    public void updateImage(BufferedImage image, Point start, Point finish) {
        mainArea.updateImage(image, start, finish);
    }

    @Override
    public void updateCoordinates(double x, double y) {
        mainArea.updateCoordinates(x, y);
    }
}
