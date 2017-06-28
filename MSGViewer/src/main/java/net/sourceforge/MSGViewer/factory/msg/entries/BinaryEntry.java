package net.sourceforge.MSGViewer.factory.msg.entries;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import net.sourceforge.MSGViewer.factory.msg.properties.PropPtypByteArray;
import net.sourceforge.MSGViewer.factory.msg.properties.PropType;

public class BinaryEntry extends SubstGEntry {

    private final byte[] value;

    public BinaryEntry(String name, byte[] value) {
        super(name, TYPE_BYTES);
        this.value = value;
    }

    @Override
    public PropType getPropType() {
        return new PropPtypByteArray(getTagName(), value.length);
    }

    @Override
    protected InputStream createEntryContent() {
        return new ByteArrayInputStream(value);
    }

}
