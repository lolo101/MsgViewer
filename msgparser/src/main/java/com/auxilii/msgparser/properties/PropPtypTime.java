package com.auxilii.msgparser.properties;

import com.auxilii.msgparser.Pid;

import static com.auxilii.msgparser.Ptyp.PtypTime;

public class PropPtypTime extends PropType {
    /**
     * difference between 1-1-1601 and 1-1-1970 in milliseconds
     */
    private static final long MS_EPOCH_OFFSET = 11644473600000L;
    private final long value;

    /**
     * @param id    id of the property
     * @param value in milliseconds since 1.1.1970
     */
    public PropPtypTime(Pid id, long value) {
        super(id, PtypTime);
        this.value = millis2PtypTime(value);
    }

    @Override
    protected long getPropertiesContent() {
        return value;
    }

    /**
     * convert a 64-bit Integer value to an MS time field.
     * MS time counts hundreds of nanoseconds since 1601.
     *
     * @return returns the time in 100 nanos since 1.1.1601
     */
    private static long millis2PtypTime(long time) {
        time += MS_EPOCH_OFFSET; // offset since 1601 in millis

        time *= 1000L; // micros
        time *= 10L; // 100 nanos

        return time;
    }
}
