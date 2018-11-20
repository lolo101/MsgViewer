package net.sourceforge.MSGViewer.MSGNavigator;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import net.sourceforge.MSGViewer.MSGNavigator.MSGNavigator.TreeNodeContainer;
import net.sourceforge.MSGViewer.factory.msg.TopLevelPropertyStream;
import java.io.ByteArrayInputStream;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.Entry;

/**
 *
 * @author martin
 */
public class EditNode extends BaseDialog{

    private final TreeNodeContainer cont;
    private final MSGNavigator navwin;

    public EditNode(Root root, MSGNavigator nav, TreeNodeContainer cont) {
        super( root, root.MlM("Edit:") + " " + cont.getEntry().getName());

        this.cont = cont;
        this.navwin = nav;

        initComponents();

        if( cont.getEntry().isDocumentEntry() )
        {
            Object data = cont.getData();

            if( data instanceof String )
            {
                jtHex.setText((String)data);
            }
            else if( data instanceof byte[] )
            {
                StringBuilder sb = new StringBuilder();
                byte[] bytes = (byte[]) data;

                for( int i = 0; i < bytes.length; i++ ) {

                    if( i > 0 && i % 20 == 0 ) {
                        sb.append("\n");
                    }

                    sb.append(String.format("%02x", bytes[i] ));

                    sb.append(" ");
                }

                jtHex.setText(sb.toString());
            }
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtHex = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 300));

        jtHex.setColumns(20);
        jtHex.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jtHex.setLineWrap(true);
        jtHex.setRows(5);
        jScrollPane1.setViewportView(jtHex);

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(430, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed


        new AutoMBox(this.getClass().getName()) {

            @Override
            public void do_stuff() throws Exception {

                if( cont.getEntry().getName().matches("__substg1\\.0_[0-9][0-9][0-9][0-9]001[fF]") )
                {
                    String data = jtHex.getText().trim();

                    Entry entry = cont.getEntry();

                    String name = entry.getName();

                    DirectoryEntry parent = entry.getParent();
                    entry.delete();

                    ByteArrayInputStream buf = new ByteArrayInputStream(data.getBytes("UTF-16LE"));

                    DocumentEntry new_entry = parent.createDocument(name, buf);

                    TopLevelPropertyStream stream = new TopLevelPropertyStream(parent);
                    stream.update(new_entry);


                    navwin.edited();


                    close();
                }
                else
                {
                    String data = jtHex.getText();

                    data = data.replaceAll("[ \t\n]", "");
                    logger.info("Data: " + data);

                    byte bytes[] = new byte[data.length() / 2];
                    int bcount = 0;

                    for (int i = 0; i < data.length(); i += 2, bcount++) {
                        char charArray[] = {data.charAt(i), data.charAt(i + 1)};
                        bytes[bcount] = (Integer.valueOf(new String(charArray), 16)).byteValue();
                    }


                    Entry entry = cont.getEntry();

                    String name = entry.getName();

                    DirectoryEntry parent = entry.getParent();
                    entry.delete();

                    parent.createDocument(name, new ByteArrayInputStream(bytes));

                    close();
                }

            }
        };


    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jtHex;
    // End of variables declaration//GEN-END:variables
}
