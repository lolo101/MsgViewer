package net.sourceforge.MSGViewer.factory.msg.entries;

import com.auxilii.msgparser.PidTag;
import net.sourceforge.MSGViewer.factory.msg.properties.PropPtypByteArray;
import net.sourceforge.MSGViewer.factory.msg.properties.PropType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.auxilii.msgparser.Ptyp.PtypBinary;

public class BinaryEntry extends SubstGEntry {

    private final byte[] value;

    public BinaryEntry(PidTag tag, byte[] value) {
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
