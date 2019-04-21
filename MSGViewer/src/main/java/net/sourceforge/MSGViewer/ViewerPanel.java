package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.AutoMBox;

import static at.redeye.FrameWork.base.BaseDialog.logger;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import at.redeye.FrameWork.base.BaseDialogBase;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.base.imagestorage.ImageUtils;
import at.redeye.FrameWork.utilities.StringUtils;
import at.redeye.Plugins.ShellExec.ShellExec;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.sourceforge.MSGViewer.factory.MessageParserFactory;

public class ViewerPanel extends javax.swing.JPanel implements HyperlinkListener {

    private String bodyText = null;
    private Message message;
    private ViewerHelper helper = null;
    private Root root = null;
    private String file_name;
    private OpenNewMailInterface open_new_mail_handler = null;
    private BaseDialogBase parent = null;
    private final MessageParserFactory parser_factory = new MessageParserFactory();

    private int wating_thread_pool_counter = 0;

    /**
     * Creates new form ViewerPanel
     */
    public ViewerPanel() {
        initComponents();

        header.addHyperlinkListener(this);
        body.addHyperlinkListener(this);

        JCBfix.setEnabled(jRText.isSelected());
    }

    public void setRoot( Root root, BaseDialogBase parent )
    {
        this.parent = parent;
        this.root = root;
        helper = new ViewerHelper(root);

        boolean rtfFormat = StringUtils.isYes(root.getSetup().getLocalConfig("RTFFormat", "yes") );
        jRRTF.setSelected(rtfFormat);
        jRText.setSelected(!rtfFormat);

        JCBfix.setSelected(StringUtils.isYes(root.getSetup().getLocalConfig("FixedFont","no" ) ) );
        JCBfix.setEnabled(jRText.isSelected());

        jSplitPane.setDividerLocation(Integer.parseInt(root.getSetup().getLocalConfig("DividerLocation","150")));
    }

