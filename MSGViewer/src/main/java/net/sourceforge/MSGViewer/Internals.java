/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Internals.java
 *
 * Created on 02.12.2010, 22:13:17
 */

package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import net.sourceforge.MSGViewer.rtfparser.ParseException;
import com.auxilii.msgparser.Message;

/**
 *
 * @author martin
 */
public class Internals extends BaseDialog {

    private String dialog_id;
    private Message message;

    private String MESSAGE_NOHTML_CODE;
    private String MESSAGE_UNPARSABLE_CODE;

    /** Creates new form Internals */
    public Internals(Root root, final Message message)
    {
        super(root, root.MlM("Detail info") + ": " + (message.getSubject() != null ? message.getSubject() : root.getAppTitle()) );

        initComponents();

        MESSAGE_NOHTML_CODE = MlM("This message does not contain HTML formatted code.");
        MESSAGE_UNPARSABLE_CODE = MlM("Cannot extract HTML formatted code from RTF coded message.");

        jTHeader.setText(message.getHeaders());
        jTHeader.setCaretPosition(0);

        jTPlain.setText(message.getBodyText());
        jTPlain.setCaretPosition(0);

        jTRTF.setText(message.getBodyRTF());
        jTRTF.setCaretPosition(0);

        if ( message.getBodyRTF() != null && message.getBodyRTF().contains("\\fromhtml")) {
            AutoLogger al = new AutoLogger(Internals.class.getName()) {

                @Override
                public void do_stuff() throws Exception {
                    jTHTML.setText(extractHTMLFromRTF(message.getBodyRTF()));
                }
            };

            if (al.isFailed()) {
                jTHTML.setText(MESSAGE_UNPARSABLE_CODE);


            }
        } else if ( message.getBodyRTF() != null && message.getBodyRTF().contains("\\purehtml")) {
             jTHTML.setText(message.getBodyRTF());
        } else {
            jTHTML.setText(MESSAGE_NOHTML_CODE);
        }

        jTHTML.setCaretPosition(0);
    }


    @Override
    public String getUniqueDialogIdentifier(Object requester)
    {
        /*
         * dadurch können wir später den Titel ändern, ohne das sich dadurch
         * die Dialog ID verändert.
         */
        if( dialog_id == null )
         dialog_id = super.getUniqueDialogIdentifier(requester);

        return dialog_id;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTHeader = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTPlain = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTRTF = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTHTML = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTHeader.setColumns(20);
        jTHeader.setRows(5);
        jScrollPane1.setViewportView(jTHeader);

        jTabbedPane1.addTab("Mail Header", jScrollPane1);

        jTPlain.setColumns(20);
        jTPlain.setRows(5);
        jScrollPane2.setViewportView(jTPlain);

        jTabbedPane1.addTab("Message Text", jScrollPane2);

        jTRTF.setColumns(20);
        jTRTF.setRows(5);
        jScrollPane3.setViewportView(jTRTF);

        jTabbedPane1.addTab("RTF Code", jScrollPane3);

        jTHTML.setColumns(20);
        jTHTML.setRows(5);
        jScrollPane4.setViewportView(jTHTML);

        jTabbedPane1.addTab("HTML Code", jScrollPane4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTHTML;
    private javax.swing.JTextArea jTHeader;
    private javax.swing.JTextArea jTPlain;
    private javax.swing.JTextArea jTRTF;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

    private String extractHTMLFromRTF(String bodyText) throws ParseException {

        HtmlFromRtf rtf2html = new HtmlFromRtf(bodyText);

        return rtf2html.getHTML();
    }

}
