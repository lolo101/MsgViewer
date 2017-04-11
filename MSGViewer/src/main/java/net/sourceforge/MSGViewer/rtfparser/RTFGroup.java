/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer.rtfparser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author martin
 */
public class RTFGroup
{    
    private StringBuilder text_content;
    private List<String> commands = new ArrayList();
    private String last_command = "";

    public RTFGroup()
    {
        
    }

    void addCommand( String command )
    {
        
        if( command.equals("\\par") )
            addTextContent("\n");
        else if( command.equals("\\tab"))
            addTextContent("\t");
        else
        {
            commands.add(command);
            last_command = command;
        }
    }

    public void addTextContent( String text )
    {
        if( text_content == null )
            text_content = new StringBuilder();

        if( last_command.startsWith("\\html") )
            text_content.append(text);
    }

    public boolean isEmpty()
    {
        if( text_content == null && commands.isEmpty() )
            return true;

        if( text_content.length() > 0 )
            return false;

        return false;
    }

    public String getTextContent()
    {
        return text_content.toString();
    }

    public List<String> getCommands()
    {
        return commands;
    }

    boolean isEmptyText() {

       if( text_content == null )
           return true;

       return text_content.length() == 0;
    }

}
