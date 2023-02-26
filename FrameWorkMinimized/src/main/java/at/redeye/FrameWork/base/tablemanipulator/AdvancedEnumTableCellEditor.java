package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.bindtypes.DBEnum;
import at.redeye.FrameWork.base.bindtypes.DBEnumAsInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.widgets.AutoCompleteCombo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class AdvancedEnumTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 1L;
    private final AutoCompleteCombo component = new AutoCompleteCombo();
    private final TableDesign tabledesign;
    private int last_row;
    private int last_col;
    private Object current_value;

    public AdvancedEnumTableCellEditor(TableDesign tabledesign, DBEnum<?> value) {
        this.tabledesign = tabledesign;

        for (String s : value.getLocalizedPossibleValues()) {
            component.addItem(s);
        }

        component.setEditable(true);
    }

    public AdvancedEnumTableCellEditor(TableDesign tabledesign, DBEnumAsInteger value) {
        this.tabledesign = tabledesign;

        for( String s : value.getPossibleValues() )
        {
            component.addItem(s);
        }

        component.setEditable(true);
    }

    @Override
    public Object getCellEditorValue() {
        tabledesign.edited_cols.add(last_col);
        tabledesign.edited_rows.add(last_row);
        return component.getText();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        last_row = TableDesign.getModelRow(table, row);
        last_col = TableDesign.getModelCol(table, column);

        current_value = value;

        System.out.println("getTableCellEditorComponent for column " + last_col);

        java.awt.EventQueue.invokeLater(() -> {
            if (component.isVisible())
                component.requestFocus();
        });

        component.setBorder(new LineBorder(Color.BLACK));

        if (value instanceof DBEnum)
            component.setSelectedItem(((DBEnum<?>) value).getLocalizedString());
        else
            component.setSelectedItem(value);

        return component;
    }

    @Override
    public boolean stopCellEditing() {

       System.out.println("Advanced stopCellEditing");
       component.hidePopup();

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