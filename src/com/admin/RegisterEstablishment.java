package com.admin;

import com.admin.registerestablishment.NameInfo;
import com.admin.registerestablishment.ReplenishInfo;
import com.combobox.Djjg;
import com.combobox.Xzqhlx;
import com.combobox.Zdhy;
import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class RegisterEstablishment extends JTabbedPane {
    Connection connection = null;
    //字体
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private JLabel titleLabel = new JLabel("设立登记");

    private JTable table = new JTable();
    private JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();

    private JButton acceptButton = new JButton("受理");
    private JButton registerButton = new JButton("设立");
    private NameInfo nameInfo = new NameInfo();
    private ReplenishInfo replenishInfo = new ReplenishInfo();

    private Vector<String> SCZTMC = new Vector<String>();

    private JButton returnButton = new JButton("返回");

    public RegisterEstablishment() {

        this.setFont(mainFont);
        this.addTab("名称信息", nameInfo);
        this.addTab("补充信息", replenishInfo);
        //表头数据添加
        this.headVector.add("市场主体名称");
        this.headVector.add("字号");
        this.headVector.add("企业法人");
        this.headVector.add("流程状态");
        //表模型
        DefaultTableModel defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);
        this.table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.jScrollPane.setBounds(10, 350, 1040, 300);
        this.nameInfo.add(jScrollPane);

        //设置按钮位置
        this.acceptButton.setBounds(380, 680, 100, 30);
        this.registerButton.setBounds(580, 680, 100, 30);
        this.nameInfo.add(acceptButton);
        this.nameInfo.add(registerButton);

        this.returnButton.setBounds(530, 680, 100, 30);
        this.replenishInfo.add(returnButton);
        returnButton.addActionListener((e) ->
        {
            this.setSelectedIndex(0);
        });
        //从数据库中获取名称;
        try {
            connection = JDBC.getConnection();
            String SQL = "select SCZTMC from market_subject_information where LCZTDM = ? and ywlx = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, "3");
            preparedStatement.setObject(2, "3");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                this.SCZTMC.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //通过名称获取信息
        try {
            connection = JDBC.getConnection();
            for (String string : SCZTMC) {
                String SQL = " select zh ,(SELECT tzrqy.TZR  FROM tzrqy WHERE tzrqy.TZQYMC = ? AND EXISTS (SELECT investor_information.TZRMC FROM investor_information WHERE TZRLX = '3' AND tzrqy.TZR = investor_information.TZRMC)),(SELECT lczt.lcztname FROM market_subject_information,lczt WHERE lczt.lcztid = market_subject_information.lcztdm AND market_subject_information.SCZTMC = ?) from market_subject_information where scztmc = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, string);
                preparedStatement.setObject(2, string);
                preparedStatement.setObject(3, string);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Vector<String> vector = new Vector<String>();
                    vector.add(string);
                    vector.add(resultSet.getString(1));
                    vector.add(resultSet.getString(2));
                    vector.add(resultSet.getString(3));
                    dataVector.add(vector);
                }
                if (table.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "未找到相关企业", "未找到相关企业", JOptionPane.OK_OPTION);
                    return;
                }
            }
            this.table.setModel(defaultTableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //受理的监听事件
        this.acceptButton.addActionListener((e) ->
        {
            if (table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "请先选中", "请先选中", JOptionPane.OK_OPTION);
                return;
            }
            try {
                this.connection = JDBC.getConnection();
                String SQL = "select GMLX, XZQHLX, scztmc, DJJGDM, JYQXQ, JYQXZ from market_subject_information where scztmc = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, table.getValueAt(table.getSelectedRow(), 0));
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    this.nameInfo.gmlxFidld.setText(resultSet.getString(1));
                    for (int i = 0; i < this.nameInfo.xzqhlxComboBox.getItemCount(); i++) {
                        if (((Xzqhlx) (nameInfo.xzqhlxComboBox.getItemAt(i))).getXzqhlxid().equals(resultSet.getString(2)))
                            ;
                        nameInfo.xzqhlxComboBox.setSelectedIndex(i);
                        break;
                    }
                    this.nameInfo.mczhField.setText(resultSet.getString(3));
                    for (int i = 0; i < this.nameInfo.djjgComboBox.getItemCount(); i++) {
                        if (((Djjg) (nameInfo.djjgComboBox.getItemAt(i))).getDjjgid().equals(resultSet.getString(4))) ;
                        nameInfo.djjgComboBox.setSelectedIndex(i);
                        break;
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    date = resultSet.getDate(5);
                    this.nameInfo.JYQXQTextField.setText(dateFormat.format(date));
                    date = resultSet.getDate(6);
                    this.nameInfo.JYQXZTextField.setText(dateFormat.format(date));
                }
                this.setSelectedIndex(1);
                JOptionPane.showMessageDialog(null, "(" + nameInfo.mczhField.getText() + ")正在受理...请填写信息");
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
        });

        //登记监听事件
        this.registerButton.addActionListener((e) ->
        {
            if (nameInfo.mczhField.getText().isEmpty()) {
                return;
            }
            try {
                this.connection = JDBC.getConnection();
                String SQL = "update market_subject_information set ZDHYFL = ? ,MTZP = ?,SZJZMC = ? ,JZLWZ = ?,SZJDMC = ?,MPH = ?,lcztdm = '4'  where scztmc = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, ((Zdhy) (this.replenishInfo.zdhyflComboBox.getSelectedItem())).getZdhyid());
                preparedStatement.setObject(2, this.replenishInfo.mtzpField.getText());
                preparedStatement.setObject(3, this.replenishInfo.szjzmcField.getText());
                preparedStatement.setObject(4, this.replenishInfo.jznwzField.getText());
                preparedStatement.setObject(5, this.replenishInfo.szjdmcField.getText());
                preparedStatement.setObject(6, this.replenishInfo.mphField.getText());
                preparedStatement.setObject(7, this.nameInfo.mczhField.getText());
                int i = preparedStatement.executeUpdate();
                if (i == 1) {
                    JOptionPane.showMessageDialog(null, "操作成功");
                    this.dataVector.remove(table.getSelectedRow());
                    this.table.setModel(defaultTableModel);
                } else {
                    JOptionPane.showMessageDialog(null, "操作失败");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
        });
    }
}
