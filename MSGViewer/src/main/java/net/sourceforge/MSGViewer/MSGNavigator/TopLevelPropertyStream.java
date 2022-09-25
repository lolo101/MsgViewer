package net.sourceforge.MSGViewer.MSGNavigator;

import com.auxilii.msgparser.Ptyp;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TopLevelPropertyStream
{
    private static final String NAME = "__properties_version1.0";
    private static final int HEADER_SIZE = 8 + 4 + 4 + 4 + 4 + 8;

    private final DirectoryEntry root;
    private DocumentEntry property_entry;
    private byte[] bytes;

    TopLevelPropertyStream(DirectoryEntry root) throws IOException
    {
        this.root = root;

        try {
            property_entry = (DocumentEntry) root.getEntry(NAME);
            try (DocumentInputStream stream = new DocumentInputStream(property_entry)) {
                bytes = stream.readAllBytes();
            }

        } catch (FileNotFoundException ignored) {
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


        for( Entry entry : root )
        {
            String name = entry.getName();
            // ignore myself
            if( name.equals(NAME) )
                continue;

            String tagname = "";

            if( name.startsWith(Ptyp.SUBSTORAGE_PREFIX) )
                tagname = name.substring(12);

            String tagtype = tagname.substring(4).toLowerCase();

            // save tagname
            offset = insert(tagname, offset);

            // flags set everything to RW
            bytes[offset] = 0x0002 | 0x004;
            offset += 4;

            if( tagtype.equals("001f")) {
                // PStringType
                // length of the String UTF16-LE Coded + 2
                String lenght_str  = String.format("%08x", ((DocumentEntry) entry).getSize()+2);

                offset = insert(lenght_str, offset);
            }

            // reserved
            offset += 4;
        }

        try {
            property_entry = (DocumentEntry) root.getEntry(NAME);
            property_entry.delete();
        } catch (FileNotFoundException ignored) {

        }

        property_entry = root.createDocument(NAME, new ByteArrayInputStream(bytes));
    }

    /**
     * deletes one entry and removes its data from the property stream
     * entry.delete() is called by this function
     */
    void delete(Entry entry) throws IOException
    {
        if( entry.getName().equals(NAME) )
            throw new RuntimeException("deleting " + NAME + " is not allowed!");

        if( entry.isDirectoryEntry() )
            throw new RuntimeException("deleting directories not supported yet");

        if( entry.getName().startsWith("__substg_version1.0") )
            throw new  RuntimeException("unly __substg entries are supported yet" );

        boolean absent = true;

        for( int offset = HEADER_SIZE; offset < bytes.length; offset+= 16 )
        {
            StringBuilder tagname = new StringBuilder();

            // property tag
            for (int i = offset + 3; i >= offset; i--) {
                tagname.append(formatByte0S(bytes[i]));
            }

            if( (Ptyp.SUBSTORAGE_PREFIX + tagname).equals(entry.getName() ) )
            {
                if( !entry.delete() )
                {
                    throw new RuntimeException("cannot delete entry");
                }

                byte[] new_bytes = new byte[bytes.length - 16];

                System.arraycopy(bytes, 0,         new_bytes,  0,      offset);
                System.arraycopy(bytes, offset+16, new_bytes, offset, new_bytes.length - offset );
                bytes = new_bytes;
                absent = false;
                break;
            }
        }

        if(absent)
            throw new RuntimeException("entry not found");

        try {
            property_entry = (DocumentEntry) root.getEntry(NAME);
            property_entry.delete();
        } catch (FileNotFoundException ignored) {

        }

        property_entry = root.createDocument(NAME, new ByteArrayInputStream(bytes));
    }

    void update(DocumentEntry entry) throws IOException
    {
        if( entry.getName().equals(NAME) )
            throw new RuntimeException("deleting " + NAME + " is not allowed!");

        if( entry.isDirectoryEntry() )
            throw new RuntimeException("deleting directories not supported yet");

        if( entry.getName().startsWith("__substg_version1.0") )
            throw new RuntimeException("only __substg entries are supported yet");

        boolean absent = true;

        for( int offset = HEADER_SIZE; offset < bytes.length; offset+= 16 )
        {
            StringBuilder tagname = new StringBuilder();

            // property tag
            for (int i = offset + 3; i >= offset; i--) {
                tagname.append(formatByte0S(bytes[i]));
            }

            if( (Ptyp.SUBSTORAGE_PREFIX + tagname).equals(entry.getName() ) )
            {
                String tagtype = tagname.toString().toLowerCase().substring(4);

                if (tagtype.equals("001f") || tagtype.equals("0102")) {
                    String lenght_str = String.format("%08x", entry.getSize() + 2);
                    insert(lenght_str, offset + 8);
                }

                absent = false;
                break;
            }
        }

         if(absent)
            throw new RuntimeException("entry not found");

        try {
            property_entry = (DocumentEntry) root.getEntry(NAME);
            property_entry.delete();
        } catch (FileNotFoundException ignored) {

        }

        property_entry = root.createDocument(NAME, new ByteArrayInputStream(bytes));
    }

    private int insert(String value, int offset)
    {
        bytes[offset++] = Integer.valueOf(value.substring(6), 16).byteValue();
        bytes[offset++] = Integer.valueOf(value.substring(4, 6), 16).byteValue();
        bytes[offset++] = Integer.valueOf(value.substring(2, 4), 16).byteValue();
        bytes[offset++] = Integer.valueOf(value.substring(0, 2), 16).byteValue();
        return offset;
    }

    private static String formatByte0S(byte b)
    {
        return String.format("%02X", b);
    }
}
