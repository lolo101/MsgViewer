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

    public void setInfo( String info );
    public void setSum( String sum );
    public void setDay( String day );
    public void setListener( DayEventListener ev );
    public void clear();
    public void setActive();
    public void setInactive();
    public void setSelected();
    public void setUnSelected();
    public boolean isActive();
    public boolean isSelected();
    public void setNormalBackground();
    public void setSaturdayBackground();
    public void setSundayBackground();
    public void setInfoRenderer(InfoRenderer renderer);
    public void update();
    public InfoRenderer getInfoRenderer();
    public void setToday();
    /*
     * param day: 1 - Monday; 7 - Sunday
     */ 
    public void    setWeekDay( int day );
    public int     getWeekDay();
    public boolean isSunday();
    public boolean isSaturday();    
    public void    setHoliday( boolean is_holiday );
    public boolean isHoliday();
}
