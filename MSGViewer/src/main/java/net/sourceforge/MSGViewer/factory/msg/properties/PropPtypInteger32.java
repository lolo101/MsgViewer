package net.sourceforge.MSGViewer.factory.msg.properties;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author martin
 */
public class PropPtypInteger32 extends PropType {

    static final String TYPE_NAME = "0003";
    private final int value;

    public PropPtypInteger32(String tagname)
    {
        this( tagname, 0);
    }

    public PropPtypInteger32(String tagname, int value)
    {
        super( tagname, TYPE_NAME );
        this.value = value;
    }

    @Override
    protected void writePropertiesContent(byte[] bytes, int offset)
    {
       ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
       buffer.putInt(value);
       byte[] int_bytes = buffer.array();

       System.arraycopy(int_bytes, 0, bytes, offset, 4);
    }
}
