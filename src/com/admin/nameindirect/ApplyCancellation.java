package com.admin.nameindirect;

import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ApplyCancellation extends JPanel {
    //JDBC
    Connection connection = null;
    private JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
    JLabel selectLabel = new JLabel("请选择业务");
    JComboBox selectJComboBox = new JComboBox();
    JLabel SCZTMCLabel = new JLabel("市场主体名称(空即查询所有)");
    JTextField SCZTMCField = new JTextField();
    JButton selectButton = new JButton("查询");
    JButton revocation = new JButton("撤销");
    Map YWLXMap = new HashMap<String, String>();
    Map LCZTMap = new HashMap<String, String>();

    public ApplyCancellation() {
        this.setLayout(null);
        YWLXMap.put("2", "预先核准");
        YWLXMap.put("预先核准", "2");
        LCZTMap.put("1", "未受理");
        LCZTMap.put("2", "未处理");
        LCZTMap.put("未受理", "1");
        LCZTMap.put("未处理", "2");
        this.headVector.add("企业名称");
        this.headVector.add("业务类型");
        this.headVector.add("流程状态");
        DefaultTableModel defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.jScrollPane.setBounds(30, 50, 1000, 300);
        this.add(jScrollPane);

        this.selectLabel.setBounds(150, 450, 160, 30);
        this.selectJComboBox.setBounds(310, 450, 200, 30);
        this.selectJComboBox.addItem("预先核准");
        this.selectJComboBox.addItem("已核调整");
        this.SCZTMCLabel.setBounds(550, 450, 160, 30);
        this.SCZTMCField.setBounds(710, 450, 200, 30);
        this.selectButton.setBounds(400, 600, 100, 30);
        this.revocation.setBounds(600, 600, 100, 30);

        this.add(selectLabel);
        this.add(selectJComboBox);
        this.add(SCZTMCLabel);
        this.add(SCZTMCField);
        this.add(selectButton);
        this.add(revocation);

        //查询的事件
        this.selectButton.addActionListener((e) ->
        {
            try {
                this.dataVector.clear();
                connection = JDBC.getConnection();
                String SQL = "";
                switch (selectJComboBox.getSelectedItem().toString()) {
                    case "预先核准": {
                        System.out.println("预先核准");
                        if (SCZTMCField.getText().isEmpty()) {
                            SQL = "select SCZTMC,YWLX,LCZTDM from market_subject_information where LCZTDM  = '1' and YWLX = '2'";
                        } else {
                            SQL = "select SCZTMC ,YWLX,LCZTDM from market_subject_information where LCZTDM  = '1' and YWLX = '2' and SCZTMC like '%" + SCZTMCField.getText() + "%'";
                        }
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(SQL);
                        while (resultSet.next()) {
                            System.out.println(resultSet);
                            Vector<String> vector = new Vector<String>();
                            vector.add(resultSet.getString(1));
                            vector.add((String) YWLXMap.get(resultSet.getString(2)));
                            vector.add((String) LCZTMap.get(resultSet.getString(3)));
                            headVector.clear();
                            this.headVector.add("企业名称");
                            this.headVector.add("业务类型");
                            this.headVector.add("流程状态");
                            dataVector.add(vector);
                        }
                        break;
                    }
                    case "已核调整": {
                        System.out.println("已核调整");
                        if (SCZTMCField.getText().isEmpty()) {
                            SQL = "select SCZTMC ,CLJG ,NTZXM from approved_adjustment where CLJG  = '2'";
                        } else {
                            SQL = "select SCZTMC ,CLJG ,NTZXM from approved_adjustment where CLJG  = '2' and SCZTMC like '%" + SCZTMCField.getText() + "%'";
                        }
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(SQL);
                        while (resultSet.next()) {
                            System.out.println(resultSet);
                            Vector<String> vector = new Vector<String>();
                            vector.add(resultSet.getString(1));
                            vector.add("已核调整");
                            vector.add((String) LCZTMap.get(resultSet.getString(2)));
                            vector.add(resultSet.getString(3));
                            headVector.clear();
                            this.headVector.add("企业名称");
                            this.headVector.add("业务类型");
                            this.headVector.add("流程状态");
                            this.headVector.add("调整项目");
                            dataVector.add(vector);
                        }
                        break;
                    }
                    default:
                        break;
                }
                DefaultTableModel defaultTableModel1 = new DefaultTableModel(dataVector, headVector);
                table.setModel(defaultTableModel1);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
            if (table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "未找相关数据");
            }
        });

        //撤销业务
        this.revocation.addActionListener((e) ->
        {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "请先选中");
                return;
            }
            //如果是预先核准
            if (table.getValueAt(row, 1).equals("预先核准")) {
                try {
                    connection = JDBC.getConnection();
                    String SQL = "delete from  market_subject_information where scztmc =?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, table.getValueAt(row, 0));
                    int i = preparedStatement.executeUpdate();
                    if (i != 0) {
                        this.dataVector.remove(table.getSelectedRow());
                        table.setModel(defaultTableModel);
                        JOptionPane.showMessageDialog(null, "撤销成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "撤销失败");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
            } else {
                //预先核准表调整
                try {
                    connection = JDBC.getConnection();
                    String SQL = "delete from approved_adjustment where SCZTMC = ? and NTZXM = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, table.getValueAt(row, 0));
                    preparedStatement.setObject(2, table.getValueAt(row, 3));
                    int i = preparedStatement.executeUpdate();
                    if (i != 0) {
                        this.dataVector.remove(table.getSelectedRow());
                        table.setModel(defaultTableModel);
                        JOptionPane.showMessageDialog(null, "撤销成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "撤销失败");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
            }
        });
    }
}
