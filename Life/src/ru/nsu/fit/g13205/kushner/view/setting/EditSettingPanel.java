package ru.nsu.fit.g13205.kushner.view.setting;

import ru.nsu.fit.g13205.kushner.SettingApplication;
import ru.nsu.fit.g13205.kushner.view.AssociatedSliderAndTextField;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.*;

/**
 * Created by Konstantin on 14.02.2016.
 */
public class EditSettingPanel extends JPanel{

    private static final String PanelName = "Properties";
    private AssociatedSliderAndTextField cellsSizePanel;
    private AssociatedSliderAndTextField linesSizePanel;
    private JButton okButton;
    private JButton cancelButton;
    private ButtonGroup buttonModeGroup;
    private JTextField mTextField;
    private JTextField nTextField;
    private JRadioButton replaceButton;
    private JRadioButton xorButton;

    private EditSetting setting;


    public EditSettingPanel(EditSetting setting) {
        this.setting = setting;

        Box totalBox = Box.createVerticalBox();

        Box box1 = Box.createHorizontalBox();
        cellsSizePanel = new AssociatedSliderAndTextField(1, 100, setting.getCellSize(), "Size Cells");
        box1.add(cellsSizePanel);

        linesSizePanel = new AssociatedSliderAndTextField(1, 100, setting.getLineWidth(), "Size Lines");
        box1.add(Box.createVerticalStrut(20));
        box1.add(linesSizePanel);
        totalBox.add(box1);

        Box box2 = Box.createHorizontalBox();
        box2.add(createSizeField("Size Field", setting.getM(), setting.getN()));
        box2.add(createModePanel("Mode"));


        Box buttonBox = Box.createVerticalBox();
        buttonBox.add(createButtonPanel());

        box2.add(buttonBox);
        totalBox.add(box2);

        this.add(totalBox);
    }

    private JPanel createButtonPanel(){
        JPanel panel = new JPanel();

        Box box = Box.createVerticalBox();
        this.okButton = new JButton("OK");
        this.cancelButton = new JButton("Cancel");

        box.add(Box.createVerticalStrut(10));
        box.add(okButton);
        box.add(Box.createVerticalStrut(10));
        box.add(cancelButton);
        panel.add(box);

        return panel;
    }

    private JPanel createModePanel(String title){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(title));

        Box totalBox = Box.createVerticalBox();

        buttonModeGroup = new ButtonGroup();

        if(setting.getMode() == 2){
            replaceButton = new JRadioButton("Replace",false);
            xorButton = new JRadioButton("XOR", true);
        }else{
            replaceButton = new JRadioButton("Replace",true);
            xorButton = new JRadioButton("XOR", false);
        }
        buttonModeGroup.add(replaceButton);
        buttonModeGroup.add(xorButton);

        totalBox.add(replaceButton);
        totalBox.add(xorButton);

        panel.add(totalBox);

        return panel;
    }

    private JPanel createSizeField(String title,int m, int n){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(title));

        Box totalBox = Box.createHorizontalBox();

        mTextField = new JTextField(String.valueOf(m),3);
        mTextField.addFocusListener(unLegalArgumentHandler("Неверно введен параметр m", mTextField, this));

        totalBox.add(createLabelEditPanel("m", mTextField));
        totalBox.add(Box.createHorizontalStrut(20));

        nTextField = new JTextField(String.valueOf(n),3);
        nTextField.addFocusListener(unLegalArgumentHandler("Неверно введен параметр n", nTextField, this));
        totalBox.add(createLabelEditPanel("n", nTextField));

        panel.add(totalBox);


        return panel;
    }

    private JPanel createLabelEditPanel(String labelText, JTextField textField){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);

        Box box = Box.createHorizontalBox();
        box.add(label);
        box.add(Box.createHorizontalStrut(10));
        box.add(textField);

        panel.add(box);

        return panel;
    }

    public JButton getOkButton(){
        return okButton;
    }

    public JButton getCancelButton(){
        return cancelButton;
    }

    public EditSetting getPropertiesPanelInfo(){
        int mode;
        if(replaceButton.isSelected()){
            mode = 1;
        }else{
            mode = 2;
        }
        return new EditSetting(Integer.valueOf(mTextField.getText()), Integer.valueOf(nTextField.getText()), cellsSizePanel.getValue(),
                linesSizePanel.getValue(), mode);
    }

    private FocusAdapter unLegalArgumentHandler(String message, JTextField field, JPanel owner){
        return new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    int value = Integer.valueOf(field.getText());
                    if (value < 0 || value > SettingApplication.MAX_FIELD_SIZE) {
                        Object[] options = {"OK"};
                        JOptionPane.showOptionDialog(owner,
                                String.format("%s .\nЕго значение должно быть в диапазоне от 0 до %d", message, SettingApplication.MAX_FIELD_SIZE), "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        field.setText("0");
                        field.requestFocusInWindow();
                    }
                }catch (NumberFormatException exp){
                    Object[] options = {"OK"};
                    JOptionPane.showOptionDialog(owner,
                            String.format("%s .\nЕго значение должно быть в диапазоне от 0 до %d", message, SettingApplication.MAX_FIELD_SIZE), "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    field.setText("0");
                    field.requestFocusInWindow();
                }
            }
        };
    }

    private void setCancelButtonActionListener(){

    }

}
