/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.entries;

/**
 *
 * @author martin
 */
public class MessageClassEntry extends StringUTF16SubstgEntry
{
    public static final String NAME = "001A";  
    public static final String IPM_NOTE = "IPM.Note";
    
    public MessageClassEntry()
    {
        super( NAME, IPM_NOTE);
    }
    
}
