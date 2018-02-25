package ru.nsu.fit.g13205.kushner.view;

import ru.nsu.fit.g13205.kushner.controller.Controller;
import ru.nsu.fit.g13205.kushner.utils.DialogProperties;
import ru.nsu.fit.g13205.kushner.utils.Properties;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Konstantin on 16.05.2016.
 */
public class RenderDialog extends JDialog {

    private Color color = new Color(0,0,0);
    private Controller controller;

    private JButton colorButton;
    private JButton okButton;
    private JButton closeDialogButton;


    private JSpinner gammaSpinner;
    private JSpinner depthSpinner;
    private JSpinner qualitySpinner;

    private JSpinner znSpinner;
    private JSpinner zfSpinner;
    private JSpinner swSpinner;
    private JSpinner shSpinner;


    private JRadioButton rougthButton;
    private JRadioButton normalButton;
    private JRadioButton fineButton;
    private ButtonGroup bGroup;

    private DialogProperties initialProperties;

    public RenderDialog(Controller controller, JFrame owner, DialogProperties initialProperties) {
        super();

        color = initialProperties.getBackgroundColor();
        this.initialProperties = initialProperties;
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createPanel(initialProperties), BorderLayout.CENTER);
        panel.add(createButtonPane(), BorderLayout.SOUTH);

        this.controller = controller;

        addButtonListener();
        addSpinerListener();
        this.getContentPane().add(panel);
        this.setResizable(false);
        this.pack();
        this.setModal(true);
        this.setLocationRelativeTo(owner);
    }

    private void addSpinerListener(){
        znSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.changeProperties(getActualProperties());
            }
        });

        zfSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.changeProperties(getActualProperties());
            }
        });

        swSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.changeProperties(getActualProperties());
            }
        });

        shSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.changeProperties(getActualProperties());
            }
        });
    }

    private Properties getActualProperties(){
        return new Properties((double)znSpinner.getValue(), (double)zfSpinner.getValue(),
                (double)swSpinner.getValue(), (double)shSpinner.getValue());
    }

    private void addButtonListener() {
        RenderDialog owner = this;
        colorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                color = JColorChooser.showDialog(owner,
                        "Choose a color...", color);
                colorButton.setBackground(color);
            }
        });


        JDialog tmp = this;

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectionQuality = 0;
                if(rougthButton.isSelected()){
                    selectionQuality = 0;
                }else if(normalButton.isSelected()){
                    selectionQuality = 1;
                }else {
                    selectionQuality = 2;
                }

                DialogProperties dialogProperties = new DialogProperties(
                        color, (double)gammaSpinner.getValue(), (int) depthSpinner.getValue(), selectionQuality,
                        (double)swSpinner.getValue(), (double)shSpinner.getValue(), (double)znSpinner.getValue(), (double)zfSpinner.getValue()
                );

                controller.newPropertiesFromDialog(dialogProperties);
                tmp.setVisible(false);
            }
        });


        closeDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tmp.setVisible(false);
            }
        });

    }

    private JPanel createPanel(DialogProperties initialProperties){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3));


        colorButton = new JButton("Color");
        colorButton.setBackground(initialProperties.getBackgroundColor());

        panel.add(namedField("ZN", znSpinner = new JSpinner(new SpinnerNumberModel(initialProperties.getZn(), 0, 1000, 0.5)), 10));
        panel.add(namedField("SW", swSpinner = new JSpinner(new SpinnerNumberModel(initialProperties.getSw(), 1, 200, 1)), 10));
        panel.add(namedField("Gamma", gammaSpinner = new JSpinner(new SpinnerNumberModel(initialProperties.getGamma(), 0, 10, 0.1)), 10));

        panel.add(namedField("ZF", zfSpinner = new JSpinner(new SpinnerNumberModel(initialProperties.getZf(), 1, 1000, 0.5)), 10));
        panel.add(namedField("SH", shSpinner = new JSpinner(new SpinnerNumberModel(initialProperties.getSh(), 1, 200, 0.5)), 10));
        panel.add(namedField("depth", depthSpinner = new JSpinner(new SpinnerNumberModel(initialProperties.getDepth(), 1, 3, 1)), 10));
        JPanel totalPanel = new JPanel();

        Box box = Box.createVerticalBox();
        box.add(panel);
        box.add(Box.createVerticalStrut(5));
        box.add(createQualityPanel());
        box.add(Box.createVerticalStrut(5));
        box.add(colorButton);

        totalPanel.add(box);

        return totalPanel;
    }

    private JPanel createQualityPanel(){
        JPanel panel = new JPanel();
        bGroup = new ButtonGroup();
        Box box = Box.createHorizontalBox();
        rougthButton = new JRadioButton("Rought");
        normalButton = new JRadioButton("Normal");
        fineButton = new JRadioButton("Fine");

        bGroup.add(rougthButton);
        bGroup.add(normalButton);
        bGroup.add(fineButton);

        box.add(rougthButton);
        box.add(Box.createHorizontalStrut(10));
        box.add(normalButton);
        box.add(Box.createHorizontalStrut(10));
        box.add(fineButton);

        if(initialProperties.getQuality() == 0){
            rougthButton.setSelected(true);
        }else if(initialProperties.getQuality() == 1){
            normalButton.setSelected(true);
        }else{
            fineButton.setSelected(true);
        }



        panel.add(box);

        return panel;
    }

    private JPanel createButtonPane(){
        JPanel panel = new JPanel();
        Box panelBox = Box.createHorizontalBox();

        panelBox.add(okButton = new JButton("OK"));
        panelBox.add(closeDialogButton = new JButton("Cancel"));

        panel.add(panelBox);
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

}
