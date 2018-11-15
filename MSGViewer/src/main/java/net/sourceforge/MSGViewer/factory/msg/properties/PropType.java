package net.sourceforge.MSGViewer.factory.msg.properties;

import java.nio.ByteBuffer;

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
    public final void writePropertiesEntry( ByteBuffer bytes ) {
        String name = tagname + typename;
        bytes.putInt(Integer.parseInt(name, 16));
        bytes.putInt(0x2 | 0x4);
        bytes.putLong(getPropertiesContent());
    }

    protected abstract long getPropertiesContent();
}
