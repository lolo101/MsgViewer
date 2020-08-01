package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialogBase;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.base.imagestorage.ImageUtils;
import at.redeye.FrameWork.utilities.StringUtils;
import at.redeye.Plugins.ShellExec.ShellExec;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;
import com.auxilii.msgparser.RecipientType;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.sourceforge.MSGViewer.factory.MessageParserFactory;

import javax.activation.MimeType;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
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

import static at.redeye.FrameWork.base.BaseDialog.logger;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ViewerPanel extends javax.swing.JPanel implements HyperlinkListener, Printable {

    private String bodyText = null;
    private Message message;
    private ViewerHelper helper = null;
    private Root root = null;
    private String file_name;
    private OpenNewMailInterface open_new_mail_handler = null;
    private BaseDialogBase parent = null;
    private final MessageParserFactory parser_factory = new MessageParserFactory();

    private final ExecutorService thread_pool = Executors.newCachedThreadPool();
    private int wating_thread_pool_counter = 0;

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

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        int pageTop = (int) (pageFormat.getImageableHeight() * pageIndex);
        double bodyScale = pageFormat.getImageableWidth() / body.getWidth();
        if (pageTop >= fullHeight(bodyScale)) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D)graphics.create(
                (int)pageFormat.getImageableX(),
                (int)pageFormat.getImageableY(),
                (int)pageFormat.getImageableWidth(),
                (int)pageFormat.getImageableHeight());
        int headerHeight = header.getHeight();
        if (pageTop < headerHeight) {
            printHeader(g2d, pageTop, headerHeight, (int) pageFormat.getImageableWidth());
        }
        printBody(g2d, pageTop, headerHeight, bodyScale);

        return PAGE_EXISTS;
    }

    private void printHeader(Graphics2D g2d, int pageTop, int headerHeight, int pageWidth) {
        int lineY = headerHeight - pageTop - 1;
        g2d.translate(0, - pageTop);
        header.print(g2d);
        g2d.drawLine(0, lineY, pageWidth, lineY);
    }

    private void printBody(Graphics2D g2d, int pageTop, int headerHeight, double scale) {
        g2d.translate(0, headerHeight - pageTop);
        g2d.scale(scale, scale);
        body.print(g2d);
        g2d.dispose();
    }

    private double fullHeight(double bodyScale) {
        return header.getHeight() + body.getHeight() * bodyScale;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane = new javax.swing.JSplitPane();
        javax.swing.JScrollPane headerScrollPane = new javax.swing.JScrollPane();
        header = new javax.swing.JEditorPane();
        header.addPropertyChangeListener("paintingForPrint", new PrintListener());
        javax.swing.JPanel MainPanel = new javax.swing.JPanel();
        javax.swing.JScrollPane bodyScrollPane = new javax.swing.JScrollPane();
        body = new javax.swing.JEditorPane();
        body.addPropertyChangeListener("paintingForPrint", new PrintListener());
        javax.swing.JPanel buttonsPanel = new javax.swing.JPanel();
        jRRTF = new javax.swing.JRadioButton();
        jRText = new javax.swing.JRadioButton();
        JCBfix = new javax.swing.JCheckBox();
        jSFontSize = new javax.swing.JSlider();
        javax.swing.JLabel fontSizeLabel = new javax.swing.JLabel();

        jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        header.setEditable(false);
        headerScrollPane.setViewportView(header);

        jSplitPane.setTopComponent(headerScrollPane);

        body.setEditable(false);
        bodyScrollPane.setViewportView(body);

        jRRTF.setText("RTF");
        jRRTF.addActionListener(this::jRRTFActionPerformed);

        jRText.setText("Text");
        jRText.addActionListener(this::jRTextActionPerformed);

        JCBfix.setText("Fixed Font");
        JCBfix.addActionListener(this::JCBfixActionPerformed);

        jSFontSize.addChangeListener(this::jSFontSizeStateChanged);

        fontSizeLabel.setText("Fontsize");

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addComponent(jRRTF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JCBfix)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontSizeLabel)
                .addGap(0, 95, Short.MAX_VALUE))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRRTF)
                        .addComponent(jRText)
                        .addComponent(JCBfix))
                    .addComponent(jSFontSize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fontSizeLabel, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bodyScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(bodyScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane.setRightComponent(MainPanel);

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
                    .collect(joining());

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
            else
            {
                body.setContentType("text/rtf");
                bodyText = message.getBodyRTF();
            }
        }
        else if( message.getBodyHtml() != null )
        {
            PrepareImages prep_images = new PrepareImages(helper, message);

            body.setContentType("text/html");
            bodyText = prep_images.prepareImages(new StringBuilder(ViewerHelper.stripMetaTags(message.getBodyHtml())));
        }
        else
        {
            body.setContentType("text/plain");
            bodyText = message.getBodyText();
        }

        body.setText(bodyText);
        body.setCaretPosition(0);
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

                String[] command_array = new String[2];

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

                String[] command_array = new String[2];

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

    void parse_int(final String file_name) throws Exception
    {
        cleanUp();

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

        printFrom(sb);

        if( message.getDate() != null )
        {
            sb.append(parent.MlM("Date: "));
            sb.append(DateFormat.getDateTimeInstance().format(message.getDate()));
            sb.append("<br/>");
        }

        printRecipients(sb, RecipientType.TO, "To: ");
        printRecipients(sb, RecipientType.CC, "CC: ");
        printRecipients(sb, RecipientType.BCC, "BCC: ");

        List<Attachment> attachments = message.getAttachments();

        final int max_icon_size = Integer.parseInt(root.getSetup().getLocalConfig(AppConfigDefinitions.IconSize));

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

                if( mime_type != null
                        && ViewerHelper.is_image_mime_type(new MimeType(mime_type))
                        && fatt.getSize() < 1024*1024*2 )
                {
                    File contentIcon = new File(content.getAbsolutePath() + "-small.jpg");
                    if (!content.exists()) {

                        write(content, fatt.getData());

                        wating_thread_pool_counter++;

                        thread_pool.execute(() -> {
                            try {
                                ImageIcon icon = ImageUtils.loadScaledImageIcon(fatt.getData(),
                                                            fatt.toString(),
                                                            max_icon_size, max_icon_size);

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
                        });
                    }

                    System.out.println(contentIcon);
                    sb.append("<img border=0 src=\"");
                    sb.append(contentIcon.toURI());
                    sb.append("\"/> ");
                }

                if( ViewerHelper.is_mail_message(fatt.getFilename(), fatt.getMimeTag() ) ) {
                    if (!content.exists())
                    {
                        write(content, fatt.getData());
                    }

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

                thread_pool.execute(() -> new AutoMBox(file_name) {
                    @Override
                    public void do_stuff() throws Exception {

                        MessageParserFactory factory = new MessageParserFactory();
                        factory.saveMessage(msg, sub_file);

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

    private void write(File content, byte[] data) {
        wating_thread_pool_counter++;

        thread_pool.execute(() -> {
            try (FileOutputStream fout = new FileOutputStream(content)) {
                fout.write(data);
            } catch( IOException ex ) {
                logger.error(ex,ex);
            }

            wating_thread_pool_counter--;
        });
    }

    private void printFrom(StringBuilder sb) {
        sb.append(parent.MlM("From: "));
        if (message.getFromEmail() == null && message.getFromName() == null) {
        } else if (message.getFromEmail() == null) {
            sb.append(parent.MlM("From: ")).append(message.getFromName());
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
            sb.append(" &lt;");
            sb.append(message.getFromEmail());
            sb.append("&gt;");
        }

        sb.append("</a>");
        sb.append("<br/>");
    }

    private void printRecipients(StringBuilder sb, RecipientType to, String s) {
        String recipientsTo = message.getRecipients().stream()
                .filter(r -> r.getType() == to)
                .map(ViewerPanel::asMailto)
                .collect(joining("; "));
        if (isNotBlank(recipientsTo)) {
            sb.append(parent.MlM(s));
            sb.append(recipientsTo);
            sb.append("<br/>");
        }
    }

    private static String asMailto(RecipientEntry recipient) {
        String name = recipient.getName();
        String email = recipient.getEmail();
        if (isNotBlank(email)) {
            return "<a href='mailto:" + email + "'>" + (isNotBlank(name) ? name + " &lt;" + email + "&gt;" : email) + "</a>";
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
    private javax.swing.JRadioButton jRRTF;
    private javax.swing.JRadioButton jRText;
    private javax.swing.JSlider jSFontSize;
    private javax.swing.JSplitPane jSplitPane;
    // End of variables declaration//GEN-END:variables

    public Message getMessage() {
       return message;
    }

    public void exportFile(File export_file) throws Exception {
        parser_factory.saveMessage(message, export_file);
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
