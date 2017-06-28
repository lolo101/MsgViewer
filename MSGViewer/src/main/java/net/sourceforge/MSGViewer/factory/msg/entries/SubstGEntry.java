package net.sourceforge.MSGViewer.factory.msg.entries;

import net.sourceforge.MSGViewer.factory.msg.properties.PropType;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

/**
 *
 * @author martin
 */
public abstract class SubstGEntry
{
    public static final String TYPE_INT32 = "0003";
    public static final String TYPE_ASCII = "001e";
    public static final String TYPE_UTF16 = "001f";
    public static final String TYPE_BYTES = "0102";

    protected final String type;
    protected final String name;

    public SubstGEntry(String name,  String type )
    {
        this.name = name;
        this.type = type;
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

    public void createEntry(  DirectoryEntry dir ) throws IOException {
        InputStream stream = createEntryContent();
        dir.createDocument("__substg1.0_" + name + type.toUpperCase(), stream);
    }

    protected abstract InputStream createEntryContent() throws IOException;
}
