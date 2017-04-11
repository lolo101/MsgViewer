/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.calendar;

import java.util.Collection;
import java.util.Vector;

import at.redeye.FrameWork.utilities.calendar.Holidays.HolidayInfo;
import java.util.Calendar;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;

/**
 *
 * @author martin
 */
public class HolidayMerger implements Holidays {

    Vector<Holidays> country_holidays = new Vector<Holidays>();
    int numCountryCodes = 0;
    String primaryCountry = "";
    Holidays primHolidays = null;
    Holidays extraHolidays = null;

    public void add( Holidays holiday )
    {
        remove( holiday );
        country_holidays.add(holiday);
        numCountryCodes++;
    }

    public void add( Holidays holiday, boolean is_extra )
    {
        remove( holiday );
        country_holidays.add(holiday);
        numCountryCodes++;
        
        if( is_extra )
            extraHolidays = holiday;
    }

    public void remove( String CountryCode )
    {
        for( int i = 0; i < country_holidays.size(); i++ )
        {
            if( country_holidays.get(i).getPrimaryCountryCode().equals(CountryCode) )
            {
                country_holidays.remove(i);
                numCountryCodes--;
                break;
            }
        }
    }
    
    public void remove( Holidays holiday )
    {
        for( int i = 0; i < country_holidays.size(); i++ )
        {
            if( country_holidays.get(i).getClass().isInstance(holiday))
            {
                country_holidays.remove(i);
                numCountryCodes--;
                return;
            }
        }
    }
    
    @Override
    public Collection<HolidayInfo> getHolidays(int year) {
        
        Collection<HolidayInfo> all = null;
                        
        if( primHolidays != null ) {
            Collection<HolidayInfo> clone = primHolidays.getHolidays(year);
            
            all = new Vector<HolidayInfo>();
            
            for( HolidayInfo hi : clone )
                all.add( new HolidayInfo(hi) );

            if( extraHolidays != null )
            {
                clone = extraHolidays.getHolidays(year);

                for( HolidayInfo hi : clone )
                    all.add( new HolidayInfo(hi) );
            }
        }
        
        for( Holidays holidays : country_holidays )
        {
            Collection<HolidayInfo> local = holidays.getHolidays(year);
                                    
            if( all == null )
            {
                all = new Vector<HolidayInfo>();
                
                for( HolidayInfo hi : local ) {
                    all.add( new HolidayInfo( hi ) );
                }
                
                continue;
            }
            
            for( HolidayInfo hi_lo : local )
            {
                boolean found = false;                
                
                for( HolidayInfo hi_all : all )
                {
                    if( hi_all.date.equals(hi_lo.date) )
                    {
                        found = true;
                        
                        if( hi_all.CountryCode.contains(hi_lo.CountryCode) )
                            continue;
                        
                        hi_all.CountryCode += "," + hi_lo.CountryCode;                        
                        break;
                    }
                }
                
                if( !found )
                {
                    // System.out.println( "added: " + hi_lo.CountryCode + " " + hi_lo.name );
                    all.add( new HolidayInfo( hi_lo ) );
                }
            }
        }
        
        if( all == null )
            all = new Vector<HolidayInfo>();
        
        for( HolidayInfo hi : all )
        {
            if( hi.merge_to_primary == false )
            {
                if( hi.CountryCode.contains(primaryCountry) == false )
                {
                    hi.official_holiday = false;
                }                                    
            }
        }
        
        return all;
    }

    @Override
    public int getNumberOfCountryCodes() {
        return numCountryCodes;
    }

    public void setPrimaryCalendar( Holidays holidays )
    {        
        primaryCountry = holidays.getPrimaryCountryCode();
        primHolidays  = holidays;
        add(holidays);
    }
        
    @Override
    public String getPrimaryCountryCode() {
        return primaryCountry;
    }

    @Override
    public HolidayInfo getHolidayForDay(DateMidnight date) {

        return getHolidayForDay(date.toLocalDate());
    }

    @Override
    public HolidayInfo getHolidayForDay(LocalDate date) {

        if( primHolidays != null ) {
            HolidayInfo hi = primHolidays.getHolidayForDay(date);

            if( hi == null && extraHolidays != null )
            {
                return extraHolidays.getHolidayForDay(date);
            }

            return hi;
        }

        return null;
    }

    @Override
    public HolidayInfo getHolidayForDay(Calendar date) {
        return getHolidayForDay(new DateMidnight(date));
    }
}
