/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.PropTypes;

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
    
    long value;       
    
    /**
     * 
     * @param tagname name of the tag
     * @param val in milliseconds since 1.1.1970
     */
    public PropPtypeTime( String tagname, long val )
    {
        super( tagname, TYPE_NAME , true );
        
        setValue( val );
    }    
    
    /**     
     * @param value milliseconds since 1.1.1970
     */
    public void setValue( long value )
    {
        this.value = MSTimeConvert.Millis2PtypeTime(value);
    }
    
    public long getValue()
    {
        return value;
    }
    
    @Override
    public void writePropertiesEntry(byte[] bytes, int offset) 
    {
       offset = writeTagName(getTagName(), getTypeName(), bytes, offset);
       offset = writeDefaultFlags(bytes, offset);
 
       ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
       buffer.putLong(value);
              
       byte[] long_bytes = buffer.array();
                    
       System.arraycopy(long_bytes, 0, bytes, offset, 8);
    }        
}
