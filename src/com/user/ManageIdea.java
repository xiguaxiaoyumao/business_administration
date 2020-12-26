package com.user;

import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ManageIdea extends JPanel {
    private Connection connection = null;

    private Font mainFont = new Font("宋体", Font.BOLD, 20);

    private JLabel titleLabel = new JLabel("办理意见");
    private JLabel usernameLabel = new JLabel("用户名");
    public JTextField userNameField = new JTextField();

    private JLabel hjmcLabel = new JLabel("环节名称");
    public JComboBox<String> hjmcComboBox = new JComboBox<String>();

    private JLabel opinionLabel = new JLabel("意见");
    public JTextPane opinionTextPane = new JTextPane();

    private JButton submitButton = new JButton("提交");

    private JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();

    //构造函数
    public ManageIdea(String username) {
        this.titleLabel.setBounds(0, 0, 130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);
        this.headVector.add("环节名称");
        this.headVector.add("我的意见");
        this.headVector.add("处理状态");

        this.hjmcComboBox.addItem("企业查询");
        this.hjmcComboBox.addItem("预先核准");
        this.hjmcComboBox.addItem("已核调整");
        this.hjmcComboBox.addItem("设立登记");
        //表模型
        DefaultTableModel defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);
        this.table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(88);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(124);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(88);
        this.jScrollPane.setBounds(600, 100, 300, 500);
        this.add(jScrollPane);

        this.usernameLabel.setBounds(200, 100, 100, 30);
        this.userNameField.setBounds(300, 100, 200, 30);
        this.add(usernameLabel);
        this.add(userNameField);
        this.hjmcLabel.setBounds(200, 200, 100, 30);
        this.hjmcComboBox.setBounds(300, 200, 200, 30);
        this.add(hjmcLabel);
        this.add(hjmcComboBox);
        this.opinionLabel.setBounds(200, 300, 100, 30);
        this.opinionTextPane.setBounds(300, 300, 200, 300);
        this.add(opinionLabel);
        this.add(opinionTextPane);
        this.submitButton.setBounds(480, 700, 100, 30);
        this.add(submitButton);
        this.userNameField.setText(username);

        //列表添加数据
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select hjmc,clyj,(select cljg.cljgname from cljg where cljg.cljgid = cljg) from opinion_processing where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Vector<String> vector = new Vector<String>();
                vector.add(resultSet.getString(1));
                vector.add(resultSet.getString(2));
                vector.add(resultSet.getString(3));
                this.dataVector.add(vector);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //提交事件
        this.submitButton.addActionListener((e) ->
        {
            if (opinionTextPane.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "请填写意见", "请填写意见", JOptionPane.OK_OPTION);
                return;
            }
            try {
                this.connection = JDBC.getConnection();
                String SQL = "insert into opinion_processing (username,hjmc,clyj,cljg) values(?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, username);
                preparedStatement.setObject(2, hjmcComboBox.getSelectedItem());
                preparedStatement.setObject(3, opinionTextPane.getText());
                preparedStatement.setObject(4, 2);
                preparedStatement.executeUpdate();
                Vector<String> vector = new Vector<String>();
                vector.add(hjmcComboBox.getSelectedItem().toString());
                vector.add(opinionTextPane.getText());
                vector.add("未处理");
                this.dataVector.add(vector);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }

            this.hjmcComboBox.setSelectedItem(0);
            this.opinionTextPane.setText("");
            JOptionPane.showMessageDialog(null, "意见提交成功 ", "意见提交成功", JOptionPane.OK_OPTION);
            this.table.setModel(defaultTableModel);
            this.jScrollPane.getViewport().add(table);
        });
        this.setLayout(null);
    }
}
