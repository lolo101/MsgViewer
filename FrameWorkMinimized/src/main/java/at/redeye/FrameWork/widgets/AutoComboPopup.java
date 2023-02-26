package at.redeye.FrameWork.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.Vector;

public class AutoComboPopup extends JPanel
        implements MouseWheelListener {
    private final JList<String> list;
    private List<String> items;
    private boolean self_triggert;

    public AutoComboPopup(List<String> items, final JTextField textField) {
        this.items = items;
        setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane();

        list = new JList<>();

        list.addMouseWheelListener(this);

        scroll.setViewportView(list);

        add(scroll, BorderLayout.NORTH);
        list.setListData(new Vector<>(items));

        list.addListSelectionListener(e -> {
            if (!self_triggert) {
                Object o = list.getSelectedValue();
                textField.setText(o.toString());
            }
        });

        this.setBackground(Color.red);

        Dimension prefSize = getPreferredSize();

        int width = Math.max(prefSize.width, textField.getWidth());
        int height = prefSize.height;

        if( height < 20 || height <= textField.getHeight() )
            height = items.size() * textField.getHeight();

        setPreferredSize(new Dimension(width, height));
        scroll.setPreferredSize(new Dimension(width, height));
    }

    public void setText( String text, boolean trigger_text_field )
    {
        if( !trigger_text_field )
            self_triggert = true;

        list.setSelectedValue(text, true);

        self_triggert = false;
    }

    /*
     * text : Text to set
     */
    public void setText( String text )
    {
        setText( text, false );
    }

    void incSelectItem() {
        int i = list.getSelectedIndex();

        //System.out.println( "index before: " + i);

        if( i < 0 )
        {
            i = 0;
        }
        else
        {
            i++;

            if( i > items.size() )
                i = items.size()-1;
        }

        //System.out.println( "selecting: " + i);
        list.setSelectedIndex(i);
        list.ensureIndexIsVisible(i);
    }

    void decSelectItem() {
        int i = list.getSelectedIndex();

        if( i < 0 )
        {
            i = 0;
        }
        else
        {
            i--;

            if( i < 0 )
                i = 0;
        }

        list.setSelectedIndex(i);
        list.ensureIndexIsVisible(i);
    }

    void setSelectedItem(Object value) {
        list.setSelectedValue(value, true);
    }

    void refresh(List<String> items) {
        this.items = items;
        list.setListData(new Vector<>(items));
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        int notches = e.getWheelRotation();

        if( notches <  0 )
        {
            for( int i = notches; i < 0; i++ )
               decSelectItem();
        }
        else
        {
            for( int i = 0; i < notches; i++ )
                incSelectItem();
        }
    }
}
