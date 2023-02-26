package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.bindtypes.DBEnum;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.base.tablemanipulator.TableDesign.ColoredCell;
import at.redeye.FrameWork.utilities.HTMLColor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

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
    private int model_row;
    /**
     * The current model_column being rendered
     */
    private int model_col;

    private boolean notSelected = true;
    private boolean hightlight = true;
    private Font font;
    private Color hColor = new Color(210, 235, 245);
    private Color heColor = new Color(245, 245, 255);
    private Color lColor = new Color(255, 255, 255);
    private Color leColor = new Color(220, 245, 235);
    private final TableDesign tabledesign;

    public NormalCellRenderer(TableDesign tabledesign) {
        this.tabledesign = tabledesign;

        Color c;

        c = HTMLColor.loadLocalColor(FrameWorkConfigDefinitions.SpreadSheetColorEven);
        if (c != null)
            hColor = c;

        c = HTMLColor.loadLocalColor(FrameWorkConfigDefinitions.SpreadSheetColorEvenEditable);
        if (c != null)
            heColor = c;

        c = HTMLColor.loadLocalColor(FrameWorkConfigDefinitions.SpreadSheetColorOdd);
        if (c != null)
            lColor = c;

        c = HTMLColor.loadLocalColor(FrameWorkConfigDefinitions.SpreadSheetColorOddEditable);
        if( c != null )
            leColor  = c;
    }

    @Override
    public Component getTableCellRendererComponent(JTable tbl, Object v, boolean isSelected, boolean isFocused, int row, int col) {
        //Store this info for later use

        hightlight = row % 2 != 0;
        this.row = row;
        this.col = col;
        this.model_col = TableDesign.getModelCol(tbl, col);
        this.model_row = TableDesign.getModelRow(tbl, row);
        this.notSelected = !isSelected;

        if (font == null) {
            font = this.getFont();
        }

        return super.getTableCellRendererComponent(tbl, v, isSelected, isFocused, row, col);
    }

    @Override
    protected void setValue(Object v) {
        Object val = tabledesign.rows.get(model_row).get(model_col);
        if (v instanceof String && val instanceof DBValue) {
            DBValue db_value = (DBValue) val;
            String s = (String) v;
            if (db_value.acceptString(s)) {
                db_value.loadFromString(s);
            }

            if (db_value instanceof DBEnum) {
                super.setValue(((DBEnum<?>) db_value).getLocalizedString());
            } else {
                super.setValue(db_value);
            }
        } else {
            if (v instanceof DBEnum)
                super.setValue(((DBEnum<?>) v).getLocalizedString());
            else
                super.setValue(v);
        }

        //Set colors dependant upon if the row is selected or not
        if (this.notSelected) {
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
        if (this.notSelected) {
            ColoredCell ccell;
            for (int ccellindex = 0; ccellindex < tabledesign.coloredCells.size(); ccellindex++) {
                ccell = tabledesign.coloredCells.get(ccellindex);
                if (ccell.col == model_col && ccell.row == model_row) {
                    this.setBackground(ccell.color);
                    break;
                }
            }
        }

        boolean missing = true;

        for (int ccellindex = 0; ccellindex < tabledesign.tooltipCells.size(); ccellindex++) {
            TableDesign.ToolTipCell cell = tabledesign.tooltipCells.get(ccellindex);
            if (cell.col == model_col && cell.row == model_row) {
                this.setToolTipText(cell.tooltip);
                missing = false;
                break;
            }
        }

        if (missing)
            setToolTipText(null);

        if (tabledesign.edited_rows.contains(model_row) &&
                tabledesign.edited_cols.contains(model_col)) {
            this.setFont(new Font(font.getFamily(), Font.BOLD, font.getSize()));
        }
    }

}