package net.sourceforge.MSGViewer.factory.msg.properties;

import com.auxilii.msgparser.PidTag;
import com.auxilii.msgparser.Ptyp;

import java.nio.ByteBuffer;

/**
 * @author martin
 * these are representation of the property data types described in
 * MS-OXCDATA Section 2.11.1
 */
public abstract class PropType {
    private final Ptyp type;
    private final PidTag tag;

    public PropType(PidTag tag, Ptyp type) {
        this.tag = tag;
        this.type = type;
    }

    public PidTag getTag() {
        return tag;
    }

    public Ptyp getType() {
        return type;
    }

    /* writes the 16 byte entry of the property stream into the given bytes
     */
    public final void writePropertiesEntry(ByteBuffer bytes) {
        bytes.putShort((short) type.id);
        bytes.putShort((short) tag.id);
        bytes.putInt(0x2 | 0x4);
        bytes.putLong(getPropertiesContent());
    }

    protected abstract long getPropertiesContent();
}
