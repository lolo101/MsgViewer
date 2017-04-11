/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.calendar;

import at.redeye.FrameWork.base.Root;
import java.util.Locale;
import org.joda.time.DateMidnight;

/**
 *
 * @author martin
 */
public class MonthNames
{
    protected static String[][] codes = {
                                {"AT"}, {"Jänner",
                                        "Februar",
                                        "März",
                                        "April",
                                        "Mai",
                                        "Juni",
                                        "Juli",
                                        "August",
                                        "September",
                                        "Oktober",
                                        "November",
                                        "Dezember" } };


    public static String getFullMonthName( int month )
    {
        Root root = Root.getLastRoot();

        Locale locale = Locale.getDefault();

        if( root != null )
           locale = new Locale(root.getDisplayLanguage());

        String country = locale.getCountry();

        if( country.isEmpty() && locale.toString().contains("_") )
        {
            String parts[] = locale.toString().split("_");

            if( parts.length > 1 )
                country = parts[1].toUpperCase();
        }

        for( int i = 0; i < codes.length; i += 2 )
        {
            if( country.equals(codes[i][0]) )
            {
                return codes[i+1][month-1];
            }
        }

        // Default verhalten

        DateMidnight cal = new DateMidnight(2009, month, 1 );

        return cal.monthOfYear().getAsText(locale);
    }

}
