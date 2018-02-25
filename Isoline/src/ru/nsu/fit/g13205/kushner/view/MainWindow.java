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
import java.util.Set;

/**
 * Created by Konstantin on 24.03.2016.
 */
public class MainWindow extends MyMainFrame implements ModelListener {

   // private JMenuItem saveItem;
    private JMenuItem newDocumentItem;
    private JMenuItem settingsItem;
    private JCheckBoxMenuItem gridItem;
    private JCheckBoxMenuItem isolineItem;
    private JCheckBoxMenuItem interactiveModeItem;



    private MainArea mainArea;

   // private JButton saveButton;
    private JButton newDocumentButton;
    private JButton settingsButton;
    private JToggleButton gridButton;
    private JToggleButton isolineButton;
    private JToggleButton interactiveModeButton;

    private boolean includeGridMode = false;
    private boolean includeIsolineMode = false;
    private boolean includeInteractiveMode = false;

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
     //   saveButton = addToolBarButton("File/Save as", "resources/saveIcon.gif", mainArea.getStatusBar());
        addToolBarSeparator();
        gridButton = addToolBarToggleButton("Edit/Draw grid", "resources/grid.gif", mainArea.getStatusBar());
        isolineButton = addToolBarToggleButton("Edit/Isoline", "resources/isoline.gif", mainArea.getStatusBar());
        settingsButton = addToolBarButton("Edit/Settings", "resources/settings.gif", mainArea.getStatusBar());
        interactiveModeButton = addToolBarToggleButton("Edit/Interactive mode", "resources/interactiveMode.gif", mainArea.getStatusBar());

        addToolBarSeparator();
        addToolBarButton("File/Exit", "resources/exit.gif", mainArea.getStatusBar());
    }

    private void createMenuBar() throws NoSuchMethodException {
        addSubMenu("File", 0);
        addMenuItem("File/Open", "Open a file", 0, "onOpenFile",  mainArea.getStatusBar());
        addMenuItem("File/New Document", "Вернуть все в начальное состояние", 0, "onNewDocument",  mainArea.getStatusBar());
       // addMenuItem("File/Save as", "Save the file", 0, "onSaveFile",  mainArea.getStatusBar());
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit", 0, "onExit",  mainArea.getStatusBar());

        addSubMenu("Edit", 0);
        addCheckBoxMenuItem("Edit/Draw grid", "Draw grid", 0, "onGrid",  mainArea.getStatusBar());
        addCheckBoxMenuItem("Edit/Isoline", "Draw isoline", 0, "onIsoline",  mainArea.getStatusBar());
        addCheckBoxMenuItem("Edit/Interactive mode", "Включить интерактивный режим рисования изолиний", 0, "onInteractiveMode",  mainArea.getStatusBar());
        addMenuItem("Edit/Settings", "Settings", 0, "onSettings",  mainArea.getStatusBar());


        addSubMenu("Help", 0);
        addMenuItem("Help/About", "о авторе", 0, "onAbout", mainArea.getStatusBar());

        //saveItem = (JMenuItem) super.getMenuElement("File/Save as");
        newDocumentItem = (JMenuItem) super.getMenuElement("File/New Document");
        gridItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Draw grid");
        isolineItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Isoline");
        interactiveModeItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Interactive mode");
        settingsItem = (JMenuItem) super.getMenuElement("Edit/Settings");
    }

    public void onOpenFile(){
        File file = this.getOpenFileName(Settings.EXTENSION, "Text file");
        Reader reader = new Reader(file);
        try {
            controller.handleNewFieldSize(mainArea.getWidth(), mainArea.height());
            //reader.readFile();
            info = reader.readFile();
            controller.handleOpenFile(info);
            openFile = true;
            initMenu();
        } catch (BadFileFormatException e) {
            errorMessage(this, e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void onNewDocument(){

    }

    public void onSaveFile(){

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

    public void onIsoline(){

        if(includeIsolineMode) {
            isolineItem.setSelected(false);
            isolineButton.setSelected(false);
            includeIsolineMode = false;
            controller.handleUnSelectDrawIsoline();
        }else{
            isolineItem.setSelected(true);
            isolineButton.setSelected(true);
            includeIsolineMode = true;
            controller.handleSelectDrawIsoline();
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

        newDocumentItem.setEnabled(openFile);
        settingsItem.setEnabled(openFile);
        gridItem.setEnabled(openFile);
        isolineItem.setEnabled(openFile);
        interactiveModeItem.setEnabled(openFile);


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
