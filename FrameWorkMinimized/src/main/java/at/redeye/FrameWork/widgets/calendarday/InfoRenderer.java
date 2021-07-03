/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets.calendarday;

import java.time.LocalDate;

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
    void setDay(LocalDate day);
    String renderSum();
    LocalDate getDay();
}
