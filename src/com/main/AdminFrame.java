package com.main;

import com.admin.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

public class AdminFrame extends JFrame {
    private JPanel mainPanel;
    private JSplitPane mainSplitpane;
    private JPanel rightPanel;
    private JPanel leftPanel;

    private JTree contentTree = new JTree();
    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("系统菜单");
    private DefaultMutableTreeNode startPageNode = new DefaultMutableTreeNode("开始页面");
    private StartPage start = new StartPage();

    private DefaultMutableTreeNode nameRegisterNode = new DefaultMutableTreeNode("名称登记");
    private DefaultMutableTreeNode namePreApprovedNode = new DefaultMutableTreeNode("预先核准");
    private DefaultMutableTreeNode nameHasApprovedNode = new DefaultMutableTreeNode("已核调整");
    private DefaultMutableTreeNode nameIndirectNode = new DefaultMutableTreeNode("辅助业务");
    private DefaultMutableTreeNode nameBusinessStatisticsNode = new DefaultMutableTreeNode("业务统计");
    private DefaultMutableTreeNode nameOpinionHandleNode = new DefaultMutableTreeNode("意见处理");

    private DefaultMutableTreeNode registerNode = new DefaultMutableTreeNode("企业登记");
    private DefaultMutableTreeNode registerEstablishmentNode = new DefaultMutableTreeNode("设立登记");
    private DefaultMutableTreeNode registerChangeRegistrationNode = new DefaultMutableTreeNode("变更登记");
    private DefaultMutableTreeNode registerCancellationNode = new DefaultMutableTreeNode("注销登记");
    private DefaultMutableTreeNode registerEquityNode = new DefaultMutableTreeNode("股权分配");
    private DefaultMutableTreeNode registerIndirectNode = new DefaultMutableTreeNode("辅助业务");
    private DefaultMutableTreeNode registerBusinessStatisticsNode = new DefaultMutableTreeNode("业务统计");

    private DefaultMutableTreeNode systemSettingNode = new DefaultMutableTreeNode("系统管理");
    private DefaultMutableTreeNode dataChangeNode = new DefaultMutableTreeNode("数据修改");
    private DefaultMutableTreeNode userManageNode = new DefaultMutableTreeNode("用户");
    private DefaultMutableTreeNode adminManageNode = new DefaultMutableTreeNode("管理员");

    DefaultTreeModel defaultTreeModel;
    DefaultTreeCellRenderer defaultTreeCellRenderer;
    JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    ImageIcon imageIcon;

