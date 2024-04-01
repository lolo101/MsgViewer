package com.auxilii.msgparser.properties;

import com.auxilii.msgparser.Pid;

import static com.auxilii.msgparser.Ptyp.PtypString;

public class PropPtypString extends PropType {

    private final int length;

    public PropPtypString(Pid id, int length) {
        super(id, PtypString);
        this.length = length * 2 + 2;
    }

    @Override
    protected long getPropertiesContent() {
        return length;
    }
}
