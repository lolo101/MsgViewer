package net.sourceforge.MSGViewer.factory.msg.properties;

import com.auxilii.msgparser.Pid;
import net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert;

import static com.auxilii.msgparser.Ptyp.PtypTime;

public class PropPtypTime extends PropType {
    private final long value;

    /**
     * @param id    id of the property
     * @param value in milliseconds since 1.1.1970
     */
    public PropPtypTime(Pid id, long value) {
        super(id, PtypTime);
        this.value = MSTimeConvert.Millis2PtypeTime(value);
    }

    @Override
    protected long getPropertiesContent() {
        return value;
    }
}
