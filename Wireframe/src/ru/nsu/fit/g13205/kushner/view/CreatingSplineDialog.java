package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.model.Spline;
import ru.nsu.fit.g13205.kushner.utils.AreaProperties;
import ru.nsu.fit.g13205.kushner.utils.Coordinates;
import ru.nsu.fit.g13205.kushner.utils.Properties;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Konstantin on 15.04.2016.
 */
public class CreatingSplineDialog extends JDialog implements SplineDialogInterface {

    private final Controller controller;
    private Color color = new Color(255, 255, 0);


    private JButton acceptButton;
    private JButton okButton;
    private JButton zoomPlusButton;
    private JButton zoomMinusButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton colorButton;

    private JButton nextFigureButton;
    private JButton prevFigureButton;

    private JTextField figureNumberField;

    private int maxFigure = 0;


    private JSpinner nSpinner;
    private JSpinner mSpinner;
    private JSpinner kSpinner;
    private JSpinner aSpinner;
    private JSpinner bSpinner;
    private JSpinner cSpinner;
    private JSpinner dSpinner;


    private JSpinner cXSpinner;
    private JSpinner cYSpinner;
    private JSpinner cZSpinner;


    private JSpinner znSpinner;
    private JSpinner zfSpinner;
    private JSpinner swSpinner;
    private JSpinner shSpinner;

    private JCheckBox interactiveCheckBox;
    private JCheckBox autoImageChackBox;

    private ImagePanel imagePanel;

    private boolean changeFlag = true;

    public CreatingSplineDialog(Controller controller, MainWindow owner) {
        super();
        this.controller = controller;

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(imagePanel = new ImagePanel(controller, this), BorderLayout.NORTH);
        panel.add(createSettingPanel(), BorderLayout.CENTER);
        panel.add(createBottomBox(), BorderLayout.SOUTH);
        //this.setModal(true);
        addSpinnerListener();
        addButtonListener();
        this.getContentPane().add(panel);

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(owner);


    }

