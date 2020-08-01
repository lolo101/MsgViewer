/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.bindtypes;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Ignore;

/**
 *
 * @author martin
 */
public class DBStringTest {

    static class TestCases
    {
        DBString instance;
        String db_value;
        String exp_value;

        public TestCases( DBString instance, String db_value, String exp_value )
        {
            this.instance = instance;
            this.db_value = db_value;
            this.exp_value = exp_value;
        }
    }

    private static ArrayList<TestCases> test_cases;
    private static final int MAX_LEN = 10;
    private static final String NORMAL_STRING = "12345";
    private static final String LONG_STRING = "12345678901234567890";

    public DBStringTest() {
    }

    @BeforeClass
    public static void setUpClass() {

        test_cases = new ArrayList();

        test_cases.add( new TestCases( new DBString("name_only", MAX_LEN ), NORMAL_STRING, NORMAL_STRING ) );
        test_cases.add( new TestCases( new DBString("with title", "With Title", MAX_LEN ), LONG_STRING, LONG_STRING ) );
        test_cases.add( new TestCases( new DBString("empty", "empty With Title", MAX_LEN ), "", "" ) );
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
     * Test of getDBType method, of class DBString.
     */
    @Test
    public void testGetDBType() {
        System.out.println("getDBType");

        for (TestCases test : test_cases)
        {
            DBString instance = test.instance;
            DBDataType expResult = DBDataType.DB_TYPE_STRING;
            DBDataType result = instance.getDBType();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of loadFromDB method, of class DBString.
     */
    @Test
    public void testLoadFromDB() {
        System.out.println("loadFromDB");

        for (TestCases test : test_cases)
        {
            Object obj = test.db_value;
            DBString instance = test.instance;
            instance.loadFromDB(obj);
            assertEquals(test.exp_value, instance.getValue());
        }
    }

    /**
     * Test of getValue method, of class DBString.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");

        for (TestCases test : test_cases)
        {
            Object obj = test.db_value;
            DBString instance = test.instance;
            instance.loadFromDB(obj);
            assertEquals(test.exp_value, instance.getValue());
        }
    }

    /**
     * Test of toString method, of class DBString.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        for (TestCases test : test_cases)
        {
            DBString instance = test.instance;
            String expResult = test.exp_value;
            String result = instance.toString();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of loadFromString method, of class DBString.
     */
    @Test
    public void testLoadFromString() {
        System.out.println("loadFromString");

        for (TestCases test : test_cases)
        {
            String obj = test.db_value;
            DBString instance = test.instance;
            instance.loadFromString(obj);
            assertEquals(test.exp_value, instance.getValue());
        }
    }

    /**
     * Test of acceptString method, of class DBString.
     */
    @Test
    public void testAcceptString() {
        System.out.println("acceptString");

        ArrayList<SimpleEntry<String,Boolean>> test_strings = new ArrayList();

        test_strings.add(new SimpleEntry<>(NORMAL_STRING, true));
        test_strings.add(new SimpleEntry<>(LONG_STRING, false));

        for (TestCases test : test_cases)
        {
            for( SimpleEntry<String,Boolean> e : test_strings )
            {
                String s = e.getKey();
                DBString instance = test.instance;
                boolean expResult = e.getValue();
                boolean result = instance.acceptString(s);
                assertEquals(expResult, result);
            }
        }
    }

    /**
     * Test of getCopy method, of class DBString.
     */
    @Test
    public void testGetCopy() {
        System.out.println("getCopy");

        for (TestCases test : test_cases)
        {
            DBString instance = test.instance;
            DBString result = instance.getCopy();

            assertNotSame(result, instance);
        }
    }

    /**
     * Test of loadFromCopy method, of class DBString.
     */
    @Test
    public void testLoadFromCopy() {
        System.out.println("loadFromCopy");

        for (TestCases test : test_cases)
        {
            Object obj = NORMAL_STRING;
            DBString instance = test.instance;
            instance.loadFromCopy(obj);

            assertEquals(obj, instance.value);
        }
    }

    /**
     * Test of getMaxLen method, of class DBString.
     */
    @Test
    public void testGetMaxLen() {
        System.out.println("getMaxLen");

        for (TestCases test : test_cases)
        {
            DBString instance = test.instance;
            int result = instance.getMaxLen();
            assertEquals(MAX_LEN, result);
        }
    }

    /**
     * Test of isEmpty method, of class DBString.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");

        for (TestCases test : test_cases)
        {
            DBString instance = test.instance;
            boolean expResult = false;
            boolean result = instance.isEmpty();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of isEmptyTrimmed method, of class DBString.
     */
    @Test @Ignore("Not implemented")
    public void testIsEmptyTrimmed() {
        System.out.println("isEmptyTrimmed");
        DBString instance = null;
        boolean expResult = false;
        boolean result = instance.isEmptyTrimmed();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
