package com.admin;

import com.jdbc.JDBC;
import com.combobox.CLJG;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;

public class NameOpinionHandle extends JPanel {
    Connection connection = null;

    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private JLabel titleLabel = new JLabel("意见处理");

    private JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
    private JLabel jLabel = new JLabel("处理办法");
    private JComboBox<CLJG> jComboBox = new JComboBox<CLJG>();
    private JButton jButton = new JButton("处理完毕");
    private JLabel jLabel2 = new JLabel("意见详情");
    JTextPane jTextPane = new JTextPane();

    public NameOpinionHandle() {
        this.setLayout(null);
        this.titleLabel.setBounds(0, 0, 130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);
        this.headVector.add("用户名");
        this.headVector.add("环节名称");
        this.headVector.add("用户意见");
        this.headVector.add("处理状态");
        //表模型
        DefaultTableModel defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);
        this.table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(58);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(58);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(184);
        this.table.getColumnModel().getColumn(3).setPreferredWidth(58);
        this.jScrollPane.setBounds(350, 50, 360, 200);
        this.add(jScrollPane);

        this.jLabel2.setBounds(250, 250, 100, 30);
        this.jTextPane.setBounds(350, 300, 360, 200);
        this.jLabel.setBounds(350, 550, 100, 30);
        this.jComboBox.setBounds(450, 550, 200, 30);
        this.jButton.setBounds(500, 650, 100, 30);
        this.add(jLabel2);
        this.add(jTextPane);
        this.add(jLabel);
        this.add(jComboBox);
        this.add(jButton);

        //列表添加数据
        try {
            this.connection = JDBC.getConnection();
            String SQL = "select username,hjmc,clyj,(select cljg.cljgname from cljg where cljg.cljgid = cljg) from opinion_processing where CLJG = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, "2");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Vector<String> vector = new Vector<String>();
                vector.add(resultSet.getString(1));
                vector.add(resultSet.getString(2));
                vector.add(resultSet.getString(3));
                vector.add(resultSet.getString(4));
                this.dataVector.add(vector);
            }
            DefaultTableModel defaultTableModel1 = new DefaultTableModel(dataVector, headVector);
            this.table.setModel(defaultTableModel1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        //表格的添加事件
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (e.getClickCount() == 1) {
                    jTextPane.setText((String) table.getValueAt(row, 2));
                }
            }
        });

        //ComboBOX 加载选项;
        try {
            connection = JDBC.getConnection();
            String SQL = "select * from CLJG";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                CLJG cljg = new CLJG();
                cljg.setCljgid(resultSet.getString(1));
                cljg.setCljgname(resultSet.getString(2));
                this.jComboBox.addItem(cljg);
            }
            this.jComboBox.setSelectedIndex(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }
        //处理的监听事件
        this.jButton.addActionListener((e) ->
        {
            if (jComboBox.getSelectedIndex() == 1) {
                return;
            }
            try {
                int row = table.getSelectedRow();
                connection = JDBC.getConnection();
                String SQL = "update opinion_processing set CLJG = ? where username = ? and hjmc = ? and clyj = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, ((CLJG) jComboBox.getSelectedItem()).getCljgid());
                preparedStatement.setObject(2, table.getValueAt(row, 0));
                preparedStatement.setObject(3, table.getValueAt(row, 1));
                preparedStatement.setObject(4, table.getValueAt(row, 2));
                int i = preparedStatement.executeUpdate();
                if (i == 1) {
                    dataVector.remove(row);
                    DefaultTableModel defaultTableModel1 = new DefaultTableModel(dataVector, headVector);
                    table.setModel(defaultTableModel1);
                    JOptionPane.showMessageDialog(null, "处理成功");
                } else {
                    JOptionPane.showMessageDialog(null, "处理失败");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
        });
    }
}
