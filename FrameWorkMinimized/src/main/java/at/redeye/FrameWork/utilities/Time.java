/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities;

import java.util.Date;
import org.joda.time.DateTime;

/**
 *
 * @author martin
 */
public class Time
{
    /*
     * minimum time in Milliseconds
     */
    public static long MIN_VALUE = 2000*60*60;

    public static boolean isMinimumTime( long millis )
    {
        return millis < MIN_VALUE;
    }

    public static boolean isMinimumTime( Date date )
    {
        return isMinimumTime( date.getTime() );
    }

    public static boolean isMinimumTime( DateTime date )
    {
        return isMinimumTime( date.getMillis() );
    }
}
