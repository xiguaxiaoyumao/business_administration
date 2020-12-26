package com.admin.namechart;

import com.jdbc.JDBC;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

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

public class CountChartPole extends JPanel {

    private ChartPanel chartPanel = null;
    private JButton jButton = new JButton("保存图片");
    public Connection connection = null;
    private Map<String, String> nameMap = new HashMap<String, String>();
    private Map<String, Integer> countMap = new HashMap<String, Integer>();

    public CountChartPole() {
        this.nameMap.put("湖南省", "430000");
        this.nameMap.put("长沙市", "430100");
        this.nameMap.put("株洲市", "430200");
        this.nameMap.put("湘潭市", "430300");
        this.nameMap.put("衡阳市", "430400");
        this.nameMap.put("邵阳市", "430500");
        this.nameMap.put("岳阳市", "430600");
        this.nameMap.put("常德市", "430700");
        this.nameMap.put("张家界市", "430800");
        this.nameMap.put("益阳市", "430900");
        this.nameMap.put("郴州市", "431000");
        this.nameMap.put("永州市", "431100");
        this.nameMap.put("怀化市", "431200");
        this.nameMap.put("娄底市", "431300");
        this.nameMap.put("湘西", "433100");
        this.setLayout(null);
        CategoryDataset categoryDataset = getDataSet();
        JFreeChart jFreeChart = ChartFactory.createBarChart3D(
                "各单位登记数量图", //图表标题
                "工商行政管理局", //目录轴的显示标签
                "数量", //数值轴的显示标签
                categoryDataset, //数据集
                PlotOrientation.VERTICAL, //图表方向
                true, //是否显示图例，对于简单的柱状图必须为false
                false, //是否生成提示工具
                false);         //是否生成url链接


        CategoryPlot categoryplot = (CategoryPlot) jFreeChart.getPlot();

        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();

        CategoryAxis domainAxis = categoryplot.getDomainAxis();

        /*------设置X轴坐标上的文字-----------*/
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 10));
        /*------设置X轴的标题文字------------*/
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
        /*------设置Y轴坐标上的文字-----------*/
        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
        /*------设置Y轴的标题文字------------*/
        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
        /*------这句代码解决了底部汉字乱码的问题-----------*/
        jFreeChart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
        /*******这句代码解决了标题汉字乱码的问题********/
        jFreeChart.getTitle().setFont(new Font("宋体", Font.PLAIN, 12));
        try {
            this.chartPanel = new ChartPanel(jFreeChart, true);
            chartPanel.setSize(1000, 600);
            JLabel jLabel = new JLabel();
            jLabel.add(chartPanel);
            jLabel.setBounds(35, 50, 1000, 600);
            this.add(jLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    ChartUtilities.saveChartAsPNG(new File(file.getAbsolutePath() + "\\PiePole.png"), jFreeChart, 800, 600);
                    JOptionPane.showMessageDialog(null, "保存成功", "保存成功", JOptionPane.OK_OPTION);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "保存失败", "保存失败", JOptionPane.OK_OPTION);
                }
            }
        });
    }

    private CategoryDataset getDataSet() {
        try {
            connection = JDBC.getConnection();
            String SQL = "select djjgdm, count(*) from market_subject_information group by djjgdm";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                this.countMap.put(resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("湖南省")), "数量", "湖南省");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("长沙市")), "数量", "长沙市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("株洲市")), "数量", "株洲市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("湘潭市")), "数量", "湘潭市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("衡阳市")), "数量", "衡阳市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("邵阳市")), "数量", "邵阳市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("岳阳市")), "数量", "岳阳市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("常德市")), "数量", "常德市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("张家界市")), "数量", "张家界市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("益阳市")), "数量", "益阳市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("郴州市")), "数量", "郴州市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("永州市")), "数量", "永州市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("怀化市")), "数量", "怀化市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("娄底市")), "数量", "娄底市");
        defaultCategoryDataset.addValue(countMap.get(nameMap.get("湘西")), "数量", "湘西");
        return defaultCategoryDataset;
    }

}
