package net.sourceforge.MSGViewer.factory.msg.PropTypes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author martin
 */
public class PropPtypString extends PropType {

    static final String TYPE_NAME = "001f";
    private String value;

    public PropPtypString(String tagname)
    {
        super( tagname, TYPE_NAME );
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    @Override
    protected void writePropertiesContent(byte[] bytes, int offset)
    {
       if( value == null ) {
           value = "";
       }

       int len = value.length()*2+2;

       ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
       buffer.putInt(len);
       byte[] int_bytes = buffer.array();

       System.arraycopy(int_bytes, 0, bytes, offset, int_bytes.length);
    }
}
