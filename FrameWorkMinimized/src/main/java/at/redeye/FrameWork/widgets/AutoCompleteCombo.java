package at.redeye.FrameWork.widgets;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;

import static java.util.function.Predicate.isEqual;

public class AutoCompleteCombo extends JTextField {

    private List<String> items;
    private Popup popup;
    private AutoComboPopup combo;

    public AutoCompleteCombo() {
        this(0);
    }

    private AutoCompleteCombo(int size) {
        super(size);
        addFocusListener(new AutoCompleteFocusListener());
        addMouseWheelListener(new AutoCompleteMouseListener());
        addMouseListener(new AutoCompleteMouseListener());
        addKeyListener(new AutoCompleteKeyListener());
        setEnabled(true);
    }

    private String get_completion(String str) {
        if (str != null && items != null) {
            int len = str.length();
            for (String candidate : items) {
                if (len > candidate.length()) {
                    continue;
                }
                if (candidate.substring(0, len).equalsIgnoreCase(str)) {
                    return candidate;
                }
            }
        }
        return null;
    }

    private class AutoCompleteKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke) {
            // this runs *just* before the user's keystroke
            // does anything to the text field. the text
            // field will be affected after this finishes.

            showPopup();
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            // by the time this method runs, the user's keystroke
            // has already had its effect on the textfield value
            // (a normal keypress is inserted/appended, etc.)

            if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                combo.incSelectItem();
            } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
                combo.decSelectItem();
            }

            char ch = ke.getKeyChar();
            if (ch == KeyEvent.CHAR_UNDEFINED) {
                // arrow keys and other non-displayable chars
                return;
            }
            int code = ke.getKeyCode();
            if (code == KeyEvent.VK_DELETE || code == KeyEvent.VK_BACK_SPACE) {
                // don't auto-complete upon char deletion
                return;
            }
            String input = getText();
            int pos = getCaretPosition();
            if (pos < input.length()) {
                // don't auto-complete if editing from middle
                return;
            }
            String match = get_completion(input);
            if (match != null) {
                setText(match);
                setSelectionStart(input.length());
                setSelectionEnd(getText().length());
                selectItem(match);
            }
        }
    }

    private class AutoCompleteFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            selectAll();
            showPopup();
        }

        @Override
        public void focusLost(FocusEvent e) {
            select(getCaretPosition(), getCaretPosition());
            hidePopup();
        }
    }

    private void showPopup() {
        if (popup == null) {
            combo = new AutoComboPopup(items, this);

            PopupFactory factory = PopupFactory.getSharedInstance();
            Point position = getLocationOnScreen();

            popup = factory.getPopup(this, combo, position.x, position.y + getHeight());

            addAncestorListener(new AncestorListener() {

                @Override
                public void ancestorAdded(AncestorEvent event) {
                }

                @Override
                public void ancestorRemoved(AncestorEvent event) {
                }

                @Override
                public void ancestorMoved(AncestorEvent event) {
                    hidePopup();
                }
            });

            combo.setText(getText());

            popup.show();
        }
    }

    public void hidePopup() {
        if( popup != null ) {
            popup.hide();
            popup = null;
            combo = null;
        }
    }

    private void selectItem( String text )
    {
        if( combo != null && text != null )
            combo.setText(text);

        setText(text);
    }

    public void addItem(String s) {

        if (items == null)
            items = new Vector<>();

        boolean missing = items.stream().noneMatch(isEqual(s));

        if (missing)
            items.add(s);

        if (combo != null)
            combo.refresh(items);
    }

    public void setSelectedItem(Object value) {
        if (combo != null)
            combo.setSelectedItem(value);

        setText(value.toString());
    }

    private class AutoCompleteMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            showPopup();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            showPopup();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (combo == null)
                return;

            int notches = e.getWheelRotation();

            if (notches < 0) {
                for (int i = notches; i < 0; i++) {
                    combo.decSelectItem();
                }
            } else {
                for (int i = 0; i < notches; i++) {
                    combo.incSelectItem();
                }
            }
        }
    }
}
