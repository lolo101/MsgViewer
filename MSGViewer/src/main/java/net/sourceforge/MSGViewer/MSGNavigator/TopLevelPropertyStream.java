/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.MSGNavigator;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 * @author martin
 */
public class TopLevelPropertyStream 
{
    public static final String NAME = "__properties_version1.0";
    private static final int HEADER_SIZE = 8 + 4 + 4 + 4 + 4 + 8; 
    
    private DirectoryEntry root;
    private DocumentEntry property_entry;
    private byte bytes[] = null;
    
    public TopLevelPropertyStream(DirectoryEntry root) throws FileNotFoundException, IOException
    {
        this.root = root;
        
        try {
            property_entry = (DocumentEntry) root.getEntry(NAME);

        } catch (FileNotFoundException ex) {
        }
       
        if( property_entry != null )
        {
            bytes = new byte[property_entry.getSize()];
            DocumentInputStream stream = new DocumentInputStream( property_entry );
            stream.read(bytes);
        }
    }
                
    /**
     * saves property field
     */
    public void save() throws IOException
    {
        int size = root.getEntryCount() * 16 + HEADER_SIZE;
                
        bytes = new byte[size];        
                                
        int offset = 0;
        
        // first 8 bytes should be set to zero in any case
        // read MS-OXMSG section 2.4.1.1 for Details
        for( int i = 0; i < 8; i++, offset++ )
            bytes[i] = 0;
        
        // write next recipient id
        for( int i = 0; i < 4; i++, offset++ )
            bytes[offset] = 0;
        
        // write next attachment id
        for( int i = 0; i < 4; i++, offset++ )
            bytes[offset] = 0;  
        
        // recipient count
        for( int i = 0; i < 4; i++, offset++ )
            bytes[offset] = 0;
        
        // attachment count
        for( int i = 0; i < 4; i++, offset++ )
            bytes[offset] = 0;

        // reserved 8 bytes
        for( int i = 0; i < 8; i++, offset++ )
            bytes[offset] = 0;
        
        
        for( Iterator<Entry> it = root.getEntries(); it.hasNext(); )
        {
            Entry entry = it.next();
            
            DocumentEntry de = null;
            
            if( entry.isDocumentEntry() )
                de = (DocumentEntry) entry;
            
            String name = entry.getName();
            // ignore myself
            if( name.equals(NAME) )
                continue;                        
            
            String tagname = "";
            
            if( name.startsWith("__substg1.0_") )
                tagname = name.substring(12);
            
            String tagtype = tagname.substring(4).toLowerCase();
            
            // save tagname
            bytes[offset++] = Integer.valueOf(tagname.substring(6),16).byteValue();
            bytes[offset++] = Integer.valueOf(tagname.substring(4,6),16).byteValue();
            bytes[offset++] = Integer.valueOf(tagname.substring(2,4),16).byteValue();
            bytes[offset++] = Integer.valueOf(tagname.substring(0,2),16).byteValue();
            
            // flags set everything to RW
            bytes[offset] = 0x0002 | 0x004;
            offset += 4;
            
            if( tagtype.equals("001f")) {
                // PStringType
                // length of the String UTF16-LE Coded + 2
                String lenght_str  = String.format("%08x", de.getSize()+2);
                
                bytes[offset++] = Integer.valueOf(lenght_str.substring(6),16).byteValue();
                bytes[offset++] = Integer.valueOf(lenght_str.substring(4,6),16).byteValue();
                bytes[offset++] = Integer.valueOf(lenght_str.substring(2,4),16).byteValue();
                bytes[offset++] = Integer.valueOf(lenght_str.substring(0,2),16).byteValue();                
            }
            
            // reserved
            offset += 4;
        }
        
        try {
            property_entry = (DocumentEntry) root.getEntry(NAME);
            property_entry.delete();
        } catch( FileNotFoundException ex ) {
            
        }
        
        property_entry = root.createDocument(NAME, new ByteArrayInputStream(bytes));
    }
    
