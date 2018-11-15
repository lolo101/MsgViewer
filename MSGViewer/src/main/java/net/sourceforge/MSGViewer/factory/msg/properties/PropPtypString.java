package net.sourceforge.MSGViewer.factory.msg.properties;

/**
 *
 * @author martin
 */
public class PropPtypString extends PropType {

    static final String TYPE_NAME = "001f";
    private final int length;

    public PropPtypString(String tagname, int length)
    {
        super( tagname, TYPE_NAME );
        this.length = length * 2 + 2;
    }

    @Override
    protected long getPropertiesContent()
    {
       return length;
    }
}