    public AdminFrame(String adminname, String limit) {
        this.setTitle("工商管理系统   当前管理员: " + adminname);
        this.setSize(1280, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setContentPane(this.mainPanel);
        this.mainSplitpane.setDividerLocation(200);
        this.mainSplitpane.setEnabled(false);
        this.defaultTreeModel = new DefaultTreeModel(this.rootNode);
        this.rootNode.add(this.startPageNode);
        this.imageIcon = new ImageIcon(getClass().getResource("/com/image/business.jpg"));
        this.setIconImage(this.imageIcon.getImage());

        //名称登记
        this.rootNode.add(this.nameRegisterNode);
        this.nameRegisterNode.add(this.namePreApprovedNode);
        this.nameRegisterNode.add(this.nameHasApprovedNode);
        this.nameRegisterNode.add(this.nameOpinionHandleNode);
        this.nameRegisterNode.add(this.nameIndirectNode);
        this.nameRegisterNode.add(this.nameBusinessStatisticsNode);


        //企业登记
        this.rootNode.add(this.registerNode);
        this.registerNode.add(this.registerEstablishmentNode);
        this.registerNode.add(this.registerChangeRegistrationNode);
        this.registerNode.add(this.registerCancellationNode);
        this.registerNode.add(this.registerEquityNode);
        this.registerNode.add(this.registerIndirectNode);
        this.registerNode.add(this.registerBusinessStatisticsNode);

        //系统管理
        this.rootNode.add(this.systemSettingNode);
        this.systemSettingNode.add(this.dataChangeNode);
        this.systemSettingNode.add(this.userManageNode);
        this.systemSettingNode.add(this.adminManageNode);
        //树
        this.contentTree.setModel(defaultTreeModel);
        contentTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                this.setBackground(Color.decode("#1d124e"));
                this.setOpaque(false);
                this.setForeground(Color.RED);
                return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            }
        });
        this.jScrollPane.getViewport().add(this.contentTree);
        this.mainSplitpane.setLeftComponent(this.jScrollPane);
        this.mainSplitpane.setRightComponent(this.start);
        this.contentTree.setSelectionPath(new TreePath(startPageNode.getPath()));
        this.defaultTreeCellRenderer = new DefaultTreeCellRenderer();
        this.contentTree.setCellRenderer(this.defaultTreeCellRenderer);
        //树的监听事件
        this.contentTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) contentTree.getLastSelectedPathComponent();
                if (node.toString().equals("系统菜单")) {
                    return;
                }
                switch (node.getParent().toString() + node.toString()) {
                    case "系统菜单开始页面": {
                        mainSplitpane.setRightComponent(start);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "名称登记预先核准": {
                        NamePreApproved namePreApproved = new NamePreApproved();
                        mainSplitpane.setRightComponent(namePreApproved);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "名称登记已核调整": {
                        NameHasApproved nameHasApproved = new NameHasApproved();
                        mainSplitpane.setRightComponent(nameHasApproved);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "名称登记辅助业务": {
                        NameIndirect nameIndirect = new NameIndirect();
                        mainSplitpane.setRightComponent(nameIndirect);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "名称登记意见处理": {
                        NameOpinionHandle nameOpinionHandle = new NameOpinionHandle();
                        mainSplitpane.setRightComponent(nameOpinionHandle);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "名称登记业务统计": {
                        NameBusinessStatistics nameBusinessStatistics = new NameBusinessStatistics();
                        mainSplitpane.setRightComponent(nameBusinessStatistics);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "企业登记设立登记": {
                        RegisterEstablishment registerEstablishment = new RegisterEstablishment();
                        mainSplitpane.setRightComponent(registerEstablishment);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "企业登记变更登记": {
                        RegisterChangeRegistration registerChangeRegistration = new RegisterChangeRegistration();
                        mainSplitpane.setRightComponent(registerChangeRegistration);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "企业登记注销登记": {
                        RegisterCancellation registerCancellation = new RegisterCancellation();
                        mainSplitpane.setRightComponent(registerCancellation);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "企业登记股权分配": {
                        RegisterEquity registerEquity = new RegisterEquity();
                        mainSplitpane.setRightComponent(registerEquity);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "企业登记辅助业务": {
                        RegisterIndirect registerIndirect = new RegisterIndirect();
                        mainSplitpane.setRightComponent(registerIndirect);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "企业登记业务统计": {
                        RegisterBusinessStatistics registerBusinessStatistics = new RegisterBusinessStatistics();
                        mainSplitpane.setRightComponent(registerBusinessStatistics);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "系统管理数据修改": {
                        if (limit.equals("1")) {
                            JOptionPane.showMessageDialog(null, "您没有权限");
                            return;
                        }
                        DataChange dataChange = new DataChange();
                        mainSplitpane.setRightComponent(dataChange);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "系统管理用户": {
                        if (limit.equals("1")) {
                            JOptionPane.showMessageDialog(null, "您没有权限");
                            return;
                        }
                        UserManage userManage = new UserManage();
                        mainSplitpane.setRightComponent(userManage);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    case "系统管理管理员": {
                        if (limit.equals("1")) {
                            JOptionPane.showMessageDialog(null, "您没有权限");
                            return;
                        }
                        AdminManage adminManage = new AdminManage();
                        mainSplitpane.setRightComponent(adminManage);
                        mainSplitpane.setDividerLocation(200);
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        this.setVisible(true);
    }

}
