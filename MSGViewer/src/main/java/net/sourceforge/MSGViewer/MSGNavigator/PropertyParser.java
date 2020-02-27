package net.sourceforge.MSGViewer.MSGNavigator;

import com.auxilii.msgparser.PidTag;
import net.sourceforge.MSGViewer.factory.msg.lib.ByteConvert;
import net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author martin
 */
public class PropertyParser
{
    private final DocumentEntry entry;

    private boolean is_toplevel = false;

    private int max_descr_lenght = 20;

    private final List<String> props = new ArrayList<>();

    public PropertyParser(DocumentEntry entry) throws IOException
    {
        this.entry = entry;

        for (PidTag descr : PidTag.values()) {
            if (descr.toString().length() > max_descr_lenght) {
                max_descr_lenght = descr.toString().length();
            }
        }

        parse();
    }

    final void parse() throws IOException
    {
        if (entry.getParent().getParent() == null) {
            is_toplevel = true;
        }

        byte[] bytes = new byte[entry.getSize()];
        try (InputStream in = new DocumentInputStream(entry)) {

            int len = in.read(bytes);
            if( len != bytes.length ) {
                throw new IOException("Not all Data read");
            }
        }

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

    private void parsePropertyEntry(byte[] bytes, int offset) {

        StringBuilder sb = new StringBuilder();

        // property tag
        for( int i = offset + 3; i >= offset; i-- ) {
            sb.append(formatByte0S(bytes[i]));
        }

        String tagname = sb.toString();

        offset += 4;

        sb.append( " " );

        sb.append((bytes[offset] & 0x0001) > 0 ? "M" : "_");
        sb.append((bytes[offset] & 0x0002) > 0 ? "R" : "_");
        sb.append((bytes[offset] & 0x0004) > 0 ? "W" : "_");

        offset += 4;

        sb.append(" VALUE: ");

        int value_start_offset = offset;

        for( int i = 0; i < 8; i++, offset++ ) {
            sb.append(formatByte0(bytes[offset]));
        }

        sb.append(" ");

        PidTag descr = PidTag.from(Integer.parseInt(tagname.substring(0, 4), 16));
        sb.append(StringUtils.rightPad(descr.toString(), max_descr_lenght));

        String tagtype = tagname.toLowerCase().substring(4);

        if( tagtype.equals("001f")) {
            String res = formatBytes0(value_start_offset, bytes);
            int length = Integer.valueOf(res, 16);

            sb.append(" PtypString length: ");
            sb.append(length - 2);

        } else if( tagtype.equals("0102") ) {
            String res = formatBytes0(value_start_offset, bytes);
            int length = Integer.valueOf(res, 16);

            sb.append(" PtypBinary length: ");
            sb.append(length);

        } else if( tagtype.equals("0040")) {
            sb.append(" PtypTime ");
            long time = ByteConvert.convertByteArrayToLong(bytes, value_start_offset);
            Date date = new Date(MSTimeConvert.PtypeTime2Millis(time));

            sb.append( date.toString() );

        } else if( tagtype.equals("000b")) {
            sb.append(" boolean");

        } else if( tagtype.equals("0003")) {
            String res = formatBytes0(value_start_offset, bytes);
            int length = Long.valueOf(res, 16).intValue();

            sb.append(" PtypInteger32 value: ");
            sb.append(length);
        }

        props.add(sb.toString());
    }

    private String formatBytes0(int value_start_offset, byte[] bytes) {
        StringBuilder res = new StringBuilder();
        for( int i = value_start_offset + 3; i >= value_start_offset; i-- ) {
            res.append(formatByte0S(bytes[i]));
        }
        return res.toString();
    }

    private String formatByte0( byte b )
    {
        return String.format("%02X ", b);
    }

    private String formatByte0S( byte b )
    {
        return String.format("%02X", b);
    }

    public List<String> getPropertyTags()
    {
        return props;
    }
}
