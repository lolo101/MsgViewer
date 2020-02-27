package net.sourceforge.MSGViewer.factory.msg.properties;

import com.auxilii.msgparser.PidTag;

import static com.auxilii.msgparser.Ptyp.PtypBoolean;

public class PropPtypBoolean extends PropType {

    private final boolean value;

    public PropPtypBoolean(PidTag tag, boolean value) {
        super(tag, PtypBoolean);
        this.value = value;
    }

    @Override
    protected long getPropertiesContent() {
        return value ? 0x1 : 0x0;
    }
}
