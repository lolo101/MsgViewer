/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.AutoMBox;
import static at.redeye.FrameWork.base.BaseDialog.logger;
import at.redeye.FrameWork.base.BaseDialogBase;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.base.imagestorage.ImageUtils;
import at.redeye.FrameWork.utilities.StringUtils;
import at.redeye.Plugins.ShellExec.ShellExec;
import com.auxilii.msgparser.Message;
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
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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

/**
 *
 * @author martin
 */
public class ViewerPanel extends javax.swing.JPanel implements HyperlinkListener {

    private String bodyText = null;
    private Message message;     
    private ViewerHelper helper = null;
    private Root root = null;
    private String file_name;
    private OpenNewMailInterface open_new_mail_handler = null;
    private BaseDialogBase parent = null;
    private MessageParserFactory parser_factory = new MessageParserFactory();
    
    private int wating_thread_pool_counter = 0;
    
    /**
     * URL where the mouse cursor is over, for the popup menu
     */
    private URL lastUrl = null;    
    
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
        
        if( StringUtils.isYes(root.getSetup().getLocalConfig("RTFFormat", "yes") ) )
        {
            jRRTF.setSelected(true);
            jRText.setSelected(false);
        }
        else
        {
            jRRTF.setSelected(false);
            jRText.setSelected(true);
        }

        JCBfix.setSelected(StringUtils.isYes(root.getSetup().getLocalConfig("FixedFont","no" ) ) );
        
        JCBfix.setEnabled(jRText.isSelected());                   
        
