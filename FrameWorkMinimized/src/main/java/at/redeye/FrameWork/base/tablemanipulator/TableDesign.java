package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.bindtypes.DBValue;

import javax.swing.*;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class TableDesign {

    public Set<Integer> edited_cols;
    public Set<Integer> edited_rows;
    public List<Vector<Object>> rows = new ArrayList<>();

    public static class Coll {

        public final String title;
        public boolean isEditable;
        DBValue dbval;
        private boolean doAutocompleteForAllOfThisColl = true;

        public Coll(String title, Boolean isEditable, DBValue val) {
            this.title = title;
            this.isEditable = isEditable;
            this.dbval = val;
        }

        void setEditable(boolean isEditable) {
            this.isEditable = isEditable;
        }

        void setDoAutocompleteForAllOfThisColl(boolean state) {
            doAutocompleteForAllOfThisColl = state;
        }

        private boolean getDoAutocompleteForAllOfThisColl() {
            return doAutocompleteForAllOfThisColl;
        }

    }

    public Vector<Coll> colls;

    public TableDesign(Vector<Coll> colls) {
        this.colls = colls;
        this.edited_cols = new HashSet<>();
        this.edited_rows = new HashSet<>();
    }

    void clear() {
        edited_cols.clear();
        edited_rows.clear();
        rows.clear();
    }

    public List<String> getAllOfCollSorted(int col) {
        Coll coll = null;

        if (col >= 0 && col < colls.size()) {
            coll = colls.get(col);
        }

        if (coll == null || !coll.getDoAutocompleteForAllOfThisColl())
            return emptyList();

        return rows.stream()
                .filter(row -> row.size() > col && col > 0)
                .map(row -> row.get(col))
                .map(Object::toString)
                .sorted()
                .collect(toList());
    }


    static int getModelCol( JTable table, int col )
    {
        return table.getColumnModel().getColumn(col).getModelIndex();
    }

    static int getModelRow( JTable table, int row )
    {
        RowSorter<?> sorter = table.getRowSorter();

        if( sorter == null )
            return row;

        return sorter.convertRowIndexToModel( row );
    }
}