    public void setopenNewMailInterface( OpenNewMailInterface open_new_mail_handler )
    {
        this.open_new_mail_handler = open_new_mail_handler;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        header = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        body = new javax.swing.JEditorPane();
        jPanel2 = new javax.swing.JPanel();
        jRRTF = new javax.swing.JRadioButton();
        jRText = new javax.swing.JRadioButton();
        JCBfix = new javax.swing.JCheckBox();
        jSFontSize = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();

        jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        header.setEditable(false);
        jScrollPane1.setViewportView(header);

        jSplitPane.setTopComponent(jScrollPane1);

        body.setEditable(false);
        jScrollPane2.setViewportView(body);

        jRRTF.setText("RTF");
        jRRTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRRTFActionPerformed(evt);
            }
        });

        jRText.setText("Text");
        jRText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRTextActionPerformed(evt);
            }
        });

        JCBfix.setText("Fixed Font");
        JCBfix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCBfixActionPerformed(evt);
            }
        });

        jSFontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSFontSizeStateChanged(evt);
            }
        });

        jLabel1.setText("Fontsize");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jRRTF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JCBfix)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(0, 95, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRRTF)
                        .addComponent(jRText)
                        .addComponent(JCBfix))
                    .addComponent(jSFontSize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSFontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSFontSizeStateChanged

        EditorKit editor = body.getEditorKit();

        if (editor instanceof HTMLEditorKit) {

            System.out.println("Value: " + jSFontSize.getValue());

            Source source = new Source(body.getText());
            source.fullSequentialParse();

            String rule = "{ font-size: " + jSFontSize.getValue() + "pt; }";
            String tags = source.getAllStartTags().stream()
                    .map(StartTag::getName)
                    .map(t -> t + rule)
                    .collect(Collectors.joining());

            HTMLEditorKit html_editor = (HTMLEditorKit) editor;
            StyleSheet sheet = html_editor.getStyleSheet();
            sheet.addRule(tags);

            String text = body.getText();
            body.setDocument(html_editor.createDefaultDocument());
            body.setText(text);

        } else {

            if( bodyText != null )
            {
                bodyText = bodyText.replaceAll("(\\\\fs)([0-9]+)", "$1" + ((jSFontSize.getValue())*2));
                System.out.println(bodyText);
                body.setText(bodyText);
            }
        }
        body.setCaretPosition(0);
    }//GEN-LAST:event_jSFontSizeStateChanged

    private void jRRTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRRTFActionPerformed

        jRText.setSelected(!jRRTF.isSelected());
        JCBfix.setEnabled(jRText.isSelected());
        updateBody();
    }//GEN-LAST:event_jRRTFActionPerformed

    private void jRTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRTextActionPerformed

         jRRTF.setSelected(!jRText.isSelected());
         JCBfix.setEnabled(jRText.isSelected());
         updateBody();
    }//GEN-LAST:event_jRTextActionPerformed

    private void JCBfixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCBfixActionPerformed

        updateBody();
    }//GEN-LAST:event_JCBfixActionPerformed

   private void updateBody()
    {
        if( message == null )
            return;

        if( jRRTF.isSelected() &&  message.getBodyRTF() != null &&  !message.getBodyRTF().isEmpty() )
        {
            if( message.getBodyRTF().contains("\\fromhtml") )
            {
                AutoMBox am = new AutoMBox(MainWin.class.getName()) {

                    @Override
                    public void do_stuff() throws Exception {
                        logger.info("extracting HTML data from RTF Code");

                        if( logger.isTraceEnabled() )
                        {
                            logger.trace("\n" + StringUtils.addLineNumbers(message.getBodyRTF()));
                        }

                        body.setContentType("text/html");
                        bodyText = helper.extractHTMLFromRTF(message.getBodyRTF(),message);

                        logger.trace(bodyText);
                    }
                };

                if( am.isFailed() )
                {
                    body.setContentType("text/rtf");
                    bodyText = message.getBodyRTF();
                }
            }
            else if( message.getBodyRTF().contains("\\purehtml") )
            {
                PrepareImages prep_images = new PrepareImages(helper, message);

                body.setContentType("text/html");
                bodyText = prep_images.prepareImages(new StringBuilder(ViewerHelper.stripMetaTags(message.getBodyRTF()))).toString();
            }
            else
            {
                body.setContentType("text/rtf");
                bodyText = message.getBodyRTF();
            }
        }
        else
        {
            body.setContentType("text/html");
            bodyText = asHtml(message.getBodyText());
        }

        body.setText(bodyText);
        body.setCaretPosition(0);
    }

    private String asHtml(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        String font = JCBfix.isSelected()
                ? "Courier New;font-size:10px;"
                : "Dialog,sans-serif;font-size:10px;";
        sb.append("<pre style=\"font-family:").append(font).append("\">");
        sb.append(ViewerHelper.prepareText(text));
        sb.append("</pre>");
        sb.append("</body></html>");
        return sb.toString();
    }

    @Override
    public void hyperlinkUpdate( final HyperlinkEvent e )
    {
        new AutoMBox(MainWin.class.getName()) {

            @Override
            public void do_stuff() throws Exception {

                if (message == null) {
                    return;
                }

                if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    return;
                }

                openUrl(e.getURL());
            }
        };
    }

    public void openUrl(URL url) throws IOException
    {
        logger.info(url);

        final String protocol = url.getProtocol();

        if( !protocol.equals("file") )
        {
            if (Setup.is_win_system()) {
                logger.info("opening: " + url);

                ShellExec shell = new ShellExec();
                int ret = shell.execute(url.toString());
                logger.debug("shell exec returned: " + ret);
            } else {

                String open_command = helper.getOpenCommand();

                String command = open_command + " \"" + url + "\"";
                logger.info(command);

                String command_array[] = new String[2];

                command_array[0] = open_command;
                command_array[1] = url.toString();

                Process p = Runtime.getRuntime().exec(command_array);
            }
            return;
        }

        File content = helper.extractUrl(url,message);

        if( content == null )
        {
            // maybe the url points to a local directory
            content = new File(url.getFile());
        }

        if( ViewerHelper.is_mail_message(content.toString() ) ) {

            if( open_new_mail_handler != null )
            {
                open_new_mail_handler.openMail( root,content.toString() );
            }
        } else {

            if (Setup.is_win_system() && root.getPlugin("ShellExec") != null ) {
                logger.info("opening: " + content.getPath());

                ShellExec shell = new ShellExec();
                int ret = shell.execute(content.getPath());
                logger.debug("shell exec returned: " + ret);
            } else {
                String open_command = helper.getOpenCommand();

                String command = open_command + " \"" + content.getPath() + "\"";
                logger.info(command);

                String command_array[] = new String[2];

                command_array[0] = open_command;
                command_array[1] = content.getPath();

                Process p = Runtime.getRuntime().exec(command_array);
            }
        }

    }


    public void parse(final String file_name)
    {
        if( file_name == null )
            return;

        this.file_name = file_name;

        parent.setWaitCursor();

        new AutoMBox(MainWin.class.getName()) {

                @Override
                public void do_stuff() throws Exception {
                    parse_int( file_name );
                }
            };

        parent.setNormalCursor();
    }

    void cleanUp() {
        message = null;
    }

    void parse_int(final String file_name) throws IOException, FileNotFoundException, Exception
    {
        cleanUp();

        final ExecutorService thread_pool = Executors.newCachedThreadPool();
        wating_thread_pool_counter = 0;

        File file = new File(file_name);

        if( !file.exists() )
            throw new FileNotFoundException( parent.MlM( String.format("File %s not found",file_name)) );

        message = parser_factory.parseMessage(file);

        final StringBuilder sb = new StringBuilder();

        sb.append("<html>");
        sb.append("<body style=\"\">");

        sb.append("<b>");
        if( message.getSubject() != null )
            sb.append(message.getSubject());
        sb.append("</b>");
        sb.append("<br/>");

        logger.info("Message From:" + message.getFromName() + "\n To:" + message.getToName() + "\n Email: " + message.getFromEmail());

        if (message.getFromEmail() == null && message.getFromName() == null) {
        } else if (message.getFromEmail() == null) {
            sb.append(parent.MlM("From: ") + message.getFromName());
        } else if (message.getFromName() == null) {
            sb.append("<a href=\"mailto:");
            sb.append(message.getFromEmail());
            sb.append("\">");
            sb.append(message.getFromEmail());
        } else {

            sb.append("<a href=\"mailto:");
            sb.append(message.getFromEmail());
            sb.append("\">");
            sb.append(message.getFromName());
        }

        if( message.getFromEmail() != null && message.getFromEmail().contains("@") )
        {
            sb.append(" [");
            sb.append(message.getFromEmail());
            sb.append("]");
        }

        sb.append("</a>");

        sb.append("<br/>");

        if( message.getDate() != null )
        {
            sb.append(parent.MlM("Date: "));
            sb.append(DateFormat.getDateTimeInstance().format(message.getDate()));
            sb.append("<br/>");
        }

        if( !message.getRecipients().isEmpty() )
        {
            sb.append(parent.MlM("To: "));
            sb.append(message.getRecipients().stream().map(ViewerPanel::asMailto).collect(toList()));
        }

        sb.append("<br>");

        List<Attachment> attachments = message.getAttachments();

        final int max_width = Integer.parseInt(root.getSetup().getLocalConfig(AppConfigDefinitions.IconSize));
        final int max_height = max_width;

        for( Attachment att : attachments )
        {
            if( att instanceof FileAttachment)
            {
                final FileAttachment fatt = (FileAttachment) att;

                File content = helper.getTempFile(fatt);

                sb.append("<a href=\"");
                sb.append(content.toURI());
                sb.append("\">");

                String mime_type = fatt.getMimeTag();

                logger.info("<a href=\"" + content.toURI() + "\"> " + mime_type);

                if( mime_type != null && ViewerHelper.is_image_mime_type(mime_type) && fatt.getSize() < 1024*1024*2 )
                {
                    File contentIcon = new File(content.getAbsolutePath() + "-small.jpg");
                    if (!content.exists()) {

                        wating_thread_pool_counter++;

                        thread_pool.execute(new Runnable() {

                            @Override
                            public void run() {

                                try (FileOutputStream fout = new FileOutputStream(content)) {
                                    fout.write(fatt.getData());
                                } catch( IOException ex ) {
                                    logger.error(ex,ex);
                                }

                                wating_thread_pool_counter--;
                            }
                        });

                        wating_thread_pool_counter++;

                        thread_pool.execute(new Runnable() {

                            @Override
                            public void run() {

                                try {

                                    ImageIcon icon = ImageUtils.loadScaledImageIcon(fatt.getData(),
                                                                fatt.toString(),
                                                                max_width, max_height );

                                    BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),
                                                            BufferedImage.TYPE_INT_RGB);

                                    Graphics2D g2 = bi.createGraphics();
                                    g2.drawImage(icon.getImage(), 0, 0, null);
                                    g2.dispose();

                                    ImageIO.write(bi, "jpg", contentIcon);

                                } catch( IOException ex ) {
                                    logger.error(ex,ex);
                                }

                                wating_thread_pool_counter--;
                            }
                        });
                    }

                    System.out.println(contentIcon);
                    sb.append("<img border=0 src=\"");
                    sb.append(contentIcon.toURI());
                    sb.append("\"/> ");
                }

                if( ViewerHelper.is_mail_message(fatt.getFilename(), fatt.getMimeTag() ) ) {
                    sb.append("<img border=0 align=\"baseline\" src=\"");
                    sb.append(helper.getMailIconFile().toURI());
                    sb.append("\"/>");
                }

                sb.append(fatt);
                sb.append("</a> ");

            } else if( att instanceof MsgAttachment) {

                MsgAttachment msgAtt = (MsgAttachment) att;
                final Message msg = msgAtt.getMessage();

                File sub_file = helper.getTempFile(msgAtt);

                thread_pool.execute(new Runnable() {

                    @Override
                    public void run() {

                        new AutoMBox(file_name) {

                            @Override
                            public void do_stuff() throws Exception {

                                MessageParserFactory factory = new MessageParserFactory();
                                factory.saveMessage(msg, sub_file);

                            }
                        };
                    }

                });


                sb.append("<a href=\"");
                sb.append(sub_file.toURI());
                sb.append("\">");

                sb.append("<img border=0 align=\"baseline\" src=\"");
                sb.append(helper.getMailIconFile().toURI());
                sb.append("\"/>");

                sb.append(msg.getSubject());

                sb.append("</a> &nbsp; ");
            } else {
                logger.error("unknown Attachment: " + att + " " + att.getClass().getName() );
            }
        }

        sb.append("</body></html>");
        header.setContentType("text/html");

        if( wating_thread_pool_counter > 0 )
        {
            updateBody(); // do something different now

            new AutoMBox(file_name) {

                @Override
                public void do_stuff() throws Exception {
                    thread_pool.shutdown();

                    if( wating_thread_pool_counter > 0 ) {
                        thread_pool.awaitTermination(1, TimeUnit.DAYS);
                    }
                }
            };

            header.setText(sb.toString());
            header.setCaretPosition(0);

        } else {

            header.setText(sb.toString());
            header.setCaretPosition(0);

            updateBody();
        }
    }

    private static String asMailto(RecipientEntry recipient) {
        String name = recipient.getName();
        String email = recipient.getEmail();
        if (isNotBlank(email)) {
            return "<a href='mailto:" + email + "'>" + (isNotBlank(name) ? name + " [" + email + "]" : email) + "</a>";
        }
        return name;
    }

    public void dispose()
    {
        root.getSetup().setLocalConfig("RTFFormat",jRRTF.isSelected() ? "yes" : "no");
        root.getSetup().setLocalConfig("FixedFont", JCBfix.isSelected() ? "yes" : "no");
        root.getSetup().setLocalConfig("DividerLocation", String.valueOf(jSplitPane.getDividerLocation()));

        helper.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox JCBfix;
    private javax.swing.JEditorPane body;
    private javax.swing.JEditorPane header;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRRTF;
    private javax.swing.JRadioButton jRText;
    private javax.swing.JSlider jSFontSize;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane;
    // End of variables declaration//GEN-END:variables

    public Message getMessage() {
       return message;
    }

    public void exportFile(File export_file) throws Exception {
        parser_factory.saveMessage(message, export_file);
    }

    public void exportFile( File file, Message message ) throws FileNotFoundException, Exception
    {
        parser_factory.saveMessage(message, file);
    }

    public String getFileName()
    {
        return file_name;
    }

    public JEditorPane getHeaderPane() {
        return header;
    }

    public JEditorPane getBodyPane() {
        return body;
    }
}
