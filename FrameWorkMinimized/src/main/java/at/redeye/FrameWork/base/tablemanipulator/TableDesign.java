package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.bindtypes.DBValue;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class TableDesign {

    public static class ColoredCell {
        int row;
        int col;
        Color color;
    }

    public static class ToolTipCell {
        int row;
        int col;
        String tooltip;
    }

    public Set<Integer> edited_cols;
    public Set<Integer> edited_rows;
    public List<Vector<Object>> rows = new Vector<>();
    protected Vector<ColoredCell> coloredCells = new Vector<>();
    protected List<ToolTipCell> tooltipCells = new ArrayList<>();

    public static class Coll {

        public final String title;
        public boolean isEditable;
        public TableValidator validator;
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

    public List<String> getAllOfCollSorted(int col) {
        List<String> all = new Vector<>();

        TableValidator validator = null;
        Coll coll = null;

        if (col >= 0 && col < colls.size()) {
            coll = colls.get(col);
            validator = coll.validator;
        }

        if (coll == null || !coll.getDoAutocompleteForAllOfThisColl())
            return all;

        for (List<Object> row : rows) {
            if (row.size() > col && col > 0) {
                Object obj = row.get(col);


                if (validator != null) {
                    all.add(validator.formatData(obj));
                } else {
                    all.add(obj.toString());
                }
            }
        }

        Collections.sort(all);

        return all;
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
