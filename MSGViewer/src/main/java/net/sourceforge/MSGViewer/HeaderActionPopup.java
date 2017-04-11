/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.utilities.ReadFile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author moberza
 */
public class HeaderActionPopup extends JPopupMenu
{
    private MainWin mainwin;

    public HeaderActionPopup(final MainWin mainwin,final URL url)
    {
        this.mainwin = mainwin;

        JMenuItem menuItem = new JMenuItem(mainwin.MlM("File open"));
        
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                new AutoMBox(HeaderActionPopup.class.getName()) {

                    @Override
                    public void do_stuff() throws Exception {

                        mainwin.openUrl(url);
                    }
                };
            }
        });

        add(menuItem);

        MainWin.logger.info("Protocol:" + url.getProtocol());

        if (url.getProtocol().equals("file")) {
            menuItem = new JMenuItem(mainwin.MlM("File save as"));

            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    new AutoMBox(HeaderActionPopup.class.getName()) {

                        @Override
                        public void do_stuff() throws Exception {

                            File file = mainwin.extractUrl(url);

                            if (file == null) {
                                return;
                            }

                            File source_file = mainwin.extractUrl(url);

                            if (source_file == null) {
                                return;
                            }

                            JFileChooser fc = new JFileChooser();

                            fc.setAcceptAllFileFilterUsed(true);
                            fc.setMultiSelectionEnabled(false);
                            fc.setSelectedFile(new File(source_file.getName()));

                            String last_path = mainwin.getLastOpenPath();

                            logger.info("last path: " + last_path);

                            if (last_path != null) {
                                fc.setCurrentDirectory(new File(last_path));
                            }

                            int retval = fc.showSaveDialog(mainwin);

                            if (retval != 0) {
                                return;
                            }

                            File target_file = fc.getSelectedFile();

                            byte bytes[] = ReadFile.getBytesFromFile(source_file);

                            FileOutputStream fout = new FileOutputStream(target_file);

                            fout.write(bytes);

                            fout.close();

                            mainwin.setLastOpenPath(target_file.getParentFile().getPath());
                        }
                    };
                }
            });

            add(menuItem);
        }

    }
}
