package com.admin;

import com.admin.preapproved.CompanyInfo;
import com.admin.preapproved.InvestorInfo;
import com.admin.preapproved.NameInfo;
import com.combobox.*;
import com.jdbc.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class NamePreApproved extends JTabbedPane {
    //连接
    Connection connection = null;
    //字体
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private JLabel titleLabel = new JLabel("预先核准");

    private JTable table = new JTable();
    private JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private Vector<String> headVector = new Vector<String>();
    private Vector<Vector<String>> dataVector = new Vector<Vector<String>>();

    private JButton acceptButton = new JButton("受理");
    private JButton approveButton = new JButton("核准");
    private JButton noApproveButton = new JButton("不予核准");
    private NameInfo nameInfo = new NameInfo();
    private CompanyInfo companyInfo = new CompanyInfo();
    private InvestorInfo investorInfo = new InvestorInfo();

    private Vector<String> SCZTMC = new Vector<String>();

    public NamePreApproved() {
        this.setFont(mainFont);
        this.addTab("名称信息", nameInfo);
        this.addTab("企业信息", companyInfo);
        this.addTab("投资人信息", investorInfo);
        //表头数据添加
        this.headVector.add("市场主体名称");
        this.headVector.add("字号");
        this.headVector.add("企业法人");
        this.headVector.add("业务类型");
        //表模型
        DefaultTableModel defaultTableModel = new DefaultTableModel(dataVector, headVector);
        this.table.setModel(defaultTableModel);
        this.jScrollPane.getViewport().add(table);
        this.table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        this.jScrollPane.setBounds(30, 350, 1000, 300);
        this.nameInfo.add(jScrollPane);

        //设置按钮位置
        this.acceptButton.setBounds(350, 680, 100, 30);
        this.approveButton.setBounds(500, 680, 100, 30);
        this.noApproveButton.setBounds(650, 680, 100, 30);
        this.nameInfo.add(acceptButton);
        this.nameInfo.add(approveButton);
        this.nameInfo.add(noApproveButton);

        //从数据库中获取名称;
        try {
            connection = JDBC.getConnection();
            String SQL = "select SCZTMC from market_subject_information where LCZTDM = ? and ywlx = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, "1");
            preparedStatement.setObject(2, "2");
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
                String SQL = " select zh ,(SELECT tzrqy.TZR  FROM tzrqy WHERE tzrqy.TZQYMC = ? AND EXISTS (SELECT investor_information.TZRMC FROM investor_information WHERE TZRLX = '3' AND tzrqy.TZR = investor_information.TZRMC)),(SELECT ywlx.ywlxname FROM market_subject_information,ywlx WHERE ywlx.ywlxid = market_subject_information.ywlx AND market_subject_information.SCZTMC = ?) from market_subject_information where scztmc = ?";
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
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "请选中需要核准的企业");
                return;
            }
            try {
                connection = JDBC.getConnection();
                String SQL = "update market_subject_information set CLRQ = ? where scztmc = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = sdf.format(new Date());
                preparedStatement.setObject(1, dateStr);
                preparedStatement.setObject(2, table.getValueAt(row, 0));
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }

            //获取详细信息
            try {
                connection = JDBC.getConnection();
                String string = "select GMLX, XZQHLX, ZH,BXZH1,BXZH2,ZHPY,SCZTMC,HYDM,LXDM,ZLDM,ZS,ZZXS,LXDH,YZBM,ZCZB,TZBZ,JYFW from market_subject_information where scztmc = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(string);
                preparedStatement.setObject(1, table.getValueAt(row, 0));
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    this.nameInfo.gmlxFidld.setText(resultSet.getString(1));
                    for (int i = 0; i < this.nameInfo.xzqhlxComboBox.getItemCount(); i++) {
                        if (this.nameInfo.xzqhlxComboBox.getItemAt(i).getXzqhlxid().equals(resultSet.getString(2))) {
                            this.nameInfo.xzqhlxComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    this.nameInfo.mczhField.setText(resultSet.getString(3));
                    this.nameInfo.bxzh1Field.setText(resultSet.getString(4));
                    this.nameInfo.bxzh2Feild.setText(resultSet.getString(5));
                    this.nameInfo.zhpyField.setText(resultSet.getString(6));
                    this.nameInfo.nsmcField.setText(resultSet.getString(7));
                    for (int i = 0; i < this.nameInfo.hymcJComboBox.getItemCount(); i++) {
                        if (this.nameInfo.hymcJComboBox.getItemAt(i).getHymcid().equals(resultSet.getString(8))) {
                            this.nameInfo.hymcJComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    for (int i = 0; i < this.companyInfo.qylxComboBox.getItemCount(); i++) {
                        if (this.companyInfo.qylxComboBox.getItemAt(i).getQylxid().equals(resultSet.getString(9))) {
                            this.companyInfo.qylxComboBox.setSelectedIndex(i);
                            break;
                        }

                    }

                    for (int i = 0; i < this.companyInfo.qyzlComboBox.getItemCount(); i++) {
                        if (this.companyInfo.qyzlComboBox.getItemAt(i).getQyzlid().equals(resultSet.getString(10))) {
                            this.companyInfo.qyzlComboBox.setSelectedIndex(i);
                            break;
                        }

                    }
                    this.companyInfo.zsszdzField.setText(resultSet.getString(11));

                    for (int i = 0; i < this.companyInfo.zzxsComboBox.getItemCount(); i++) {
                        if (this.companyInfo.zzxsComboBox.getItemAt(i).getZzxsid().equals(resultSet.getString(12))) {
                            this.companyInfo.zzxsComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    this.companyInfo.lxdhfield.setText(resultSet.getString(13));
                    this.companyInfo.yzbmfidld.setText(resultSet.getString(14));
                    this.companyInfo.zczbfield.setText(resultSet.getString(15));

                    for (int i = 0; i < this.companyInfo.zczbbzComboBox.getItemCount(); i++) {
                        if (this.companyInfo.zczbbzComboBox.getItemAt(i).getBzid().equals(resultSet.getString(16))) {
                            this.companyInfo.zczbbzComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    this.companyInfo.jyfwFeild.setText(resultSet.getString(17));
                } else {
                    JOptionPane.showMessageDialog(null, "信息加载失败");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }

            //加载投资人名称
            Vector<String> TZRMC = new Vector<String>();
            try {
                connection = JDBC.getConnection();
                String SQL = "SELECT tzrqy.TZR FROM tzrqy WHERE tzrqy.TZQYMC =?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, this.nameInfo.nsmcField.getText());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    TZRMC.add(resultSet.getString(1));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }

            //通过名称获取信息
            for (String string : TZRMC) {
                try {
                    connection = JDBC.getConnection();
                    String SQL = "select TZRMC,(SELECT TZRLX.TZRLXNAME FROM tzrlx,investor_information WHERE tzrlx.TZRLXID = investor_information.TZRLX AND TZRMC = ?),(SELECT zjlx.ZJLXNAME FROM zjlx,investor_information WHERE ZJLX.ZJLXID = investor_information.ZJLX AND TZRMC = ?),ZJHM,CZBL,(SELECT xb.XBNAME FROM xb ,investor_information WHERE xb.XBID = investor_information.XB AND TZRMC = ?),LXDH,(SELECT GJ.GJNAME FROM gj ,investor_information WHERE gj.GJID = investor_information.GJ AND TZRMC = ?),ZS,(SELECT djjgdm.DJJGDMNAME FROM djjgdm,investor_information WHERE djjgdm.DJJGDMID = investor_information.DJJG AND TZRMC = ?) from investor_information where tzrmc = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, string);
                    preparedStatement.setObject(2, string);
                    preparedStatement.setObject(3, string);
                    preparedStatement.setObject(4, string);
                    preparedStatement.setObject(5, string);
                    preparedStatement.setObject(6, string);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Vector<String> vector = new Vector<String>();
                        vector.add(resultSet.getString(1));
                        vector.add(resultSet.getString(2));
                        vector.add(resultSet.getString(3));
                        vector.add(resultSet.getString(4));
                        vector.add(resultSet.getString(5));
                        vector.add(resultSet.getString(6));
                        vector.add(resultSet.getString(7));
                        vector.add(resultSet.getString(8));
                        vector.add(resultSet.getString(9));
                        vector.add(resultSet.getString(10));
                        this.investorInfo.dataVector.add(vector);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    JDBC.returnConnection(connection);
                }
            }
            DefaultTableModel defaultTableModel1 = new DefaultTableModel(this.investorInfo.dataVector, this.investorInfo.headVector);
            this.investorInfo.table.setModel(defaultTableModel1);
            if (this.investorInfo.table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "信息加载失败");
            } else {
                JOptionPane.showMessageDialog(null, "信息加载成功");
            }
        });

        //核准
        this.approveButton.addActionListener((E) ->
        {
            if (nameInfo.mczhField.getText().isEmpty()) {
                return;
            }
            //更新企业信息
            try {
                connection = JDBC.getConnection();
                String SQL = "update market_subject_information set GMLX = ?,XZQHLX= ?,ZH= ?,BXZH1= ?,BXZH2= ?,ZHPY= ?,SCZTMC= ?,HYDM= ?,LXDM= ?,ZLDM= ?,ZCH= ?,ZS= ?,ZZXS= ?,LXDH= ?,YZBM= ?,ZCZB= ?,TZBZ= ?,JYFW= ?,JGZTDM= ?,LCZTDM= ?,YWLX= ? ,djjgdm = ?,JYQXQ = ? ,JYQXZ = ?where scztmc = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, nameInfo.gmlxFidld.getText());
                preparedStatement.setObject(2, ((Xzqhlx) (nameInfo.xzqhlxComboBox.getSelectedItem())).getXzqhlxid());
                preparedStatement.setObject(3, nameInfo.mczhField.getText());
                preparedStatement.setObject(4, nameInfo.bxzh1Field.getText());
                preparedStatement.setObject(5, nameInfo.bxzh2Feild.getText());
                preparedStatement.setObject(6, nameInfo.zhpyField.getText());
                preparedStatement.setObject(7, nameInfo.nsmcField.getText());
                preparedStatement.setObject(8, ((Hymc) (nameInfo.hymcJComboBox.getSelectedItem())).getHymcid());
                preparedStatement.setObject(9, ((Qylx) (companyInfo.qylxComboBox.getSelectedItem())).getQylxid());
                preparedStatement.setObject(10, ((Qyzl) companyInfo.qyzlComboBox.getSelectedItem()).getQyzlid());
                preparedStatement.setObject(11, "313131313131313");
                preparedStatement.setObject(12, companyInfo.zsszdzField.getText());
                preparedStatement.setObject(13, ((Zzxs) (companyInfo.zzxsComboBox.getSelectedItem())).getZzxsid());
                preparedStatement.setObject(14, companyInfo.lxdhfield.getText());
                preparedStatement.setObject(15, companyInfo.yzbmfidld.getText());
                preparedStatement.setObject(16, companyInfo.zczbfield.getText());
                preparedStatement.setObject(17, ((Bz) (companyInfo.zczbbzComboBox.getSelectedItem())).getBzid());
                preparedStatement.setObject(18, companyInfo.jyfwFeild.getText());
                preparedStatement.setObject(19, "1");
                preparedStatement.setObject(20, "3");
                preparedStatement.setObject(21, "3");
                preparedStatement.setObject(22, ((Djjg) (nameInfo.djjgComboBox.getSelectedItem())).getDjjgid());
                preparedStatement.setObject(23, nameInfo.JYQXQTextField.getText());
                preparedStatement.setObject(24, nameInfo.JYQXZTextField.getText());
                preparedStatement.setObject(25, this.nameInfo.nsmcField.getText());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }

            //将投资人信息添加到数据库
            try {
                connection = JDBC.getConnection();
                String SQL = " update investor_information set TZRLX= ?,ZJLX= ?,ZJHM= ?,CZBL= ?,XB= ?,LXDH= ?,GJ= ?,ZS= ?,DJJG= ? where TZRMC = ?";
                for (int i = 0; i < investorInfo.table.getRowCount(); i++) {
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setObject(10, investorInfo.table.getValueAt(i, 0));
                    for (int j = 0; j < investorInfo.tzrlxComboBox.getItemCount(); j++) {
                        if (investorInfo.table.getValueAt(i, 1).equals(investorInfo.tzrlxComboBox.getItemAt(j).toString())) {
                            preparedStatement.setObject(1, ((Tzrlx) investorInfo.tzrlxComboBox.getItemAt(j)).getTzrid());
                            break;
                        }
                    }
                    for (int j = 0; j < investorInfo.zjlxComboBox.getItemCount(); j++) {
                        if (investorInfo.table.getValueAt(i, 2).equals(investorInfo.zjlxComboBox.getItemAt(j).toString())) {
                            preparedStatement.setObject(2, ((Zjlx) (investorInfo.zjlxComboBox.getItemAt(j))).getZjlxid());
                            break;
                        }
                    }
                    preparedStatement.setObject(3, investorInfo.table.getValueAt(i, 3));
                    preparedStatement.setObject(4, investorInfo.table.getValueAt(i, 4));
                    if (investorInfo.table.getValueAt(i, 4).equals("男")) {
                        preparedStatement.setObject(5, "1");
                    } else {
                        preparedStatement.setObject(5, "2");
                    }
                    preparedStatement.setObject(6, investorInfo.table.getValueAt(i, 6));

                    for (int j = 0; j < investorInfo.gjComboBox.getItemCount(); j++) {
                        if (investorInfo.table.getValueAt(i, 7).equals(investorInfo.gjComboBox.getItemAt(j).toString())) {
                            preparedStatement.setObject(7, ((Gj) (investorInfo.gjComboBox.getItemAt(j))).getGjid());
                            break;
                        }
                    }
                    preparedStatement.setObject(8, investorInfo.table.getValueAt(i, 8));

                    for (int j = 0; j < investorInfo.djjgComboBox.getItemCount(); j++) {
                        if (investorInfo.table.getValueAt(i, 9).equals(investorInfo.djjgComboBox.getItemAt(j).toString())) {
                            preparedStatement.setObject(9, investorInfo.djjgComboBox.getItemAt(j).getDjjgid());
                            break;
                        }
                    }
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }

            //核准日期
            try {
                connection = JDBC.getConnection();
                String SQL = "update market_subject_information set HZRQ = ? where scztmc = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(new Date());
                preparedStatement.setObject(1, dateStr);
                preparedStatement.setObject(2, this.nameInfo.nsmcField.getText());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
            dataVector.remove(table.getSelectedRow());
            DefaultTableModel defaultTableModel1 = new DefaultTableModel(dataVector, headVector);
            table.setModel(defaultTableModel1);
            JOptionPane.showMessageDialog(null, "企业已核准", "企业已核准", JOptionPane.OK_OPTION);
        });

        this.noApproveButton.addActionListener((e) ->
        {
            if (nameInfo.mczhField.getText().isEmpty()) {
                return;
            }
            try {
                connection = JDBC.getConnection();
                String SQL = "update market_subject_information set LCZTDM ='2' ,djjgdm = ?,JYQXQ = ? ,JYQXZ = ?,HZRQ = ? where scztmc = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setObject(1, ((Djjg) (nameInfo.djjgComboBox.getSelectedItem())).getDjjgid());
                preparedStatement.setObject(2, nameInfo.JYQXQTextField.getText());
                preparedStatement.setObject(3, nameInfo.JYQXZTextField.getText());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(new Date());
                preparedStatement.setObject(4, dateStr);
                preparedStatement.setObject(5, this.nameInfo.nsmcField.getText());
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "操作成功", "操作成功", JOptionPane.OK_OPTION);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }
        });

    }
}
