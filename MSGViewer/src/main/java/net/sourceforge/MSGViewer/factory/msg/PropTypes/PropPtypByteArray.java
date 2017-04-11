/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.PropTypes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author martin
 */
public class PropPtypByteArray extends PropType {
    
    static final String TYPE_NAME = "0102";
    private byte value[];
    
    public PropPtypByteArray(String tagname)
    {
        super( tagname, TYPE_NAME, false );
    }      
    
    public void setValue( byte value[] )
    {
        this.value = value;
    }
    
    public byte[] getValue()
    {
        return value;
    }    
    
    @Override
    public void writePropertiesEntry(byte[] bytes, int offset) 
    {              
       offset = writeTagName(getTagName(), getTypeName(), bytes, offset);
       offset = writeDefaultFlags(bytes, offset);
       
       if( value == null )
           value = new byte[0];
       
       int len = value.length+4;
 
       ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);      
       buffer.putInt(len);
       byte[] int_bytes = buffer.array();
                   
       for( int i = 0; i < int_bytes.length; i++ )
           bytes[offset++] = int_bytes[i];              
    }      
}
