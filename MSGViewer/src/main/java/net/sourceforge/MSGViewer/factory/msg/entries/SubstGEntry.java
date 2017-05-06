package net.sourceforge.MSGViewer.factory.msg.entries;

import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

    private final String type;
    private final String name;

    public SubstGEntry(String name,  String type )
    {
        this.name = name;
        this.type = type;
    }

    public void createEntry( DirectoryEntry dir, String value ) throws IOException
    {
        InputStream in = decode(value);

        dir.createDocument("__substg1.0_" + name + type.toUpperCase(), in);
    }

    private InputStream decode(String value) throws UnsupportedEncodingException {
        switch(type) {
            case TYPE_ASCII:
                return new ByteArrayInputStream(value.getBytes("ISO-8859-1"));
            case TYPE_UTF16:
                return new ByteArrayInputStream(value.getBytes("UTF-16LE"));
            default:
                throw new IllegalArgumentException(type);
        }
    }

    public void createEntry( DirectoryEntry dir, byte value[] ) throws IOException
    {
        InputStream in = new ByteArrayInputStream(value);

        dir.createDocument("__substg1.0_" + name + type.toUpperCase(), in);
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
