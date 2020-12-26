package com.admin;

import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AdminManage extends JPanel {
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private JLabel titleLabel = new JLabel("用户");

    Connection connection = null;
    //查询控件
    private JButton selectButton = new JButton("查询");
    private JTextField queryField = new JTextField();
    private JLabel remindLabel = new JLabel("请输入用户名称");

    Map<String, String> map = new HashMap<String, String>();
    //查询表
    private JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 5) {
                return true;
            }
            return false;
        }

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            if (column == 5) {
                return new DefaultCellEditor(jComboBox);
            }
            return super.getCellEditor(row, column);
        }
    };
    private JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
    private DefaultTableModel defaultTableModel;
    private DefaultTableCellRenderer defaultTableCellRenderer;

    int row = 0;
    private JComboBox<String> jComboBox = new JComboBox<>();

    public AdminManage() {
        this.setLayout(null);
        jComboBox.addItem("限制登录");
        jComboBox.addItem("普通管理员");
        jComboBox.addItem("高级管理员");
        map.put("限制登录", "0");
        map.put("普通管理员", "1");
        map.put("高级管理员", "2");
        this.titleLabel.setBounds(0, 0, 130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);

        this.queryField.setBounds(250, 160, 200, 30);
        this.selectButton.setBounds(600, 160, 150, 30);
        this.remindLabel.setBounds(250, 190, 100, 30);

        this.add(titleLabel);
        this.add(queryField);
        this.add(selectButton);
        this.add(remindLabel);

        this.headVector.add("编号");
        this.headVector.add("管理员名");
        this.headVector.add("密码");
        this.headVector.add("电话号码");
        this.headVector.add("上一次登录时间");
        this.headVector.add("当前权限");


        //表模型
        this.defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.defaultTableCellRenderer = new DefaultTableCellRenderer();
        this.defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.jScrollPane.setBounds(10, 300, 1060, 500);
        this.add(jScrollPane);
        this.tableInit();

        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row = table.getSelectedRow();
            }
        });
        this.jComboBox.addActionListener((e) ->
        {
            this.change();
        });
        this.selectButton.addActionListener((e) ->
        {
            this.select();
        });
    }

    private void tableInit() {
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from admin";
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Vector vector = new Vector();
                vector.add(resultSet.getString(1));
                vector.add(resultSet.getString(2));
                vector.add(resultSet.getString(3));
                vector.add(resultSet.getString(4));
                vector.add(resultSet.getString(5));
                if (resultSet.getString(6).equals("0")) {
                    vector.add("限制登录");
                } else if (resultSet.getString(6).equals("1")) {
                    vector.add("普通管理员");
                } else {
                    vector.add("高级管理员");
                }
                dataVector.add(vector);
            }
            DefaultTableModel defaultTableModel = new DefaultTableModel(dataVector, headVector);
            this.table.setModel(defaultTableModel);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }

    private void change() {
        try {
            connection = JDBC.getConnection();
            String SQL = "update admin set admin.limit =? where adminid  = " + new Integer((String) table.getValueAt(row, 0));
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, map.get(jComboBox.getSelectedItem()));
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }

    private void select() {
        if (queryField.getText().isEmpty()) {
            return;
        }
        try {
            dataVector.clear();
            ;
            connection = JDBC.getConnection();
            String SQL = "select * from admin where adminname like ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, "%" + queryField.getText() + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Vector vector = new Vector();
                vector.add(resultSet.getString(1));
                vector.add(resultSet.getString(2));
                vector.add(resultSet.getString(3));
                vector.add(resultSet.getString(4));
                vector.add(resultSet.getString(5));
                if (resultSet.getString(6).equals("0")) {
                    vector.add("限制登录");
                } else if (resultSet.getString(6).equals("1")) {
                    vector.add("普通管理员");
                } else {
                    vector.add("高级管理员");
                }
                dataVector.add(vector);
            }
            DefaultTableModel defaultTableModel = new DefaultTableModel(dataVector, headVector);
            this.table.setModel(defaultTableModel);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
    }
}
