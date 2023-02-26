package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBInteger;
import at.redeye.FrameWork.base.bindtypes.DBString;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.widgets.AutoCompleteTextField;
import at.redeye.FrameWork.widgets.documentfields.DocumentFieldDateTime;
import at.redeye.FrameWork.widgets.documentfields.DocumentFieldInteger;
import at.redeye.FrameWork.widgets.documentfields.DocumentFieldLimit;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class AdvancedTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 1L;
    private final AutoCompleteTextField component = new AutoCompleteTextField();
    private final TableDesign tabledesign;
    private int last_row;
    private int last_col;
    private Object current_value;

    public AdvancedTableCellEditor(TableDesign tabledesign) {
        this.tabledesign = tabledesign;
    }

    public Object getCellEditorValue() {
        System.out.println("getCellEditorValue");

        tabledesign.edited_cols.add(last_col);
        tabledesign.edited_rows.add(last_row);
        return component.getText();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        last_row = TableDesign.getModelRow(table, row);
        last_col = TableDesign.getModelCol(table, column);
        current_value = value;

        System.out.println("getTableCellEditorComponent col: " + last_col + " row: " + last_row + " Value " + (current_value != null ? current_value.toString() : "(null)"));

        component.enable_complete(false);

        component.set_items(tabledesign.getAllOfCollSorted(last_col));
        component.enable_complete(true);

        component.setBackground(Color.YELLOW);

        System.out.println("comp size: " + component.getPreferredSize());

        if (current_value instanceof DBValue) {
            if (current_value instanceof DBString) {
                DBString s = (DBString) current_value;
                component.setDocument(new DocumentFieldLimit(s.getMaxLen()));

            } else if (current_value instanceof DBInteger) {

                component.setDocument(new DocumentFieldInteger());

            } else if (current_value instanceof DBDateTime) {

                component.setDocument(new DocumentFieldDateTime());

            }

            DBValue val = (DBValue) current_value;

            if (!val.acceptString(String.valueOf(value))) {
                component.setBorder(new LineBorder(Color.RED));
                return component;
            }
            component.setBorder(new LineBorder(Color.BLACK));
            component.setText(String.valueOf(value));
            return component;

        }

        component.setText(String.valueOf(value));

        if( value != null )
            System.out.println( "value:" + value.getClass().getName() );

        return component;
    }

    @Override
    public boolean stopCellEditing() {

        System.out.println("Advanced stopCellEditing");

        if (current_value instanceof DBValue) {
            DBValue val = (DBValue) current_value;
            String s = component.getText();

            if (!val.acceptString(s)) {
                component.setBorder(new LineBorder(Color.RED));
                return false;
            }
            val.loadFromString(s);
        }

        component.setBackground(Color.WHITE);

        return super.stopCellEditing();
    }
}