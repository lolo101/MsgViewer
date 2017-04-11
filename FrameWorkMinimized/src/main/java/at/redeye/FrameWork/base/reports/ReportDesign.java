/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.reports;

/**
 *
 * @author martin
 */
public interface ReportDesign
{
    public String startReport();
    public String endReport();
    
    public String setTitle( String title );
    
    public String bold(String text);
    public String bold_title(String text);
    public String blockquote_start();
    public String blockquote_end();
    public String blockquote( String t );
    public String newline();
    public String normal_text(String t);
}
