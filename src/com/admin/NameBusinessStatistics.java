package com.admin;

import com.admin.namechart.AdjustChartPie;
import com.admin.namechart.IdeaChartPie;
import com.admin.namechart.CountChartPole;

import javax.swing.*;
import java.awt.*;

public class NameBusinessStatistics extends JTabbedPane {
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private IdeaChartPie ideaChartPie = new IdeaChartPie();
    private CountChartPole countChartPole = new CountChartPole();
    private AdjustChartPie adjustChartPie = new AdjustChartPie();

    //构造函数
    public NameBusinessStatistics() {
        this.setFont(mainFont);
        this.addTab("各机关登记企业数量", countChartPole);
        this.addTab("已核调整类型", adjustChartPie);
        this.addTab("意见类型", ideaChartPie);
    }
}

