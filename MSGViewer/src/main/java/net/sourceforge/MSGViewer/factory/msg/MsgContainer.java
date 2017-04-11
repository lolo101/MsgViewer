/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg;

import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropPtypInteger32;
import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropType;
import net.sourceforge.MSGViewer.factory.msg.entries.StringUTF16SubstgEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.SubstGEntry;
import com.auxilii.msgparser.RecipientEntry;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;

/**
 *
 * @author martin
 */
public class MsgContainer 
{
    public static final String NAME = "__properties_version1.0";
    public static final String NAMED_NAME = "__nameid_version1.0";
    
    public static final String GUID_STREAM_NAME = "__substg1.0_00020102";
    public static final String ENTRY_STREAM_NAME = "__substg1.0_00030102";
    public static final String STRING_STREAM_NAME = "__substg1.0_00040102";
    
    private static final int HEADER_SIZE = 8 + 4 + 4 + 4 + 4 + 8;     
    private ArrayList<PropType> properties = new ArrayList();
    private ArrayList<SubstGEntry> substg_streams = new ArrayList();
        
    private ArrayList<RecipientEntry> recipients = new ArrayList();
    
    public MsgContainer()
    {
        
    }
    
    public void addProperty( PropType prop )
    {
        properties.add(prop);
    }
    
    public void addVarEntry( SubstGEntry entry )
    {
       addProperty(entry.getPropType());
       substg_streams.add(entry);
    }
    
    public void write( DirectoryEntry root ) throws IOException
    {
        int size = HEADER_SIZE + properties.size() * 16;
        byte bytes[] = new byte[size];
        
        int offset = 8;
        
        // next recip id       
        writeInt(bytes,offset, recipients.size());
        offset +=4;
        
        // next attachment id       
        writeInt(bytes,offset, 0);
        offset +=4;        
        
        // recip count       
        writeInt(bytes,offset, recipients.size());
        offset +=4;            
                
        // attachment count       
        writeInt(bytes,offset, 0);
        offset +=4;               
        
        offset = HEADER_SIZE;                
        
        for( PropType prop : properties )
        {
            prop.writePropertiesEntry(bytes, offset);
            offset += 16;
        }
        
        for( SubstGEntry entry : substg_streams )
            entry.createEntry(root);
        
        int count = 0 ;
        for( RecipientEntry rec : recipients ) {               
            writeRecipientEntry( root, rec, count++ );
        }
        
        createPropEntry(bytes,root);
        createNamedEntry(root);
    }
    
    private void writeInt( byte[] bytes, int offset, int value ) {
        
       ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
       buffer.putInt(value);
              
       byte[] int_bytes = buffer.array();
                    
       System.arraycopy(int_bytes, 0, bytes, offset, 4);
    }
    
    void createPropEntry(byte bytes[], DirectoryEntry root ) throws IOException
    {
        ByteArrayInputStream buffer = new ByteArrayInputStream(bytes);
        
        DocumentEntry prop_entry = null;
        
        try {
            prop_entry = (DocumentEntry) root.getEntry(NAME);
        } catch( FileNotFoundException ex ) {
            
        }
        
        if( prop_entry != null )
            prop_entry.delete();
        
        prop_entry = root.createDocument(NAME, buffer);             
    }
    
    void createNamedEntry(DirectoryEntry root ) throws IOException
    {
        DirectoryEntry prop_entry = null;
        
        try {
            prop_entry = (DirectoryEntry) root.getEntry(NAMED_NAME);
        } catch( FileNotFoundException ex ) {
            
        }
        
        if( prop_entry != null )
            prop_entry.delete();
        
        prop_entry = root.createDirectory(NAMED_NAME);
        
        
        prop_entry.createDocument(GUID_STREAM_NAME, new ByteArrayInputStream(new byte[0]));
        prop_entry.createDocument(ENTRY_STREAM_NAME, new ByteArrayInputStream(new byte[0]));
        prop_entry.createDocument(STRING_STREAM_NAME, new ByteArrayInputStream(new byte[0]));
    }
    
    void addRecipient( RecipientEntry entry ) {
        recipients.add(entry);
    }

    private void writeRecipientEntry(DirectoryEntry root , RecipientEntry rec, int id) throws IOException 
    {
        DirectoryEntry rec_dir = root.createDirectory(String.format("__recip_version1.0_#%08d", id));

        ArrayList<PropType> p_entries = new ArrayList();
        ArrayList<SubstGEntry> s_streams = new ArrayList();

        p_entries.add(new PropPtypInteger32("3000", id));
        p_entries.add(new PropPtypInteger32("0C15", 1));

        SubstGEntry to_name = new StringUTF16SubstgEntry("3001", rec.getToName());
        SubstGEntry to_email = new StringUTF16SubstgEntry("3003", rec.getToEmail());

        s_streams.add(to_name);
        s_streams.add(to_email);

        p_entries.add(to_name.getPropType());
        p_entries.add(to_email.getPropType());

        int size = 8 + p_entries.size() * 16;
        byte bytes[] = new byte[size];

        int offset = 8;
        
        for (PropType prop : p_entries) {
            prop.writePropertiesEntry(bytes, offset);
            offset += 16;
        }
        
        createPropEntry(bytes,rec_dir);
        
        for( SubstGEntry entry : s_streams )
            entry.createEntry(rec_dir);        
    }
}
