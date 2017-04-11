/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets;

import javax.swing.JComboBox;

/**
 *
 * @author mmattl
 */
public class DBFilterComboBox extends JComboBox {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String colName;

    public DBFilterComboBox () {
        super();

    }

    public DBFilterComboBox (String colName) {
        super();
        this.colName = colName;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

}
