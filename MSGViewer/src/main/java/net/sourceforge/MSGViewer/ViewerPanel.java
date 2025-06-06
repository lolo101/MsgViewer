package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
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
import jakarta.activation.MimeType;
import jakarta.activation.MimeTypeParseException;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.sourceforge.MSGViewer.factory.MessageParser;
import net.sourceforge.MSGViewer.factory.MessageSaver;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static at.redeye.FrameWork.base.BaseDialog.logger;
import static java.util.stream.Collectors.joining;
import static net.sourceforge.MSGViewer.CLIHelpMSGViewer.CLI_STARTUP_TEXT_VIEW;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ViewerPanel extends JPanel implements Printable, MessageView {

    public static final String FILE_NAME_PROPERTY = "file_name";
    private static final Pattern RTF_FONT_SIZE_PATTERN = Pattern.compile("(\\\\fs)([0-9]+)");
    private final BaseDialog parent;
    private final Root root;
    private final ViewerHelper helper;
    private final AttachmentRepository attachmentRepository;
    private Message message;
    private String file_name;
    private OpenNewMailInterface open_new_mail_handler;

    private final ExecutorService thread_pool = Executors.newCachedThreadPool();
    private int wating_thread_pool_counter;

    public ViewerPanel(BaseDialog parent) {
        this.parent = parent;
        root = parent.getRoot();
        helper = new ViewerHelper(root);
        attachmentRepository = new AttachmentRepository(root);

        initComponents();

        new DropTarget(header, DnDConstants.ACTION_COPY_OR_MOVE, new EditorDropTarget(this), true, null);
        new DropTarget(body, DnDConstants.ACTION_COPY_OR_MOVE, new EditorDropTarget(this), true, null);

        boolean rtfFormat = StringUtils.isYes(root.getSetup().getConfig("RTFFormat", "yes"))
                && Stream.of(root.getStartupArgs()).noneMatch(s -> s.equals(CLI_STARTUP_TEXT_VIEW));

        jRRTF.setSelected(rtfFormat);
        jRHTML.setSelected(!rtfFormat);

        jSplitPane.setDividerLocation(Integer.parseInt(root.getSetup().getConfig("DividerLocation", "150")));
        header.setText("Drag a msg file into this window");
    }

    public void setOpenNewMailInterface(OpenNewMailInterface open_new_mail_handler) {
        this.open_new_mail_handler = open_new_mail_handler;
    }

    public void view(String file_name) {
        logger.info("filename: {}", file_name);

        file_name = decode(file_name);

        if (message == null) {
            parse(file_name);
            firePropertyChange(FILE_NAME_PROPERTY, null, file_name);
        } else {
            open_new_mail_handler.openMail(file_name);
        }
    }

    private static String decode(String file_name) {
        if (file_name.startsWith("file:")) {
            return new AutoMBox<>(ViewerPanel.class.getName(), () -> {
                String decoded_file_name = URLDecoder.decode(file_name, StandardCharsets.UTF_8);
                return new URL(decoded_file_name).getFile();
            }).resultOrElse(file_name);
        }
        return file_name;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        int pageTop = (int) (pageFormat.getImageableHeight() * pageIndex);
        double bodyScale = pageFormat.getImageableWidth() / body.getWidth();
        if (pageTop >= fullHeight(bodyScale)) {
            return NO_SUCH_PAGE;
        }
        printPage(graphics, pageFormat, pageTop, bodyScale);
        return PAGE_EXISTS;
    }

    private void printPage(Graphics graphics, PageFormat pageFormat, int pageTop, double bodyScale) {
        Graphics2D g2d = (Graphics2D) graphics.create(
                (int) pageFormat.getImageableX(),
                (int) pageFormat.getImageableY(),
                (int) pageFormat.getImageableWidth(),
                (int) pageFormat.getImageableHeight());
        int headerHeight = header.getHeight();
        if (pageTop < headerHeight) {
            printHeader(g2d, pageTop, headerHeight, (int) pageFormat.getImageableWidth());
        }
        printBody(g2d, pageTop, headerHeight, bodyScale);
        g2d.dispose();
    }

    private void printHeader(Graphics2D g2d, int pageTop, int headerHeight, int pageWidth) {
        int lineY = headerHeight - pageTop - 1;
        g2d.translate(0, -pageTop);
        header.print(g2d);
        g2d.drawLine(0, lineY, pageWidth, lineY);
    }

    private void printBody(Graphics2D g2d, int pageTop, int headerHeight, double scale) {
        g2d.translate(0, headerHeight - pageTop);
        g2d.scale(scale, scale);
        body.print(g2d);
    }

    private double fullHeight(double bodyScale) {
        return header.getHeight() + body.getHeight() * bodyScale;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
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
        jRHTML = new javax.swing.JRadioButton();
        jSFontSize = new javax.swing.JSlider();
        javax.swing.JLabel fontSizeLabel = new javax.swing.JLabel();

        jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        header.setEditable(false);
        header.addHyperlinkListener(this::headerHyperlinkUpdate);
        headerScrollPane.setViewportView(header);

        jSplitPane.setTopComponent(headerScrollPane);

        body.setEditable(false);
        body.addHyperlinkListener(this::bodyHyperlinkUpdate);
        bodyScrollPane.setViewportView(body);

        buttonGroup.add(jRRTF);
        jRRTF.setText("RTF");
        jRRTF.addActionListener(this::jRRTFActionPerformed);

        buttonGroup.add(jRHTML);
        jRHTML.setText("HTML");
        jRHTML.addActionListener(this::jRHTMLActionPerformed);

        jSFontSize.addChangeListener(this::jSFontSizeStateChanged);

        fontSizeLabel.setText("Fontsize");

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(buttonsPanelLayout.createSequentialGroup()
                    .addComponent(jRRTF)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jRHTML)
                    .addGap(108, 108, 108)
                    .addComponent(jSFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fontSizeLabel)
                    .addGap(0, 84, Short.MAX_VALUE))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(buttonsPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRRTF)
                            .addComponent(jRHTML))
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

        if (editor instanceof HTMLEditorKit html_editor) {

            System.out.println("Value: " + jSFontSize.getValue());

            Source source = new Source(body.getText());
            source.fullSequentialParse();

            String rule = "{ font-size: " + jSFontSize.getValue() + "pt; }";
            String tags = source.getAllStartTags().stream()
                    .map(StartTag::getName)
                    .map(t -> t + rule)
                    .collect(joining());

            StyleSheet sheet = html_editor.getStyleSheet();
            sheet.addRule(tags);

            String text = body.getText();
            body.setDocument(html_editor.createDefaultDocument());
            body.setText(text);

        } else {

            String text = body.getText();
            if (text != null) {
                String resizedText = RTF_FONT_SIZE_PATTERN.matcher(text).replaceAll("$1" + ((jSFontSize.getValue()) * 2));
                System.out.println(resizedText);
                body.setText(resizedText);
            }
        }
        body.setCaretPosition(0);
    }//GEN-LAST:event_jSFontSizeStateChanged

    private void jRRTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRRTFActionPerformed

        updateBody();
    }//GEN-LAST:event_jRRTFActionPerformed

    private void jRHTMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRHTMLActionPerformed

        updateBody();
    }//GEN-LAST:event_jRHTMLActionPerformed

    private void bodyHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_bodyHyperlinkUpdate
        hyperlinkUpdate(evt);
    }//GEN-LAST:event_bodyHyperlinkUpdate

    private void headerHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_headerHyperlinkUpdate
        hyperlinkUpdate(evt);
    }//GEN-LAST:event_headerHyperlinkUpdate

    private void updateBody() {
        if (message == null)
            return;

        Content content = bodyText();

        logger.trace(content.text());

        body.setContentType(content.type());
        body.setText(content.text());
        body.setCaretPosition(0);
    }

    private Content bodyText() {
        String bodyRTF = message.getBodyRTF();
        if (jRRTF.isSelected() && isNotEmpty(bodyRTF)) {
            if (bodyRTF.contains("\\fromhtml")) {
                return new AutoMBox<>(MainWin.class.getName(), () -> {
                    logger.info("extracting HTML data from RTF Code");

                    if (logger.isTraceEnabled()) {
                        logger.trace("\n{}", StringUtils.addLineNumbers(bodyRTF));
                    }

                    Source source = ViewerHelper.extractHTMLFromRTF(bodyRTF);
                    return new Content("text/html", helper.prepareImages(message, source));
                }).resultOrElse(new Content("text/rtf", bodyRTF));
            }
            return new Content("text/rtf", bodyRTF);
        }
        if (message.getBodyHtml() != null) {
            Source source = ViewerHelper.toHtmlSource(message.getBodyHtml());
            return new Content("text/html", helper.prepareImages(message, source));
        }
        return new Content("text/plain", message.getBodyText());
    }

    private void hyperlinkUpdate(final HyperlinkEvent e) {
        new AutoMBox<>(ViewerPanel.class.getName(), () -> {
            if (message != null && e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) && e.getURL() != null) {
                openUrl(e.getURL().toURI());
            }
        }).run();
    }

    private void openUrl(URI uri) throws IOException {
        logger.info(uri);

        final String protocol = uri.getScheme();

        if (protocol.equals("file")) {
            openLocalFile(uri);
        } else {
            open(uri.toString());
        }

    }

    private void openLocalFile(URI uri) throws IOException {
        Path content = helper.extractUrl(uri, message);

        if (ViewerHelper.is_mail_message(content.toString())) {

            if (open_new_mail_handler != null) {
                open_new_mail_handler.openMail(content.toString());
            }
        } else {
            open(content.toString());
        }
    }

    private void open(String path) throws IOException {
        if (Setup.is_win_system() && root.getPlugins().getAvailable("ShellExec") != null) {
            logger.info("opening: {}", path);

            ShellExec shell = new ShellExec();
            int ret = shell.execute(path);
            logger.debug("shell exec returned: {}", ret);
        } else {
            String open_command = ViewerHelper.getOpenCommand();

            logger.info("{} \"{}\"", open_command, path);

            String[] command_array = new String[2];

            command_array[0] = open_command;
            command_array[1] = path;

            Runtime.getRuntime().exec(command_array);
        }
    }

    public void parse(final String file_name) {
        if (file_name == null)
            return;

        this.file_name = file_name;

        parent.setWaitCursor();

        new AutoMBox<>(MainWin.class.getName(), this::doParse).run();

        parent.setNormalCursor();
    }

    private void doParse() throws Exception {
        wating_thread_pool_counter = 0;

        parseMessage();
        updateHeader();
        updateBody();

        if (wating_thread_pool_counter > 0) {
            new AutoMBox<>(file_name, () -> {
                thread_pool.shutdown();
                if (wating_thread_pool_counter > 0) {
                    thread_pool.awaitTermination(1, TimeUnit.DAYS);
                }
            }).run();
        }
    }

    private void parseMessage() throws Exception {
        Path file = Path.of(file_name);
        if (!Files.exists(file))
            throw new FileNotFoundException(parent.MlM(String.format("File %s not found", file_name)));
        message = new MessageParser(file).parseMessage();
        logger.info("Message From:{}\n To:{}\n Email: {}", message.getFromName(), message.getToName(), message.getFromEmail());
    }

    private void updateHeader() throws MimeTypeParseException {
        header.setContentType("text/html");
        header.setText(headerHtml());
        header.setCaretPosition(0);
    }

    private String headerHtml() throws MimeTypeParseException {
        return "<html><body>" + "<b>" + printSubject() + "</b><br/>"
                + printLine("From: ", printFrom())
                + printLine("Date: ", printDate())
                + printLine("To: ", printRecipients(RecipientType.TO))
                + printLine("CC: ", printRecipients(RecipientType.CC))
                + printLine("BCC: ", printRecipients(RecipientType.BCC))
                + printAttachments()
                + "</body></html>";
    }

    private String printLine(String label, String value) {
        return isBlank(value) ? "" : parent.MlM(label) + value + "<br/>";
    }

    private String printSubject() {
        String subject = message.getSubject();
        return subject == null ? "" : subject;
    }

    private String printFrom() {
        String messageFromName = message.getFromName();
        String messageFromEmail = ViewerHelper.isValidEmail(message.getFromEmail())
                ? message.getFromEmail()
                : message.getFromSMTPAddress();
        return mailTo(messageFromName, messageFromEmail);
    }

    private String printDate() {
        ZonedDateTime date = message.getDate();
        return date == null ? "" : date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
    }

    private String printRecipients(RecipientType to) {
        String recipientsTo = message.getRecipients().stream()
                .filter(r -> r.getType() == to)
                .map(ViewerPanel::asMailto)
                .collect(joining("; "));
        return isBlank(recipientsTo) ? "" : recipientsTo;
    }

    private String printAttachments() throws MimeTypeParseException {
        StringBuilder sb = new StringBuilder();
        for (Attachment att : message.getAttachments()) {
            sb.append(printAttachment(att));
        }
        return sb.toString();
    }

    private String printAttachment(Attachment att) throws MimeTypeParseException {
        if (att instanceof FileAttachment) {
            return printFileAttachment((FileAttachment) att);
        }
        if (att instanceof MsgAttachment) {
            return printMsgAttachment((MsgAttachment) att);
        }
        logger.error("unknown Attachment: {} {}", att, att.getClass().getName());
        return "";
    }

    private String printFileAttachment(FileAttachment att) throws MimeTypeParseException {
        Path content = attachmentRepository.getTempFile(att);

        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"");
        sb.append(content.toUri());
        sb.append("\">");

        String mime_type = att.getMimeTag();

        logger.info("<a href=\"{}\"> {}", content.toUri(), mime_type);

        if (mime_type != null && ViewerHelper.is_image_mime_type(new MimeType(mime_type))) {
            File thumbnailFile = new File(content.toAbsolutePath() + "-small.jpg");
            if (!Files.exists(content)) {
                write(content, att.getData());
                async(() -> printThumbnail(att, thumbnailFile));
            }

            sb.append("<img border=0 src=\"").append(thumbnailFile.toURI()).append("\"/> ");
        }

        if (ViewerHelper.is_mail_message(att.getLongFilename())) {
            if (!Files.exists(content)) {
                write(content, att.getData());
            }

            sb.append(ViewerHelper.printMailIconHtml());
        }

        sb.append(att);
        sb.append("</a> ");
        return sb.toString();
    }

    private String printMsgAttachment(MsgAttachment att) {
        final Message msg = att.message();

        Path sub_file = attachmentRepository.getTempFile(att);

        async(() -> new AutoMBox<>(file_name, () -> new MessageSaver(attachmentRepository, msg).saveMessage(sub_file)).run());

        return "<a href=\"" + sub_file.toUri() + "\">" + ViewerHelper.printMailIconHtml() + msg.getSubject() + "</a>&nbsp;";
    }

    private static void printThumbnail(FileAttachment att, File thumbnailFile) {
        try {
            final int max_icon_size = Integer.parseInt(AppConfigDefinitions.IconSize.getConfigValue());
            ImageIcon icon = ImageUtils.loadScaledImageIcon(att.getData(), att.toString(), max_icon_size, max_icon_size);
            BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(icon.getImage(), 0, 0, null);
            g2.dispose();

            ImageIO.write(bi, "jpg", thumbnailFile);
        } catch (IOException ex) {
            logger.error(ex, ex);
        }
    }

    private static String asMailto(RecipientEntry recipient) {
        String name = recipient.getName();
        String mailTo = recipient.mailTo();
        return mailTo(name, mailTo);
    }

    private static String mailTo(String name, String email) {
        return isBlank(email)
                ? name
                : "<a href='mailto:" + email + "'>" + (isBlank(name) ? email : name + " &lt;" + email + "&gt;") + "</a>";
    }

    private void write(Path content, byte[] data) {
        async(() -> {
            try (OutputStream fout = Files.newOutputStream(content)) {
                fout.write(data);
            } catch (IOException ex) {
                logger.error(ex, ex);
            }
        });
    }

    public void dispose() {
        root.getSetup().setLocalConfig("RTFFormat", jRRTF.isSelected() ? "yes" : "no");
        root.getSetup().setLocalConfig("DividerLocation", String.valueOf(jSplitPane.getDividerLocation()));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane body;
    private javax.swing.JEditorPane header;
    private javax.swing.JRadioButton jRHTML;
    private javax.swing.JRadioButton jRRTF;
    private javax.swing.JSlider jSFontSize;
    private javax.swing.JSplitPane jSplitPane;
    // End of variables declaration//GEN-END:variables

    public Message getMessage() {
        return message;
    }

    public void exportFile(Path export_file) throws Exception {
        if (message == null) {
            JOptionPane.showMessageDialog(parent,
                    root.MlM("No message to save"),
                    root.MlM("Error"),
                    JOptionPane.ERROR_MESSAGE);
        }
        new MessageSaver(attachmentRepository, message).saveMessage(export_file);
    }

    public String getFileName() {
        return file_name;
    }

    private void async(Runnable runnable) {
        wating_thread_pool_counter++;
        thread_pool.execute(() -> {
            runnable.run();
            wating_thread_pool_counter--;
        });
    }
}
