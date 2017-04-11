/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.bindtypes.DBEnum;
import at.redeye.FrameWork.base.bindtypes.DBEnumAsInteger;

import at.redeye.FrameWork.base.bindtypes.DBSqlAsInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author martin
 */
public class EnumTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 1L;
    JComboBox component = new JComboBox();
    TableDesign tabledesign;
    int last_row = 0;
    int last_col = 0;
    Object current_value;    
    
    public EnumTableCellEditor(TableDesign tabledesign, DBEnum value ) {
        this.tabledesign = tabledesign;
        
        for( String s : value.getPossibleValues() )
        {
            // System.out.println(" xxxxx (" + value.getName() + ") value: " + s);
            component.addItem(s);
        }                
    }

    public EnumTableCellEditor(TableDesign tabledesign, DBEnumAsInteger value) {
        this.tabledesign = tabledesign;
        
        for( String s : value.getPossibleValues() )
        {
            // System.out.println("yyyyyyyyy e as integer (" + value.getName() + ") value: " + s);
            component.addItem(s);
        }
    }

    public Object getCellEditorValue() {
        /*
        System.out.println( "value+:" + ((JTextField)component).getText() );
        System.out.println( "add row:" + last_row );
         */
        // System.out.println("getCellEditorValue");

        tabledesign.edited_cols.add(last_col);
        tabledesign.edited_rows.add(last_row);
        return component.getSelectedItem().toString();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        System.out.println("getTableCellEditorComponent");

        last_row = TableDesign.getModelRow(table, row);
        last_col =  TableDesign.getModelCol(table, column);

        current_value = value;

        DBValue val = (DBValue) tabledesign.rows.get(last_row).get(last_col);

        if( val instanceof  DBSqlAsInteger ) {
            DBSqlAsInteger sql_val = (DBSqlAsInteger) val;

            component.removeAllItems();

            for( String s : sql_val.getPossibleValues() )
            {
               component.addItem(s);
            }
        }

        component.setSelectedItem(value);

        return component;
    }

    @Override
    public boolean stopCellEditing() {

        System.out.println("Enum stopCellEditing");
        return super.stopCellEditing();
    }
}