    private void addButtonListener(){
        CreatingSplineDialog owner = this;
        colorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                color = JColorChooser.showDialog(owner,
                        "Choose a color...", color);
                colorButton.setBackground(color);
                controller.handleUpdateColor(color);
            }
        });

        nextFigureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleSwitchOnNextFigure();
            }
        });

        prevFigureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleSwitchOnPrevFigure();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleAddFigure();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleDeleteFigure();
            }
        });
    }

    private void addSpinnerListener(){

        nSpinner.addChangeListener(propertiesChangeListener(nSpinner));
        mSpinner.addChangeListener(propertiesChangeListener(mSpinner));
        kSpinner.addChangeListener(propertiesChangeListener(kSpinner));

        aSpinner.addChangeListener(propertiesChangeListener(aSpinner));
        bSpinner.addChangeListener(propertiesChangeListener(bSpinner));
        cSpinner.addChangeListener(propertiesChangeListener(cSpinner));
        dSpinner.addChangeListener(propertiesChangeListener(dSpinner));


        cXSpinner.addChangeListener(propertiesChangeListener(cXSpinner));
        cYSpinner.addChangeListener(propertiesChangeListener(cYSpinner));
        cZSpinner.addChangeListener(propertiesChangeListener(cZSpinner));

        znSpinner.addChangeListener(areaPropertiesChangeListener(znSpinner));
        zfSpinner.addChangeListener(areaPropertiesChangeListener(zfSpinner));
        swSpinner.addChangeListener(areaPropertiesChangeListener(shSpinner));
        shSpinner.addChangeListener(areaPropertiesChangeListener(shSpinner));
    }

    private ChangeListener propertiesChangeListener(JSpinner spinner){
        return new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.handleUpdateProperties(getActualProperties());
            }
        };
    }

    private ChangeListener areaPropertiesChangeListener(JSpinner spinner){
        return new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.handleUpdateAreaProperties(getActualAreaProperties());
            }
        };
    }



    private Box createBottomBox(){

        Box totalBox = Box.createVerticalBox();

        Box switchFigureBox = Box.createHorizontalBox();

        switchFigureBox.add(createSwitchFigurePanel(prevFigureButton = new JButton(new ImageIcon("resources/ArrowLeft.gif")),
                nextFigureButton = new JButton(new ImageIcon("resources/ArrowRight.gif")), figureNumberField = new JTextField(), "#"));

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalStrut(15));
        buttonBox.add(okButton = new JButton("OK"));
        buttonBox.add(Box.createHorizontalStrut(15));
        /*buttonBox.add(zoomPlusButton = new JButton(new ImageIcon("resources/zoomPlus.gif")));
        buttonBox.add(Box.createHorizontalStrut(15));
        buttonBox.add(zoomMinusButton = new JButton(new ImageIcon("resources/zoomMinus.gif")));
        buttonBox.add(Box.createHorizontalStrut(15));*/
        buttonBox.add(addButton = new JButton(new ImageIcon("resources/add.gif")));
        buttonBox.add(Box.createHorizontalStrut(15));
        buttonBox.add(deleteButton = new JButton(new ImageIcon("resources/delete.gif")));
        buttonBox.add(Box.createHorizontalStrut(15));

        /*buttonBox.add(interactiveCheckBox = new JCheckBox("интерактивно"));
        buttonBox.add(Box.createHorizontalStrut(15));
        buttonBox.add(autoImageChackBox = new JCheckBox("автомаштаб"));
        buttonBox.add(Box.createHorizontalStrut(15));*/

        //interactiveCheckBox.setSelected(true);
        //autoImageChackBox.setSelected(true);

        totalBox.add(Box.createVerticalStrut(3));
        totalBox.add(switchFigureBox);
        totalBox.add(Box.createVerticalStrut(3));
        totalBox.add(buttonBox);
        totalBox.add(Box.createVerticalStrut(3));

        return totalBox;
    }

    private JPanel createSwitchFigurePanel(JButton leftButton, JButton rightButton, JTextField textField, String label){
        JPanel panel = new JPanel();

        Box totalBox = Box.createHorizontalBox();
        totalBox.add(Box.createHorizontalStrut(5));
        totalBox.add(new JLabel(label));
        totalBox.add(Box.createHorizontalStrut(5));
        totalBox.add(leftButton);
        totalBox.add(Box.createHorizontalStrut(5));
        totalBox.add(textField);
        textField.setColumns(3);
        totalBox.add(Box.createHorizontalStrut(5));
        totalBox.add(rightButton);
        totalBox.add(Box.createHorizontalStrut(5));

        panel.add(totalBox);
        return panel;
    }

    private JPanel createSettingPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,5));
        SpinnerModel model = new SpinnerNumberModel(0, 0, 1, 0.02);

        panel.add(namedField("n", nSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 40, 1)), 10));
        panel.add(namedField("m", mSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 40, 1)), 10));
        panel.add(namedField("k", kSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1)), 12));

        Box colorBox = Box.createVerticalBox();
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalStrut(18));
        colorButton = new JButton("Color");
        colorButton.setOpaque(true);
        colorButton.setBackground(color);

        colorButton.setMinimumSize(new Dimension(90, 25));
        colorButton.setMaximumSize(new Dimension(90, 25));



        box.add(colorButton);
        colorBox.add(Box.createVerticalStrut(4));
        colorBox.add(box);

        panel.add(colorBox);

        panel.add(namedField("cX", cXSpinner = new JSpinner(new SpinnerNumberModel(0, -100, 100, 0.5)), 9));

        panel.add(namedField("a", aSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1, 0.02)), 10));
        panel.add(namedField("b", bSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 1, 0.02)), 12));
        panel.add(namedField("c", cSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 6.28, 0.1)), 12));
        panel.add(namedField("d", dSpinner = new JSpinner(new SpinnerNumberModel(6.28, 0, 6.28, 0.1)), 11));
        panel.add(namedField("cY", cYSpinner = new JSpinner(new SpinnerNumberModel(0, -100, 100, 0.5)), 9));

        panel.add(namedField("zn", znSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 0.2)), 5));
        panel.add(namedField("zf", zfSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 0.2)), 9));
        panel.add(namedField("sw", swSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 0.2)), 4));
        panel.add(namedField("sh", shSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 0.2)), 6));
        panel.add(namedField("cZ", cZSpinner = new JSpinner(new SpinnerNumberModel(0, -100, 100, 0.5)), 10));

        return panel;
    }

    private Box namedField(String name, JSpinner spinner, int strutWidth){
        Box totalBox = Box.createVerticalBox();
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalStrut(strutWidth));
        box.add(new JLabel(name));
        box.add(Box.createHorizontalStrut(2));
        box.add(spinner);
        totalBox.add(Box.createVerticalStrut(2));
        totalBox.add(box);
        return totalBox;
    }

    private Properties getActualProperties(){
        return new Properties((int)nSpinner.getValue(), (int)mSpinner.getValue(), (int)kSpinner.getValue(),
                (double)aSpinner.getValue(), (double)bSpinner.getValue(),
                (double)cSpinner.getValue() == 6.28d ? 2 * Math.PI : (double)cSpinner.getValue(),
                (double)dSpinner.getValue() == 6.28d ? 2 * Math.PI : (double)dSpinner.getValue(),
                color,
                (double)cXSpinner.getValue(), (double)cYSpinner.getValue(), (double)cZSpinner.getValue());
    }

    private AreaProperties getActualAreaProperties(){
        return new AreaProperties((double) znSpinner.getValue(), (double) zfSpinner.getValue(),
                (double) swSpinner.getValue(),(double)shSpinner.getValue());
    }

    public JButton getOkButton() {
        return okButton;
    }

    public Properties getProperties(){
        return getActualProperties();
    }

    public void updateMainImage(){
        controller.handleUpdateMainImage(getActualProperties());
    }

    @Override
    public void updateProperties(Properties properties, AreaProperties areaProperties, int number){
        nSpinner.setValue(properties.getN());
        mSpinner.setValue(properties.getM());
        kSpinner.setValue(properties.getK());
        aSpinner.setValue(properties.getA());
        bSpinner.setValue(properties.getB());
        cSpinner.setValue(properties.getC());
        dSpinner.setValue(properties.getD());
        cXSpinner.setValue(properties.getcX());
        cYSpinner.setValue(properties.getcY());
        cZSpinner.setValue(properties.getcZ());
        colorButton.setBackground(properties.getColor());
        color = properties.getColor();

        znSpinner.setValue(areaProperties.getZn());
        zfSpinner.setValue(areaProperties.getZf());
        swSpinner.setValue(areaProperties.getSw());
        shSpinner.setValue(areaProperties.getSh());

        figureNumberField.setText(String.valueOf(number));

    }

    @Override
    public void updateImage(BufferedImage image, Spline spline) {
        imagePanel.updateImage(image, spline);
    }

    @Override
    public void updateImageAndDraggedPoint(BufferedImage image, Spline spline, Coordinates point) {
        imagePanel.updateImageAndDraggedPoint(image, spline, point);
    }

    @Override
    public void setMaxFigure(int number, Color color) {
        maxFigure = number;
    }

    @Override
    public void setCurrentNumberFigure(int number, Color color) {

        changeFlag = false;
        changeFlag = true;
    }
}
