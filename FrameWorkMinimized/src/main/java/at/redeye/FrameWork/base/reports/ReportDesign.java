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
    String startReport();
    String endReport();

    String setTitle(String title);

    String bold(String text);
    String bold_title(String text);
    String blockquote_start();
    String blockquote_end();
    String blockquote(String t);
    String newline();
    String normal_text(String t);
}
