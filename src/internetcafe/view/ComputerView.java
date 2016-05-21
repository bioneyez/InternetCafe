/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.view;

import internetcafe.controller.ComputerController;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Lorand
 */
public class ComputerView extends JFrame{
    private ComputerController computerController;
    private JTable table;
    private ComputerTableModel computerTableModel;
    
    public ComputerView(ComputerController computerController) {
        this.computerController = computerController;
        setSize(600,400);
        initTable();
    }
    
    private void initTable() {
        computerTableModel = new ComputerTableModel();
        computerTableModel.addAllComputer(computerController.getAllComputers());
        
        table = new JTable(computerTableModel);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                  JTable target = (JTable)e.getSource();
                  int row = target.getSelectedRow();
                  int column = target.getSelectedColumn();
                  System.out.println(row + " " +  column);
                  
                   
                 }
                }
             });
        
        
        
        JScrollPane sp = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        
        add(sp);
    }
}
