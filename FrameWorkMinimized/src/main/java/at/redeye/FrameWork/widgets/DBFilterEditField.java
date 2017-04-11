/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets;

import javax.swing.JTextField;

/**
 *
 * @author mmattl
 */
public class DBFilterEditField extends JTextField {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String colName;

    public DBFilterEditField() {
        super ();
    }

     public DBFilterEditField(String colName) {
        super();
        this.colName = colName;
     }

     public DBFilterEditField (String text, String colName) {
         super (text);
         this.colName = colName;
     }



    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }



}
