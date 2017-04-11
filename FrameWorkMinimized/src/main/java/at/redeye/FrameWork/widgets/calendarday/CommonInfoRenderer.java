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
public class CommonInfoRenderer implements InfoRenderer 
{
    protected StringBuilder info = new StringBuilder();
    
    @Override
    public void clear() {
         info.delete(0, info.length());
    }

    @Override
    public void setInfo(String info) {
        this.info.append(info);
    }

    @Override
    public void update() {
        // nix zu tun
    }

    @Override
    public String render() {
        return "<html><body><font size=\"2\">" + info.toString() + "</font></body></html>";
    }

    @Override
    public void addContent(Object data) {
        info.append(data);
    }

    @Override
    public InfoRenderer getNewInstance() {
        return new CommonInfoRenderer();
    }

    @Override
    public void setDay(DateMidnight day) {
        // brauch ma net
    }

    @Override
    public String renderSum()
    {
        return "";
    }

    @Override
    public DateMidnight getDay() {
        return null;
    }
}
