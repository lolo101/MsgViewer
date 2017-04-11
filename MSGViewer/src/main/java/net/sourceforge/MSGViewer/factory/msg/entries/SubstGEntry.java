/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.entries;

import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

/**
 *
 * @author martin
 */
public abstract class SubstGEntry 
{
    public static final String TYPE_ASCII = "001e";
    public static final String TYPE_UTF16 = "001f";
    public static final String TYPE_BYTES = "0102";
    
    String type;
    String name;
    
    public SubstGEntry(String name,  String type )
    {
        this.name = name;
        this.type = type;
    }
    
    public void createEntry( DirectoryEntry dir, String value ) throws IOException
    {
        InputStream in = null;
        
        if( type.equals(TYPE_ASCII) ) {
            // ASCII 
            in = new ByteArrayInputStream(value.getBytes( "ISO-8859-1"));            
        } else if( type.equals(TYPE_UTF16) ) {
            in = new ByteArrayInputStream(value.getBytes( "UTF-16LE"));
        }  
        
        dir.createDocument("__substg1.0_" + name + type.toUpperCase(), in);       
    }
    
    public void createEntry( DirectoryEntry dir, byte value[] ) throws IOException
    {    
        InputStream in = null;
        
        if( type.equals(TYPE_BYTES) ) {
            in = new ByteArrayInputStream(value);
        }
        
        dir.createDocument("__substg1.0_" + name + type.toUpperCase(), in);
    }
    
    public static void createBinaryNullEntry(  DirectoryEntry dir, String name, String type, int len ) throws IOException
    {
        InputStream in = null;
        
        in = new ByteArrayInputStream(new byte[len]);
        
        dir.createDocument("__substg1.0_" + name + type.toUpperCase(),in);
    }   
        
    public String getTypeName() {
        return type;
    }
    
    public String getTagName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name + type;
    }
    
    public abstract PropType getPropType();
    
    public abstract void createEntry(  DirectoryEntry dir ) throws IOException;
}
