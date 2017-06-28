package net.sourceforge.MSGViewer.factory.msg.entries;

import static net.sourceforge.MSGViewer.factory.msg.entries.SubstGEntry.TYPE_ASCII;
import static net.sourceforge.MSGViewer.factory.msg.entries.SubstGEntry.TYPE_UTF16;

import java.io.ByteArrayInputStream;
import net.sourceforge.MSGViewer.factory.msg.properties.PropPtypString;
import net.sourceforge.MSGViewer.factory.msg.properties.PropType;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author martin
 */
public class StringUTF16SubstgEntry extends SubstGEntry
{
    private final String value;

    public StringUTF16SubstgEntry( String name , String value ) {
        super( name, TYPE_UTF16 );
        this.value = value == null ? "" : value;
    }

    @Override
    public PropType getPropType() {
        return new PropPtypString(getTagName(), value.length());
    }

    @Override
    protected InputStream createEntryContent() throws UnsupportedEncodingException {
        switch(type) {
            case TYPE_ASCII:
                return new ByteArrayInputStream(value.getBytes("ISO-8859-1"));
            case TYPE_UTF16:
                return new ByteArrayInputStream(value.getBytes("UTF-16LE"));
            default:
                throw new IllegalArgumentException(type);
        }
    }

}
