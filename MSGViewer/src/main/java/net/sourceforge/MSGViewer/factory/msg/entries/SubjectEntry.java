/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.entries;

/**
 *
 * @author martin
 */
public class SubjectEntry extends StringUTF16SubstgEntry 
{
    public static final String NAME = "0037";   
    
    public SubjectEntry()
    {
        super( NAME );        
    }
    
    public SubjectEntry(String subject )
    {
        super( NAME, subject );        
    }    
    
 
}
