package at.redeye.FrameWork.base;

import javax.swing.*;
import java.util.Collection;

public interface BindVarInterface {

    interface Pair {

        void gui_to_var();

        void var_to_gui();

        Object get_first();
    }

    class TextStringPair implements Pair {

        private final JTextField textfield;
        private final StringBuffer value;

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

    void bindVar(JTextField jtext, StringBuffer var);

    void var_to_gui();

    void gui_to_var();

    Collection<Pair> getBindVarPairs();
}
