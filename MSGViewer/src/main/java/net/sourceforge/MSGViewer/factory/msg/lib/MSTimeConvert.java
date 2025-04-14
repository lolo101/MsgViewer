package net.sourceforge.MSGViewer.factory.msg.lib;

public class MSTimeConvert {

    /**
     * difference between 1-1-1601 and 1-1-1970 in milliseconds
     */
    public static final long MS_EPOCH_OFFSET = 11644473600000L;

    /**
     * convert a 64-bit Integer value to a normal time field.
     * MS time counts hundreds of nanoseconds since 1601.
     *
     * @return the time in millis since 1970 same as System.currentTime() does
     * this is the value in UTC timezone!
     */
    public static long PtypeTime2Millis(long time) {
        time /= 10L; // micros
        time /= 1000L; // millis

        time -= MS_EPOCH_OFFSET; // offset since 1601

        return time;
    }
}
