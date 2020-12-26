package com.admin.registerchart;

import com.jdbc.JDBC;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class RegisterChartSeries extends JPanel {
    private Connection connection = null;
    private JFreeChart jFreeChart;
    private JButton jButton = new JButton("保存图片");
    private ChartPanel chartPanel;
    private Vector<Integer> vector = new Vector<Integer>();
    private XYDataset xydataset;
    private String[] string = {"2020-01-01", "2020-02-01", "2020-03-01", "2020-04-01", "2020-05-01", "2020-06-01", "2020-07-01", "2020-08-01", "2020-09-01", "2020-10-01", "2020-11-01", "2020-12-01", "2021-01-01"};

    public RegisterChartSeries() {
        this.setLayout(null);
        this.xydataset = createDataset();
        this.jFreeChart = ChartFactory.createTimeSeriesChart("月登记企业数量", "日期", "数量", xydataset, true, true, true);
        XYPlot xyplot = (XYPlot) jFreeChart.getPlot();
        DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
        dateaxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
        this.chartPanel = new ChartPanel(jFreeChart, true);
        dateaxis.setLabelFont(new Font("黑体", Font.BOLD, 14));         //水平底部标题
        dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));  //垂直标题
        ValueAxis rangeAxis = xyplot.getRangeAxis();//获取柱状
        rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
        this.jFreeChart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
        this.jFreeChart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));//设置标题字体
        this.chartPanel.setSize(800, 600);
        JLabel jLabel = new JLabel();
        jLabel.setBounds(130, 50, 800, 600);
        jLabel.add(chartPanel);
        this.add(jLabel);
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
                    ChartUtilities.saveChartAsPNG(new File(file.getAbsolutePath() + "\\PieSeries.png"), jFreeChart, 800, 600);
                    JOptionPane.showMessageDialog(null, "保存成功", "保存成功", JOptionPane.OK_OPTION);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "保存失败", "保存失败", JOptionPane.OK_OPTION);
                }
            }
        });
    }

    private XYDataset createDataset() {
        try {
            connection = JDBC.getConnection();
            for (int i = 0; i < 12; i++) {
                String SQL = "select count(*) from market_subject_information WHERE HZRQ BETWEEN ? AND ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, string[i]);
                preparedStatement.setObject(2, string[i + 1]);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    vector.add(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //添加数据
        TimeSeries timeseries = new TimeSeries("企业数量", org.jfree.data.time.Month.class);
        timeseries.add(new Month(1, 2001), new Integer((Integer) vector.get(0)));
        timeseries.add(new Month(2, 2001), new Integer((Integer) vector.get(1)));
        timeseries.add(new Month(3, 2001), new Integer((Integer) vector.get(2)));
        timeseries.add(new Month(4, 2001), new Integer((Integer) vector.get(3)));
        timeseries.add(new Month(5, 2001), new Integer((Integer) vector.get(4)));
        timeseries.add(new Month(6, 2001), new Integer((Integer) vector.get(5)));
        timeseries.add(new Month(7, 2001), new Integer((Integer) vector.get(6)));
        timeseries.add(new Month(8, 2001), new Integer((Integer) vector.get(7)));
        timeseries.add(new Month(9, 2001), new Integer((Integer) vector.get(8)));
        timeseries.add(new Month(10, 2001), new Integer((Integer) vector.get(9)));
        timeseries.add(new Month(11, 2001), new Integer((Integer) vector.get(10)));
        timeseries.add(new Month(12, 2001), new Integer((Integer) vector.get(11)));

        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        timeseriescollection.addSeries(timeseries);
        return timeseriescollection;
    }

}
