/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

/**
 *
 * @author martin
 */
public class StartupWindow
{
    protected Popup popup = null;

    public StartupWindow(String icon_path)
    {
        URL url = getClass().getResource(icon_path);

        if (url == null) {
            System.out.println("Connot load Image " + icon_path);
            return;
        }

        ImageIcon icon = new javax.swing.ImageIcon(url);

        JLabel label = new JLabel();
        label.setIcon(icon);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label);

        PopupFactory factory = PopupFactory.getSharedInstance();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        // Get size of each screen
        int screenWidth = 100;
        int screenHeight = 100;

        for (int i = 0; i < gs.length; i++) {
            DisplayMode dm = gs[i].getDisplayMode();
            screenWidth = dm.getWidth();
            screenHeight = dm.getHeight();
            break;
        }

        popup = factory.getPopup(null, panel, screenWidth / 2 - icon.getIconWidth() / 2, screenHeight / 2 - icon.getIconHeight() / 2);
        popup.show();

    }

    public void close()
    {
        if (popup != null) {
            popup.hide();
        }
    }
}
