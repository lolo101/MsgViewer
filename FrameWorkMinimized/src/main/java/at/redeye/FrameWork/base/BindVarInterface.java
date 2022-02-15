package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBFlagInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.widgets.datetime.IDateTimeComponent;

import javax.swing.*;
import java.util.Collection;
import java.util.Date;

public interface BindVarInterface {

    interface Pair {

        void gui_to_var();

        void var_to_gui();

        Object get_first();
    }

    class TextStringPair implements Pair {

        JTextField textfield;
        StringBuffer value;

        public TextStringPair(JTextField textfield, StringBuffer value) {
            this.textfield = textfield;
            this.value = value;
        }

        @Override
        public void gui_to_var() {
            value.delete(0, value.length());
            value.append(textfield.getText());
        }

        @Override
        public void var_to_gui() {
            textfield.setText(value.toString());
        }

        @Override
        public JTextField get_first() {
            return textfield;
        }

    }

    class ComboStringPair implements Pair {

        JComboBox<?> combo;
        DBValue value;

        public ComboStringPair(JComboBox<?> combo, DBValue value) {
            this.combo = combo;
            this.value = value;
        }

        @Override
        public void gui_to_var() {
            Object o = combo.getSelectedItem();
            value.loadFromString((o != null ? o.toString() : ""));
        }

        @Override
        public void var_to_gui() {
            combo.setSelectedItem(value.toString());
        }

        @Override
        public JComboBox<?> get_first() {
            return combo;
        }

    }

    class DateComponentPair implements Pair {

        IDateTimeComponent comp;
        DBDateTime value;

        public DateComponentPair(IDateTimeComponent comp, DBDateTime value) {
            this.comp = comp;
            this.value = value;
        }

        @Override
        public void gui_to_var() {
            String date_str = comp.getDate();

            if (date_str.trim().isEmpty())
                value.loadFromCopy(new Date(0));
            else
                value.loadFromString(date_str);
        }

        @Override
        public void var_to_gui() {
            comp.setDate(DBDateTime.getStdString(value.getValue()));
        }

        @Override
        public IDateTimeComponent get_first() {
            return comp;
        }

    }

    class TextDBStringPair implements Pair {

        JTextField textfield;
        DBValue value;

        public TextDBStringPair(JTextField textfield, DBValue value) {
            this.textfield = textfield;
            this.value = value;
        }

        @Override
        public void gui_to_var() {
            value.loadFromString(textfield.getText());
        }

        @Override
        public void var_to_gui() {
            textfield.setText(value.toString());
        }

        @Override
        public JTextField get_first() {
            return textfield;
        }

    }

    class FlagCheckboxPair implements Pair {

        JCheckBox checkbox;
        DBFlagInteger value;

        public FlagCheckboxPair(JCheckBox checkbox, DBFlagInteger value) {
            this.checkbox = checkbox;
            this.value = value;
        }

        @Override
        public void gui_to_var() {
            if (checkbox.isSelected())
                value.loadFromString("X");
            else
                value.loadFromString(" ");
        }

        @Override
        public void var_to_gui() {
            checkbox.setSelected(value.getValue() != 0);
        }

        @Override
        public JCheckBox get_first() {
            return checkbox;
        }

    }

    class TextAreaDBValuePair implements Pair {

        JTextArea textarea;
        DBValue value;

        public TextAreaDBValuePair(JTextArea textarea, DBValue value) {
            this.textarea = textarea;
            this.value = value;
        }

        @Override
        public void gui_to_var() {
            value.loadFromString(textarea.getText());
        }

        @Override
        public void var_to_gui() {
            textarea.setText(value.toString());
        }

        @Override
        public JTextArea get_first() {
            return textarea;
        }

    }


    class TextAreaStringBufferPair implements Pair {

        JTextArea textarea;
        StringBuffer value;

        public TextAreaStringBufferPair(JTextArea textarea, StringBuffer value) {
            this.textarea = textarea;
            this.value = value;
        }

        @Override
        public void gui_to_var() {
            value.setLength(0);
            value.append(textarea.getText());
        }

        @Override
        public void var_to_gui() {
            textarea.setText(value.toString());
        }

        @Override
        public JTextArea get_first() {
            return textarea;
        }

    }

    void bindVar(JTextField jtext, StringBuffer var);

    void bindVar(JTextArea jtext, StringBuffer var);

    void bindVar(JTextArea jtext, DBValue var);

    void bindVar(JTextField jtext, DBValue var);

    void bindVar(JComboBox<?> jComboBox, DBValue var);

    void bindVar(IDateTimeComponent comp, DBDateTime var);

    void bindVar(JCheckBox jCDefault, DBFlagInteger _default);

    void var_to_gui();

    void gui_to_var();

    Collection<Pair> getBindVarPairs();

    void addBindVarPair(Pair pair);
}
