/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.PropTypes;

/**
 *
 * @author martin
 */
public class PropPtypBoolean extends PropType {
 
    static final String TYPE_NAME = "000b";
    boolean value;
    
    public PropPtypBoolean( String tagname )
    {
        super( tagname, TYPE_NAME, true );        
        value = false;
    }
        
    public PropPtypBoolean( String tagname, boolean value )
    {
        super( tagname, TYPE_NAME, true );
        this.value = value;
    }
    
    public void setValue( boolean value )
    {
        this.value = value;
    }
    
    public boolean getValue()
    {
        return value;
    }
    
    @Override
    public void writePropertiesEntry(byte[] bytes, int offset) 
    {
       offset = writeTagName(getTagName(), getTypeName(), bytes, offset);
       offset = writeDefaultFlags(bytes, offset);
       bytes[offset] = (byte) (value ? 0x1 : 0x0);
    }
}
