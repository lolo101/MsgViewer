package net.sourceforge.MSGViewer.factory.msg.properties;

/**
 *
 * @author martin
 */
public class PropPtypByteArray extends PropType {

    static final String TYPE_NAME = "0102";
    private final int length;

    public PropPtypByteArray(String tagname, int length)
    {
        super( tagname, TYPE_NAME );
        this.length = length + 4;
    }

    @Override
    protected long getPropertiesContent()
    {
       return length;
    }
}
