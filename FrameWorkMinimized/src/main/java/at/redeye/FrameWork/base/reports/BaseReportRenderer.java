/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.reports;

import org.apache.log4j.Logger;

/**
 *
 * @author martin
 */
public class BaseReportRenderer 
{
    public static Logger logger = Logger.getLogger(BaseReportRenderer.class.getName());

    protected ReportDesign design = new DefaultReportDesign();
    protected StringBuilder text = new StringBuilder();
    
    protected void clear()
    {
        text.setLength(0);        
    }
    
    protected void html_start()
    {
        text.append(design.startReport());
    }
    
    protected void html_stop()
    {
        text.append(design.endReport());
    }
    
    protected void html_setTitle( String title )
    {
        text.append(design.setTitle(title));
    }
    
    protected void html_bold( String t )
    {
        text.append(design.bold(t));
    }
    
    protected void html_bold_title( String t )
    {
        text.append(design.bold_title(t));
    }
    
    protected void html_blockquote( String t )
    {
        text.append(design.blockquote(t));
    }
    
    protected void html_blockquote_start()
    {
        text.append(design.blockquote_start());
    }
    
    protected void html_blockquote_end()
    {
        text.append(design.blockquote_end());
    }
    
    protected void html_newline()
    {
        text.append(design.newline());
    }
    
    protected void html_normal_text( String t )
    {
        text.append(design.normal_text(t));
    }
}
