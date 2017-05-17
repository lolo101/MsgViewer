package net.sourceforge.MSGViewer.factory.msg.PropTypes;

/**
 *
 * @author martin
 */
public class PropPtypBoolean extends PropType {

    static final String TYPE_NAME = "000b";
    private final boolean value;

    public PropPtypBoolean( String tagname )
    {
        this( tagname, false);
    }

    public PropPtypBoolean( String tagname, boolean value )
    {
        super( tagname, TYPE_NAME );
        this.value = value;
    }

    @Override
    protected void writePropertiesContent(byte[] bytes, int offset)
    {
       bytes[offset] = (byte) (value ? 0x1 : 0x0);
    }
}
