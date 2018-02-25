package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.SettingApplication;
import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.model.Impact;
import ru.nsu.fit.g13205.kushner.model.LifeModelListener;
import ru.nsu.fit.g13205.kushner.view.setting.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Konstantin on 20.02.2016.
 */
public class MainWindow extends MyMainFrame implements  LifeModelListener {

    public final static Font font = new Font("Verdana", Font.PLAIN, 11);

    private Timer timer;

    private int width;
    private int height;
    private final MainArea mainArea;

    private String windowTitle;

    private JToggleButton xorButton;
    private JToggleButton replaceButton;
    private JToggleButton impactButton;
    private JToggleButton runButton;
    private JButton fileOpenButton;
    private JButton fileSaveButton;
    private JButton newFileButton;
    private JButton stepButton;
    private JButton clearButton;
    private JButton editSettingButton;
    private JButton gameSettingButton;

    private JCheckBoxMenuItem xorItem;
    private JCheckBoxMenuItem replaceItem;
    private JCheckBoxMenuItem impactItem;
    private JCheckBoxMenuItem runItem;
    private JMenuItem fileOpenItem;
    private JMenuItem fileSaveItem;
    private JMenuItem newFileItem;
    private JMenuItem stepItem;
    private JMenuItem clearItem;
    private JMenuItem editSettingItem;
    private JMenuItem gameSettingItem;

    private boolean lockImpact = false;
    private int cellWidth = Math.round(Math.round(SettingApplication.CELL_DEFAULT_SIZE * Math.sqrt(3)));
    private int maxWidthImpact = String.format("%.1f", 6 * SettingApplication.FST_IMPACT + 6 * SettingApplication.SND_IMPACT).length() *
            SettingApplication.FONT_WIDTH * 4;


    private double lifeBegin = SettingApplication.LIFE_BEGIN;
    private double lifeEnd = SettingApplication.LIFE_END;
    private double birthBegin = SettingApplication.BIRTH_BEGIN;
    private double birthEnd = SettingApplication.BIRTH_END;
    private double FSTImpact = SettingApplication.FST_IMPACT;
    private double SNDImpact = SettingApplication.SND_IMPACT;

    private int m = SettingApplication.M_DEFAULT_VALUE;
    private int n = SettingApplication.N_DEFAULT_VALUE;
    private int cellSize = SettingApplication.CELL_DEFAULT_SIZE;
    private int lineWidth = SettingApplication.LINE_DEFAULT_WIDTH;
    private int mode = SettingApplication.DEFAULT_MODE;

    private File currentFile;
    private GameAreaProperties properties;
    private Controller controller;

    private boolean showImpact = false;


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
        this.width = width;
        this.height = height;
        this.windowTitle = windowTitle;
        setLocationRelativeTo(null);

        this.properties = new GameAreaProperties(m, n, cellSize, mode, lineWidth);

