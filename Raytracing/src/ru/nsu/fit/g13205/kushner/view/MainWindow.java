package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.model.ModelListener;
import ru.nsu.fit.g13205.kushner.utils.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Konstantin on 14.05.2016.
 */
public class MainWindow extends MyMainFrame implements ModelListener{

    private final Controller controller;
    private final WindowPanel windowPanel;

    private JButton settingsRenderButton;
    private JMenuItem settingsRenderMenuItem;

    private JMenuItem openSceneFileMenuItem;
    private JButton openSceneButton;

    private JMenuItem initMenuItem;
    private JButton initButton;

    private JMenuItem renderMenuItem;
    private JButton renderButton;

    private JMenuItem wireframeMenuItem;
    private JButton wireframeButton;

    private JMenuItem openRenderFileMenuItem;
    private JButton openRenderFileButton;

    private JMenuItem saveMenuItem;
    private JButton saveButton;

    private JMenuItem saveImageMenuItem;
    private JButton saveImage;

    private DialogProperties properties;

    private boolean rendering = false;

    public MainWindow(int x, int y, String title, Controller controller) {
        super(x, y, title);
        this.controller = controller;
        this.windowPanel = new WindowPanel(controller);
        this.add(new JScrollPane(windowPanel));
        try {
            createMenuBar();
            createToolBar();
            saveImage.setEnabled(false);
            saveImageMenuItem.setEnabled(false);
            saveButton.setEnabled(false);
            saveMenuItem.setEnabled(false);
            initButton.setEnabled(false);
            initMenuItem.setEnabled(false);
            settingsRenderButton.setEnabled(false);
            settingsRenderMenuItem.setEnabled(false);
            renderButton.setEnabled(false);
            renderMenuItem.setEnabled(false);
            wireframeButton.setEnabled(false);
            wireframeMenuItem.setEnabled(false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    private void createMenuBar() throws NoSuchMethodException {

        addSubMenu("File", 0);
        addMenuItem("File/Open", "загрузить файл сцены", 0, "onOpenSceneFile",  windowPanel.getStatusBarLabel());
        addMenuItem("File/Render setting", "загрузить настройки рендеринга", 0, "onLoadRenderSettings",  windowPanel.getStatusBarLabel());
        addMenuItem("File/Save render settings", "сохранииь настройки рендеринга", 0, "onSave",  windowPanel.getStatusBarLabel());
        addMenuItem("File/Save image", "сохранить изображение в формате bmp", 0, "onSaveImage",  windowPanel.getStatusBarLabel());

        addSubMenu("Edit", 0);
        addMenuItem("Edit/Settings", "настройки рендеринга", 0, "onRenderSettings",  windowPanel.getStatusBarLabel());
        addMenuItem("Edit/Init", "положение по умолчанию", 0, "onInit",  windowPanel.getStatusBarLabel());
        addMenuItem("Edit/Render", "отрендерить", 0, "onRender",  windowPanel.getStatusBarLabel());
        addMenuItem("Edit/Select view", "режим выбора ракурса", 0, "onWireframe",  windowPanel.getStatusBarLabel());

        openSceneFileMenuItem = (JMenuItem) super.getMenuElement("File/Open");
        saveMenuItem = (JMenuItem) super.getMenuElement("File/Save render settings");
        saveImageMenuItem = (JMenuItem) super.getMenuElement("File/Save image");


        openRenderFileMenuItem = (JMenuItem) super.getMenuElement("Edit/Render setting");
        settingsRenderMenuItem = (JMenuItem) super.getMenuElement("Edit/Settings");
        initMenuItem = (JMenuItem) super.getMenuElement("Edit/Init");
        renderMenuItem = (JMenuItem) super.getMenuElement("Edit/Render");
        wireframeMenuItem = (JMenuItem) super.getMenuElement("Edit/Select view");
    }

    private void createToolBar(){
        openSceneButton = addToolBarButton("File/Open", "icons/openFile.gif", windowPanel.getStatusBarLabel());
        openRenderFileButton = addToolBarButton("File/Render setting", "icons/loadRenderSettings.gif", windowPanel.getStatusBarLabel());

        saveButton = addToolBarButton("File/Save render settings", "icons/save.gif", windowPanel.getStatusBarLabel());
        saveImage = addToolBarButton("File/Save image", "icons/saveImage.gif", windowPanel.getStatusBarLabel());

        settingsRenderButton = addToolBarButton("Edit/Settings", "icons/settings.gif", windowPanel.getStatusBarLabel());
        initButton = addToolBarButton("Edit/Init", "icons/reset.gif", windowPanel.getStatusBarLabel());
        renderButton = addToolBarButton("Edit/Render", "icons/render.gif", windowPanel.getStatusBarLabel());
        wireframeButton = addToolBarButton("Edit/Select view", "icons/wireframe.gif", windowPanel.getStatusBarLabel());
    }

    public void onInit(){
        controller.pushOnInitButton();
    }

    public void onRenderSettings(){
        controller.getProperties();

        try {
            RenderDialog dialog = new RenderDialog(controller, this, properties);
            dialog.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onSaveImage(){
        File file = this.getSaveFileName(Settings.IMAGE_EXTENSION, "bmp");
        try {
            controller.saveImage(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onLoadRenderSettings(){
        File file = this.getOpenFileName(Settings.RENDER_EXTENSION, "Scene file");
        try {
            RenderFileReader reader = new RenderFileReader();
            RenderInfo fileInfo = reader.read(file);
            controller.openRenderFile(fileInfo);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void onSave(){
        File file = this.getSaveFileName(Settings.RENDER_EXTENSION, "render");
        try {
            controller.saveRenderFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onWireframe(){
        controller.showWireframe();
        wireframeButton.setSelected(true);
        wireframeMenuItem.setSelected(true);

        renderButton.setSelected(false);
        renderMenuItem.setSelected(false);
    }

    public void onRender(){
        rendering = true;
        controller.renderScene();

    }

    public void onOpenSceneFile(){
        File file = this.getOpenFileName(Settings.SCENE_EXTENSION, "Scene file");
        String path = file.getAbsolutePath();
        path = path.replaceAll(file.getName(), "");

        String name = file.getName();

        String renderName = name.substring(0, name.lastIndexOf('.')) + "." + Settings.RENDER_EXTENSION;
        String pathToRenderFile = path + renderName;

        File f1 = new File(path);

        boolean flag = false;

        RenderInfo renderFileInfo = null;

        File fileRender = new File(pathToRenderFile);
        for(File f: f1.listFiles()){
            if(f.equals(fileRender)){
                RenderFileReader reader = new RenderFileReader();
                renderFileInfo = null;
                try {
                    renderFileInfo = reader.read(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //controller.openRenderFile(fileInfo);
                flag = true;

                break;
            }
        }

        try {
            SceneFileReader reader = new SceneFileReader();
            SceneFileInfo sceneFileInfo = reader.read(file);
            if(flag){
                controller.openSceneFileWithRender(sceneFileInfo, renderFileInfo);
            }else {
                controller.openSceneFile(sceneFileInfo);
            }
            saveImage.setEnabled(true);
            saveImageMenuItem.setEnabled(true);
            saveButton.setEnabled(true);
            saveMenuItem.setEnabled(true);
            initButton.setEnabled(true);
            initMenuItem.setEnabled(true);
            settingsRenderButton.setEnabled(true);
            settingsRenderMenuItem.setEnabled(true);
            renderButton.setEnabled(true);
            renderMenuItem.setEnabled(true);
            wireframeButton.setEnabled(true);
            wireframeMenuItem.setEnabled(true);

            wireframeMenuItem.setSelected(true);
            wireframeButton.setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateImage(BufferedImage image) {
        windowPanel.updateImage(image);
    }

    @Override
    public void windowPanelRepaint() {
        if(rendering){
            renderButton.setSelected(true);
            renderMenuItem.setSelected(true);
            wireframeButton.setSelected(false);
            wireframeMenuItem.setSelected(false);
        }
        windowPanel.repaint();
    }

    @Override
    public void updateProperties(DialogProperties properties) {
        this.properties = properties;
    }

    @Override
    public void setUpdatable(boolean isUpdatable) {
        windowPanel.setUpdatable(isUpdatable);
    }

    @Override
    public void setStatusBarText(String text) {
        windowPanel.setStatusBarText(text);
    }
}
