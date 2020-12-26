package com.util;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldHintListener implements FocusListener {
    private String hintText;
    private JTextField jTextField;

    public JTextFieldHintListener(JTextField jTextField, String hintText) {
        this.jTextField = jTextField;
        this.hintText = hintText;
        jTextField.setText(hintText);
    }

    @Override
    public void focusGained(FocusEvent e) {
        String temp = jTextField.getText();
        if (temp.equals(hintText)) {
            jTextField.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        String temp = jTextField.getText();
        if (temp.equals("")) {
            jTextField.setText(hintText);
        }
    }

}