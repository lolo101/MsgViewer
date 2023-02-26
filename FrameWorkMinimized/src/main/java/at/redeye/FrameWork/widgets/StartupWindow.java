package at.redeye.FrameWork.widgets;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class StartupWindow
{
    private Popup popup;

    public StartupWindow(String icon_path)
    {
        URL url = getClass().getResource(icon_path);

        if (url == null) {
            System.out.println("Connot load Image " + icon_path);
            return;
        }

        Icon icon = new javax.swing.ImageIcon(url);

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

        for (GraphicsDevice g : gs) {
            DisplayMode dm = g.getDisplayMode();
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
