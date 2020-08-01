/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.bindtypes.DBEnum;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.base.tablemanipulator.TableDesign.ColoredCell;
import at.redeye.FrameWork.utilities.HTMLColor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author martin
 */
public class NormalCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    /**
     * The current row being rendered
     */
    protected int row;
    /**
     * The current column being rendered
     */
    protected int col;

    /**
     * The current model_row being rendered
     */
    protected int model_row;
    /**
     * The current model_column being rendered
     */
    protected int model_col;

    /**
     * If this cell is part of the "selected" row
     */
    protected boolean isSelected;
    /**
     * If this cell is in focus (the one clicked upon)
     */
    protected boolean isFocused;
    boolean hightlight = true;
    Font font = null;
    Color hColor = new Color(210, 235, 245);
    Color heColor = new Color(245, 245, 255);
    Color lColor = new Color(255, 255, 255);
    Color leColor = new Color(220, 245, 235);
    TableDesign tabledesign;

    public NormalCellRenderer(Root root, TableDesign tabledesign) {
        this.tabledesign = tabledesign;

        Color c;

        c = HTMLColor.loadLocalColor(root, FrameWorkConfigDefinitions.SpreadSheetColorEven);
        if( c != null )
            hColor  = c;

        c = HTMLColor.loadLocalColor(root, FrameWorkConfigDefinitions.SpreadSheetColorEvenEditable );
        if( c != null )
            heColor  = c;

        c  = HTMLColor.loadLocalColor(root, FrameWorkConfigDefinitions.SpreadSheetColorOdd );
        if( c != null )
            lColor  = c;

        c = HTMLColor.loadLocalColor(root, FrameWorkConfigDefinitions.SpreadSheetColorOddEditable );
        if( c != null )
            leColor  = c;
    }

    @Override
    public Component getTableCellRendererComponent(JTable tbl, Object v, boolean isSelected, boolean isFocused, int row, int col) {
        //Store this info for later use

        if (row % 2 != 0) {
            hightlight = true;
        } else {
            hightlight = false;
        }
        this.row = row;
        this.col = col;
        this.model_col = TableDesign.getModelCol(tbl, col);
        this.model_row = TableDesign.getModelRow(tbl, row);
        this.isSelected = isSelected;
        this.isFocused = isFocused;

        if (font == null) {
            font = this.getFont();
        }

        return super.getTableCellRendererComponent(tbl, v, isSelected, isFocused, row, col);
    }

    @Override
    protected void setValue(Object v) {
        // System.out.println("setValue: " + row + " " + " col " + col + " " + v.toString());

        // System.out.println( tabledesign.colls.get(col).Title );
        if (tabledesign.colls.get(model_col).validator != null) {
            if (v instanceof DBValue) {
                String res = tabledesign.colls.get(model_col).validator.formatData(v);
                super.setValue(res);
            } else {
                Object val = tabledesign.rows.get(model_row).get(model_col);

                if (val instanceof DBValue && v instanceof String) {
                    tabledesign.colls.get(model_col).validator.loadToValue((DBValue) val, (String) v,model_row);
                    String res = tabledesign.colls.get(model_col).validator.formatData(val);
                    super.setValue(res);
                } else {
                    super.setValue(v);
                }
            }
        } else {
            Object val = tabledesign.rows.get(model_row).get(model_col);
            if (v instanceof String && val instanceof DBValue) {
                DBValue db_value = (DBValue) val;
                String s = (String) v;
                if (db_value.acceptString(s)) {
                    db_value.loadFromString(s);
                }

                if( db_value instanceof DBEnum )
                {
                    super.setValue( ((DBEnum)db_value).getLocalizedString() );
                }
                else
                {
                    super.setValue(db_value);
                }
            } else {


                if( v instanceof DBEnum )
                    super.setValue( ((DBEnum)v).getLocalizedString() );
                else
                    super.setValue(v);
            }
        }

        //Set colors dependant upon if the row is selected or not
        if (!this.isSelected) {
            if (hightlight) {
                if (!tabledesign.colls.get(model_col).isEditable) {
                    this.setBackground(hColor);
                } else {
                    this.setBackground(heColor);
                }
            } else {

                if (!tabledesign.colls.get(model_col).isEditable) {
                    this.setBackground(lColor);
                } else {
                    this.setBackground(leColor);
                }
            }
        }

        // User set color
        if (!this.isSelected) {
            ColoredCell ccell;
            for (int ccellindex= 0; ccellindex < tabledesign.coloredCells.size(); ccellindex++) {
                ccell = tabledesign.coloredCells.get(ccellindex);
                if (ccell.col == model_col && ccell.row == model_row) {
                    this.setBackground(ccell.color);
                    break;
                }
            }
        }

        boolean found = false;

        for (int ccellindex = 0; ccellindex < tabledesign.tooltipCells.size(); ccellindex++) {
            TableDesign.ToolTipCell cell = tabledesign.tooltipCells.get(ccellindex);
            if (cell.col == model_col && cell.row == model_row) {
                this.setToolTipText(cell.tooltip);
                found = true;
                break;
            }
        }

        if( !found )
            setToolTipText(null);


        if (tabledesign.edited_rows.contains(model_row) &&
                tabledesign.edited_cols.contains(model_col)) {
            this.setFont(new Font(font.getFamily(), Font.BOLD, font.getSize()));
        }
    }

}