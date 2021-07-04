package at.redeye.FrameWork.utilities;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void testContains() {
        System.out.println("contains");

        assertFalse(StringUtils.contains(' ', ""));
        assertTrue(StringUtils.contains('x', "xxxx"));
        assertTrue(StringUtils.contains('x', "xyz"));
        assertFalse(StringUtils.contains('x', "yyyyyyyyyyyy"));

        try {
            StringUtils.contains('x', null);
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }
    }

    @Test
    public void testSkip_char() {
        System.out.println("skip_char");

        assertEquals(12, StringUtils.skip_char(new StringBuilder( "das ist     ein Text"), " ", 7));
        assertEquals(2,  StringUtils.skip_char(new StringBuilder( "das ist     ein Text"), " ", 2));
        assertEquals(4,  StringUtils.skip_char(new StringBuilder( "das ist     ein Text"), " ", 4));
        assertEquals(4,  StringUtils.skip_char(new StringBuilder( "das ist     ein Text"), " ", 3));

        try {
            StringUtils.skip_char(null, null, 0);
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try {
            StringUtils.skip_char(new StringBuilder("x"), null, 0);
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try {
            StringUtils.skip_char(null, " ", 0);
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }
    }

    @Test
    public void testSkip_char_reverse() {
        System.out.println("skip_char_reverse");

        assertEquals(15, StringUtils.skip_char_reverse(new StringBuilder("das ist ein    Test"), " ", 15));
        assertEquals(10, StringUtils.skip_char_reverse(new StringBuilder("das ist ein    Test"), " ", 14));
        assertEquals(10, StringUtils.skip_char_reverse(new StringBuilder("das ist ein    Test"), " ", 10));

        assertEquals(0,StringUtils.skip_char_reverse(null, null, 0));
        assertEquals(0,StringUtils.skip_char_reverse(new StringBuilder("x"), null, 0));
        assertEquals(0,StringUtils.skip_char_reverse(null, " ", 0));

        try {
            StringUtils.skip_char_reverse(null, null, 10);
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try {
            StringUtils.skip_char_reverse(new StringBuilder("xyz"), null, 1);
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try {
            StringUtils.skip_char_reverse(null, " ", 1);
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }
    }

    @Test
    public void testSkip_spaces_reverse() {
        System.out.println("skip_spaces_reverse");

        assertEquals(15, StringUtils.skip_spaces_reverse(new StringBuilder("das ist ein    Test"), 15));
        assertEquals(10, StringUtils.skip_spaces_reverse(new StringBuilder("das ist ein \t   Test"), 14));
        assertEquals(10, StringUtils.skip_spaces_reverse(new StringBuilder("das ist ein    Test"), 10));


        try
        {
            StringUtils.skip_spaces_reverse(null, 1 );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }
    }

    @Test
    public void testSkip_spaces() {
        System.out.println("skip_spaces");

        assertEquals(12, StringUtils.skip_spaces(new StringBuilder( "das ist     ein Text"), 7));
        assertEquals(2,  StringUtils.skip_spaces(new StringBuilder( "das ist     ein Text"), 2));
        assertEquals(4,  StringUtils.skip_spaces(new StringBuilder( "das ist     ein Text"), 4));
        assertEquals(4,  StringUtils.skip_spaces(new StringBuilder( "das ist     ein Text"), 3));


        try
        {
            StringUtils.skip_spaces(null, 1 );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

    }

    @Test
    public void testIs_space() {
        System.out.println("is_space");

        assertTrue(StringUtils.is_space(' '));
        assertTrue(StringUtils.is_space('\t'));
        assertTrue(StringUtils.is_space('\r'));
        assertTrue(StringUtils.is_space('\n'));
        assertFalse(StringUtils.is_space('\0'));
        assertFalse(StringUtils.is_space('x'));
    }

    @Test
    public void testSplit_str() {
        System.out.println("split_str");

        String[] testArr = { "hallo", "das", "ist", "", "ein", "" , "", "test", "" };

        StringBuilder joined_string = new StringBuilder();

        for( String str: testArr )
        {
            if( joined_string.length() > 0 ) {
                joined_string.append('|');
            }

            joined_string.append(str);
        }

        List<String> split_str = StringUtils.split_str(joined_string, "|");

        assertEquals(testArr.length, split_str.size());

        for( int i = 0; i < testArr.length; i++ )
        {
            if( !testArr[i].equals(split_str.get(i)) ) {
                fail( String.format("Target: '%s' Index %d '%s' != '%s'",
                        joined_string, i, testArr[i],split_str.get(i) ) );
            }
        }

    }

    @Test
    public void testStrip_StringBuilder_String() {
        System.out.println("strip");

        try
        {
            StringUtils.strip( (StringBuilder)null, "" );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try
        {
            StringUtils.strip( (StringBuilder)null, null );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try
        {
            StringUtils.strip( new StringBuilder("xxxx"), null );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        assertEquals("xyz", StringUtils.strip( new StringBuilder("xyz"), ""));
        assertEquals("xyz", StringUtils.strip( new StringBuilder(" xyz "), " "));
        assertEquals("xyz", StringUtils.strip( new StringBuilder("  xyz"), " "));
        assertEquals("xyz\t", StringUtils.strip( new StringBuilder("  xyz\t"), " "));
        assertEquals("xyz\t", StringUtils.strip( new StringBuilder("  xyz\t "), " "));
        assertEquals("xy z", StringUtils.strip( new StringBuilder("  xy z\t "), " \t"));
        assertEquals("xy z", StringUtils.strip( new StringBuilder("  xy z\t \t"), " \t"));
        assertEquals("xy z", StringUtils.strip( new StringBuilder("  xy z\t \n"), " \t\n"));
    }

    @Test
    public void testStrip_post_StringBuilder_String() {
        System.out.println("strip_post");

        try
        {
            StringUtils.strip_post( (StringBuilder)null, "" );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try
        {
            StringUtils.strip_post( (StringBuilder)null, null );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try
        {
            StringUtils.strip_post( new StringBuilder("xxxx"), null );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        assertEquals("xyz", StringUtils.strip_post(new StringBuilder("xyz"), ""));
        assertEquals(" xyz", StringUtils.strip_post(new StringBuilder(" xyz "), " "));
        assertEquals("  xyz", StringUtils.strip_post(new StringBuilder("  xyz"), " "));
        assertEquals("  xyz\t", StringUtils.strip_post(new StringBuilder("  xyz\t"), " "));
        assertEquals("  xyz\t", StringUtils.strip_post(new StringBuilder("  xyz\t "), " "));
        assertEquals("  xy z", StringUtils.strip_post(new StringBuilder("  xy z\t "), " \t"));
        assertEquals("  xy z", StringUtils.strip_post(new StringBuilder("  xy z\t \t"), " \t"));
        assertEquals("  xy z", StringUtils.strip_post(new StringBuilder("  xy z\t \n"), " \t\n"));

    }

    @Test
    public void testStrip_post_String_String() {
        System.out.println("strip_post");

        try
        {
            StringUtils.strip_post( (String)null, "" );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try
        {
            StringUtils.strip_post( (String)null, null );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try
        {
            StringUtils.strip_post( "xxxx", null );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        assertEquals("xyz", StringUtils.strip_post("xyz", ""));
        assertEquals(" xyz", StringUtils.strip_post(" xyz ", " "));
        assertEquals("  xyz", StringUtils.strip_post("  xyz", " "));
        assertEquals("  xyz\t", StringUtils.strip_post("  xyz\t", " "));
        assertEquals("  xyz\t", StringUtils.strip_post("  xyz\t ", " "));
        assertEquals("  xy z", StringUtils.strip_post("  xy z\t ", " \t"));
        assertEquals("  xy z", StringUtils.strip_post("  xy z\t \t", " \t"));
        assertEquals("  xy z", StringUtils.strip_post("  xy z\t \n", " \t\n"));

    }

    @Test
    public void testStrip_String_String() {
        System.out.println("strip");

        try
        {
            StringUtils.strip( (String)null, "" );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try
        {
            StringUtils.strip( (String)null, null );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        try
        {
            StringUtils.strip( "xxxx", null );
            fail( "nullpointer throws no exception");
        } catch( NullPointerException ex ) {

        }

        assertEquals("xyz", StringUtils.strip("xyz", ""));
        assertEquals("xyz", StringUtils.strip(" xyz ", " "));
        assertEquals("xyz", StringUtils.strip("  xyz", " "));
        assertEquals("xyz\t", StringUtils.strip("  xyz\t", " "));
        assertEquals("xyz\t", StringUtils.strip("  xyz\t ", " "));
        assertEquals("xy z", StringUtils.strip("  xy z\t ", " \t"));
        assertEquals("xy z", StringUtils.strip("  xy z\t \t", " \t"));
        assertEquals("xy z", StringUtils.strip("  xy z\t \n", " \t\n"));
    }

    @Test
    public void testSet_defaultAutoLineLenght() {
        System.out.println("set_defaultAutoLineLenght");

        StringUtils.set_defaultAutoLineLenght(10);
        assertEquals(10, StringUtils.get_defaultAutoLineLenght());
    }

    @Test
    public void testGet_defaultAutoLineLenght() {
        System.out.println("get_defaultAutoLineLenght");

        StringUtils.set_defaultAutoLineLenght(10);
        assertEquals(10, StringUtils.get_defaultAutoLineLenght());
    }

    String testTextA = "Bla ist eine Kleinstadt im Süden von Mali. Sie liegt etwa 85 Kilometer südlich von Ségou";

    String testTextA10 =
            "Bla ist eine\n" +
            "Kleinstadt im\n" +
            "Süden von Mali.\n" +
            "Sie liegt etwa\n" +
            "85 Kilometer\n" +
            "südlich von\n" +
            "Ségou";

    String testTextA20 =
         "Bla ist eine Kleinstadt\n" +
         "im Süden von Mali.\n" +
         "Sie liegt etwa 85\n" +
         "Kilometer südlich von\n" +
         "Ségou";

    String testTextA30 =
            "Bla ist eine Kleinstadt im Süden\n" +
            "von Mali. Sie liegt etwa 85\n" +
            "Kilometer südlich von Ségou";

    String testTextA40 =
            "Bla ist eine Kleinstadt im Süden von Mali.\n" +
            "Sie liegt etwa 85 Kilometer südlich von\n" +
            "Ségou";

    String testTextA50 =
            "Bla ist eine Kleinstadt im Süden von Mali.\n" +
            "Sie liegt etwa 85 Kilometer südlich von Ségou";

    String testTextB =
            "Bla ist eine Kleinstadt im Süden von Mali. Sie liegt etwa 85 Kilometer südlich von Ségou an der wichtigsten Fernverkehrsstraße des Landes. In Bla leben 61.338 Menschen[1] (Zensus 2009). ";

    String testTextB50 =
            "Bla ist eine Kleinstadt im Süden von Mali.\n" +
            "Sie liegt etwa 85 Kilometer südlich von Ségou an der\n" +
            "wichtigsten Fernverkehrsstraße des Landes. In Bla leben 61.\n" +
            "338 Menschen[1] (Zensus 2009).";

    String testTextC =
            "Bla ist eine Kleinstadt im Süden von Mali. Sie liegt etwa 85 Kilometer südlich von Ségou an der wichtigsten Fernverkehrsstraße des Landes. In Bla leben 61.338 Menschen[1] (Zensus 2009). " +
            "Die Stadt ist ein wichtiger Warenumschlagplatz für die nordöstlich gelegen Städte Mopti und Gao, sowie für das südlich gelegene Sikasso und den Export nach Burkina Faso und Elfenbeinküste. " +
            "Bla ist die Hauptstadt der \"Djonka\", einer Untergruppe der Bambara.";

    String testTextC50 =
            "Bla ist eine Kleinstadt im Süden von Mali.\n"+
            "Sie liegt etwa 85 Kilometer südlich von Ségou an der\n"+
            "wichtigsten Fernverkehrsstraße des Landes. In Bla leben 61.\n"+
            "338 Menschen[1] (Zensus 2009). Die Stadt ist ein\n"+
            "wichtiger Warenumschlagplatz für die nordöstlich\n"+
            "gelegen Städte Mopti und Gao, sowie für das südlich\n"+
            "gelegene Sikasso und den Export nach Burkina Faso\n"+
            "und Elfenbeinküste. Bla ist die Hauptstadt der \"Djonka\",\n"+
            "einer Untergruppe der Bambara.";

    protected String diff_text( String a, String b )
    {
        if( a.equals(b) ) {
            return "";
        }

        String[] split_a = a.split("\n");
        String[] split_b = b.split("\n");

        assertEquals(split_b.length, split_a.length);

        StringBuilder res = new StringBuilder();

        for( int i = 0; i < split_a.length; i++ )
        {
            if( !split_a[i].equals(split_b[i]))
            {
                res.append(String.format("diff at line %d a : '%s'\n", i, split_a[i] ) );
                res.append(String.format("diff at line %d b : '%s'\n", i, split_b[i] ) );

                res.append("                       ");

                for( int j = 0; j < split_a[i].length() && j < split_b[i].length(); j++ )
                {
                    if( split_a[i].charAt(j) != split_b[i].charAt(j) )
                    {
                        res.append("^");
                    }
                    else
                    {
                        res.append(" ");
                    }
                }

                if( split_a[i].length() != split_b[i].length() )
                {
                    res.append("length error");
                }

                res.append("\n");
                break;
            }
        }
        return res.toString();
    }

    void assertText( String expResult, String Result )
    {
        String erg = diff_text(expResult, Result);

        if( !erg.isEmpty() )
        {
            System.out.println(erg);
            fail( erg );
        }
    }

    @Test
    public void testAutoLineBreak_String() {
        System.out.println("autoLineBreak");

        String res;

        StringUtils.set_defaultAutoLineLenght(10);
        res = StringUtils.autoLineBreak(testTextA);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA10, res);

        StringUtils.set_defaultAutoLineLenght(20);
        res = StringUtils.autoLineBreak(testTextA);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA20, res);

        StringUtils.set_defaultAutoLineLenght(30);
        res = StringUtils.autoLineBreak(testTextA);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA30, res);

        StringUtils.set_defaultAutoLineLenght(40);
        res = StringUtils.autoLineBreak(testTextA);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA40, res);

        StringUtils.set_defaultAutoLineLenght(50);
        res = StringUtils.autoLineBreak(testTextA);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA50, res);

        StringUtils.set_defaultAutoLineLenght(50);
        res = StringUtils.autoLineBreak(testTextB);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextB50, res);

        StringUtils.set_defaultAutoLineLenght(50);
        res = StringUtils.autoLineBreak(testTextC);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextC50, res);
    }

    @Test
    public void testAutoLineBreak_StringBuilder_int() {
        System.out.println("autoLineBreak");

        String res;

        res = StringUtils.autoLineBreak(new StringBuilder(testTextA),10);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA10, res);

        res = StringUtils.autoLineBreak(new StringBuilder(testTextA),20);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA20, res);

        res = StringUtils.autoLineBreak(new StringBuilder(testTextA),30);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA30, res);

        res = StringUtils.autoLineBreak(new StringBuilder(testTextA),40);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA40, res);

        res = StringUtils.autoLineBreak(new StringBuilder(testTextA),50);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA50, res);

        res = StringUtils.autoLineBreak(new StringBuilder(testTextB),50);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextB50, res);

        res = StringUtils.autoLineBreak(new StringBuilder(testTextC),50);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextC50, res);
    }

    @Test
    public void testAutoLineBreak_StringBuilder() {
        System.out.println("autoLineBreak");

        String res;

        StringUtils.set_defaultAutoLineLenght(10);
        res = StringUtils.autoLineBreak(new StringBuilder(testTextA));
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA10, res);

        StringUtils.set_defaultAutoLineLenght(20);
        res = StringUtils.autoLineBreak(new StringBuilder(testTextA));
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA20, res);

        StringUtils.set_defaultAutoLineLenght(30);
        res = StringUtils.autoLineBreak(new StringBuilder(testTextA));
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA30, res);

        StringUtils.set_defaultAutoLineLenght(40);
        res = StringUtils.autoLineBreak(new StringBuilder(testTextA));
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA40, res);

        StringUtils.set_defaultAutoLineLenght(50);
        res = StringUtils.autoLineBreak(new StringBuilder(testTextA));
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA50, res);

        StringUtils.set_defaultAutoLineLenght(50);
        res = StringUtils.autoLineBreak(new StringBuilder(testTextB));
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextB50, res);

        StringUtils.set_defaultAutoLineLenght(50);
        res = StringUtils.autoLineBreak(new StringBuilder(testTextC));
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextC50, res);
    }

    @Test
    public void testAutoLineBreak_String_int() {
        System.out.println("autoLineBreak");

        String res;

        res = StringUtils.autoLineBreak(testTextA,10);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA10, res);

        res = StringUtils.autoLineBreak(testTextA,20);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA20, res);

        res = StringUtils.autoLineBreak(testTextA,30);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA30, res);

        res = StringUtils.autoLineBreak(testTextA,40);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA40, res);

        res = StringUtils.autoLineBreak(testTextA,50);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextA50, res);

        res = StringUtils.autoLineBreak(testTextB,50);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextB50, res);

        res = StringUtils.autoLineBreak(testTextC,50);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText(testTextC50, res);
    }

    @Test
    public void testFormatDouble_double_int() {
        System.out.println("formatDouble");

        assertEquals(String.format("%.3f", 12.234), StringUtils.formatDouble(12.2340000, 3) );
        assertEquals(String.format("%.2f", 0.03), StringUtils.formatDouble(0.03, 3) );
        assertEquals("12", StringUtils.formatDouble(12.0000, 3) );

    }

    @Test
    public void testFormatDouble_double() {
        System.out.println("formatDouble");

        assertEquals(String.format("%.3f", 12.234), StringUtils.formatDouble(12.2340000) );
        assertEquals(String.format("%.2f", 0.03), StringUtils.formatDouble(0.03) );
        assertEquals("12", StringUtils.formatDouble(12.0000) );

    }

    @Test
    public void testIsYes() {
        System.out.println("isYes");

        assertTrue(StringUtils.isYes("yes"));
        assertFalse(StringUtils.isYes("no"));
        assertTrue(StringUtils.isYes("X"));
        assertFalse(StringUtils.isYes(" "));
        assertFalse(StringUtils.isYes(null));
        assertFalse(StringUtils.isYes("0"));
        assertTrue(StringUtils.isYes("1"));
        assertFalse(StringUtils.isYes("hugo"));
    }

    @Test
    public void testExceptionToString() {
        System.out.println("exceptionToString");

        try
        {
            int i = Integer.parseInt("asdfasdf");
        } catch( NumberFormatException ex ) {

            String res = StringUtils.exceptionToString(ex);

            if( res.length() < 100 )
            {
                System.out.println(res);
                fail( "something wrong with that exception to String function");
            }
        }


    }

    @Test
    public void testSkipLeadingLines() {
        System.out.println("skipLeadingLines");

        String testTest =
                "ONE\n" +
                "TWO\r\n" +
                "THREE\n" +
                "FOUR\n" +
                "\r\n" +
                "\r\n" +
                "FIVE\n";

        assertEquals(testTest.substring(4), StringUtils.skipLeadingLines(testTest, 1));
        assertEquals(testTest, StringUtils.skipLeadingLines(testTest, 0));
        assertEquals(testTest, StringUtils.skipLeadingLines(testTest, -1));
        assertEquals(testTest.substring(9), StringUtils.skipLeadingLines(testTest, 2));
        assertEquals("", StringUtils.skipLeadingLines(testTest, 100));
    }

    @Test
    public void testByteArrayToString() {
        System.out.println("byteArrayToString");

        byte[] bytes = {72,97,108,108,111};

        assertEquals("Hallo",StringUtils.byteArrayToString(bytes));
    }

}
