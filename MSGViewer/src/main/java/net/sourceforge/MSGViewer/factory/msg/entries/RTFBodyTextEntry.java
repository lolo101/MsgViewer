package net.sourceforge.MSGViewer.factory.msg.entries;

import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropPtypByteArray;
import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

/**
 *
 * @author martin
 */
public class RTFBodyTextEntry extends SubstGEntry
{
    public static final String NAME = "1009";
    private static final Logger LOGGER = Logger.getLogger(RTFBodyTextEntry.class);

    private byte[] value = null;

    public RTFBodyTextEntry(String text)
    {
        super( NAME, TYPE_BYTES );
        try {
            value = text.getBytes("UTF-16LE");
        } catch( UnsupportedEncodingException ex ) {
            LOGGER.error(ex,ex);
        }
    }

    public RTFBodyTextEntry(byte[] bodyCompressesRTF) {
        super( NAME, TYPE_BYTES );
        value = bodyCompressesRTF;
    }

    public void setValue( byte value[] ) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public PropType getPropType() {
        PropPtypByteArray prop = new PropPtypByteArray(getTagName());
        prop.setValue(value);
        return prop;
    }

    @Override
    public void createEntry(DirectoryEntry dir) throws IOException {
         if( value == null ) {
             value = new byte[0];
         }
         createEntry(dir,value);
    }

}
