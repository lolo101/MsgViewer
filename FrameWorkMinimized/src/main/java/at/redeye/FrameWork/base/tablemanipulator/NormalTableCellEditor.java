/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.widgets.documentfields.DocumentFieldDateTime;
import at.redeye.FrameWork.widgets.documentfields.DocumentFieldInteger;
import at.redeye.FrameWork.widgets.documentfields.DocumentFieldLimit;
import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBInteger;
import at.redeye.FrameWork.base.bindtypes.DBString;
import at.redeye.FrameWork.base.bindtypes.DBValue;

import java.awt.Color;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author martin
 */
public class NormalTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 1L;
    JTextField component = new JTextField();
    TableDesign tabledesign;
    int last_row = 0;
    int last_col = 0;
    Object current_value;

    public NormalTableCellEditor(TableDesign tabledesign) {
        this.tabledesign = tabledesign;
    }

    public Object getCellEditorValue() {
        /*
        System.out.println( "value+:" + ((JTextField)component).getText() );
        System.out.println( "add row:" + last_row );
         */
        System.out.println("getCellEditorValue");

        tabledesign.edited_cols.add(last_col);
        tabledesign.edited_rows.add(last_row);
        return component.getText();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        System.out.println("getTableCellEditorComponent");

        last_row = TableDesign.getModelRow(table, row);
        last_col =  TableDesign.getModelCol(table, column);

        current_value = value;

        component.setBackground(Color.YELLOW);

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
        System.out.println( "value:" + value.getClass().getName() );

        return component;
    }

    @Override
    public boolean stopCellEditing() {

        System.out.println("Normal stopCellEditing");

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