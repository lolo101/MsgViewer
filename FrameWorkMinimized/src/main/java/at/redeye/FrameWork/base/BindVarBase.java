package at.redeye.FrameWork.base;

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
}
