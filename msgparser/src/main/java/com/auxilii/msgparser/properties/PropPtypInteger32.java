package com.auxilii.msgparser.properties;

import com.auxilii.msgparser.Pid;

import static com.auxilii.msgparser.Ptyp.PtypInteger32;

public class PropPtypInteger32 extends PropType {

    private final int value;

    public PropPtypInteger32(Pid id, int value) {
        super(id, PtypInteger32);
        this.value = value;
    }

    @Override
    protected long getPropertiesContent() {
        return value;
    }
}
