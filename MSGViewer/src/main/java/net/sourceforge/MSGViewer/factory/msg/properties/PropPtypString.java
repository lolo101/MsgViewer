package net.sourceforge.MSGViewer.factory.msg.properties;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author martin
 */
public class PropPtypString extends PropType {

    static final String TYPE_NAME = "001f";
    private final int length;

    public PropPtypString(String tagname, int length)
    {
        super( tagname, TYPE_NAME );
        this.length = length * 2 + 2;
    }

    @Override
    protected void writePropertiesContent(byte[] bytes, int offset)
    {
       ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
       buffer.putInt(length);
       byte[] int_bytes = buffer.array();

       System.arraycopy(int_bytes, 0, bytes, offset, int_bytes.length);
    }
}
