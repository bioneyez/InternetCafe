/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.view;

import internetcafe.cache.MemberListener;
import internetcafe.entity.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Lorand
 */
class MemberTableModel extends AbstractTableModel implements MemberListener {
    private List<Member> entries= new ArrayList<>();
    private static final List<String> columnNames = new ArrayList<>();
    
    static {
        columnNames.add("username");
        columnNames.add("address");
        columnNames.add("idNumber");
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(!entries.isEmpty()) {
            return getValueAt(0, columnIndex).getClass();
        }
        return Object.class;
    }
    
    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String column = columnNames.get(columnIndex);
        Member m = entries.get(rowIndex);
        switch(column) {
            case "username": return m.getUsername();
            case "address" : return m.getAddress();
            case "idNumber": return m.getIdNumber();
            default: throw new IllegalArgumentException(column + ".....");
        }
    }
    
    public Member getRow(int rowIndex) {
        return entries.get(rowIndex);
    }
    
    public void addAllMember(List<Member> members) {
        int firstRow = entries.size();
        entries.addAll(members);
        int lastRow = entries.size()-1;
        fireTableRowsInserted(firstRow,lastRow);
    }

    @Override
    public void memberInserted(Member member) {
        SwingUtilities.invokeLater(() -> {
            addAllMember(Arrays.asList(member));
        });
        
    }
}
