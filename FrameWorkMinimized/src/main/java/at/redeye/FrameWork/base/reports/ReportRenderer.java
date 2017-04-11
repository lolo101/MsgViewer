/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.reports;

/**
 *
 * @author martin
 */
public interface ReportRenderer {
    
    public boolean collectData();
    public String render();
}
