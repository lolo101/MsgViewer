/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import javax.swing.SwingUtilities;

/**
 * 
 * @author Mario Mattl
 */
public class DefaultAutoRefresh {

	public static void DefaultAutoRefreshTable(final AutoRefreshInterface afif) {
		
		
		// This normally causes a re-read of table data, so we have to put
		// the operation onto the EventQueue, because we don't want to disturb redraw events! 
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				afif.feed_table(true);

			}

		});

	}

}
