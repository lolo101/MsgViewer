/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.Plugin;

/**
 *
 * @author martin
 */
public interface Plugin {

    String getName();
    String getLicenceText();
    String getChangeLog();
    String getVersion();

    void initPlugin( Object obj );

    /**     
     * @return true if the external jar file can be loaded
     */
    boolean isAvailable();

    @Override
    String toString();
}
