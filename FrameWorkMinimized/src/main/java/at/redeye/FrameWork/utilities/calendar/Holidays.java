/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.calendar;

import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Collection;

/**
 *
 * @author Administrator
 */
public interface Holidays {

    class HolidayInfo
    {
        public LocalDate date;

        public boolean floating_holiday;
        public boolean official_holiday;
        public boolean merge_to_primary=false;

        public String name;
        public String CountryCode;

        public HolidayInfo(HolidayInfo hi) {
            date = new LocalDate( hi.date );
            floating_holiday = hi.floating_holiday;
            official_holiday = hi.official_holiday;
            merge_to_primary = hi.merge_to_primary;
            name = hi.name;
            CountryCode = hi.CountryCode;
        }

        private HolidayInfo()
        {}

        public HolidayInfo( LocalDate date, boolean floating, boolean official, String name, String CountryCode )
        {
            this.date = date;
            floating_holiday = floating;
            official_holiday = official;
            this.name = name;
            this.CountryCode = CountryCode;
        }

        public HolidayInfo( LocalDate date, boolean floating, boolean official, String name, String CountryCode, boolean merge_to_primary )
        {
            this.date = date;
            floating_holiday = floating;
            official_holiday = official;
            this.name = name;
            this.CountryCode = CountryCode;
            this.merge_to_primary = merge_to_primary;
        }

        public HolidayInfo( int year, int month, int day, boolean floating, boolean official, String name, String CountryCode )
        {
            date = new LocalDate( year, month, day );
            floating_holiday = floating;
            official_holiday = official;
            this.name = name;
            this.CountryCode = CountryCode;
        }
    }

    /*
     * @return returns a list of holidays for this year.
     * @year
     */
    Collection<HolidayInfo> getHolidays(int year);

    int getNumberOfCountryCodes();

    String getPrimaryCountryCode();

    /*
     * @return returns a list of holidays a specific date.
     * @date date for the holiday we are looking for
     */
    HolidayInfo getHolidayForDay(DateMidnight date);

/*
     * @return returns a list of holidays a specific date.
     * @date date for the holiday we are looking for
     */
HolidayInfo getHolidayForDay(LocalDate date);


    HolidayInfo getHolidayForDay(Calendar cal);
}
