package net.sourceforge.MSGViewer.factory.msg.properties;

import com.auxilii.msgparser.Pid;
import com.auxilii.msgparser.Ptyp;

import java.nio.ByteBuffer;

/**
 * @author martin
 * these are representation of the property data types described in
 * MS-OXCDATA Section 2.11.1
 */
public abstract class PropType {
    private static final int PROPATTR_READABLE = 0x2;
    private static final int PROPATTR_WRITABLE = 0x4;
    private final Ptyp type;
    private final Pid id;

    public PropType(Pid id, Ptyp type) {
        this.id = id;
        this.type = type;
    }

    /**
     * Writes the 16-byte entry of the property stream into the given bytes.
     * See [MS-OXMSG] §2.4.2
     */
    public final void writePropertiesEntry(ByteBuffer bytes) {
        int tag = (id.id << 16) + type.id;
        bytes.putInt(tag);
        bytes.putInt(PROPATTR_READABLE | PROPATTR_WRITABLE);
        bytes.putLong(getPropertiesContent());
    }

    protected abstract long getPropertiesContent();
}
