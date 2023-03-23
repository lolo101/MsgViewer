package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.Message;
import net.htmlparser.jericho.Source;
import net.sourceforge.MSGViewer.rtfparser.ParseException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class Internals extends BaseDialog {

    private String dialog_id;

    private static final String MESSAGE_NOHTML_CODE = "This message does not contain HTML formatted code.";
    private static final String MESSAGE_UNPARSABLE_CODE = "Cannot extract HTML formatted code from RTF coded message.";

    /**
     * Creates new form Internals
     */
    public Internals(Root root, final Message message) {
        super(root, root.MlM("Detail info") + ": " + (message.getSubject() != null ? message.getSubject() : root.getAppTitle()));

        initComponents();

        jTHeader.setText(message.getHeaders());
        jTHeader.setCaretPosition(0);

        jTPlain.setText(message.getBodyText());
        jTPlain.setCaretPosition(0);

        jTRTF.setText(message.getBodyRTF());
        jTRTF.setCaretPosition(0);

        jTHTML.setText(getText(message));
        jTHTML.setCaretPosition(0);
    }

    private String getText(Message message) {
        if (message.getBodyRTF() != null && message.getBodyRTF().contains("\\fromhtml")) {
            return new AutoLogger<>(Internals.class.getName(),
                    () -> extractHTMLFromRTF(message.getBodyRTF()).toString()
            ).resultOrElse(MlM(MESSAGE_UNPARSABLE_CODE));
        }
        if (message.getBodyHtml() != null) {
            try (InputStream stream = new ByteArrayInputStream(message.getBodyHtml())) {
                return new Source(stream).toString();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return MlM(MESSAGE_NOHTML_CODE);
    }

    @Override
    public String getUniqueDialogIdentifier() {
        /*
         * dadurch können wir später den Titel ändern, ohne das sich dadurch
         * die Dialog ID verändert.
         */
        if (dialog_id == null)
            dialog_id = super.getUniqueDialogIdentifier();

        return dialog_id;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JTabbedPane jTabbedPane1 = new javax.swing.JTabbedPane();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTHeader = new javax.swing.JTextArea();
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        jTPlain = new javax.swing.JTextArea();
        javax.swing.JScrollPane jScrollPane3 = new javax.swing.JScrollPane();
        jTRTF = new javax.swing.JTextArea();
        javax.swing.JScrollPane jScrollPane4 = new javax.swing.JScrollPane();
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
    private javax.swing.JTextArea jTHTML;
    private javax.swing.JTextArea jTHeader;
    private javax.swing.JTextArea jTPlain;
    private javax.swing.JTextArea jTRTF;
    // End of variables declaration//GEN-END:variables

    private static Source extractHTMLFromRTF(String bodyText) throws ParseException {

        HtmlFromRtf rtf2html = new HtmlFromRtf(bodyText);

        return rtf2html.getHTML();
    }

}
