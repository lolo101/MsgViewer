/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.entries;

/**
 *
 * @author martin
 */
public class HeadersEntry extends StringUTF16SubstgEntry
{
    public static final String NAME = "007d";    
    
    public HeadersEntry( String headers )
    {
        super( NAME, headers );        
    }  
    
}
