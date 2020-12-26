package com.user;

import com.combobox.*;
import com.jdbc.JDBC;
import com.user.preapproved.CompanyInfo;
import com.user.preapproved.InvestorInfo;
import com.user.preapproved.NameInfo;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreApproved extends JTabbedPane {
    Connection connection = null;
    //主字体
    private Font mainFont = new Font("宋体", Font.BOLD, 20);
    private NameInfo nameInfo = new NameInfo();
    private CompanyInfo companyInfo = new CompanyInfo();
    private InvestorInfo investorInfo = new InvestorInfo();

    private JButton lastButton = new JButton("上一步");
    private JButton nextButton = new JButton("下一步");
    private JButton lastButton2 = new JButton("上一步");
    private JButton nextButton2 = new JButton("下一步");
    private JButton lastButton3 = new JButton("上一步");
    private JButton completeButton = new JButton("完成");

    //构造函数
    public PreApproved(String username) {
        this.setFont(mainFont);
        this.addTab("名称信息", nameInfo);
        this.addTab("企业信息", companyInfo);
        this.addTab("投资人信息", investorInfo);
        this.setEnabled(false);
        this.nextButton.setBounds(680, 680, 100, 30);
        this.lastButton.setBounds(280, 680, 100, 30);
        this.nextButton2.setBounds(680, 680, 100, 30);
        this.lastButton2.setBounds(280, 680, 100, 30);
        this.completeButton.setBounds(680, 680, 100, 30);
        this.lastButton3.setBounds(280, 680, 100, 30);
        this.lastButton.setEnabled(false);
        this.nameInfo.add(lastButton);
        this.nameInfo.add(nextButton);
        this.companyInfo.add(lastButton2);
        this.companyInfo.add(nextButton2);
        this.investorInfo.add(lastButton3);
        this.investorInfo.add(completeButton);

        this.nextButton.addActionListener((e) ->
        {
            if (nameInfo.MCRemind() == false || nameInfo.gmlxFidld.getText().isEmpty() || nameInfo.mczhField.getText().isEmpty() || nameInfo.zhpyField.getText().isEmpty() || nameInfo.nsmcField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "请检查资料", "请检查资料", JOptionPane.OK_OPTION);
                return;
            }
            this.setSelectedIndex(1);
        });
        this.lastButton2.addActionListener((e) ->
        {
            this.setSelectedIndex(0);
        });
        this.nextButton2.addActionListener((e) ->
        {
            if (companyInfo.zsszdzField.getText().isEmpty() || companyInfo.lxdhfield.getText().isEmpty() || companyInfo.yzbmfidld.getText().isEmpty() || companyInfo.zczbfield.getText().isEmpty() || companyInfo.jyfwFeild.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "请检查是否填写完整", "请检查是否填写完整", JOptionPane.OK_OPTION);
                return;
            }
            this.setSelectedIndex(2);
        });
        this.lastButton3.addActionListener((e) ->
        {
            this.setSelectedIndex(1);
        });

        //完成的监听事件
        this.completeButton.addActionListener((e) ->
        {
            if (this.investorInfo.table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "不能没有投资人", "不能没有投资人", JOptionPane.OK_OPTION);
                return;
            }
            int value = 0;
            for (int i = 0; i < this.investorInfo.table.getRowCount(); i++) {
                if (this.investorInfo.table.getValueAt(i, 1).equals("企业法人")) {
                    value++;
                }
            }
            if (value != 1) {
                JOptionPane.showMessageDialog(null, "一个企业法人", "一个企业法人", JOptionPane.OK_OPTION);
                return;
            }
            if (nameInfo.MCRemind() == false) {
                JOptionPane.showMessageDialog(null, "不能再次提交", "不能再次提交", JOptionPane.OK_OPTION);
                return;
            }

            //将企业信息添加到数据库
            try {
                this.connection = JDBC.getConnection();
                String SQL = "insert into market_subject_information (GMLX,XZQHLX,ZH,BXZH1,BXZH2,ZHPY,SCZTMC,HYDM,LXDM,ZLDM,ZCH,ZS,ZZXS,LXDH,YZBM,ZCZB,TZBZ,JYFW,JGZTDM,LCZTDM,YWLX,USERNAME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
                preparedStatement.setObject(1, this.nameInfo.gmlxFidld.getText());
                preparedStatement.setObject(2, ((Xzqhlx) (this.nameInfo.xzqhlxComboBox.getSelectedItem())).getXzqhlxid());
                preparedStatement.setObject(3, this.nameInfo.mczhField.getText());
                preparedStatement.setObject(4, this.nameInfo.bxzh1Field.getText());
                preparedStatement.setObject(5, this.nameInfo.bxzh2Feild.getText());
                preparedStatement.setObject(6, this.nameInfo.zhpyField.getText());
                preparedStatement.setObject(7, this.nameInfo.nsmcField.getText());
                preparedStatement.setObject(8, ((Hymc) (this.nameInfo.hymcJComboBox.getSelectedItem())).getHymcid());
                preparedStatement.setObject(9, ((Qylx) (this.companyInfo.qylxComboBox.getSelectedItem())).getQylxid());
                preparedStatement.setObject(10, ((Qyzl) (this.companyInfo.qyzlComboBox.getSelectedItem())).getQyzlid());
                preparedStatement.setObject(11, "313131313131313");
                preparedStatement.setObject(12, this.companyInfo.zsszdzField.getText());
                preparedStatement.setObject(13, ((Zzxs) (this.companyInfo.zzxsComboBox.getSelectedItem())).getZzxsid());
                preparedStatement.setObject(14, this.companyInfo.lxdhfield.getText());
                preparedStatement.setObject(15, this.companyInfo.yzbmfidld.getText());
                preparedStatement.setObject(16, this.companyInfo.zczbfield.getText());
                preparedStatement.setObject(17, ((Bz) (this.companyInfo.zczbbzComboBox.getSelectedItem())).getBzid());
                preparedStatement.setObject(18, this.companyInfo.jyfwFeild.getText());
                preparedStatement.setObject(19, "9");
                preparedStatement.setObject(20, "1");
                preparedStatement.setObject(21, "2");
                preparedStatement.setObject(22, username);
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(connection);
            }

            //将投资人信息添加到数据库
            try {
                this.connection = JDBC.getConnection();
                String SQL = "insert into investor_information (TZRMC,TZRLX,ZJLX,ZJHM,CZBL,XB,LXDH,GJ,ZS,DJJG) values (?,?,?,?,?,?,?,?,?,?)";
                for (int i = 0; i < investorInfo.table.getRowCount(); i++) {
                    PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
                    preparedStatement.setObject(1, this.investorInfo.table.getValueAt(i, 0));
                    for (int j = 0; j < this.investorInfo.tzrlxComboBox.getItemCount(); j++) {
                        if (this.investorInfo.table.getValueAt(i, 1).equals(this.investorInfo.tzrlxComboBox.getItemAt(j).toString())) {
                            preparedStatement.setObject(2, ((Tzrlx) (this.investorInfo.tzrlxComboBox.getItemAt(j))).getTzrid());
                        }
                    }
                    for (int j = 0; j < this.investorInfo.zjlxComboBox.getItemCount(); j++) {
                        if (this.investorInfo.table.getValueAt(i, 2).equals(this.investorInfo.zjlxComboBox.getItemAt(j).toString())) {
                            preparedStatement.setObject(3, ((Zjlx) (this.investorInfo.zjlxComboBox.getItemAt(j))).getZjlxid());
                        }
                    }
                    preparedStatement.setObject(4, this.investorInfo.table.getValueAt(i, 3));
                    preparedStatement.setObject(5, this.investorInfo.table.getValueAt(i, 4));
                    if (this.investorInfo.table.getValueAt(i, 4).equals("男")) {
                        preparedStatement.setObject(6, "1");
                    } else {
                        preparedStatement.setObject(6, "2");
                    }
                    preparedStatement.setObject(7, this.investorInfo.table.getValueAt(i, 6));

                    for (int j = 0; j < this.investorInfo.gjComboBox.getItemCount(); j++) {
                        if (this.investorInfo.table.getValueAt(i, 7).equals(this.investorInfo.gjComboBox.getItemAt(j).toString())) {
                            preparedStatement.setObject(8, ((Gj) (this.investorInfo.gjComboBox.getItemAt(j))).getGjid());
                        }
                    }
                    preparedStatement.setObject(9, this.investorInfo.table.getValueAt(i, 8));

                    for (int j = 0; j < this.investorInfo.djjgComboBox.getItemCount(); j++) {
                        if (this.investorInfo.table.getValueAt(i, 9).equals(this.investorInfo.djjgComboBox.getItemAt(j).toString())) {
                            preparedStatement.setObject(10, this.investorInfo.djjgComboBox.getItemAt(j).getDjjgid());
                            break;
                        }
                    }
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(this.connection);
            }

            //企业和投资人多对多的关系添加到数据库
            try {
                this.connection = JDBC.getConnection();
                String SQL = "insert into tzrqy (tzr,tzqymc) values(?,?)";
                PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
                for (int i = 0; i < this.investorInfo.table.getRowCount(); i++) {
                    preparedStatement.setObject(1, this.investorInfo.table.getValueAt(i, 0));
                    preparedStatement.setObject(2, this.nameInfo.nsmcField.getText());
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(this.connection);
            }

            //补充表增加一条;
            try {
                this.connection = JDBC.getConnection();
                String SQL = "insert into market_subject_supplementary_information (bzsm) values(?)";
                PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
                preparedStatement.setObject(1, "预先核准创表");
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(this.connection);
            }

            //将补充表标识加到企业信息表
            try {
                this.connection = JDBC.getConnection();
                String SQL = "update market_subject_information set BCBS = WYBS where scztmc = ?";
                PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
                preparedStatement.setObject(1, nameInfo.nsmcField.getText());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                JDBC.returnConnection(this.connection);
            }
            this.nameInfo.nsmcField.setText(this.nameInfo.nsmcField.getText() + " ");
            JOptionPane.showMessageDialog(null, "预先核准已申请", "预先核准已申请", JOptionPane.OK_OPTION);
        });
    }
}
