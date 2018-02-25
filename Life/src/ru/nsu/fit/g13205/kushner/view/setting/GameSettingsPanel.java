package ru.nsu.fit.g13205.kushner.view.setting;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileNotFoundException;

/**
 * Created by Konstantin on 25.02.2016.
 */
public class GameSettingsPanel extends JPanel {
    private String title;

    private JTextField lifeBeginField;
    private JTextField lifeEndField;
    private JTextField birthBeginField;
    private JTextField birthEndField;
    private JTextField FSTImpactField;
    private JTextField SNDImpactField;

    private JButton okButton;
    private JButton cancelButton;

    private double lifeBegin;
    private double lifeEnd;
    private double birthBegin;
    private double birthEnd;
    private double FSTImpact;
    private double SNDImpact;


    public GameSettingsPanel(GameSetting setting) {

        super();
        setBorder(new LineBorder(Color.black));
        this.lifeBegin = setting.getLifeBegin();
        this.lifeEnd = setting.getLifeEnd();
        this.birthBegin = setting.getBirthBegin();
        this.birthEnd = setting.getBirthEnd();
        this.FSTImpact = setting.getFSTImpact();
        this.SNDImpact = setting.getSNDImpact();

        add(createPanel());
        addTextFieldListener();

    }

    private Box createPanel(){
        Box totalBox = Box.createVerticalBox();

        totalBox.add(createBox("LIFE_BEGIN:", lifeBeginField = new JTextField(String.valueOf(lifeBegin), 10), 21));
        totalBox.add(Box.createVerticalStrut(5));

        totalBox.add(createBox("LIFE_END:", lifeEndField = new JTextField(String.valueOf(lifeEnd), 10), 32));
        totalBox.add(Box.createVerticalStrut(5));

        totalBox.add(createBox("BIRTH_BEGIN:", birthBeginField = new JTextField(String.valueOf(birthBegin), 10), 10));
        totalBox.add(Box.createVerticalStrut(5));

        totalBox.add(createBox("BIRTH_END:", birthEndField = new JTextField(String.valueOf(birthEnd), 10), 21));
        totalBox.add(Box.createVerticalStrut(5));

        totalBox.add(createBox("FST_IMPACT:", FSTImpactField = new JTextField(String.valueOf(FSTImpact), 10), 13));
        totalBox.add(Box.createVerticalStrut(5));

        totalBox.add(createBox("SND_IMPACT:", SNDImpactField = new JTextField(String.valueOf(SNDImpact), 10), 10));
        totalBox.add(Box.createVerticalStrut(5));

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(okButton = new JButton("OK"));
        buttonBox.add(Box.createHorizontalStrut(20));
        buttonBox.add(cancelButton = new JButton("Cancel"));
        totalBox.add(buttonBox);

        return totalBox;
    }

    private Box createBox(String tittle, JTextField textField, int verticalStrutWidth){
        Box box = Box.createHorizontalBox();
        box.add(new JLabel(tittle));
        box.add(Box.createHorizontalStrut(verticalStrutWidth));
        box.add(textField);

        return box;
    }

    private void addTextFieldListener(){
        lifeBeginField.addFocusListener(focusLostHandler("Неверно введен введено значение - LIFE_BEGIN", lifeBeginField, this));
        lifeEndField.addFocusListener(focusLostHandler("Неверно введен введено значение - LIFE_END", lifeEndField, this));
        birthBeginField.addFocusListener(focusLostHandler("Неверно введен введено значение - BIRTH_BEGIN", birthBeginField, this));
        birthEndField.addFocusListener(focusLostHandler("Неверно введен введено значение - BIRTH_END", birthEndField, this));
        FSTImpactField.addFocusListener(focusLostHandler("Неверно введен введено значение - FST_IMPACT", FSTImpactField, this));
        SNDImpactField.addFocusListener(focusLostHandler("Неверно введен введено значение - SND_IMPACT", SNDImpactField, this));
    }

    private FocusAdapter focusLostHandler(String message, JTextField field, JPanel owner) {
        return new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    double value = Double.valueOf(field.getText());
                    int length;
                    if(field.getText().contains(".")) {
                        length = field.getText().split("\\.")[1].length();
                    }else{
                        length = 0;
                    }
                    if (value < 0 ) {
                        Object[] options = {"OK"};
                        JOptionPane.showOptionDialog(owner,
                                String.format("%s .\nЕго значение не должно быть меньше 0\nи содержать символы отличные от цифр.", message), "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        field.setText("0.0");
                        field.requestFocusInWindow();
                    }else if(length > 1){
                        Object[] options = {"OK"};
                        JOptionPane.showOptionDialog(owner,
                                String.format("%s .\nЗначение не должно содержать больше одного знака после запятой", message), "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        field.setText("0.0");
                        field.requestFocusInWindow();
                    }
                } catch (NumberFormatException exp) {
                    Object[] options = {"OK"};
                    JOptionPane.showOptionDialog(owner,
                            String.format("%s .\nЕго значение не должно быть меньше 0\nи содержать символы отличные от цифр.", message), "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    field.setText("0.0");
                    field.requestFocusInWindow();
                }
            }
        };
    }

    public GameSetting getSetting(){
        return new GameSetting(Double.valueOf(lifeBeginField.getText()),
                Double.valueOf(lifeEndField.getText()),
                Double.valueOf(birthBeginField.getText()),
                Double.valueOf(birthEndField.getText()),
                Double.valueOf(FSTImpactField.getText()),
                Double.valueOf(SNDImpactField.getText()));
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

}
