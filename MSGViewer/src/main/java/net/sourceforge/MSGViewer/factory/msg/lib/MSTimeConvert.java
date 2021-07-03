package net.sourceforge.MSGViewer.factory.msg.lib;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class MSTimeConvert {

    /**
     * convert a 64 bit Integer value to a normal time field.
     * MS hat here nanoseconds since 1601
     *
     * @return the time in millis since 1970 same as System.currentTime() does
     * this is the value in UTC timezone!
     */
    public static long PtypeTime2Millis(long time) {
        time /= 10L; // micro
        time /= 1000L; // milli

        time -= 11644473600000L; // offset since 1601

        return time;
    }


    /**
     * convert a 64 bit Integer value to a normal time field.
     * MS hat here nanoseconds since 1601
     *
     * @return returns the time in nabos since 1.1.1601
     */
    public static long Millis2PtypeTime(long time) {
        time += 11644473600000L; // offset since 1601

        time *= 10L; // micro
        time *= 1000L; // milli

        return time;
    }

    public static void main(String[] args) {
        LocalDateTime dt = LocalDateTime.of(1601, 1, 1, 0, 0, 0, 0);

        long millis = 0;
        boolean first = true;

        while (dt.getYear() < 1970) {
            if (!first)
                millis += dt.minus(1, ChronoUnit.MILLIS).get(ChronoField.MILLI_OF_DAY) + 1;

            first = false;

            dt = dt.plusDays(1);
        }

        millis += 86400000;

        System.out.println(millis);

        long val = 129356138460000000L;

        long mil = PtypeTime2Millis(val);

        Instant date = Instant.ofEpochMilli(mil);

        System.out.println("date: " + date + " millis " + date.getEpochSecond());

        Instant date_known = LocalDateTime.of(2010, 11, 30, 18, 4, 6, 0).toInstant(ZoneOffset.UTC);

        System.out.println("date: " + date_known + " millis " + date_known.getEpochSecond());

        System.out.println("diff: " + (date_known.getEpochSecond() - date.getEpochSecond()));
    }
}
