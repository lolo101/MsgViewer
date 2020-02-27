package net.sourceforge.MSGViewer.factory.msg.properties;

import com.auxilii.msgparser.PidTag;
import net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert;

import static com.auxilii.msgparser.Ptyp.PtypTime;

public class PropPtypTime extends PropType {
    private final long value;

    /**
     * @param tag   name of the tag
     * @param value in milliseconds since 1.1.1970
     */
    public PropPtypTime(PidTag tag, long value) {
        super(tag, PtypTime);
        this.value = MSTimeConvert.Millis2PtypeTime(value);
    }

    @Override
    protected long getPropertiesContent() {
        return value;
    }
}
