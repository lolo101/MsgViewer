/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets.calendar;

import at.redeye.FrameWork.utilities.calendar.Holidays;
import at.redeye.FrameWork.widgets.calendarday.DayEventListener;
import at.redeye.FrameWork.widgets.calendarday.DisplayDay;
import at.redeye.FrameWork.widgets.calendarday.InfoRenderer;


/**
 *
 * @author martin
 */
public interface DisplayMonth {

    public void setMonth( int Month, int year );
    
    public int getDaysOfMonth();
    
    /*
     * @param day : value 1 - 31 if the day does not exists null is returned
     */
    public DisplayDay getDay( int day );    
    
    public void setListener( DayEventListener listener );
    
    public int isWhatDayOfMonth( DisplayDay day );
    
    public void setHolidays( Holidays holidays );

    public Holidays getHolidays();
    
    public void setInfoRenderer( InfoRenderer renderer );
    
    public int getMonth();
    
    public int getYear();
}
