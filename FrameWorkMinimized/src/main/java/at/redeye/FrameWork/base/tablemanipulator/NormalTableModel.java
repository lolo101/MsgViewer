package at.redeye.FrameWork.base.tablemanipulator;

import javax.swing.table.DefaultTableModel;

public class NormalTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;
    private final TableDesign tabledesign;

    public NormalTableModel(TableDesign tabledesign) {
        this.tabledesign = tabledesign;
    }

    @Override
    public boolean isCellEditable(int rowindex, int columnindex) {
        return tabledesign.colls.get(columnindex).isEditable;
    }
}