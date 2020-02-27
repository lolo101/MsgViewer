package net.sourceforge.MSGViewer.factory.msg.properties;

import com.auxilii.msgparser.PidTag;

import static com.auxilii.msgparser.Ptyp.PtypInteger32;

public class PropPtypInteger32 extends PropType {

    private final int value;

    public PropPtypInteger32(PidTag tag, int value) {
        super(tag, PtypInteger32);
        this.value = value;
    }

    @Override
    protected long getPropertiesContent() {
        return value;
    }
}
