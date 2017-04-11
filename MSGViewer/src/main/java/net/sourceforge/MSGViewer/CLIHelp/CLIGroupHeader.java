/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.CLIHelp;

/**
 *
 * @author martin
 */
public class CLIGroupHeader extends CLIOption 
{    
    public CLIGroupHeader( String title )
    {
        super( title, ""  );        
    }
    
    @Override
    public String getName() {    
        return "";
    }
    
    @Override
    public void buildShortHelpText( StringBuilder sb, int fill_len_to_short_description, int max_line_len )
    {
        if( sb.length() > 0 )
            sb.append("\n");
        
        sb.append(super.getName());
        
        sb.append("\n");
    }
}