    /**
     * deletes one entry and removes it's data from the property stream
     * entry.delete() is called by this function
     * @param entry
     * @throws IOException 
     */
    public void delete( Entry entry ) throws IOException
    {
        if( entry.getName().equals(NAME) )
            throw new RuntimeException("deleting " + NAME + " is not allowed!");
        
        if( entry.isDirectoryEntry() )
            throw new RuntimeException("deleting directories not supported yet");
        
        if( entry.getName().startsWith("__substg_version1.0") )
            throw new  RuntimeException("unly __substg entries are supported yet" );
        
        boolean found = false;
        
        for( int offset = HEADER_SIZE; offset < bytes.length; offset+= 16 )
        {
            String tagname = "";

            // property tag
            for (int i = offset + 3; i >= offset; i--) {
                tagname += formatByte0S(bytes[i]);
            }
            
            if( ("__substg1.0_" + tagname).equals(entry.getName() ) )
            {
                if( !entry.delete() )
                {
                    throw new RuntimeException("cannot delete entry");
                }                                
                
                byte new_bytes[] = new byte[bytes.length-16];
                
                System.arraycopy(bytes, 0,         new_bytes,  0,      offset);
                System.arraycopy(bytes, offset+16, new_bytes, offset, new_bytes.length - offset );
                bytes = new_bytes;
                found = true;
                break;
            }
        }
        
        if( !found )
            throw new RuntimeException("entry not found");
        
        try {
            property_entry = (DocumentEntry) root.getEntry(NAME);
            property_entry.delete();
        } catch( FileNotFoundException ex ) {
           
        }
        
        property_entry = root.createDocument(NAME, new ByteArrayInputStream(bytes));        
    }
    
    private String formatByte0S( byte b )
    {        
        return String.format("%02X", b);
    }      
    
    public void update( DocumentEntry entry ) throws IOException
    {
        if( entry.getName().equals(NAME) )
            throw new RuntimeException("deleting " + NAME + " is not allowed!");
        
        if( entry.isDirectoryEntry() )
            throw new RuntimeException("deleting directories not supported yet");
        
        if( entry.getName().startsWith("__substg_version1.0") )
            throw new  RuntimeException("unly __substg entries are supported yet" );
        
        boolean found = false;
        
        for( int offset = HEADER_SIZE; offset < bytes.length; offset+= 16 )
        {
            String tagname = "";

            // property tag
            for (int i = offset + 3; i >= offset; i--) {
                tagname += formatByte0S(bytes[i]);
            }
            
            if( ("__substg1.0_" + tagname).equals(entry.getName() ) )
            {
                String tagtype = tagname.toLowerCase().substring(4);
                 
                int voffset = offset + 8;
                
                if (tagtype.equals("001f")) {                                        
                    // PStringType
                    // length of the String UTF16-LE Coded + 2
                    String lenght_str  = String.format("%08x", entry.getSize()+2);
                
                    bytes[voffset++] = Integer.valueOf(lenght_str.substring(6),16).byteValue();
                    bytes[voffset++] = Integer.valueOf(lenght_str.substring(4,6),16).byteValue();
                    bytes[voffset++] = Integer.valueOf(lenght_str.substring(2,4),16).byteValue();
                    bytes[voffset++] = Integer.valueOf(lenght_str.substring(0,2),16).byteValue();                                    

                } else if (tagtype.equals("0102")) {                                        
                    // Binary
                    String lenght_str  = String.format("%08x", entry.getSize()+2);
                
                    bytes[voffset++] = Integer.valueOf(lenght_str.substring(6),16).byteValue();
                    bytes[voffset++] = Integer.valueOf(lenght_str.substring(4,6),16).byteValue();
                    bytes[voffset++] = Integer.valueOf(lenght_str.substring(2,4),16).byteValue();
                    bytes[voffset++] = Integer.valueOf(lenght_str.substring(0,2),16).byteValue();                            
                }
                
                found = true;
                break;
            }
        }
        
         if( !found )
            throw new RuntimeException("entry not found");
         
        try {
            property_entry = (DocumentEntry) root.getEntry(NAME);
            property_entry.delete();
        } catch( FileNotFoundException ex ) {
            
        }
        
        property_entry = root.createDocument(NAME, new ByteArrayInputStream(bytes));                
    }
    
}
