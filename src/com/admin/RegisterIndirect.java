package com.admin;

import com.admin.registerindirect.LicensePrint;

import javax.swing.*;
import java.awt.*;

public class RegisterIndirect extends JTabbedPane {
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private LicensePrint licensePrint = new LicensePrint();

    public RegisterIndirect() {
        this.addTab("营业执照打印", licensePrint);
        this.setSize(1060, 800);
        this.setFont(mainFont);
    }
}
