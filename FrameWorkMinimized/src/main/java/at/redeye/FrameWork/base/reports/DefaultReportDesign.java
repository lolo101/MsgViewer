/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.reports;

import at.redeye.FrameWork.base.reports.ReportDesign;

/**
 *
 * @author martin
 */
public class DefaultReportDesign implements ReportDesign 
{

    public String startReport() {
        return "<html><body><font face=\"Verdana\">";
    }

    public String endReport() {
        return "</font></body></html>";
    }

    public String setTitle(String title) {
        return "<center><h1>" + title + "</h1></center>";
    }
    
    public String bold( String text )
    {
        return "<b>" + text + "</b>";
    }

    public String bold_title( String text )
    {
        return "<h3><u>" + text + "</u></h3>";
    }

    
    public String blockquote( String text )
    {
        return "<blockquote>" + text + "</blockquote>";
    }
    
    public String blockquote_start()
    {
        return "<blockquote>";
    }
    
    public String newline()
    {
        return "<br/>";
    }
    
    public String normal_text(String t)
    {
        return t;
    }
    
    public String blockquote_end()
    {
        return "</blockquote>";
    }
}
