/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets;

import java.awt.Color;
import java.awt.Container;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 *
 * @author martin
 */
public class NoticeIfChangedTextField extends AutoCompleteTextField implements IKnowNextFocus
{        
    boolean changed = false;
    Container next_on_focus = null;
    Container prev_on_focus = null;
    Color defaultBackgroundColor = null;
    Color editedBackgroundColor = Color.yellow;
    String orig_text = null;

    public NoticeIfChangedTextField()
    {
        super();
        defaultBackgroundColor = getBackground();
    }

    @Override
    public void setText( String text )
    {
        String old_text = getText();

        if( text.isEmpty() && old_text.isEmpty() )
        {
            //setChanged(false);
        }
        else if( text.equals(old_text) )
        {
            // setChanged(false);
        }
        else
        {            
            super.setText(text);
            
            // wir machen das, weil vielleicht is ja noch ein
            // Document rumnudler dazwischen
            if( !old_text.equals(getText()) )
            {
                setChanged(true);
            }

            if( orig_text != null && orig_text.equals(getText()) )
            {
                setChanged(false);
            }
        }
    }

    public void setChanged(boolean state)
    {
        changed = state;

        if( changed )
        {
            setBackground(editedBackgroundColor);
        }
        else
        {
            orig_text = getText();
            setBackground(defaultBackgroundColor);
        }
    }

    public void setDefaultBackgroundColor( Color color )
    {
        defaultBackgroundColor = color;
    }

    public void setEditedBackgroundColor( Color color )
    {
        editedBackgroundColor = color;
    }

    private void checkIfChanged()
    {
       if( orig_text == null )
            return;

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

             if( orig_text == null )
                return;

                if( orig_text.equals(getText()) )
                    setChanged(false);
                else
                    setChanged(true);
            }
        });
    }

    @Override
    public void setDocument( Document doc )
    {
        if (doc != null) {
            doc.addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {

                    if( orig_text == null )
                        setChanged(true);
                    else
                        checkIfChanged();

                }

                @Override
                public void removeUpdate(DocumentEvent e) {

                     checkIfChanged();

                    if( orig_text == null )
                        setChanged(true);
                    else
                        checkIfChanged();
                    
                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                    if( orig_text == null )
                        setChanged(true);
                    else
                        checkIfChanged();
                }
            });
        }

        super.setDocument(doc);
    }

    public boolean hasChanged()
    {
        return changed;
    }

    @Override
    public Container get_next_on_focus() {
        return next_on_focus;
    }

    @Override
    public Container get_prev_on_focus() {
        return prev_on_focus;
    }

    @Override
    public void set_next_on_focus(Container next) {
        next_on_focus = next;
    }

    @Override
    public void set_prev_on_focus(Container prev) {
        prev_on_focus = prev;
    }
}
