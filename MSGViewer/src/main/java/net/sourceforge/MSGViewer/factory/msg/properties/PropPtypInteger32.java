package net.sourceforge.MSGViewer.factory.msg.properties;

/**
 *
 * @author martin
 */
public class PropPtypInteger32 extends PropType {

    static final String TYPE_NAME = "0003";
    private final int value;

    public PropPtypInteger32(String tagname, int value)
    {
        super( tagname, TYPE_NAME );
        this.value = value;
    }

    @Override
    protected long getPropertiesContent()
    {
       return value;
    }
}
