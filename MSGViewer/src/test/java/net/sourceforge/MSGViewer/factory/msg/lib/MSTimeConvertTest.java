package net.sourceforge.MSGViewer.factory.msg.lib;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import static net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert.MS_EPOCH_OFFSET;
import static net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert.PtypeTime2Millis;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MSTimeConvertTest {
    @Test
    public void testMsEpochOffset() {
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

        assertEquals(MS_EPOCH_OFFSET, millis);
    }

    @Test
    public void testPtypeTime2Millis() {
        long val = 129356138460000000L;

        long mil = PtypeTime2Millis(val);

        Instant date = Instant.ofEpochMilli(mil);

        System.out.println("date: " + date + " millis " + date.getEpochSecond());

        Instant date_known = LocalDateTime.of(2010, 11, 30, 18, 4, 6, 0).toInstant(ZoneOffset.UTC);

        System.out.println("date: " + date_known + " millis " + date_known.getEpochSecond());

        assertEquals(date_known, date);
    }
}
