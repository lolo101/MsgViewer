package at.redeye.FrameWork.widgets;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class AutoCompleteTextField extends JTextField
        implements FocusListener, KeyListener, ComboBoxEditor {

    private List<String> items = null;
    private int matched_index = -1;
    private boolean do_completion = true;

    public AutoCompleteTextField() {
        this(0);
    }

    public AutoCompleteTextField(int size) {
        super(size);
        addFocusListener(this);
        enable_complete(true);
        setEnabled(true);
    }

    public void set_items(List<String> v) {
        // it is assumed that 'items' is pre-sorted in manner that
        // auto-complete will operate on (namely, first match wins)
        //
        // **IMPORTANT**
        // Before you (re)set the items to complete against, you might
        // want to consider calling enable_complete(false) first, then
        // passing the new vector in, then calling enable_complete(true).
        items = v;
        clear();
    }

    public void enable_complete(boolean b) {
        do_completion = b;
        if (b) {
            addKeyListener(this);
        } else {
            removeKeyListener(this);
        }
    }

    public void clear() {
        matched_index = -1;
        setText("");
    }

    public Object getItem() {
        return matched_index > -1 ? items.get(matched_index) : null;
    }

    public void setItem(Object o) {
        String match = get_completion(o == null ? null : o.toString());
        setText(match == null ? "" : match);
    }

    private String get_completion(String str) {
        if (!do_completion) {
            return str;
        }
        if (str != null && items != null) {
            int len = str.length();
            for (int i = 0; i < items.size(); i++) {
                String candidate = items.get(i);
                if (len > candidate.length()) {
                    continue;
                }
                if (candidate.substring(0, len).equalsIgnoreCase(str)) {
                    matched_index = i;
                    return candidate;
                }
            }
        }
        matched_index = -1;
        return null;
    }

    public void keyPressed(KeyEvent ke) {
        // this runs *just* before the user's keystroke
        // does anything to the text field. the text
        // field will be affected after this finishes.
    }

    public void keyTyped(KeyEvent ke) {
        // run after keyPressed but before
        // keyReleased (and before the textfield value
        // is modified; the event can be cancelled here
        // to prevent action)
    }

    public void keyReleased(KeyEvent ke) {
        // by the time this method runs, the user's keystroke
        // has already had it's effect on the textfield value
        // (a normal keypress is inserted/appended, etc.)

        char ch = ke.getKeyChar();
        int code = ke.getKeyCode();
        String input = getText();
        int pos = getCaretPosition();
        if (ch == KeyEvent.CHAR_UNDEFINED) {
            // arrow keys and other non-displayable chars
            return;
        }
        if (code == KeyEvent.VK_DELETE || code == KeyEvent.VK_BACK_SPACE) {
            // don't auto-complete upon char deletion
            return;
        }
        if (pos < input.length()) {
            // don't auto-complete if editing from middle
            return;
        }
        String match = get_completion(input);
        if (match != null) {
            setText(match);
            setSelectionStart(input.length());
            setSelectionEnd(getText().length());
        }
    }

    public void focusGained(FocusEvent e) {
        selectAll();
    }

    public void focusLost(FocusEvent e) {
        select(getCaretPosition(), getCaretPosition());
    }

    public java.awt.Component getEditorComponent() {
        // this satisfies ComboBoxEditor interface
        // (along with getItem and setItem)

        return this;
    }
}
