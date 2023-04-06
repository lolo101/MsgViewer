package at.redeye.FrameWork.utilities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    public void testStrip_StringBuilder_String() {
        System.out.println("strip");

        assertThrows(NullPointerException.class, () -> StringUtils.strip(null, ""));
        assertThrows(NullPointerException.class, () -> StringUtils.strip(null, null));
        assertThrows(NullPointerException.class, () -> StringUtils.strip("xxxx", null));

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
    public void testStrip_post_StringBuilder_String() {
        System.out.println("strip_post");

        assertThrows(NullPointerException.class, () -> StringUtils.strip_post(null, ""));
        assertThrows(NullPointerException.class, () -> StringUtils.strip_post(null, null));
        assertThrows(NullPointerException.class, () -> StringUtils.strip_post("xxxx", null));

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
    public void testStrip_post_String_String() {
        System.out.println("strip_post");

        assertThrows(NullPointerException.class, () -> StringUtils.strip_post(null, ""));
        assertThrows(NullPointerException.class, () -> StringUtils.strip_post(null, null));
        assertThrows(NullPointerException.class, () -> StringUtils.strip_post("xxxx", null));

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

        assertThrows(NullPointerException.class, () -> StringUtils.strip(null, ""));
        assertThrows(NullPointerException.class, () -> StringUtils.strip(null, null));
        assertThrows(NullPointerException.class, () -> StringUtils.strip("xxxx", null));

        assertEquals("xyz", StringUtils.strip("xyz", ""));
        assertEquals("xyz", StringUtils.strip(" xyz ", " "));
        assertEquals("xyz", StringUtils.strip("  xyz", " "));
        assertEquals("xyz\t", StringUtils.strip("  xyz\t", " "));
        assertEquals("xyz\t", StringUtils.strip("  xyz\t ", " "));
        assertEquals("xy z", StringUtils.strip("  xy z\t ", " \t"));
        assertEquals("xy z", StringUtils.strip("  xy z\t \t", " \t"));
        assertEquals("xy z", StringUtils.strip("  xy z\t \n", " \t\n"));
        assertEquals("", StringUtils.strip("   ", " "));
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

    protected static String diff_text(String a, String b)
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

    static void assertText(String expResult, String Result)
    {
        String erg = diff_text(expResult, Result);
        assertEquals("", erg);
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

        res = StringUtils.autoLineBreak("short",50);
        System.out.println("=== RES ===\n" +  res + "\n============\n");
        assertText("short", res);
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
}
