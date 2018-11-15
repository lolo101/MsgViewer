package net.sourceforge.MSGViewer.factory.msg.properties;

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
    protected long getPropertiesContent()
    {
       return value ? 0x1 : 0x0;
    }
}
