/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.calendar;

import java.util.Locale;

/**
 *
 * @author martin
 */
public class CalendarFactory
{
    public static Holidays getDefaultHolidays()
    {
        Locale l = Locale.getDefault();
        String country = l.getCountry();

        if (country.equals("AT")) {
            return new AustrianHolidays();
        } else if( country.equals("DE") ) {
            return new GermanHolidays();
        } else if( country.equals("CH") ) {
            return new SwitzerlandHolidays();
        }

        return null;
    }
}
