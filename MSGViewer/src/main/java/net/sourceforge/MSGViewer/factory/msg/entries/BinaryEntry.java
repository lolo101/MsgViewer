package net.sourceforge.MSGViewer.factory.msg.entries;

import com.auxilii.msgparser.Pid;
import net.sourceforge.MSGViewer.factory.msg.properties.PropPtypByteArray;
import net.sourceforge.MSGViewer.factory.msg.properties.PropType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.auxilii.msgparser.Ptyp.PtypBinary;

public class BinaryEntry extends SubStorageEntry {

    private final byte[] value;

    public BinaryEntry(Pid tag, byte[] value) {
        super(tag, PtypBinary);
        this.value = value;
    }

    @Override
    public PropType getPropType() {
        return new PropPtypByteArray(getTag(), value.length);
    }

    @Override
    protected InputStream createEntryContent() {
        return new ByteArrayInputStream(value);
    }

}
