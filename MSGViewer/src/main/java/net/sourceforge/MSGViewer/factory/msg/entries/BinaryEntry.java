package net.sourceforge.MSGViewer.factory.msg.entries;

import java.io.IOException;
import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropPtypByteArray;
import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropType;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

public class BinaryEntry extends SubstGEntry {

    private final byte[] value;

    public BinaryEntry(String name, byte[] value) {
        super(name, TYPE_BYTES);
        this.value = value;
    }

    @Override
    public PropType getPropType() {
        PropPtypByteArray prop = new PropPtypByteArray(getTagName());
        prop.setValue(value);
        return prop;
    }

    @Override
    public void createEntry(DirectoryEntry dir) throws IOException {
        createEntry(dir, value);
    }

}