        this.mainArea = new MainArea(properties, controller);
        try {
            createMenuBar();
        } catch (NoSuchMethodException e) {
            //    e.printStackTrace();
        }
        this.add(mainArea);
        createToolBar();
        initMainWindow();
    }

    private void createMenuBar() throws NoSuchMethodException {
        //file sub menu
        addSubMenu("File", 0);
        addMenuItem("File/New", "Create a new file", 0, "onNewFile",  mainArea.getStatusBarLabel());
        addMenuItem("File/Open", "Open an existing file", 0, "onOpenFile" , mainArea.getStatusBarLabel());
        addMenuItem("File/Save", "Save the active file", 0, "onSaveFile", mainArea.getStatusBarLabel());
        addMenuItem("File/Save as", "Save the active file with a new name", 0, "onSaveAsFile", mainArea.getStatusBarLabel());
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit", 0, "onExit", mainArea.getStatusBarLabel());

        //edit sub menu
        addSubMenu("Edit", 0);
        addCheckBoxMenuItem("Edit/Replace", "set replace paint mode", 0, "onReplace", mainArea.getStatusBarLabel());
        addCheckBoxMenuItem("Edit/Xor", "set XOR paint mode", 0, "onXor", mainArea.getStatusBarLabel());
        addMenuItem("Edit/Clear", "clear map", 0, "onClear", mainArea.getStatusBarLabel());


        addSubMenu("Edit/Setting", 0);
        addMenuItem("Edit/Setting/Edit settings", "change editing setting", 0, "onEditSetting", mainArea.getStatusBarLabel());
        addMenuItem("Edit/Setting/Game settings", "change gaming setting", 0, "onGameSetting", mainArea.getStatusBarLabel());

        //view sub menu
        addSubMenu("View", 0);
        addCheckBoxMenuItem("View/Toolbar", "show/hide toolbar", 0, "onShowTollBar", mainArea.getStatusBarLabel());
        addCheckBoxMenuItem("View/Status bar", "show/hide status bar", 0, "onShowStatusBar", mainArea.getStatusBarLabel());
        addCheckBoxMenuItem("View/Display Impact Value", "Display impact value for each cell", 0, "onImpact", mainArea.getStatusBarLabel());

        //simulation sub menu
        addSubMenu("Simulation", 0);
        addCheckBoxMenuItem("Simulation/Run", "run continous simulation", 0, "onRun", mainArea.getStatusBarLabel());
        addMenuItem("Simulation/Step", "simulation one step", 0, "onStep", mainArea.getStatusBarLabel());
        addMenuItem("Simulation/Stop", "stop simulation", 0, "onStop", mainArea.getStatusBarLabel());

        //help sub menu
        addSubMenu("Help", 0);
        //addMenuItem("Help/Rules", "rules", 0, "onRules");
        addMenuItem("Help/Help", "help", 0, "onHelp", mainArea.getStatusBarLabel());

        //about sub menu
        addSubMenu("About", 0);
        addMenuItem("About/About", "about author", 0, "onAbout", mainArea.getStatusBarLabel());
    }

    private void initMainWindow(){
        JMenuItem tmp = (JMenuItem) super.getMenuElement("View/Toolbar");
        tmp.setSelected(true);

        tmp = (JMenuItem) super.getMenuElement("View/Status bar");
        tmp.setSelected(true);

        xorItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Xor");
        replaceItem = (JCheckBoxMenuItem) super.getMenuElement("Edit/Replace");
        impactItem = (JCheckBoxMenuItem) super.getMenuElement("View/Display Impact Value");
        runItem = (JCheckBoxMenuItem) super.getMenuElement("Simulation/Run");
        fileOpenItem = (JMenuItem)super.getMenuElement("File/Open");
        fileSaveItem = (JMenuItem)super.getMenuElement("File/Save");
        newFileItem = (JMenuItem)super.getMenuElement("File/New");
        stepItem = (JMenuItem)super.getMenuElement("Simulation/Step");
        clearItem = (JMenuItem)super.getMenuElement("Edit/Clear");
        editSettingItem = (JMenuItem)super.getMenuElement("Edit/Setting/Edit settings");
        gameSettingItem = (JMenuItem)super.getMenuElement("Edit/Setting/Game settings");

        if(mode == 1){
            replaceButton.setSelected(true);
            replaceItem.setSelected(true);
            xorButton.setSelected(false);
            xorButton.setSelected(false);
        }else {
            replaceButton.setSelected(false);
            replaceItem.setSelected(false);
            xorButton.setSelected(true);
            xorItem.setSelected(true);
        }

    }

    public void createToolBar(){
        fileOpenButton = addToolBarButton("File/Open", "resources/openFile-Folder.gif", mainArea.getStatusBarLabel());
        fileSaveButton = addToolBarButton("File/Save", "resources/save.gif", mainArea.getStatusBarLabel());
        newFileButton = addToolBarButton("File/New", "resources/createNewDocument.gif", mainArea.getStatusBarLabel());
        impactButton = addToolBarToggleButton("View/Display Impact Value", "resources/showImpact.gif", mainArea.getStatusBarLabel());
        xorButton = addToolBarToggleButton("Edit/Xor", "resources/xor.gif", mainArea.getStatusBarLabel());

        replaceButton = addToolBarToggleButton("Edit/Replace", "resources/replace.gif", mainArea.getStatusBarLabel());
        clearButton = addToolBarButton("Edit/Clear", "resources/clear.gif", mainArea.getStatusBarLabel());
        editSettingButton = addToolBarButton("Edit/Setting/Edit settings", "resources/settings.gif", mainArea.getStatusBarLabel());
        gameSettingButton = addToolBarButton("Edit/Setting/Game settings", "resources/gameSetting.gif", mainArea.getStatusBarLabel());
        runButton = addToolBarToggleButton("Simulation/Run", "resources/run.gif", mainArea.getStatusBarLabel());
        stepButton = addToolBarButton("Simulation/Step", "resources/step.gif", mainArea.getStatusBarLabel());
        addToolBarButton("Simulation/Stop", "resources/stop.gif", mainArea.getStatusBarLabel());
        addToolBarButton("Help/Help", "resources/help.gif", mainArea.getStatusBarLabel());
        addToolBarButton("About/About", "resources/about.gif", mainArea.getStatusBarLabel());
    }


    public void onNewFile() {
        Object[] options = {"Yes, save", "Yes, save as", "No"};
        int n = JOptionPane.showOptionDialog(this, "do you want save file", "",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if(n == 0){
            onSaveFile();
        }
        if(n == 1){
            onSaveAsFile();
        }
        currentFile = null;
        onClear();
        onEditSetting();
    }

    public void onOpenFile() {
        Object[] options = {"Yes, save", "Yes, save as", "No"};
        int n = JOptionPane.showOptionDialog(this, "do you want save file", "",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if(n == 0){
            onSaveFile();
        }
        if(n == 1){
            onSaveAsFile();
        }
        File file = this.getOpenFileName(SettingApplication.EXTENSION, "Text file");
        try {
            currentFile = file;
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(file));
            }catch (NullPointerException e){
                //e.printStackTrace();
                return;
            }
            String str = reader.readLine();
            String[] tmp = str.split(" ");
            int countActiveCell;

            properties.setM(Integer.valueOf(tmp[0]));
            properties.setN(Integer.valueOf(getStr(tmp[1])));
            properties.setWidthBorder(Integer.valueOf(getStr(reader.readLine())));
            properties.setSize(Integer.valueOf(getStr(reader.readLine())));
            countActiveCell = Integer.valueOf(getStr(reader.readLine()));


            mainArea.clearField();
            mainArea.updateSize();
            controller.handleChangeSize(properties.getM(), properties.getN());

            //boolean[][] coloredCells = new boolean[properties.getN()][properties.getM()];
            for(int i = 0; i < countActiveCell; i++){
                str = reader.readLine();
                tmp = str.split("/")[0].split(" ");
                //coloredCells[Integer.valueOf(getStr(tmp[1]))][Integer.valueOf(tmp[0])] = true;
                mainArea.setColoredCell(Integer.valueOf(tmp[0]), Integer.valueOf(getStr(tmp[1])));
                controller.handleChangeStatusCell(Integer.valueOf(tmp[0]), Integer.valueOf(getStr(tmp[1])), true);
            }
            cellWidth = Math.round(Math.round(properties.getSize() * Math.sqrt(3)));
            if(maxWidthImpact > cellWidth){
                lockImpact = true;
                onImpact();
                mainArea.rePaint();
            }else{
                if(lockImpact){
                    lockImpact = false;
                    onImpact();
                }
                mainArea.rePaint();
            }

            mainArea.rePaint();

        } catch (NumberFormatException e){
            properties.setM(SettingApplication.M_DEFAULT_VALUE);
            properties.setN(SettingApplication.N_DEFAULT_VALUE);
            properties.setWidthBorder(SettingApplication.LINE_DEFAULT_WIDTH);
            properties.setSize(SettingApplication.CELL_DEFAULT_SIZE);
            mainArea.clearField();
            mainArea.rePaint();
            errorMessage(this, "неверный формат файла");
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            errorMessage(this, "Неверный формат файла");
        }catch (IndexOutOfBoundsException e){
            errorMessage(this, "некорректные данные в файле\nКоордината точки выходит за пределы заданных ранее границ поля");
        }catch (NullPointerException e){
            errorMessage(this, "Неверный формат файла\nФайл закончился раньше времени");
        }
    }

    private String getStr(String str){
        if(str.contains("/")){
            str = str.split("/")[0];
            if(str.contains("")){
                str = str.split(" ")[0];
            }
        }
        return str;
    }

    private void errorMessage(JFrame owner, String message){
        Object[] options = {"OK"};
        JOptionPane.showOptionDialog(owner,
                String.format("%s", message), "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    }

    public void onSaveFile(){
        try{
            mainArea.saveFieldToFile(currentFile);
        }catch (NullPointerException exp){
            onSaveAsFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSaveAsFile(){

        currentFile = this.getSaveFileName(SettingApplication.EXTENSION, "Text file");

        try {
            mainArea.saveFieldToFile(currentFile);
        } catch (FileNotFoundException | NullPointerException e) {
            //e.printStackTrace();
        }
    }

    public void onExit() {

        Object[] options = {"Yes, save", "Yes, save as", "Cancel", "Exit"};
        int n = JOptionPane.showOptionDialog(this, "do you want save file", "",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[3]);

        if(n == 0){
            onSaveFile();
        }
        if(n == 1){
            onSaveAsFile();
        }


        if(n == 2){
            return;
        }
        if(n == 3) {
            System.exit(0);
        }
    }

    public void onXor(){
        mode = 2;
        xorButton.setSelected(true);
        replaceButton.setSelected(false);
        properties.setMode(2);
    }

    public void onReplace(){
        mode = 1;
        replaceButton.setSelected(true);
        xorButton.setSelected(false);
        properties.setMode(1);
    }

    public void onClear(){
        mainArea.clearField();
        mainArea.rePaint();
        mainArea.clearField();
    }

    public void onEditSetting(){
        JDialog frame;
        frame = new JDialog(this, "Edit settings", true);
        EditSettingPanel propertiesPanel = new EditSettingPanel(getActualEditSettings());
        JButton okButton = propertiesPanel.getOkButton();
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    EditSetting setting = propertiesPanel.getPropertiesPanelInfo();
                    m = setting.getM();
                    n = setting.getN();
                    mode = setting.getMode();
                    cellSize = setting.getCellSize();
                    lineWidth = setting.getLineWidth();
                    setActualEditProperties();
                    mainArea.updateSize();
                    if(mode == 1){
                        replaceButton.setSelected(true);
                        xorButton.setSelected(false);
                    }else{
                        replaceButton.setSelected(false);
                        xorButton.setSelected(true);
                    }
                } catch(IllegalArgumentException | NegativeArraySizeException exp1){
                    return;
                }
                cellWidth = Math.round(Math.round(cellSize * Math.sqrt(3)));
                if(maxWidthImpact > cellWidth){
                    lockImpact = true;
                    onImpact();
                    mainArea.rePaint();
                }else{
                    if(lockImpact){
                        lockImpact = false;
                        onImpact();
                    }
                    mainArea.rePaint();
                }

                controller.handleChangeSize(m, n);
                frame.setVisible(false);

            }
        });

        JButton cancelButton = propertiesPanel.getCancelButton();
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });

        frame.getContentPane().add(propertiesPanel);
        frame.setLocationRelativeTo(this);
        frame.pack();
        frame.setVisible(true);
    }

    private boolean checkValue(double value, JDialog owner) {
        String tmp = String.valueOf(value);
        int length;
        if (tmp.contains(".")) {
            length = tmp.split("\\.")[1].length();
        } else {
            length = 0;
        }
        String message = "";
        try {
            if (value < 0) {
                return false;

            } else if (length > 1) {
                return false;
            }
        } catch (NumberFormatException exp) {
            return false;
        }
        return true;
    }

    public void onGameSetting(){
        JDialog frame;
        frame = new JDialog(this, "Game settings", true);
        GameSettingsPanel settingPanel = new GameSettingsPanel(new GameSetting(lifeBegin, lifeEnd, birthBegin, birthEnd,
                FSTImpact, SNDImpact));
        JButton okButton = settingPanel.getOkButton();
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameSetting setting;
                try{
                    setting = settingPanel.getSetting();
                    if(!checkValue(lifeBegin = setting.getLifeBegin(), frame)){
                        return;
                    }
                    if (!checkValue(lifeEnd = setting.getLifeEnd(), frame)) {
                        return;
                    }

                    if (!checkValue(birthBegin = setting.getBirthBegin(), frame)) {
                        return;
                    }
                    if (!checkValue(birthEnd = setting.getBirthEnd(), frame)) {
                        return;
                    }
                    if (!checkValue(SNDImpact = setting.getSNDImpact(), frame)) {
                        return;
                    }
                    if (!checkValue(FSTImpact = setting.getFSTImpact(), frame)) {
                        return;
                    }
                } catch(IllegalArgumentException | NegativeArraySizeException exp1){
                    return;
                }
                double maxImpact = 6 * FSTImpact + 6 * SNDImpact;
                maxWidthImpact = String.format("%.1f", maxImpact).length() * SettingApplication.FONT_WIDTH * 4;

                if(maxWidthImpact > cellWidth){
                    lockImpact = true;
                    onImpact();
                }else{
                    if(lockImpact){
                        lockImpact = false;
                        onImpact();
                    }
                }

                controller.handleNewGameSetting(setting);
                frame.setVisible(false);
            }
        });

        JButton cancelButton = settingPanel.getCancelButton();
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });

        frame.getContentPane().add(settingPanel);
        frame.setLocationRelativeTo(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void onShowTollBar(){
        JCheckBoxMenuItem tmp = (JCheckBoxMenuItem) super.getMenuElement("View/Toolbar");
        if(tmp.isSelected()){
            toolBar.setVisible(true);
        }else{
            toolBar.setVisible(false);
        }

    }

    public void onShowStatusBar(){
        JCheckBoxMenuItem tmp = (JCheckBoxMenuItem) super.getMenuElement("View/Status bar");
        if(tmp.isSelected()){
            mainArea.openStatusBar();
        }else{
            mainArea.closeStatusBar();
        }
    }

    public void onImpact(){
        if(lockImpact){
            impactButton.setSelected(false);
            impactItem.setSelected(false);
            impactButton.setEnabled(false);
            impactItem.setEnabled(false);
            mainArea.updateImpactStatus(false);
            showImpact = false;
            return;
        }else{
            impactButton.setEnabled(true);
            impactItem.setEnabled(true);
        }
        try {
            if (showImpact && !lockImpact) {
                impactButton.setSelected(false);
                impactItem.setSelected(false);
                mainArea.updateImpactStatus(false);
                showImpact = false;
            } else {
                impactButton.setSelected(true);
                impactItem.setSelected(true);
                mainArea.updateImpactStatus(true);
                showImpact = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onRun(){

        mainArea.lockPanel();

        fileOpenButton.setEnabled(false);
        fileSaveButton.setEnabled(false);
        newFileButton.setEnabled(false);
        stepButton.setEnabled(false);
        clearButton.setEnabled(false);
        editSettingButton.setEnabled(false);
        gameSettingButton.setEnabled(false);
        xorButton.setEnabled(false);
        replaceButton.setEnabled(false);



        fileOpenItem.setEnabled(false);
        fileSaveItem.setEnabled(false);
        newFileItem.setEnabled(false);
        stepItem.setEnabled(false);
        clearItem.setEnabled(false);
        editSettingItem.setEnabled(false);
        gameSettingItem.setEnabled(false);

        xorItem.setEnabled(false);
        replaceItem.setEnabled(false);

        runButton.setEnabled(false);
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleStepAction();
            }
        });
        timer.start();
        //controller.handleStartAction();
    }

    public void onStep(){
        controller.handleStepAction();
    }

    public void onStop(){
        mainArea.unLockPanel();

        fileOpenItem.setEnabled(true);
        fileSaveItem.setEnabled(true);
        newFileItem.setEnabled(true);
        stepItem.setEnabled(true);
        clearItem.setEnabled(true);
        editSettingItem.setEnabled(true);
        gameSettingItem.setEnabled(true);
        runButton.setEnabled(true);
        xorItem.setEnabled(true);
        replaceItem.setEnabled(true);

        mainArea.setEnabled(true);

        fileOpenButton.setEnabled(true);
        fileSaveButton.setEnabled(true);
        newFileButton.setEnabled(true);
        stepButton.setEnabled(true);
        clearButton.setEnabled(true);
        editSettingButton.setEnabled(true);
        gameSettingButton.setEnabled(true);
        xorButton.setEnabled(true);
        replaceButton.setEnabled(true);

        runButton.setSelected(false);
        timer.stop();
    }

    public void onHelp(){
        JOptionPane.showMessageDialog(this, SettingApplication.HELP_INFORMATION);
    }

    public void onAbout(){
        JOptionPane.showMessageDialog(this, SettingApplication.ABOUT_MESSAGE);
    }


    private EditSetting getActualEditSettings(){
        return new EditSetting(properties.getM(), properties.getN(), properties.getSize(), properties.getWidthBorder(), properties.getMode());
    }

    private void setActualEditProperties(){

        properties.setM(m);
        properties.setN(n);
        properties.setMode(mode);
        properties.setWidthBorder(lineWidth);
        properties.setSize(cellSize);
    }


    @Override
    public void updateImpact(Impact[][] impacts) {
        mainArea.updateImpact(impacts);
    }

    @Override
    public void updateSize(Impact[][] impacts) {

    }
}
