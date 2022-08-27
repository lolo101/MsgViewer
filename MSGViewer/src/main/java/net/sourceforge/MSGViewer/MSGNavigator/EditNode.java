package net.sourceforge.MSGViewer.MSGNavigator;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.Entry;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class EditNode extends BaseDialog {

    private final TreeNodeContainer cont;
    private final MSGNavigator navwin;

    public EditNode(Root root, MSGNavigator nav, TreeNodeContainer cont) {
        super(root, root.MlM("Edit:") + " " + cont.getEntry().getName());

        this.cont = cont;
        this.navwin = nav;

        initComponents();

        if (cont.getEntry().isDocumentEntry()) {
            Object data = cont.getData();

            if (data instanceof String) {
                jtHex.setText((String) data);
            } else if (data instanceof byte[]) {
                StringBuilder sb = new StringBuilder();
                byte[] bytes = (byte[]) data;

                for (int i = 0; i < bytes.length; i++) {

                    if (i > 0 && i % 20 == 0) {
                        sb.append("\n");
                    }

                    sb.append(String.format("%02x", bytes[i]));

                    sb.append(" ");
                }

                jtHex.setText(sb.toString());
            }
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jtHex = new javax.swing.JTextArea();
        javax.swing.JButton jSaveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 300));

        jtHex.setColumns(20);
        jtHex.setFont(new java.awt.Font("Courier New", Font.PLAIN, 12)); // NOI18N
        jtHex.setLineWrap(true);
        jtHex.setRows(5);
        jScrollPane1.setViewportView(jtHex);

        jSaveButton.setText("Save");
        jSaveButton.addActionListener(this::jSaveButtonActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jSaveButton)
                                .addContainerGap(430, Short.MAX_VALUE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSaveButton)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveButtonActionPerformed

        new AutoMBox<>(this.getClass().getName(), () -> {

            if (cont.getEntry().getName().matches("__substg1\\.0_[0-9][0-9][0-9][0-9]001[fF]")) {
                String data = jtHex.getText().trim();

                Entry entry = cont.getEntry();

                String name = entry.getName();

                DirectoryEntry parent = entry.getParent();
                entry.delete();

                DocumentEntry new_entry;
                try (ByteArrayInputStream buf = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_16LE))) {
                    new_entry = parent.createDocument(name, buf);
                }

                TopLevelPropertyStream stream = new TopLevelPropertyStream(parent);
                stream.update(new_entry);


                navwin.edited();
            } else {
                String data = jtHex.getText();

                data = data.replaceAll("[ \t\n]", "");
                logger.info("Data: " + data);

                byte[] bytes = new byte[data.length() / 2];
                int bcount = 0;

                for (int i = 0; i < data.length(); i += 2, bcount++) {
                    char[] charArray = {data.charAt(i), data.charAt(i + 1)};
                    bytes[bcount] = (Integer.valueOf(new String(charArray), 16)).byteValue();
                }


                Entry entry = cont.getEntry();

                String name = entry.getName();

                DirectoryEntry parent = entry.getParent();
                entry.delete();

                try (ByteArrayInputStream buf = new ByteArrayInputStream(bytes)) {
                    parent.createDocument(name, buf);
                }
            }
            close();

        }).run();


    }//GEN-LAST:event_jSaveButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea jtHex;
    // End of variables declaration//GEN-END:variables
}
