package com.user;

import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class StatusQuery extends JPanel {
    private Connection connection = null;

    DefaultTableModel defaultTableModel = null;
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private Font minorFont = new Font("宋体", Font.BOLD, 15);

    private JLabel titleLabel = new JLabel("状态查询");
    private JButton selectButton = new JButton("查询");
    public JTextField queryField = new JTextField();
    private JLabel remindLabel = new JLabel("请输入企业名称");

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
    public StatusQuery() {
        this.titleLabel.setSize(130, 30);
        this.titleLabel.setFont(mainFont);
        this.add(titleLabel);

        this.headVector.add("企业名称");
        this.headVector.add("字号");
        this.headVector.add("业务类型");
        this.headVector.add("字号拼音");
        this.headVector.add("名称状态");
        this.headVector.add("受理机关");
        this.defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);

        this.table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.jScrollPane.setBounds(10, 300, 1040, 450);

        this.queryField.setBounds(275, 150, 400, 50);
        this.queryField.setFont(mainFont);

        this.selectButton.setBounds(675, 150, 150, 50);
        this.selectButton.setFont(mainFont);

        this.remindLabel.setBounds(275, 200, 400, 20);
        this.remindLabel.setFont(minorFont);

        this.add(queryField);

        this.add(selectButton);

        this.add(remindLabel);

        this.add(jScrollPane);

        this.selectButton.addActionListener((e) ->
        {
            select();
        });

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
        setLayout(null);
    }


    public void select() {
        Vector<String> SCZTMC = new Vector<String>();
        if (queryField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "不能为空", "不能为空", JOptionPane.OK_OPTION);
            return;
        }
        try {
            connection = JDBC.getConnection();
            String SQL = "select SCZTMC from market_subject_information where SCZTMC like ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, "%" + queryField.getText() + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SCZTMC.add(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBC.returnConnection(connection);
        }

        for (String string : SCZTMC) {
            try {
                connection = JDBC.getConnection();
                String SQL = "select ZH,(select ywlx.ywlxname from market_subject_information ,ywlx where ywlx.ywlxid = market_subject_information.YWLX and scztmc = ?),ZHPY,(select lcztname from market_subject_information,lczt where lczt.lcztid=market_subject_information.lcztdm and scztmc =?),(select djjgdmname from market_subject_information,djjgdm where djjgdm.djjgdmid=djjgdm and scztmc = ?) from market_subject_information where scztmc like ? and scztmc = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, string);
                preparedStatement.setObject(2, string);
                preparedStatement.setObject(3, string);
                preparedStatement.setObject(4, "%" + queryField.getText() + "%");
                preparedStatement.setObject(5, string);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    Vector<String> vector = new Vector<String>();
                    vector.add(string);
                    vector.add(resultSet.getString(1));
                    vector.add(resultSet.getString(2));
                    vector.add(resultSet.getString(3));
                    vector.add(resultSet.getString(4));
                    vector.add(resultSet.getString(5));
                    this.dataVector.add(vector);
                    this.table.setModel(defaultTableModel);
                    this.jScrollPane.getViewport().add(table);
                } else {
                    JOptionPane.showMessageDialog(null, "未找到相关企业", "未找到相关企业", JOptionPane.OK_OPTION);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
        }
    }
}
