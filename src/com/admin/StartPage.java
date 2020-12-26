package com.admin;

import javax.swing.*;

public class StartPage extends JPanel {
    private JLabel jLabel;

    public StartPage() {
        this.setLayout(null);
        this.setSize(1060, 800);

        ImageIcon imgBack = new ImageIcon(this.getClass().getResource("/com/image/start.png"));
        this.jLabel = new JLabel(imgBack);
        this.jLabel.setBounds(30, 0, 1007, 760);
        this.add(jLabel);
    }
}
