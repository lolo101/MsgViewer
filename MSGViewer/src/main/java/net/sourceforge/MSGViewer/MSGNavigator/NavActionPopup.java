package net.sourceforge.MSGViewer.MSGNavigator;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.utilities.StringUtils;

import javax.swing.*;

public class NavActionPopup extends JPopupMenu {

    public NavActionPopup(final MSGNavigator mainwin) {
        {
            JMenu menu_settings = new JMenu(mainwin.MlM("Options"));
            add(menu_settings);

            // SETTINGS
            {
                final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(mainwin.MlM("View size"));
                menuItem.setState(StringUtils.isYes(mainwin.getRoot().getSetup().getLocalConfig(MSGNavigator.SETTING_SHOW_SIZE, "true")));
                menuItem.addActionListener(e -> {
                    mainwin.getRoot().getSetup().setLocalConfig(MSGNavigator.SETTING_SHOW_SIZE, String.valueOf(menuItem.getState()));
                    mainwin.reload();
                });
                menu_settings.add(menuItem);
            }


            // AUTOSAVE
            {
                final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(mainwin.MlM("Autosave"));
                menuItem.setState(StringUtils.isYes(mainwin.getRoot().getSetup().getLocalConfig(MSGNavigator.SETTING_AUTOSAVE, "false")));
                menuItem.addActionListener(e -> mainwin.getRoot().getSetup().setLocalConfig(MSGNavigator.SETTING_AUTOSAVE, String.valueOf(menuItem.getState())));
                menu_settings.add(menuItem);
            }
        }

        {
            JMenuItem menuItem = new JMenuItem(mainwin.MlM("Save"));
            menuItem.addActionListener(e -> new AutoMBox<>(NavActionPopup.class.getName(), mainwin::save).run());
            add(menuItem);
        }

        {
            JMenuItem menuItem = new JMenuItem(mainwin.MlM("Delete element"));
            menuItem.addActionListener(e -> new AutoMBox<>(NavActionPopup.class.getName(), mainwin::deleteSelectedElement).run());
            add(menuItem);
        }

        {
            JMenuItem menuItem = new JMenuItem(mainwin.MlM("Inspect element"));
            menuItem.addActionListener(e -> new AutoMBox<>(NavActionPopup.class.getName(), mainwin::showSelectedElement).run());
            add(menuItem);
        }

        {
            JMenuItem menuItem = new JMenuItem(mainwin.MlM("Edit element"));
            menuItem.addActionListener(e -> new AutoMBox<>(NavActionPopup.class.getName(), mainwin::editSelectedElement).run());
            add(menuItem);
        }
    }
}
