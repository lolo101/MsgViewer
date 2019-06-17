package net.sourceforge.MSGViewer.factory.msg.properties;

import net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert;

/**
 *
 * @author martin
 */
public class PropPtypTime extends PropType
{
    static final String TYPE_NAME = "0040";

    private final long value;

    /**
     *
     * @param tagname name of the tag
     * @param value in milliseconds since 1.1.1970
     */
    public PropPtypTime( String tagname, long value )
    {
        super( tagname, TYPE_NAME );

        this.value = MSTimeConvert.Millis2PtypeTime(value);
    }

    @Override
    protected long getPropertiesContent()
    {
       return value;
    }
}
