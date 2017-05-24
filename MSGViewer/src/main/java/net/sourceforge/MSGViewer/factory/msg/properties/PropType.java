package net.sourceforge.MSGViewer.factory.msg.properties;

/**
 *
 * @author martin
 * these are representation of the property data types described in
 * MS-OXCDATA Section 2.11.1
 */
public abstract class PropType
{
    private final String typename;
    private final String tagname;

    public PropType( String tagname, String typename)
    {
        this.tagname = tagname;
        this.typename = typename;
    }

    public String getTagName()
    {
        return tagname;
    }

    public String getTypeName() {
        return typename;
    }

    /* writes the 16 byte entry of the property stream into the given bytes
     */
    public final void writePropertiesEntry( byte[] bytes, int offset ) {
       offset = writeTagName(bytes, offset);
       offset = writeDefaultFlags(bytes, offset);
       writePropertiesContent(bytes, offset);
    }

    private int writeTagName(byte[] bytes, int offset )
    {
        String name = tagname + typename;

        bytes[offset++] = Integer.valueOf(name.substring(6), 16).byteValue();
        bytes[offset++] = Integer.valueOf(name.substring(4, 6), 16).byteValue();
        bytes[offset++] = Integer.valueOf(name.substring(2, 4), 16).byteValue();
        bytes[offset++] = Integer.valueOf(name.substring(0, 2), 16).byteValue();

        return offset;
    }

    private static int writeDefaultFlags(  byte[] bytes, int offset )
    {
        bytes[offset] = 0x2 | 0x4;

        return offset + 4;
    }

    protected abstract void writePropertiesContent(byte[] bytes, int offset);
}
