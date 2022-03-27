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

        switch (country) {
            case "AT":
                return new AustrianHolidays();
            case "DE":
                return new GermanHolidays();
            case "CH":
                return new SwitzerlandHolidays();
            default:
                return null;
        }
    }
}
