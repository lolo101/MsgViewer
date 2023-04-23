package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBFlagInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.widgets.datetime.IDateTimeComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class BindVarBase implements BindVarInterface {

    private final Collection<Pair> pairs = new ArrayList<>();

    @Override
    public void bindVar(JTextField jtext, StringBuffer var) {
        pairs.add(new TextStringPair(jtext, var));
    }

    @Override
    public void bindVar(JTextField jtext, DBValue var) {
        pairs.add(new TextDBStringPair(jtext, var));
    }

    @Override
    public void bindVar(JCheckBox jCDefault, DBFlagInteger _default) {
        pairs.add(new FlagCheckboxPair(jCDefault, _default));
    }

    @Override
    public void bindVar(IDateTimeComponent comp, DBDateTime dateTime) {
        pairs.add(new DateComponentPair(comp, dateTime));
    }

    @Override
    public void bindVar(JComboBox<?> jcombo, DBValue var) {
        pairs.add(new ComboStringPair(jcombo, var));
    }

    /**
     * in jTextArea an eine StringBuffer anbinden
     *
     * @param jtext das Textfeld
     * @param var der StringBuffer
     *
     * Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird dann der
     * demenstprechende Inhalt entweder vom GUI zu Variablen, oder umgekehrt
     * übertragen.
     */
    @Override
    public void bindVar(JTextArea jtext, StringBuffer var) {
        pairs.add(new TextAreaStringBufferPair(jtext, var));
    }

    /**
     * in jTextArea an eine DBValue anbinden
     *
     * @param jtext das Textfeld
     * @param var der DBValue
     *
     * Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird dann der
     * demenstprechende Inhalt entweder vom GUI zu Variablen, oder umgekehrt
     * übertragen.
     */
    @Override
    public void bindVar(JTextArea jtext, DBValue var) {
        pairs.add(new TextAreaDBValuePair(jtext, var));
    }

    @Override
    public void var_to_gui() {
        for (Pair pair : pairs) {
            pair.var_to_gui();
        }
    }

    @Override
    public void gui_to_var() {
        for (Pair pair : pairs) {
            pair.gui_to_var();
        }
    }

    @Override
    public Collection<Pair> getBindVarPairs() {
        return pairs;
    }

    @Override
    public void addBindVarPair(Pair pair) {
        pairs.add(pair);
    }
}
