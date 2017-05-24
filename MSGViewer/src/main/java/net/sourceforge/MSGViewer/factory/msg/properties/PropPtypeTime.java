package net.sourceforge.MSGViewer.factory.msg.properties;

import net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author martin
 */
public class PropPtypeTime extends PropType
{
    static final String TYPE_NAME = "0040";

    private final long value;

    /**
     *
     * @param tagname name of the tag
     * @param value in milliseconds since 1.1.1970
     */
    public PropPtypeTime( String tagname, long value )
    {
        super( tagname, TYPE_NAME );

        this.value = MSTimeConvert.Millis2PtypeTime(value);
    }

    @Override
    protected void writePropertiesContent(byte[] bytes, int offset)
    {
       ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
       buffer.putLong(value);

       byte[] long_bytes = buffer.array();

       System.arraycopy(long_bytes, 0, bytes, offset, 8);
    }
}
