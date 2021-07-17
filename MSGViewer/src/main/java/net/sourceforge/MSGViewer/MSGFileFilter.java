package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Root;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MSGFileFilter extends FileFilter
{
    private final Root root;

    public MSGFileFilter( Root root )
    {
        this.root = root;
    }

    @Override
    public boolean accept(File pathname)
    {
        return pathname.isDirectory()
                || pathname.toString().toLowerCase().endsWith(".msg")
                || pathname.toString().toLowerCase().endsWith(".mbox")
                || pathname.toString().toLowerCase().endsWith(".eml");

    }

    @Override
    public String getDescription() {
       return root.MlM("*.msg, *.mbox, *.eml");
    }
}
