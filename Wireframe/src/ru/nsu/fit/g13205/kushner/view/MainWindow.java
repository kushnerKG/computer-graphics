package ru.nsu.fit.g13205.kushner.view;

import com.sun.org.apache.regexp.internal.RE;
import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.model.ModelListener;
import ru.nsu.fit.g13205.kushner.model.Spline;
import ru.nsu.fit.g13205.kushner.utils.Coordinates;
import ru.nsu.fit.g13205.kushner.utils.FileInfo;
import ru.nsu.fit.g13205.kushner.utils.Properties;
import ru.nsu.fit.g13205.kushner.utils.Reader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Konstantin on 09.04.2016.
 */
public class MainWindow extends MyMainFrame implements ModelListener {

    private Controller controller;
    private WindowPanel windowPanel;

    private JMenuItem settingsMenuItem;
    private JMenuItem saveMenuItem;

    private JButton settingsButton;
    private JButton saveButton;
    private JButton openButton;
    private JButton resetButton;

    public MainWindow(int x, int y, String title, Controller controller) {
        super(x, y, title);
        this.controller = controller;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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

        windowPanel = new WindowPanel(controller);
        try {
            createMenuBar();
            createToolBar();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.add(windowPanel);

        init(false);

    }

    private void init(boolean v){
        saveButton.setEnabled(v);
        saveMenuItem.setEnabled(v);
    }

    private void createMenuBar() throws NoSuchMethodException {
        addSubMenu("File", 0);
        addMenuItem("File/Open", "Open a file", 0, "onOpenFile",  windowPanel.getStatusBarLabel());
        addMenuItem("File/Save", "Sace scene", 0, "onSave", windowPanel.getStatusBarLabel());

        addSubMenu("Edit", 0);
        addMenuItem("Edit/Settings", "Setting", 0, "onSetting",  windowPanel.getStatusBarLabel());
        addMenuItem("Edit/Reset", "Reset", 0, "onReset",  windowPanel.getStatusBarLabel());

        settingsMenuItem = (JMenuItem) super.getMenuElement("Edit/Settings");
        saveMenuItem = (JMenuItem) super.getMenuElement("File/Save");
    }

    private void createToolBar(){
        addToolBarButton("File/Open", "resources/openFile.gif", windowPanel.getStatusBarLabel());

        saveButton = addToolBarButton("File/Save", "resources/save.gif", windowPanel.getStatusBarLabel());


        settingsButton = addToolBarButton("Edit/Settings", "resources/settings.gif", windowPanel.getStatusBarLabel());
        addToolBarButton("Edit/Reset", "resources/reset.gif", windowPanel.getStatusBarLabel());

    }

    @Override
    public void WindowPanelRepaint(){
        windowPanel.repaint();
    }

    @Override
    public void updateImage(BufferedImage image, Spline spline) {
        windowPanel.updateImage(image);
    }

    @Override
    public void updateImageAndDraggedPoint(BufferedImage image, Spline spline, Coordinates point) {
    }

    public void onReset(){
        controller.handleReset();
    }

    public void onSave(){
        File file = this.getSaveFileName(Settings.EXTENSION, "Text file");
        try {
            controller.handleSave(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onOpenFile(){
        File file = this.getOpenFileName(Settings.EXTENSION, "Text file");
        try {
            Reader reader = new Reader(file);
            FileInfo fileInfo = reader.read();
            controller.handleOpenFile(fileInfo);
            init(true);
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    public void onSetting(){
        settingsButton.setEnabled(false);
        settingsMenuItem.setEnabled(false);
        CreatingSplineDialog dialog = new  CreatingSplineDialog(controller, this);
        controller.handleOpenDialog(dialog);
        init(true);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                controller.handleCloseDialog();
                settingsButton.setEnabled(true);
                settingsMenuItem.setEnabled(true);
            }
        });

        dialog.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Properties properties = dialog.getProperties();
                settingsButton.setEnabled(true);
                settingsMenuItem.setEnabled(true);
                controller.handleOkButton();
                dialog.setVisible(false);

            }
        });

        dialog.setVisible(true);
    }

    public void onExit(){
        System.exit(0);
    }

}
