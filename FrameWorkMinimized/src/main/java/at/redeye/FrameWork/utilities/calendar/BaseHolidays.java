/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.calendar;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.utilities.calendar.Holidays.HolidayInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;

public abstract class BaseHolidays {

    public String CountryCode;
    protected int last_used_year=0;
    protected Collection<HolidayInfo> last_used_holidays = null;
    Root root;

    public BaseHolidays( String CountryCode )
    {
        this.CountryCode = CountryCode;

        root = Root.getLastRoot();
    }

    public HolidayInfo create( LocalDate date, boolean floating, boolean official, String name )
    {
        return new HolidayInfo( date, floating, official, MlM(name), CountryCode );
    }

    public HolidayInfo create( int year, int month, int day, boolean floating, boolean official, String name )
    {
        return new HolidayInfo( year, month, day, floating, official, MlM(name), CountryCode );
    }

    private String MlM( String message )
    {
        if( root != null )
            return root.MlM(message);

        return message;
    }

    public static LocalDate getEaster( int year )
    {
        Easterformular easter_formular = new Easterformular(year);

        int day = easter_formular.easterday();

        if( day <= 31 )
        {
            return LocalDate.of( year, 3, day );
        } else {
            return LocalDate.of( year, 4, day - 31 );
        }
    }

    public int getNumberOfCountryCodes()
    {
        return 1;
    }

    public static LocalDate getEuropeanSummerTimeBegin(int year)
    {
        return getLastSundayOf( year, 3 );
    }

    public static LocalDate getEuropeanSummerTimeEnd(int year)
    {
        return getLastSundayOf( year, 10 );
    }

    public static LocalDate getLastSundayOf(int year, int month)
    {
        LocalDate dm = LocalDate.of( year, month, 31 );

        while( true )
        {
            if( dm.getDayOfWeek() == DayOfWeek.SUNDAY )
                return dm;

            dm = dm.minusDays(1);
        }
    }

    public abstract Collection<HolidayInfo> getHolidays(int year);


     public HolidayInfo getHolidayForDay( Calendar calendar )
     {
        if( last_used_year != calendar.get(Calendar.YEAR) ||
                last_used_holidays == null )
        {
            last_used_year = calendar.get(Calendar.YEAR);
            last_used_holidays = getHolidays(last_used_year);
        }

        for( HolidayInfo hi : last_used_holidays )
        {
            if( hi.date.getYear()        == calendar.get(Calendar.YEAR) &&
                hi.date.getMonth().getValue() == calendar.get(Calendar.MONTH)+1 &&
                hi.date.getDayOfMonth()  == calendar.get(Calendar.DAY_OF_MONTH) )
            {
                return hi;
            }
        }

        return null;
     }

    public HolidayInfo getHolidayForDay( LocalDate date )
    {
        if( last_used_year != date.getYear() ||
                last_used_holidays == null )
        {
            last_used_year = date.getYear();
            last_used_holidays = getHolidays(last_used_year);
        }

        for( HolidayInfo hi : last_used_holidays )
        {
            if( hi.date.getYear()        == date.getYear() &&
                hi.date.getMonth() == date.getMonth() &&
                hi.date.getDayOfMonth()  == date.getDayOfMonth() )
            {
                return hi;
            }
        }

        return null;
    }
}
