package at.redeye.FrameWork.widgets;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;

public class AutoCompleteCombo extends JTextField
        implements FocusListener, KeyListener, MouseWheelListener, MouseListener {

    private List<String> items = null;
    private int matched_index = -1;
    private boolean do_completion = true;
    private Popup popup = null;
    private AutoComboPopup combo = null;

    public AutoCompleteCombo() {
        this(0);
    }

    public AutoCompleteCombo(int size) {
        super(size);
        addFocusListener(this);
        addMouseWheelListener(this);
        addMouseListener(this);
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

    public void clearItems()
    {
        if( items != null )
            items.clear();

        clear();
        hidePopup();
    }

    public Object getItem() {
        return matched_index > -1 ? items.get(matched_index) : null;
    }

    public void setItem(Object o) {
        String match = get_completion(o == null ? null : o.toString());
        setText(match == null ? "" : match);
        selectItem(match);
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

        showPopup();
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

        if( ke.getKeyCode() == KeyEvent.VK_DOWN )
        {
            combo.incSelectItem();
            //System.out.println( "down");
        } else if(  ke.getKeyCode() == KeyEvent.VK_UP ) {
            combo.decSelectItem();
            //System.out.println( "up");
        }

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
            selectItem(match);
        }
    }

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

    public void showPopup()
    {
        if( popup == null )
        {
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

        boolean found = false;

        for (String item : items) {
            if (item.equals(s)) {
                found = true;
                break;
            }
        }

        if (!found)
            items.add(s);

        if (combo != null)
            combo.refresh(items);
    }

    public Object getSelectedItem()
    {
        if( combo != null )
        {
            Object o = combo.getSelectedItem();

            if( o == null )
                return "";

            return o;
        }

        return "";
    }

    public void setSelectedItem(Object value) {
        if( combo != null )
            combo.setSelectedItem( value );

        setText(value.toString());
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if( combo == null )
            return;

        int notches = e.getWheelRotation();

        if( notches <  0 )
        {
            for( int i = notches; i < 0; i++ )
            {
                combo.decSelectItem();
            }
        }
        else
        {
            for( int i = 0; i < notches; i++ )
            {
                combo.incSelectItem();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        showPopup();
    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {
        showPopup();
    }

    public void mouseExited(MouseEvent e) {}

}
