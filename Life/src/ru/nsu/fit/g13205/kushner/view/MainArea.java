package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.model.Impact;
import ru.nsu.fit.g13205.kushner.view.setting.GameAreaProperties;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Konstantin on 20.02.2016.
 */
public class MainArea extends JPanel {
    private JLabel statusBarLabel;
    private GameArea gameArea;
    private JScrollPane scrollPane;
    private Controller controller;
    private GameAreaProperties properties;


    public MainArea(GameAreaProperties properties, Controller controller) {
        super();
        this.controller = controller;
        this.properties = properties;
        setLayout(new BorderLayout());
        this.add(createStatusBar(), BorderLayout.SOUTH);
        gameArea = createGameArea();
        scrollPane = new JScrollPane(gameArea);
        this.add(scrollPane, BorderLayout.CENTER);
        repaint();
    }

    public void closeStatusBar(){
        statusBarLabel.setVisible(false);
    }

    public void openStatusBar(){
        statusBarLabel.setVisible(true);
    }

    private GameArea createGameArea(){
        GameArea area = new GameArea(properties, controller);

        return area;
    }

    private JPanel createStatusBar(){
        Box bottomBox = Box.createHorizontalBox();

        JPanel tmpPanel = new JPanel();
        tmpPanel.setBackground(new Color(134, 151, 255));
        tmpPanel.setLayout(new BorderLayout());

        statusBarLabel = new JLabel("Status bar");
        statusBarLabel.setFont(MainWindow.font);


        bottomBox.add(statusBarLabel);
        bottomBox.add(Box.createHorizontalGlue());
        tmpPanel.add(bottomBox, BorderLayout.WEST);

        return tmpPanel;
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

    public void rePaint(){
        gameArea.update();
        this.repaint();
    }

    public void setColoredCell(int m, int n){
        gameArea.setColoredCell(m, n);
    }



    public JLabel getStatusBarLabel() {
        return statusBarLabel;
    }

    public void clearField(){
        gameArea.clearField();
    }

    public void saveFieldToFile(File file) throws FileNotFoundException {
        gameArea.saveFieldToFile(file);
    }

    public void updateSize(){
        gameArea.updateSize();
    }


    public void updateImpact(Impact[][] impact){
        gameArea.updateImpact(impact);
    }

    public void updateImpactStatus(boolean status){
        gameArea.updateStatusImpact(status);
    }


    public void lockPanel(){
        gameArea.lockPanel();
    }

    public void unLockPanel(){
        gameArea.unLockPanel();
    }

    public void updateSize(Impact[][] impacts) {

    }
}
