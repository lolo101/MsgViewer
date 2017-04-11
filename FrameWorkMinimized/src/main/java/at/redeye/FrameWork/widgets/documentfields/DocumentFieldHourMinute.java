/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets.documentfields;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 *
 * @author martin
 */
public class DocumentFieldHourMinute extends DocumentFieldLimit {
            
    private static final long serialVersionUID = 1L;

    public DocumentFieldHourMinute()
    {
        super( 5 );
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        
        if( str != null )                    
            if( str.matches("[0-9:]+"))
                super.insertString(offs, str, a);
    }
        
}
