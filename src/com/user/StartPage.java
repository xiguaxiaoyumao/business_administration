package com.user;

import javax.swing.*;

public class StartPage extends JPanel {
    private JLabel jLabel;
    private ImageIcon imgBack = new ImageIcon(this.getClass().getResource("/com/image/start.png"));

    public StartPage() {
        this.setLayout(null);
        //this.setBackground(MyColor.BACK);
        this.jLabel = new JLabel(this.imgBack);
        this.jLabel.setBounds(24, 0, 1007, 760);
        this.add(this.jLabel);
    }
}
