/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Root;
import java.io.File;



/**
 *
 * @author martin
 */
public class MSGFileFilter extends javax.swing.filechooser.FileFilter
{
    Root root;

    public MSGFileFilter( Root root )
    {
        this.root = root;
    }

    @Override
    public boolean accept(File pathname)
    {
        if( pathname.isDirectory() )
            return true;
        
        if( pathname.toString().toLowerCase().endsWith(".msg") )
            return true;
        else if( pathname.toString().toLowerCase().endsWith(".mbox"))
            return true;
        else if( pathname.toString().toLowerCase().endsWith(".eml"))
            return true;        

        return false;            
    }

    @Override
    public String getDescription() {
       return root.MlM("Outlook *.msg, or *.mbox, or *.eml");
    }

}
