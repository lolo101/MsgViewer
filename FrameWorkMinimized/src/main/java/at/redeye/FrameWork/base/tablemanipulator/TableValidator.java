package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.bindtypes.DBValue;

import javax.swing.*;

public abstract class TableValidator {

    public String formatData(Object data) {
        return String.valueOf(data);
    }

    public boolean rejectData(String data) {
        return false;
    }

    public boolean loadToValue(DBValue val, String s, int row) {
        return false;
    }

    public boolean wantDoLoadSelf() {
        return false;
    }

    /*
     * This Function is called before the JTable gets The Editor Component
     * param: component the component
     * value: value of the row and column. Maybe a DBValue
     * tabledisgn: the tabledesign
     * row: row of the table
     * column: column of the table
     */
    public void updateComponentBeforeEdit(JComponent component, Object value, TableDesign tabledesign, int row, int column) {
    }
}