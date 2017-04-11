package at.redeye.FrameWork.utilities;

import java.util.Date;
import org.joda.time.DateTime;

/**
 *
 * @author martin  
 */
public class HMSTime {

    long millis;

    public HMSTime()
    {
        millis = 0;
    }

    public HMSTime(long millis) 
    {
        this.millis = millis;
    }
    
    public HMSTime( DateTime jdt )
    {
        millis = jdt.getMillisOfDay();
    }
    
    public HMSTime( Date date )
    {
        millis = new DateTime( date ).getMillisOfDay();
    }
        
    public String toString( String format )
    {                        
        String fields[] = format.split(":");

        long m = Math.abs(millis);

        StringBuilder res = new StringBuilder();        

        for( int i = 0; i < fields.length; i++ )
        {
            if( res.length() > 0 )
                res.append(":");
            
            if( fields[i].matches("H+"))
            {
                long hours   = getHours(m);
        
                if( hours < 10 )
                    res.append("0");
                
                res.append(String.valueOf(hours));
            }
            else if( fields[i].matches("m+") )
            {
                long minutes = getMinutesOfHour(m);

                if( minutes < 10 )
                    res.append("0");
                
                res.append(String.valueOf(minutes));
            }
            else if( fields[i].matches("s+"))
            {
                long seconds = getSecondsOfHour(m);
                
                if( seconds < 10 )
                    res.append("0");                
                
                res.append(String.valueOf(seconds));
            }
        }

        if( millis < 0 )
            return "-" + res.toString();

        return res.toString();
    }
    
    @Override
    public String toString()
    {
        return toString("HH:mm:ss");
    }

    public long getHours()
    {
        return getHours(millis);
    }

    public static long getHours(long millis)
    {
        return millis / 1000 / 60 / 60;
    }

    public long getMinutesOfHour()
    {
        return getMinutesOfHour(millis);
    }

    public static long getMinutesOfHour(long millis)
    {
        long rest  = ( millis / 1000 ) - getHours(millis) * 60 * 60;
        
        return rest / 60;
    }

    public long getSecondsOfHour()
    {
        return getSecondsOfHour(millis);
    }

    public static long getSecondsOfHour(long millis)
    {
        long rest = ( millis / 1000 ) - getHours(millis) * 60 * 60 - getMinutesOfHour(millis) * 60;
        
        return rest;
    }
    
    public void addMillis( long millis )
    {
        this.millis += millis;
    }
    
    public void addSeconds( long seconds )
    {
        millis += seconds * 1000;
    }
    
    public void addMinutes( long minutes )
    {
        millis += minutes * 60 * 1000;
    }
    
    public void addHours( long hours )
    {
        millis += hours * 60 * 60 * 1000;
    }

    public void setTime( long millis )
    {
        this.millis = millis;
    }

    public long getMillis()
    {
        return millis;
    }

    public void minusMillis( long millis )
    {
        this.millis -= millis;
    }

    @Override
    public boolean equals( Object other )
    {
        if (this == other)
            return true;

        if (other == null)
            return false;

        if (other.getClass() != getClass())
            return false;

        HMSTime other_time = (HMSTime) other;

        return millis == other_time.millis;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (int) (this.millis ^ (this.millis >>> 32));
        return hash;
    }
}
