/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.MSGNavigator;

import net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert;
import net.sourceforge.MSGViewer.factory.msg.lib.ByteConvert;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

/**
 *
 * @author martin
 */
public class PropertyParser 
{
    public static class PropertyTag
    {
        String descr;
        String tagname;
        
        public PropertyTag( String tagname, String descr )
        {
            this.tagname = tagname;
            this.descr = descr;
        }
        
        @Override
        public String toString()
        {
            return descr;
        }
    }
    
    private DocumentEntry entry;
    
    private boolean is_toplevel = false;
    private boolean is_attachment = false;
    private boolean is_reciepient = false;
    
    private int max_descr_lenght = 20;
    
    private ArrayList<PropertyTag> props = new ArrayList<PropertyTag>();
        
    public PropertyParser(DocumentEntry entry) throws IOException
    {           
        this.entry = entry;

        for (String descr : MSGNavigator.props.values()) {
            if (descr.length() > max_descr_lenght) {
                max_descr_lenght = descr.length();
            }
        }
        
        parse();
    }
    
    final void parse() throws IOException
    {
        if (entry.getParent().getParent() == null) {
            is_toplevel = true;
        } else if (entry.getParent().getName().startsWith("__recip_version1.0")) {
            is_reciepient = true;
        } else if (entry.getParent().getName().startsWith("__attach_version1.0")) {
            is_attachment = true;
        }
        
        DocumentInputStream in = new DocumentInputStream(entry);
        
        byte bytes[] = new byte[entry.getSize()];
        int len = in.read(bytes);
        
        if( len != bytes.length )
            throw new IOException("Not all Data read");        
        
        // RESERVED 8 bytes (should by zero)        
        int offset = 8;
        
        if (is_toplevel) {
            // NEXT Recipient ID 4 bytes

            // todo read Recipient id
            offset+= 4;
            
            /*
            for (int i = 0; i < 4; i++, offset++) {
                sb.append(formatByte(bytes[offset]));
            }             
             */

            // NEXT Attachment ID 4 bytes
            
            // todo read next Attachment id
            offset+= 4;

            /*
            for (int i = 0; i < 4; i++, offset++) {
                sb.append(formatByte(bytes[offset]));
            }*/

            
            // Recipient Count 4 bytes
            
            // todo read Recipient count
            offset+= 4;

            /*
            for (int i = 0; i < 4; i++, offset++) {
                sb.append(formatByte(bytes[offset]));
            }*/

            
            
            // Attachment Count 4 bytes
            
            // todo readAttachment Count 
            offset+= 4;            
            /*
            for (int i = 0; i < 4; i++, offset++) {
                sb.append(formatByte(bytes[offset]));
            }*/

            // Reserved 8 bytes (should by zero)

            offset += 8;
        }                
        
        for( ; offset < bytes.length; offset += 16) {           
            parsePropertyEntry( bytes, offset );
        }        
    }
    
    private String formatByte( byte b )
    {
        if( b == 0 )
            return "__ ";
        
        return String.format("%02X ", b);
    }
    
    private String formatByte0( byte b )
    {        
        return String.format("%02X ", b);
    }    
    
    private String formatByte0S( byte b )
    {        
        return String.format("%02X", b);
    }      

    private void parsePropertyEntry(byte[] bytes, int offset) {
        
        StringBuilder sb = new StringBuilder();
        
        // sb.append("TAG: ");
        
        String tagname = "";     
        
        // property tag
        for( int i = offset + 3; i >= offset; i-- ) {
            sb.append(formatByte0S(bytes[i]));
            tagname += formatByte0S(bytes[i]);
        }
        
        offset += 4;
        
        // sb.append(" FLAGS: ");
        sb.append( " " );
/*
        for( int i = offset; i < offset + 4; i++ )
            sb.append(formatByte0(bytes[i]));        
  */      
        if( (bytes[offset] & 0001) > 0 ) {
            sb.append("M");
        } else {
            sb.append("_");
        }
        
        if( (bytes[offset] & 0002) > 0 ) {
            sb.append("R");
        } else {
            sb.append("_");
        }        
        
        if( (bytes[offset] & 0004) > 0 ) {
            sb.append("W");
        } else {
            sb.append("_");
        }        
        
        offset += 4;
        
        sb.append(" VALUE: ");
        
        int value_start_offset = offset;
        
        for( int i = 0; i < 8; i++, offset++ )
            sb.append(formatByte0(bytes[offset]));        
        
        sb.append(" ");
        
        String descr = org.apache.commons.lang3.StringUtils.defaultString(MSGNavigator.props.get(tagname.toLowerCase().substring(0,4)));
        sb.append(org.apache.commons.lang3.StringUtils.rightPad(descr,max_descr_lenght));
        
        String tagtype = tagname.toLowerCase().substring(4);
        
        if( tagtype.equals("001f")) {
            
            
            String res = "";
            
            for( int i = value_start_offset + 3; i >= value_start_offset; i-- ) {
                res += formatByte0S(bytes[i]);                
            }
            
            int length = Integer.valueOf(res, 16);
            
            sb.append(" PtypString length: ");
            sb.append(String.valueOf(length - 2));
            
        } else if( tagtype.equals("0102") ) {                        
            String res = "";
            
            for( int i = value_start_offset + 3; i >= value_start_offset; i-- ) {
                res += formatByte0S(bytes[i]);                
            }
            
            int length = Integer.valueOf(res, 16);       
            
            sb.append(" PtypBinary length: ");
            sb.append(String.valueOf(length));
            
        } else if( tagtype.equals("0040")) {
            sb.append(" PtypTime ");

            
            long time = ByteConvert.convertByteArrayToLong(bytes, value_start_offset);      
            
            Date date = new Date(MSTimeConvert.PtypeTime2Millis(time));

            sb.append( date.toString() );
            
        } else if( tagtype.equals("000b")) {
            sb.append(" boolean");            
            
        } else if( tagtype.equals("0003")) {
            
            String res = "";
            
            for( int i = value_start_offset + 3; i >= value_start_offset; i-- ) {
                res += formatByte0S(bytes[i]);                
            }
            
            int length = Long.valueOf(res, 16).intValue();             
            
            sb.append(" PtypInteger32 value: ");
            sb.append(length);
        }
        
        props.add(new PropertyTag(tagname, sb.toString()));
    }
       
    public ArrayList<PropertyTag> getPropertyTags()
    {
        return props;
    }
    
    
    

}
