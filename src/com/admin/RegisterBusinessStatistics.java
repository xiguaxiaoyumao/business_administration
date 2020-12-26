package com.admin;


import com.admin.registerchart.CancelRegisterChartSeries;
import com.admin.registerchart.ChangeChartPie;
import com.admin.registerchart.RegisterChartSeries;

import javax.swing.*;
import java.awt.*;

public class RegisterBusinessStatistics extends JTabbedPane {
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private CancelRegisterChartSeries cancelRegisterChartSeries = new CancelRegisterChartSeries();
    private RegisterChartSeries registerChartSeries = new RegisterChartSeries();
    private ChangeChartPie changeChartPie = new ChangeChartPie();

    public RegisterBusinessStatistics() {
        this.setFont(mainFont);
        this.addTab("月登记企业数量变化图", registerChartSeries);
        this.addTab("月注销企业数量变化图", cancelRegisterChartSeries);
        this.addTab("变更类型", changeChartPie);
        this.setSize(1060, 800);

    }
}
