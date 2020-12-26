package com.admin.namechart;

import com.jdbc.JDBC;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class IdeaChartPie extends JPanel {
    private Connection connection = null;
    private ChartPanel chartPanel = null;
    private JFreeChart jFreeChart = null;
    private JButton jButton = new JButton("保存图片");
    private JLabel jLabel = new JLabel();
    private Map<String, Integer> map = null;
    private String projectType[] = {"企业查询", "预先核准", "已核调整", "设立登记"};
    private String color[] = {"#749f83", "#2f4554", "#61a0a8", "#d48265"};

    public IdeaChartPie() {
        this.setLayout(null);
        DefaultPieDataset dataset = new DefaultPieDataset();
        try {
            connection = JDBC.getConnection();
            String SQL = "select HJMC,COUNT(*) from opinion_processing GROUP BY HJMC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            this.map = new HashMap<String, Integer>();
            while (resultSet.next()) {
                this.map.put(resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        this.jFreeChart = ChartFactory.createPieChart("意见统计", dataset, true, true, false);
        setChart(jFreeChart);
        PiePlot pieplot = (PiePlot) jFreeChart.getPlot();
        for (int i = 0; i < projectType.length; i++) {
            if (map.get(projectType[i]) == null) {
                dataset.setValue(projectType[i], 0);
            } else {
                dataset.setValue(projectType[i], new Integer(map.get(projectType[i])));
            }
            pieplot.setSectionPaint(projectType[i], Color.decode(color[i]));
        }

        try {
            // 创建图形显示面板
            this.chartPanel = new ChartPanel(jFreeChart, true);
            // // 设置图片大小
            this.chartPanel.setSize(800, 600);
            // 保存图片到指定文件夹
            //ChartUtilities.saveChartAsPNG(new File("d:\\PieChart2.png"), chart, 1500, 800);
            //System.err.println("成功");
            this.jLabel.setBounds(130, 50, 800, 600);
            this.jLabel.add(chartPanel);
            this.add(jLabel);
        } catch (Exception e) {
            System.err.println("创建图形时出错");
        }
        this.jFreeChart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
        this.jFreeChart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));//设置标题字体
        this.jButton.setBounds(450, 680, 100, 30);
        this.add(jButton);
        this.jButton.addActionListener((e) ->
        {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.showDialog(new JLabel(), "选择");
            File file = jfc.getSelectedFile();
            if (file.isDirectory()) {
                try {
                    ChartUtilities.saveChartAsPNG(new File(file.getAbsolutePath() + "\\PieChart.png"), jFreeChart, 800, 600);
                    JOptionPane.showMessageDialog(null, "保存成功", "保存成功", JOptionPane.OK_OPTION);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "保存失败", "保存失败", JOptionPane.OK_OPTION);
                }
            }
        });
    }

    public static void setChart(JFreeChart chart) {
        chart.setTextAntiAlias(true);
        PiePlot pieplot = (PiePlot) chart.getPlot();// 设置图表背景颜色
        pieplot.setBackgroundPaint(ChartColor.WHITE);
        pieplot.setLabelBackgroundPaint(null);// 标签背景颜色
        pieplot.setLabelFont(new Font("黑体", Font.BOLD, 15));
        pieplot.setLabelOutlinePaint(null);// 标签边框颜色
        pieplot.setLabelShadowPaint(null);// 标签阴影颜色
        pieplot.setOutlinePaint(null); // 设置绘图面板外边的填充颜色
        pieplot.setShadowPaint(null); // 设置绘图面板阴影的填充颜色
        pieplot.setSectionOutlinesVisible(false);
        pieplot.setNoDataMessage("没有可供使用的数据！");
    }
}

