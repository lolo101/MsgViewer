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
    private final Ptyp type;
    private final Pid id;

    public PropType(Pid id, Ptyp type) {
        this.id = id;
        this.type = type;
    }

    public Pid getId() {
        return id;
    }

    public Ptyp getType() {
        return type;
    }

    /* writes the 16 byte entry of the property stream into the given bytes
     */
    public final void writePropertiesEntry(ByteBuffer bytes) {
        int tag = (id.id << 16) + type.id;
        bytes.putInt(tag);
        bytes.putInt(0x2 | 0x4);
        bytes.putLong(getPropertiesContent());
    }

    protected abstract long getPropertiesContent();
}
