package at.redeye.FrameWork.utilities.calendar;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class BaseHolidaysTest {
    @Test
    public void testGetEaster() {
        System.out.println("getEaster");

        LocalDate[] dates = {LocalDate.of(2008, 3, 23),
                LocalDate.of(2009, 4, 12),
                LocalDate.of(2010, 4, 4),
                LocalDate.of(2011, 4, 24)
        };

        for (LocalDate dm : dates) {
            System.out.println("getEaster " + dm.getYear());
            int year = dm.getYear();
            LocalDate result = BaseHolidays.getEaster(year);
            assertEquals(dm, result);
        }

        for (int year = 1970; year < 2030; year++) {
            System.out.print("getEaster " + year + ": ");
            LocalDate result = BaseHolidays.getEaster(year);
            System.out.println(result);
        }
    }

    @Test
    public void testGetEuropeanSummerTimeBegin() {
        assertEquals(
                LocalDate.of(2010, 3, 28),
                BaseHolidays.getEuropeanSummerTimeBegin(2010));
    }

    @Test
    public void testGetEuropeanSummerTimeEnd() {
        assertEquals(
                LocalDate.of(2010, 10, 31),
                BaseHolidays.getEuropeanSummerTimeEnd(2010));
    }

    @Test
    public void testGetLastSundayOf() {
        assertEquals(
                LocalDate.of(2010, 2, 28),
                BaseHolidays.getLastSundayOf(2010, 2));
    }

}