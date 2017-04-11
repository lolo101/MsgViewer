/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBFlagInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.widgets.datetime.IDateTimeComponent;
import javax.swing.JTextArea;

/**
 * 
 * @author martin
 */
public class BasePanel extends javax.swing.JPanel implements BindVarInterface {
	protected BindVarBase bind_vars = new BindVarBase();

	@Override
	public void bindVar(JTextField jtext, StringBuffer var) {
		bind_vars.bindVar(jtext, var);
	}

	@Override
	public void var_to_gui() {
		bind_vars.var_to_gui();
	}

	@Override
	public void gui_to_var() {
		bind_vars.gui_to_var();
	}

	@Override
	public void bindVar(JTextField jtext, DBValue var) {
		bind_vars.bindVar(jtext, var);
	}

	@Override
	public void bindVar(JComboBox jComboBox, DBValue var) {
		bind_vars.bindVar(jComboBox, var);
	}

	@Override
	public void bindVar(IDateTimeComponent comp, DBDateTime dateTime) {
		bind_vars.bindVar(comp, dateTime);
	}

	@Override
	public void bindVar(JCheckBox box, DBFlagInteger var) {
		bind_vars.bindVar(box, var);
	}

	/**
	 * in jTextArea an eine StringBuffer anbinden
	 * 
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            der StringBuffer
	 * 
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
        @Override
	public void bindVar(JTextArea jtext, StringBuffer var) {
		bind_vars.bindVar(jtext, var);
	}   
        
	/**
	 * in jTextArea an eine DBValue anbinden
	 * 
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            der DBValue
	 * 
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
        @Override
	public void bindVar(JTextArea jtext, DBValue var) {
		bind_vars.bindVar(jtext, var);
	}             
        
	@Override
	public Collection<Pair> getBindVarPairs() {
		return bind_vars.getBindVarPairs();
	}

	@Override
	public void addBindVarPair(Pair pair) {
		bind_vars.addBindVarPair(pair);
	}

}
