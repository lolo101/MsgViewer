package at.redeye.FrameWork.widgets.documentfields;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DocumentFieldLimit extends PlainDocument {
    private static final long serialVersionUID = 1L;
    private final int limit;


    public DocumentFieldLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

        if (str != null)
            if (getLength() + str.length() <= limit)
                super.insertString(offs, str, a);
    }

}
