package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.BaseDialogBase;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class TableDesign extends AbstractTableModel {

    public final Set<Integer> edited_cols = new HashSet<>();
    public Set<Integer> edited_rows = new HashSet<>();
    public final List<DBConfig> rows = new ArrayList<>();
    private final BaseDialogBase base;

    public final List<Coll> colls;

    public TableDesign(BaseDialogBase base, List<Coll> colls) {
        this.base = base;
        this.colls = colls;
    }

    void clear() {
        edited_cols.clear();
        edited_rows.clear();
        rows.clear();
    }

    public List<String> getPossibleValues(int row) {
        return Arrays.asList(rows.get(row).getPossibleValues());
    }


    static int getModelCol(JTable table, int col) {
        return table.getColumnModel().getColumn(col).getModelIndex();
    }

    static int getModelRow(JTable table, int row) {
        RowSorter<?> sorter = table.getRowSorter();

        if (sorter == null)
            return row;

        return sorter.convertRowIndexToModel(row);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return colls.size();
    }

    @Override
    public DBValue getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).getAllValues().get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return base.MlM(colls.get(column).title);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return DBValue.class;
    }

    @Override
    public boolean isCellEditable(int rowindex, int columnindex) {
        return colls.get(columnindex).isEditable;
    }

    public void remove(int row) {
        rows.remove(row);

        Set<Integer> er = new HashSet<>();

        for (Integer editedRow : edited_rows) {
            if (editedRow == row) {
                continue;
            }

            if (editedRow < row) {
                er.add(editedRow);
            } else {
                er.add(editedRow - 1);
            }
        }

        edited_rows = er;
    }
}
