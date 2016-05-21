/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.view;

import internetcafe.controller.ComputerController;
import internetcafe.controller.MemberController;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Lorand
 */
public class MemberView extends JFrame{
    private MemberController memberController;
    private ComputerController computerController;
    private JTable table;
    private MemberTableModel memberTableModel;
    
    public MemberView(MemberController memberController,ComputerController computerController) {
        this.computerController = computerController;
        this.memberController = memberController;
        setSize(600,400);
        initTable();
    }
    
    private void initTable() {
        memberTableModel = new MemberTableModel();
        memberTableModel.addAllMember(memberController.getAllMembers());
        
        table = new JTable(memberTableModel);
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    new MemberInfoView(memberController,computerController,memberTableModel.getRow(row)).setVisible(true);
                    System.out.println(row);
                 }
                }
             });
        JScrollPane sp = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        
        add(sp);
    }
}
