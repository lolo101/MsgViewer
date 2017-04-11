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
    public void clear();

    public InfoRenderer getNewInstance();    
    public void setInfo( String info );
    public void update();
    public String render();
    public void addContent( Object data );    
    public void setDay( DateMidnight day );
    public String renderSum();
    public DateMidnight getDay();
}
