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

    void setMonth(int Month, int year);

    int getDaysOfMonth();

    /*
     * @param day : value 1 - 31 if the day does not exists null is returned
     */
    DisplayDay getDay(int day);

    void setListener(DayEventListener listener);

    int isWhatDayOfMonth(DisplayDay day);

    void setHolidays(Holidays holidays);

    Holidays getHolidays();

    void setInfoRenderer(InfoRenderer renderer);

    int getMonth();

    int getYear();
}
