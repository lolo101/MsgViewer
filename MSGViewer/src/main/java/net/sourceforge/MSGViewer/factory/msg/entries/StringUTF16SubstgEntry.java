package net.sourceforge.MSGViewer.factory.msg.entries;

import net.sourceforge.MSGViewer.factory.msg.properties.PropPtypString;
import net.sourceforge.MSGViewer.factory.msg.properties.PropType;
import java.io.IOException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

/**
 *
 * @author martin
 */
public class StringUTF16SubstgEntry extends SubstGEntry
{
    private final String value;

    public StringUTF16SubstgEntry( String name , String value ) {
        super( name, TYPE_UTF16 );
        this.value = value;
    }

    @Override
    public PropType getPropType() {
        PropPtypString prop = new PropPtypString(getTagName());
        prop.setValue(value);
        return prop;
    }

    @Override
    public void createEntry(DirectoryEntry dir) throws IOException {
        createEntry(dir,value == null ? "" : value);
    }

}
