/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets.calendarday;

/**
 *
 * @author martin
 */
public interface DisplayDay {

    void setInfo(String info);
    void setSum(String sum);
    void setDay(String day);
    void setListener(DayEventListener ev);
    void clear();
    void setActive();
    void setInactive();
    void setSelected();
    void setUnSelected();
    boolean isActive();
    boolean isSelected();
    void setNormalBackground();
    void setSaturdayBackground();
    void setSundayBackground();
    void setInfoRenderer(InfoRenderer renderer);
    void update();
    InfoRenderer getInfoRenderer();
    void setToday();
    /*
     * param day: 1 - Monday; 7 - Sunday
     */
    void    setWeekDay(int day);
    int     getWeekDay();
    boolean isSunday();
    boolean isSaturday();
    void    setHoliday(boolean is_holiday);
    boolean isHoliday();
}
