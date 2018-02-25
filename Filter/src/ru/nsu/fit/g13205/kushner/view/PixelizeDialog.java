package ru.nsu.fit.g13205.kushner.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Created by Konstantin on 16.03.2016.
 */
public class PixelizeDialog extends JDialog {
    private JButton okButton;
    private JButton cancelButton;
    private ButtonGroup buttonModeGroup;

    private JRadioButton twoButton;
    private JRadioButton fiveButton;
    private JRadioButton sevenButton;
    private JRadioButton tenButton;

    public PixelizeDialog(JFrame owner) {
        JPanel panel = new JPanel();
        Box totalBox = Box.createVerticalBox();
        totalBox.add(createModePanel());
        totalBox.add(Box.createVerticalStrut(10));
        totalBox.add(createButtonPanel());


        panel.add(totalBox);
        this.setModal(true);
        this.getContentPane().add(panel);
        this.setLocationRelativeTo(owner);
        this.pack();
    }

    private Box createButtonPanel(){

        Box box = Box.createVerticalBox();
        this.okButton = new JButton("OK");
        this.cancelButton = new JButton("Cancel");

        box.add(Box.createVerticalStrut(10));
        box.add(okButton);
        box.add(Box.createVerticalStrut(10));
        box.add(cancelButton);

        return box;
    }

    private Box createModePanel(){

        Box box = Box.createVerticalBox();

        buttonModeGroup = new ButtonGroup();

        twoButton = new JRadioButton("2 => 1", false);
        fiveButton = new JRadioButton("5 => 1", false);
        sevenButton = new JRadioButton("7 => 1", false);
        tenButton = new JRadioButton("10 => 1", false);

        buttonModeGroup.add(twoButton);
        buttonModeGroup.add(fiveButton);
        buttonModeGroup.add(sevenButton);
        buttonModeGroup.add(tenButton);

        box.add(twoButton);
        box.add(fiveButton);
        box.add(sevenButton);
        box.add(tenButton);

        return box;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public int getValue(){
        if(twoButton.isSelected()){
            return 2;
        }
        if(fiveButton.isSelected()){
            return 5;
        }
        if(sevenButton.isSelected()){
            return 7;
        }
        if(tenButton.isSelected()){
            return 10;
        }
        return 1;
    }
}
