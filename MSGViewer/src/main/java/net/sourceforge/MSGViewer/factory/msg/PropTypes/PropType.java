/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.PropTypes;

/**
 *
 * @author martin
 * these are representation of the property data types described in 
 * MS-OXCDATA Section 2.11.1
 */
public abstract class PropType 
{
    private String typename;
    private boolean fixed_length;
    private String tagname;
    
    public PropType( String typename, boolean fixed_length )
    {
        this.typename = typename;
        this.fixed_length = fixed_length;
    }
    
    public PropType( String tagname, String typename, boolean fixed_length )
    {
        this.tagname = tagname;
        this.typename = typename;
        this.fixed_length = fixed_length;
    }    
    
    public String getTagName()
    {
        return tagname;
    }
    
    public boolean isFixeLength() {
        return fixed_length;
    }
    
    public String getTypeName() {
        return typename;
    }
    
    /* writes the 16 byte entry of the property stream into the given bytes    
     */
    public abstract void writePropertiesEntry( byte bytes[], int offset );
    
    static public int writeTagName( String tagname,  String typename, byte bytes[], int offset )
    {
        String name = tagname + typename;
        
        bytes[offset++] = Integer.valueOf(name.substring(6), 16).byteValue();
        bytes[offset++] = Integer.valueOf(name.substring(4, 6), 16).byteValue();
        bytes[offset++] = Integer.valueOf(name.substring(2, 4), 16).byteValue();
        bytes[offset++] = Integer.valueOf(name.substring(0, 2), 16).byteValue();
        
        return offset;
    }
    
    static public int writeDefaultFlags(  byte bytes[], int offset )
    {
        bytes[offset] = 0x2 | 0x4;
                
        return offset + 4;
    }    
}
