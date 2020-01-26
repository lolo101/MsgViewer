package net.sourceforge.MSGViewer.factory.msg.lib;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;

/**
 *
 * @author martin
 * Cervrting function for converting dates and times to javaformat and back
 */
public class MSTimeConvert {

    /**
     * convert a 64 bit Integer value to a normal time field.
     * MS hat here nanoseconds since 1601
     * @return returns the time in millis since 1970 sam as Syste.currentTime() does
     *         this is the value in UTC timezone!
     */
    public static long PtypeTime2Millis( long time )
    {
        time /= 10L; // micro
        time /= 1000L; // milli

        time -= 11644473600000L; // offset since 1601

        return time;
    }


    /**
     * convert a 64 bit Integer value to a normal time field.
     * MS hat here nanoseconds since 1601
     * @return returns the time in nabos since 1.1.1601
     */
    public static long Millis2PtypeTime( long time )
    {
        time += 11644473600000L; // offset since 1601

        time *= 10L; // micro
        time *= 1000L; // milli

        return time;
    }

    public static void main(String[] args)
    {
       //  System.out.println("x: " + PtypeTime2Millis(1));

       DateTime dt = new DateTime(1601,1,1,0,0,0,0,DateTimeZone.UTC);

       long millis = 0;
       boolean first = true;

       while( dt.getYear() < 1970 )
       {
           if( !first )
            millis += dt.minusMillis(1).getMillisOfDay() + 1;

           first = false;

           dt = dt.plusDays(1);
       }

       millis += 86400000;

       System.out.println(millis);



        //long val = 129356287862480000L;
        long val = 129356138460000000L;

        long mil = PtypeTime2Millis(val);

        Date date = new Date(mil);

        System.out.println("date: " + date.toString() + " millis " + date.getTime());

        Date date_known = new DateTime(2010,11,30, 19,04,06,00,DateTimeZone.getDefault()).toDate();

        System.out.println("date: " + date_known.toString() + " millis " + date_known.getTime());

        System.out.println("diff: " + (date_known.getTime() - date.getTime()));
    }
}