        jSplitPane.setDividerLocation(Integer.parseInt(root.getSetup().getLocalConfig("DividerLocation","150")));        
    }
    
    public void loadFile( String file_name )
    {
         this.file_name = file_name;
         
         parse(file_name);
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
            
            Source source = new Source(body.getText());
            source.fullSequentialParse();

            ArrayList<String> tags = new ArrayList();

            for (StartTag tag : source.getAllStartTags()) {                
                tags.add(tag.getName());
            }
            
            
            HTMLEditorKit html_editor = (HTMLEditorKit) editor;

            System.out.println("Value: " + jSFontSize.getValue());

            StyleSheet sheet = html_editor.getStyleSheet();          
            
            String rule = "{ font-size: " + (jSFontSize.getValue()) + "pt; }";

            StringBuilder sb = new StringBuilder();

            for (String tag : tags) {
                sb.append(tag);
                sb.append(rule);
            }

            sheet.addRule(sb.toString());

            String text = body.getText();
            body.setDocument(html_editor.createDefaultDocument());
            body.setText(text);                       
            body.setCaretPosition(0);

        } else {                                             
           
            
            if( bodyText != null )
            {
                bodyText = bodyText.replaceAll("(\\\\fs)([0-9]+)", "$1" + ((jSFontSize.getValue())*2));
                System.out.println(bodyText);
                body.setText(bodyText);             
                body.setCaretPosition(0);
            }                    
        }
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
            if( message.getBodyRTF() == null )
                return;
            
            if( message.getBodyRTF().contains("\\fromhtml") )
            {
                AutoMBox am = new AutoMBox(MainWin.class.getName()) {

                    @Override
                    public void do_stuff() throws Exception {
                        body.setContentType("text/html");
                        logger.info("extracting HTML data from RTF Code");

                        if( logger.isTraceEnabled() )
                        {
                            logger.trace("\n" + StringUtils.addLineNumbers(message.getBodyRTF()));
                        }

                        bodyText = helper.extractHTMLFromRTF(message.getBodyRTF(),message);
                        
                        logger.trace(bodyText);
                        // System.out.println("\n\n");
                        body.setText(bodyText);
                    }
                };


                if( am.isFailed() )
                {
                    body.setContentType("text/rtf");
                    bodyText = message.getBodyRTF();
                    body.setText(bodyText);
                }
            }
            else if( message.getBodyRTF().contains("\\purehtml") )
            {
                body.setContentType("text/html");
                
                PrepareImages prep_images = new PrepareImages(helper.getTmpDir().getPath(), message);

                String html = prep_images.prepareImages(new StringBuilder(ViewerHelper.stripMetaTags(message.getBodyRTF()))).toString();                
                bodyText = html;
                body.setText(html);  
            }
            else
            {
                body.setContentType("text/rtf");
                bodyText = message.getBodyRTF();
                body.setText(message.getBodyRTF());
            }

            // System.out.println(message.getBodyRTF());
        }
        else
        {
            /*
            body.setContentType("text/plain");
            body.setText(message.getBodyText());

            if (JCBfix.isSelected()) {
                body.setFont(new java.awt.Font("Courier New", 0, 12));
            } else {
                body.setFont(new java.awt.Font("Dialog", 0, 12));
            }
             */

            StringBuilder sb = new StringBuilder();

            sb.append("<html><body>");

            String font = "Dialog,sans-serif;font-size:10px;";

            if (JCBfix.isSelected()) {
                font = "Courier New;font-size:10px;";
            }

            sb.append("<pre style=\"font-family:" + font + "\">");

            // System.out.println(message.getBodyText());

            String text = message.getBodyText();
/*
            try {
                CharacterSetConversion cs = new CharacterSetConversion(message);
                text = cs.getEncoded(message.getBodyText());
            } catch( UnsupportedEncodingException ex ) {
                logger.error(StringUtils.exceptionToString(ex));
            }
*/
            sb.append(helper.prepareText(text));

            sb.append("</pre>");
            sb.append("</body></html>");

            body.setContentType("text/html");
            bodyText = sb.toString();
            body.setText(bodyText);
        }

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
                    if (e.getEventType().equals(HyperlinkEvent.EventType.ENTERED)) {
                        lastUrl = e.getURL();
                    } else if (e.getEventType().equals(HyperlinkEvent.EventType.EXITED)) {
                        lastUrl = null;
                    }

                    return;
                }

                lastUrl = null;
        
                openUrl(e.getURL());
            }      
        };
    }   
    
    public void openUrl(URL url) throws IOException
    {               
        logger.info(url);

        final String protocoll = url.getProtocol();

        if( !protocoll.equals("file") )
        {
            if (Setup.is_win_system()) {
                logger.info("opening: " + url);

                ShellExec shell = new ShellExec();
                int ret = shell.execute(url.toString());
                logger.debug("shell exec returned: " + ret);
            } else {

                String open_command = helper.getOpenCommand();

                String command = open_command + " \"" + url.toString() + "\"";
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

        if( helper.is_mail_message(content.toString() ) ) {
            
            if( open_new_mail_handler != null )
            {
                open_new_mail_handler.openMail( root,content.toString() );
            }
            /*
            MainWin win = new MainWin(root,content.toString());
            
            if( !menubar.isVisible() )
                win.hideMenuBar();
            
            invokeDialog(win);
            */
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
        if (message == null) {
            return;
        }

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
                
        /*
        if( file.getName().toLowerCase().endsWith(".msg") )
        {
            jMNav.setEnabled(true);
        } else {
            jMNav.setEnabled(false);
        }*/
        
        message = parser_factory.parseMessage(file);        
        
        // last_path = file.getParentFile().getPath();

        final StringBuilder sb = new StringBuilder();

        // setTitle( MlM(root.getAppTitle()) + ": " + message.getSubject() );
       
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
            sb.append(parent.MlM("From:") + message.getFromName());
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
            sb.append(parent.MlM("Date"));
            sb.append(": ");
            sb.append(DateFormat.getDateTimeInstance().format(message.getDate()));
            sb.append("<br/>");
        }

        if( message.getToEmail() != null || message.getToName() != null )
        {
            sb.append(parent.MlM("To:"));
            sb.append(" ");
        }

        if( message.getToName() != null )
        {
            logger.info("toName: " + message.getToName());
            sb.append(message.getToName());
        }

        if( message.getToEmail() != null && message.getToEmail().contains("@") )
        {
            if( message.getToName() != null )
                sb.append(" [");

            sb.append(message.getToEmail());

            if( message.getToName() != null )
                sb.append("]");
        }

        sb.append("<br>");

        List<Attachment> attachments = message.getAttachments();

        final int max_width = Integer.parseInt(root.getSetup().getLocalConfig(AppConfigDefinitions.IconSize));
        final int max_height = max_width;

        for( Attachment att : attachments )
        {
            //System.out.println(att.getClass().getName());
            if( att instanceof FileAttachment)
            {
                final FileAttachment fatt = (FileAttachment) att;
                               
                String encoded_file_name = URLEncoder.encode(fatt.toString(),"utf-8");
                
                sb.append("<a href=\"file://");
                sb.append(encoded_file_name); 
                sb.append("\">");                         

                String mime_type = fatt.getMimeTag();

                logger.info("<a href=\"file://" + encoded_file_name + "\"> " + mime_type);


                if( mime_type != null && helper.is_image_mime_type(mime_type) && fatt.getSize() < 1024*1024*2 )
                {
                    File message_dir = helper.getTmpDir();

                    if( !message_dir.isDirectory() && !message_dir.mkdirs() )
                    {
                        logger.error( "Cannot create tmp dir: " + message_dir.getPath() );
                    }
                    else
                    {
                        final File content = new File(message_dir + "/" + fatt.toString());

                        if (!content.exists()) {
                            
                            wating_thread_pool_counter++;
                            
                            thread_pool.execute(new Runnable() {

                                @Override
                                public void run() {
                                    
                                    try {
                                        FileOutputStream fout = new FileOutputStream(content);
                                        fout.write(fatt.getData());
                                        fout.close();                                        
                                        
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
                                                                    PrepareImages.getFileName(fatt),
                                                                    max_width, max_height );
                                        
                                        File file_small = new File(content.getAbsolutePath() + "-small.jpg");
                                        
                                        BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),
                                                                BufferedImage.TYPE_INT_ARGB);                                        
                                        
                                        Graphics2D g2 = bi.createGraphics();
                                        g2.drawImage(icon.getImage(), 0, 0, null);
                                        g2.dispose();
                                        
                                        ImageIO.write(bi, "jpg", file_small);
                                        
                                    } catch( IOException ex ) {
                                        logger.error(ex,ex);
                                    }
                                    
                                    wating_thread_pool_counter--;
                                }
                            });                            
                            
                        }
/*
                        ImageIcon icon = ImageUtils.loadImageIcon(fatt.getData(), file_name);
                        Dimension dim = ImageUtils.getScaledSize(icon.getImage(), max_width, max_height, max_height);

                        String extra = "/";

                        if( Setup.is_win_system() )
                            extra = "";

                        sb.append("<img border=0 src=\"file:/" + extra + content.getAbsolutePath() + "\" width=\"" 
                                        + String.valueOf(dim.width) + "\" height=\""
                                        + String.valueOf(dim.height) + "\"  /> ");
                         * 
                         */

                        String extra = "/";

                        if( Setup.is_win_system() )
                            extra = "";
                        
                        System.out.println(extra + content.getAbsolutePath() + "-small.jpg" );
                        sb.append("<img border=0 src=\"file:/" + extra + content.getAbsolutePath() + "-small.jpg\"/> ");
                    }
                }
                
                if( helper.is_mail_message(fatt.getFilename(), fatt.getMimeTag() ) ) {
                    sb.append("<img border=0 align=\"baseline\" src=\"file:");
                    sb.append(helper.getMailIconName(helper.getTmpDir()));
                    sb.append("\"/>");
                }
                
                if( !fatt.getFilename().isEmpty() )
                    sb.append(fatt.getFilename());
                else
                    sb.append(fatt.toString());

                sb.append("</a> ");

            } else if( att instanceof MsgAttachment) {
                 
                MsgAttachment msgAtt = (MsgAttachment) att;
                final Message msg = msgAtt.getMessage();                                
                
                File message_dir = helper.getTmpDir();

                if( !message_dir.isDirectory() && !message_dir.mkdirs() )
                {
                        logger.error( "Cannot create tmp dir: " + message_dir.getPath() );
                }
                else
                {                
                    final String sub_file_name = message_dir + "/" + msg.hashCode() + ".mbox";
                    
                    thread_pool.execute(new Runnable() {

                        @Override
                        public void run() {
                            
                            new AutoMBox(file_name) {

                                @Override
                                public void do_stuff() throws Exception {
                                    
                                    MessageParserFactory factory = new MessageParserFactory();                                                                    
                                    factory.saveMessage(msg, new File(  sub_file_name ));
                                    
                                }
                            };                                                        
                        }
                        
                    });
                    
                                    
                    sb.append("<a href=\"file://");
                    sb.append(sub_file_name);
                    sb.append("\">");     
                
                    sb.append("<img border=0 align=\"baseline\" src=\"file:");
                    sb.append(helper.getMailIconName(helper.getTmpDir()));
                    sb.append("\"/>");
                    
                    sb.append(msg.getSubject());
                
                    sb.append("</a> &nbsp; ");
                }
                
                
            } else {
                logger.error("unknown Attachment: " + att.toString() + " " + att.getClass().getName() );
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
