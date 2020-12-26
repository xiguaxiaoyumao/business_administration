package com.util;


import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JPasswordFieldHintListener implements FocusListener {
    private String hintText;
    private JPasswordField jPasswordField;

    public JPasswordFieldHintListener(JPasswordField jPasswordField, String hintText) {
        this.jPasswordField = jPasswordField;
        this.hintText = hintText;
        jPasswordField.setEchoChar('\0');
        jPasswordField.setText(hintText);
    }

    @Override
    public void focusGained(FocusEvent e) {
        String pswd = new String(jPasswordField.getPassword()).trim();
        if (pswd.equals(hintText)) {
            jPasswordField.setText("");
            jPasswordField.setEchoChar('*');
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        String pswd = new String(jPasswordField.getPassword()).trim();
        if (pswd.equals("")) {
            jPasswordField.setEchoChar('\0');
            jPasswordField.setText(hintText);
        }
    }
}