/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets.documentfields;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author martin
 */
public class DocumentFieldLimit extends PlainDocument {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int limit;
    
    
    public DocumentFieldLimit( int limit )
    {
        super();
        this.limit = limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

        //System.out.println("str: " + this.getText(0, getLength()) + " => " + str);

        if( str != null )            
            if( getLength() + str.length() <= limit )
                super.insertString(offs, str, a);
    }
        
}
