package at.redeye.FrameWork.widgets;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;

public class NoticeIfChangedTextField extends AutoCompleteTextField {
    private boolean changed;
    private final Color defaultBackgroundColor;
    private final Color editedBackgroundColor = Color.yellow;
    private String orig_text;

    public NoticeIfChangedTextField() {
        defaultBackgroundColor = getBackground();
    }

    @Override
    public void setText(String text) {
        String old_text = getText();

        if (!text.equals(old_text)) {
            super.setText(text);

            setChanged(true);

            if (orig_text != null && orig_text.equals(getText())) {
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

    private void checkIfChanged()
    {
       if( orig_text == null )
            return;

        java.awt.EventQueue.invokeLater(() -> {

            if (orig_text == null)
                return;

            setChanged(!orig_text.equals(getText()));
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

}
