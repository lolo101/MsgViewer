/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.calendar;

import org.junit.*;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class BaseHolidaysTest {

    public BaseHolidaysTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEaster method, of class BaseHolidays.
     */
    @Test
    public void testGetEaster() {
        System.out.println("getEaster");

        LocalDate[] dates = { LocalDate.of(2008,3,23),
                              LocalDate.of(2009,4,12),
                              LocalDate.of(2010,4,4),
                              LocalDate.of(2011,4,24)
                              };

        for( LocalDate dm : dates )
        {
            System.out.println("getEaster " + dm.getYear());
            int year = dm.getYear();
            LocalDate result = BaseHolidays.getEaster(year);
            assertEquals(dm, result);
        }

        for( int year = 1970; year < 2030; year++ )
        {
            System.out.print("getEaster " + year + ": ");
            LocalDate result = BaseHolidays.getEaster(year);
            System.out.println(result);
        }
    }

    /**
     * Test of getEuropeanSummerTimeBegin method, of class BaseHolidays.
     */
    @Test
    public void testGetEuropeanSummerTimeBegin() {
        assertEquals(
                LocalDate.of(2010, 3, 28),
                BaseHolidays.getEuropeanSummerTimeBegin(2010));
    }

    /**
     * Test of getEuropeanSummerTimeEnd method, of class BaseHolidays.
     */
    @Test
    public void testGetEuropeanSummerTimeEnd() {
        assertEquals(
                LocalDate.of(2010, 10, 31),
                BaseHolidays.getEuropeanSummerTimeEnd(2010));
    }

    /**
     * Test of getLastSundayOf method, of class BaseHolidays.
     */
    @Test
    public void testGetLastSundayOf() {
        assertEquals(
                LocalDate.of(2010, 2, 28),
                BaseHolidays.getLastSundayOf(2010, 2));
    }

}