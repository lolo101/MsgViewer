/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.AutoMBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author moberza
 */
public class BodyActionPopup extends JPopupMenu
{
    private MainWin mainwin;

    static final String ENCODING_REPLACEMENT="<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";

    public BodyActionPopup(final MainWin mainwin)
    {
        this.mainwin = mainwin;

        JMenuItem menuItem = new JMenuItem(mainwin.MlM("Open by your webbrowser"));
        
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                new AutoMBox(BodyActionPopup.class.getName()) {

                    @Override
                    public void do_stuff() throws Exception {

                        String html = mainwin.getHTMLCode();

                        html = html.replaceFirst("<[hH][eE][aA][dD]>",ENCODING_REPLACEMENT);

                        File file = File.createTempFile("message", ".html", mainwin.getMailDirectory());

                        FileOutputStream fout = new FileOutputStream(file);

                        fout.write(html.getBytes("UTF-8"));

                        fout.close();

                        mainwin.openUrl(new URL("file:" + file.getPath()));
                    }
                };
            }
        });

        add(menuItem);
        
        JMenuItem emlItem = new JMenuItem(mainwin.MlM("Convert to EML and open it with your default mail client."));
        
        emlItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                new AutoMBox(BodyActionPopup.class.getName()) {

                    @Override
                    public void do_stuff() throws Exception {

                        String file = mainwin.getFileName();
                        
                        int idx = file.lastIndexOf(".");
                        
                        if( idx >= 0 ) {
                            file = file.substring(0,idx);
                        }

                        file += ".eml";
                        File tmpfile = new File(mainwin.getMailDirectory() + "/" + new File(file).getName());
                        
                        mainwin.exportFile(tmpfile);
                        
                        mainwin.openUrl(new URL("file:" + tmpfile.getPath()));
                    }
                };
            }
        });        
        
         add(emlItem);
    }
}
