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
public class PropPtypInteger32 extends PropType {
    
    static final String TYPE_NAME = "0003";
    private int value = 0;
    
    public PropPtypInteger32(String tagname)
    {
        super( tagname, TYPE_NAME, true );
    }    
    
    public PropPtypInteger32(String tagname, int value)
    {
        super( tagname, TYPE_NAME, true );
        this.value = value;
    }      
    
    public void setValue( int value )
    {
        this.value = value;
    }
    
    public int getValue()
    {
        return value;
    }
    
    @Override
    public void writePropertiesEntry(byte[] bytes, int offset) 
    {
       offset = writeTagName(getTagName(), getTypeName(), bytes, offset);
       offset = writeDefaultFlags(bytes, offset);
 
       ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
       buffer.putInt(value);
       byte[] int_bytes = buffer.array();
        
       System.arraycopy(int_bytes, 0, bytes, offset, 4);
    }    
    	
/*
    public int convertByteArrayToInt(byte[] bytebuf) {

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(bytebuf);

        return buffer.getInt(0);
    }

    public byte[] convertIntToByteArray(int val) {

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(val);
        return buffer.array();
    }
     * 
     */
}
