package com.admin;

import com.admin.nameindirect.ApplyCancellation;
import com.admin.nameindirect.NameRechecking;
import com.admin.nameindirect.NotePrint;

import javax.swing.*;
import java.awt.*;

public class NameIndirect extends JTabbedPane {
    private Font mainFont = new Font("宋体", Font.BOLD, 20);

    ApplyCancellation applyCancellation = new ApplyCancellation();
    NotePrint notePrint = new NotePrint();
    NameRechecking nameRechecking = new NameRechecking();

    public NameIndirect() {
        this.setSize(1060, 800);
        this.setFont(mainFont);
        this.add("撤销申请", applyCancellation);
        this.add("文书打印", notePrint);
        this.add("字号查重", nameRechecking);
    }
}
