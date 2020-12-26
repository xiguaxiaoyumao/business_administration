package com.admin.nameindirect;

import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;
import java.text.DateFormat;
import java.util.Vector;

public class NameRechecking extends JPanel {
    private Connection connection = null;

    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private Font minorFont = new Font("宋体", Font.BOLD, 15);
    //基本控件
    public JButton selectButton = new JButton("查询");
    public JTextField queryField = new JTextField();
    private JLabel remindLabel = new JLabel("请输入企业名称或者注册号");
    //表格
    public JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
    private DefaultTableModel defaultTableModel = null;

    //构造函数
    public NameRechecking() {
        this.setLayout(null);
        this.queryField.setBounds(275, 150, 400, 50);
        this.queryField.setFont(mainFont);
        this.selectButton.setBounds(675, 150, 150, 50);
        this.selectButton.setFont(mainFont);
        this.remindLabel.setBounds(275, 200, 400, 20);
        this.remindLabel.setFont(minorFont);
        this.jScrollPane.setBounds(10, 300, 1040, 450);

        this.add(queryField);
        this.add(selectButton);
        this.add(remindLabel);
        this.add(jScrollPane);

        //表头添加数据
        this.headVector.add("名称");
        this.headVector.add("注册号");
        this.headVector.add("类型");
        this.headVector.add("法定代表人");
        this.headVector.add("注册资本");
        this.headVector.add("处理日期");
        this.headVector.add("住所");
        this.headVector.add("营业期限始");
        this.headVector.add("营业期限至");
        this.headVector.add("经营范围");
        this.headVector.add("登记机关");
        this.headVector.add("核准日期");
        this.headVector.add("登记状态");

        //表模型
        this.defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);
        //设置列宽
        this.table.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(106);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(3).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(4).setPreferredWidth(75);
        this.table.getColumnModel().getColumn(5).setPreferredWidth(86);
        this.table.getColumnModel().getColumn(6).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(7).setPreferredWidth(86);
        this.table.getColumnModel().getColumn(8).setPreferredWidth(86);
        this.table.getColumnModel().getColumn(9).setPreferredWidth(80);
        this.table.getColumnModel().getColumn(10).setPreferredWidth(60);
        this.table.getColumnModel().getColumn(11).setPreferredWidth(86);
        this.table.getColumnModel().getColumn(12).setPreferredWidth(55);

        //列宽不能改变 不能重新排序
        //this.table.getTableHeader().setResizingAllowed(false);
        this.table.getTableHeader().setReorderingAllowed(false);
        //居中
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);

        //查询监听事件
        selectButton.addActionListener((e) ->
        {
            this.select();
        });
        //键盘事件
        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER) {
                    select();
                }
            }
        };
        //给文本框添加键盘事件
        this.queryField.addKeyListener(listener);
    }

    //查询函数
    private void select() {
        Vector<String> SCZTMC = new Vector<String>();
        if (queryField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "不能为空", "不能为空", JOptionPane.OK_OPTION);
            return;
        }
        try {
            connection = JDBC.getConnection();
            String SQL = "select SCZTMC from market_subject_information where SCZTMC like ? and JGZTDM != ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, "%" + queryField.getText() + "%");
            preparedStatement.setObject(2, "9");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SCZTMC.add(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //根据名称查数据
        try {
            Connection connection = JDBC.getConnection();
            dataVector.clear();
            for (String string : SCZTMC) {
                String SQL = "select ZCH,(SELECT lxdm.LXDMNAME FROM market_subject_information , lxdm WHERE market_subject_information.LXDM = lxdm.LXDMID AND market_subject_information.SCZTMC = ?),(SELECT tzrqy.TZR  FROM tzrqy WHERE tzrqy.TZQYMC = ? AND EXISTS (SELECT investor_information.TZRMC FROM investor_information WHERE TZRLX = '3' AND tzrqy.TZR = investor_information.TZRMC)),ZCZB,CLRQ,ZS,JYQXQ,JYQXZ,JYFW,(SELECT djjgdm.DJJGDMNAME FROM market_subject_information , djjgdm WHERE market_subject_information.DJJGDM = DJJGDM.DJJGDMID AND market_subject_information.SCZTMC = ?),HZRQ,(SELECT jgzt.JGZTDMNAME FROM market_subject_information , jgzt WHERE market_subject_information.JGZTDM = jgzt.JGZTDMID AND market_subject_information.SCZTMC = ?)from market_subject_information where SCZTMC = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, string);
                preparedStatement.setObject(2, string);
                preparedStatement.setObject(3, string);
                preparedStatement.setObject(4, string);
                preparedStatement.setObject(5, string);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Vector vector = new Vector();
                    Date date = null;
                    DateFormat dateFormat = DateFormat.getDateInstance();
                    vector.add(string);
                    vector.add(resultSet.getString(1));
                    vector.add(resultSet.getString(2));
                    vector.add(resultSet.getString(3));
                    vector.add(resultSet.getInt(4));
                    date = resultSet.getDate(5);
                    if (date == null) {
                        vector.add("");
                    } else {
                        vector.add(dateFormat.format(date));
                    }
                    vector.add(resultSet.getString(6));
                    date = resultSet.getDate(7);
                    if (date == null) {
                        vector.add("");
                    } else {
                        vector.add(dateFormat.format(date));
                    }
                    date = resultSet.getDate(8);
                    if (date == null) {
                        vector.add("");
                    } else {
                        vector.add(dateFormat.format(date));
                    }
                    vector.add(resultSet.getString(9));
                    vector.add(resultSet.getString(10));
                    date = resultSet.getDate(11);
                    if (date == null) {
                        vector.add("");
                    } else {
                        vector.add(dateFormat.format(date));
                    }
                    vector.add(resultSet.getString(12));
                    defaultTableModel.addRow(vector);
                }
            }
            table.setModel(defaultTableModel);
            if (table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "未找到相关企业", "未找到相关企业", JOptionPane.OK_OPTION);
                queryField.setText("");
                return;
            } else {
                //JOptionPane.showMessageDialog(null,"查询成功","查询成功",JOptionPane.OK_OPTION);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }
}