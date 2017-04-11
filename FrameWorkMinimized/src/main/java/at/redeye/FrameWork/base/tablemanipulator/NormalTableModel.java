/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.tablemanipulator;


import javax.swing.table.DefaultTableModel;

/**
 *
 * @author martin
 */
public class NormalTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;
    TableDesign tabledesign;

    public NormalTableModel(TableDesign tabledesign) {
        super();
        this.tabledesign = tabledesign;
    }

    @Override
    public boolean isCellEditable(int rowindex, int columnindex) {
        return tabledesign.colls.get(columnindex).isEditable;
    }
}