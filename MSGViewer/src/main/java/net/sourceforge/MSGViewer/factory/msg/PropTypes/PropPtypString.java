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
        super( tagname, TYPE_NAME, false );
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public void writePropertiesEntry(byte[] bytes, int offset)
    {
       offset = writeTagName(getTagName(), getTypeName(), bytes, offset);
       offset = writeDefaultFlags(bytes, offset);

       if( value == null ) {
           value = "";
       }

       int len = value.length()*2+2;

       ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
       buffer.putInt(len);
       byte[] int_bytes = buffer.array();

       for( int i = 0; i < int_bytes.length; i++ ) {
           bytes[offset++] = int_bytes[i];
       }
    }
}
