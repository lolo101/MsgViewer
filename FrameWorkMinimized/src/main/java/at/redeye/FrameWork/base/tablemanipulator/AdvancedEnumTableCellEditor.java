/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.bindtypes.DBEnum;
import at.redeye.FrameWork.base.bindtypes.DBEnumAsInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.widgets.AutoCompleteCombo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 *
 * @author martin
 */
public class AdvancedEnumTableCellEditor extends AbstractCellEditor implements TableCellEditor {


    private static final long serialVersionUID = 1L;
    AutoCompleteCombo component = new AutoCompleteCombo();
    TableDesign tabledesign;
    int last_row = 0;
    int last_col = 0;
    Object current_value;

    public AdvancedEnumTableCellEditor(TableDesign tabledesign, DBEnum value ) {
        this.tabledesign = tabledesign;

        for( String s : value.getLocalizedPossibleValues() )
        {
            component.addItem(s);
        }

        // component.set_items(value.getPossibleValues());
        component.setEditable(true);
    }

    public AdvancedEnumTableCellEditor(TableDesign tabledesign, DBEnumAsInteger value) {
        this.tabledesign = tabledesign;

        for( String s : value.getPossibleValues() )
        {
            component.addItem(s);
        }

        // component.set_items(value.getPossibleValues());
        component.setEditable(true);
    }

    @Override
    public Object getCellEditorValue() {
        /*
        System.out.println( "value+:" + ((JTextField)component).getText() );
        System.out.println( "add row:" + last_row );
         */
        // System.out.println("getCellEditorValue");

        tabledesign.edited_cols.add(last_col);
        tabledesign.edited_rows.add(last_row);
        // return component.getSelectedItem().toString();
        return component.getText();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        last_row = TableDesign.getModelRow(table, row);
        last_col =  TableDesign.getModelCol(table, column);

        current_value = value;

        System.out.println("getTableCellEditorComponent for column " + last_col);

       java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                if(component.isVisible())
                    component.requestFocus();
            }
        });

        component.setBorder(new LineBorder(Color.BLACK));

        if( tabledesign.colls.get(last_col).validator != null )
        {
            tabledesign.colls.get(last_col).validator.updateComponentBeforeEdit(component, value, tabledesign, last_row, last_col);
        }

        if( value instanceof DBEnum )
            component.setSelectedItem(((DBEnum)value).getLocalizedString());
        else
            component.setSelectedItem(value);

        return component;
    }

    @Override
    public boolean stopCellEditing() {

       System.out.println("Advanced stopCellEditing");
       component.hidePopup();

        if (tabledesign.colls.get(last_col).validator != null) {
            if (!tabledesign.colls.get(last_col).validator.acceptData(component.getText())) {
                component.setBorder(new LineBorder(Color.RED));
                return false;
            }
        }

       DBValue val = null;

        if (current_value instanceof DBValue) {
           val = (DBValue) current_value;
        } else {
           Object o =  tabledesign.rows.get(last_row).get(last_col);
           if( o instanceof DBValue )
               val = (DBValue) o;
        }

       if( val != null )
       {
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