/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.calendar;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.utilities.calendar.Holidays.HolidayInfo;
import java.util.Calendar;

import java.util.Collection;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

/**
 *
 * @author martin
 */
public abstract class BaseHolidays {
    
    public String CountryCode = "";
    protected int last_used_year=0;
    protected Collection<HolidayInfo> last_used_holidays = null;
    Root root;

    public BaseHolidays( String CountryCode )
    {
        this.CountryCode = CountryCode;

        root = Root.getLastRoot();
    }
    
    public HolidayInfo create( DateMidnight date, boolean floating, boolean official, String name )
    {
        return new HolidayInfo( date.toLocalDate(), floating, official, MlM(name), CountryCode );
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
            return new LocalDate( year, 3, day );
        } else {
            return new LocalDate( year, 4, day - 31 );
        }
    }
    
    public int getNumberOfCountryCodes()
    {
        return 1;
    }
    
    public LocalDate getEuropeanSummerTimeBegin( int year )
    {
        return getLastSundayOf( year, 3 );
    }
    
    public LocalDate getEuropeanSummerTimeEnd( int year )
    {
        return getLastSundayOf( year, 10 );
    }
    
    public LocalDate getLastSundayOf( int year, int month )
    {
        LocalDate dm = new LocalDate( year, month, 31 );
       
        while( true )
        {
            if( dm.getDayOfWeek() == DateTimeConstants.SUNDAY )
                return dm;
            
            dm = dm.minusDays(1);
        }
    }
    
    public abstract Collection<HolidayInfo> getHolidays(int year);


     public HolidayInfo getHolidayForDay( Calendar date )
     {
        if( last_used_year != date.get(Calendar.YEAR) ||
                last_used_holidays == null )
        {
            last_used_year = date.get(Calendar.YEAR);
            last_used_holidays = getHolidays(last_used_year);
        }

        for( HolidayInfo hi : last_used_holidays )
        {
            if( hi.date.getYear()        == date.get(Calendar.YEAR) &&
                hi.date.getMonthOfYear() == date.get(Calendar.MONTH)+1 &&
                hi.date.getDayOfMonth()  == date.get(Calendar.DAY_OF_MONTH) )
            {
                return hi;
            }
        }

        return null;
     }

    public HolidayInfo getHolidayForDay( DateMidnight date )
    {
        return getHolidayForDay(date.toLocalDate());
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
                hi.date.getMonthOfYear() == date.getMonthOfYear() &&
                hi.date.getDayOfMonth()  == date.getDayOfMonth() )
            {
                return hi;
            }
        }

        return null;
    }
}
