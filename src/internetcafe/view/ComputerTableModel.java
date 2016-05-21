/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.view;

import internetcafe.cache.ComputerListener;
import internetcafe.entity.Computer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Lorand
 */
class ComputerTableModel extends AbstractTableModel implements ComputerListener {
    private List<Computer> entries= new ArrayList<>();
    private static final List<String> columnNames = new ArrayList<>();
    
    static {
        columnNames.add("ID");
        columnNames.add("specification");
        columnNames.add("operationSystem");
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
        Computer c = entries.get(rowIndex);
        switch(column) {
            case "ID": return c.getId();
            case "specification" : return c.getSpecification();
            case "operationSystem": return c.getOperationSystem();
            default: throw new IllegalArgumentException(column + ".....");
        }
    }
    
    public void addAllComputer(List<Computer> computers) {
        int firstRow = entries.size();
        entries.addAll(computers);
        int lastRow = entries.size()-1;
        fireTableRowsInserted(firstRow,lastRow);
    }

    @Override
    public void computerInserted(Computer computer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
