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
public class DocumentFieldInteger extends PlainDocument {        
            

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {                
        
        if( str != null )                    
            if( str.matches("[0-9-+]+"))
                super.insertString(offs, str, a);
    }
        
}
