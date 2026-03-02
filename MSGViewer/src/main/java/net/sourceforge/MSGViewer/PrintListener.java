package net.sourceforge.MSGViewer;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;

public class PrintListener implements PropertyChangeListener {

    private Color background;
    private Color foreground;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        boolean printing = (boolean) evt.getNewValue();
        JComponent component = (JComponent) evt.getSource();
        if (printing) {
            background = component.getBackground();
            foreground = component.getForeground();
            component.setBackground(Color.WHITE);
            component.setForeground(Color.BLACK);
        } else {
            component.setBackground(background);
            component.setForeground(foreground);
        }
    }
}
