/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.calendar;

import org.junit.*;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
    @Test @Ignore("Not implemented")
    public static void testGetEuropeanSummerTimeBegin() {
        System.out.println("getEuropeanSummerTimeBegin");
        int year = 0;
        BaseHolidays instance = null;
        LocalDate expResult = null;
        LocalDate result = instance.getEuropeanSummerTimeBegin(year);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEuropeanSummerTimeEnd method, of class BaseHolidays.
     */
    @Test @Ignore("Not implemented")
    public static void testGetEuropeanSummerTimeEnd() {
        System.out.println("getEuropeanSummerTimeEnd");
        int year = 0;
        BaseHolidays instance = null;
        LocalDate expResult = null;
        LocalDate result = instance.getEuropeanSummerTimeEnd(year);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLastSundayOf method, of class BaseHolidays.
     */
    @Test @Ignore("Not implemented")
    public void testGetLastSundayOf() {
        System.out.println("getLastSundayOf");
        int year = 0;
        int month = 0;
        BaseHolidays instance = null;
        LocalDate expResult = null;
        LocalDate result = BaseHolidays.getLastSundayOf(year, month);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}