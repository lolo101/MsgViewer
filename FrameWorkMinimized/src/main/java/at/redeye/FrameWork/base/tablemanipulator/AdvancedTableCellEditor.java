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
    AutoCompleteTextField component = new AutoCompleteTextField();
    TableDesign tabledesign;
    int last_row = 0;
    int last_col = 0;
    Object current_value;

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
        last_col =  TableDesign.getModelCol(table, column);
        current_value = value;

        System.out.println("getTableCellEditorComponent col: " + last_col + " row: " + last_row + " Value " + (current_value != null ? current_value.toString() : "(null)"));

        component.enable_complete(false);

        if( tabledesign.colls.get(last_col).getAutoCompleteForCollAtAll() )
        {
            component.set_items(tabledesign.getAllOfCollSorted(last_col));
            component.enable_complete(true);
        }

        component.setBackground(Color.YELLOW);

        System.out.println("comp size: " + component.getPreferredSize());

        if (tabledesign.colls.get(last_col).validator != null) {
            if (value instanceof DBValue) {
                String sc = tabledesign.colls.get(last_col).validator.formatData(value);

                if (sc != null) {
                    component.setBorder(new LineBorder(Color.BLACK));
                    component.setText(sc);
                    return component;
                } else {
                    component.setBorder(new LineBorder(Color.RED));
                    return component;
                }
            } else {

                component.setText((String) value);
                return component;
            }

        } else if (current_value instanceof DBValue) {


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
            } else {
                component.setBorder(new LineBorder(Color.BLACK));
                component.setText(String.valueOf(value));
                return component;
            }

        } else {

            component.setText(String.valueOf(value));
        }

        if( value != null )
            System.out.println( "value:" + value.getClass().getName() );

        return component;
    }

    @Override
    public boolean stopCellEditing() {

        System.out.println("Advanced stopCellEditing");

        if (tabledesign.colls.get(last_col).validator != null) {
            if (!tabledesign.colls.get(last_col).validator.acceptData(component.getText())) {
                component.setBorder(new LineBorder(Color.RED));
                return false;
            }
        }

        if (current_value instanceof DBValue) {
            DBValue val = (DBValue) current_value;
            String s = component.getText();

            boolean do_self = true;

            if (tabledesign.colls.get(last_col).validator != null) {
                if (tabledesign.colls.get(last_col).validator.wantDoLoadSelf()) {
                    do_self = false;

                    if (!tabledesign.colls.get(last_col).validator.loadToValue(val, s, last_row)) {
                        component.setBorder(new LineBorder(Color.RED));
                        return false;
                    }
                }
            }

            if (do_self) {
                if (!val.acceptString(s)) {
                    component.setBorder(new LineBorder(Color.RED));
                    return false;
                } else {
                    val.loadFromString(s);
                }
            }
        }

        component.setBackground(Color.WHITE);

        return super.stopCellEditing();
    }
}