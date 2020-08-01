/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets.calendarday;

import org.joda.time.DateMidnight;

/**
 *
 * @author martin
 */
public interface InfoRenderer
{
    void clear();

    InfoRenderer getNewInstance();
    void setInfo(String info);
    void update();
    String render();
    void addContent(Object data);
    void setDay(DateMidnight day);
    String renderSum();
    DateMidnight getDay();
}
