package net.sourceforge.MSGViewer.MSGNavigator;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.utilities.StringUtils;
import net.sourceforge.MSGViewer.factory.msg.TopLevelPropertyStream;
import com.auxilii.msgparser.FieldInformation;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author martin
 */
public class MSGNavigator extends BaseDialog {

    public static final String SETTING_SHOW_SIZE = "MSG_NAVIGATOR_SHOW_SIZE";
    public static final String SETTING_AUTOSAVE = "MSG_SETTING_AUTOSAVE";

    static final Map<String,String> props = new HashMap<>();
    private POIFSFileSystem fs;
    private final File file;

    private boolean setting_show_size = false;

    static {
        props.put("001a", "Message Class");
        props.put("1035", "MessageId");
        props.put("0037", "Subject");
        props.put("0c1f", "FromEmail");
        props.put("0042", "FromName");
        props.put("0076", "ToEmail");
        props.put("3001", "ToName");
        props.put("0e04", "PidTagDisplayTo");
        props.put("0e03", "DisplayCc");
        props.put("0e02", "DisplayBcc");
        props.put("1000", "BodyText");
        props.put("1009", "RTFBodyText");
        props.put("007d", "Headers");
        props.put("0044", "recv name");
        props.put("004d", "author");
        props.put("0050", "reply");
        props.put("005a", "sender");
        props.put("0065", "sent email");
        props.put("0076", "received email");
        props.put("0078", "repr. email");
        props.put("0c1a", "sender name");
        props.put("0e1d", "subject normalized");
        props.put("1046", "sender email");
        props.put("3003", "email address");
        props.put("1008", "rtf sync");

        // attachment stuff
        props.put("3701", "attachment data");
        props.put("3704", "file name");
        props.put("3707", "long Filen name");
        props.put("370e", "mime tag");
        props.put("3703", "extension");

        // added by mobby
        props.put("0041", "PidTagSentRepresentingEntryId"); //  Identifies an address book EntryID.
        props.put("0c19", "PidTagSenderEntryId"); // Identifies an address book EntryID that contains the sending mailbox owner's address book EntryID.
        props.put("0c1e", "PidTagSenderAddressType"); //  Contains the sending mailbox owner's e-mail address type.
        props.put("0064", "PidTagSentRepresentingAddressType"); //  Contains an e-mail address type.
        props.put("0070", "PidTagConversationTopic"); //   Contains an unchanging copy of the original subject.
        props.put("300b", "PidTagSearchKey"); //  Contains a unique binary-comparable key that identifies an object for a search.
        props.put("0002", "<b>GUID Stream</b>");
        props.put("0003", "<b>Entry Stream</b>");
        props.put("0004", "<b>String Stream</b>");
        props.put("3007", "PidTagCreationTime");
        props.put("3008", "PidTagLastModificationTime");
        props.put("0ff7", "PidTagAccessLevel");
        props.put("0ff4", "PidTagAccess");
        props.put("340d", "PidTagStoreSupportMask");
        props.put("0039", "PidTagClientSubmitTime");
        props.put("0e1f", "PidTagRtfInSync");
        props.put("003b", "PidTagSentRepresentingSearchKey");
        props.put("003f", "PidTagReceivedByEntryId");
        props.put("0051", "PidTagReceivedBySearchKey");
        props.put("0070", "PidTagConversationTopic");
        props.put("0071", "PidTagConversationIndex");
        props.put("0075", "PidTagReceivedByAddressType");
        props.put("0c1d", "PidTagSenderSearchKey");
        props.put("0e06", "PidTagMessageDeliveryTime");
        props.put("0e07", "PidTagMessageFlags");
        props.put("0e28", "PidTagPrimarySendAccount");
        props.put("0e29", "PidTagNextSendAcct");
        props.put("3fde", "PidTagInternetCodepage");
        props.put("003d", "PidTagSubjectPrefix");
        props.put("0c15", "PidTagRecipientType");
        props.put("0fff", "PidTagEntryId");
        props.put("3002", "PidTagAddressType");
        props.put("0ff6", "PidTagInstanceKey");
        props.put("3000", "PidTagRowid");
        props.put("3712", "PidTagAttachContentId");
        props.put("8007", "PidLidContactItemData");
        props.put("", "");
        props.put("", "");
        props.put("", "");
    }


     public static class TreeNodeContainer extends DefaultMutableTreeNode
     {
         private final Entry entry;
         private Object data;

         public TreeNodeContainer( Entry entry, String name  )
         {
             super( name );
             this.entry = entry;
         }

         public Entry getEntry() {
             return entry;
         }

         public Object getData() {
             return data;
         }

         public void setData( Object data ) {
             this.data = data;
         }

     }

     public static class PropertyContainer extends TreeNodeContainer
     {
         private final String tag;

         public PropertyContainer( Entry entry, String name, String property_tag )
         {
             super( entry, name );
             this.tag = property_tag;
         }

         public String getPropertyTag()
         {
             return tag;
         }
     }



    /** Creates new form MSGNavigator */
    public MSGNavigator(Root root, File file)
    {
        super( root, root.MlM("Navigate:") + " " + file.getName());

        initComponents();

        this.file = file;


        /*
        props.put("8008", "X-MimeOLE");
        props.put("8005", "X-GMX-AntiVirus");
        props.put("8004", "X-GMX-AntiSpam");
        props.put("8006", "X-Mailer");
        props.put("8009", "X-Provags-ID");
        props.put("8007", "X-Authenticated");
        */


        reload();
    }

    final void reload() {

        setting_show_size = StringUtils.isYes(root.getSetup().getLocalConfig(SETTING_SHOW_SIZE,"true") );

         EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new AutoMBox(MSGNavigator.class.getName()) {

                    @Override
                    public void do_stuff() throws Exception {
                        parse(file);
                    }
                };

            }
        });
    }

    void parse( File file ) throws FileNotFoundException, IOException
    {
        InputStream in = new FileInputStream(file);

        fs = new POIFSFileSystem(in);
        DirectoryNode fs_root = fs.getRoot();

        DefaultMutableTreeNode localRoot = new DefaultMutableTreeNode(fs_root.getShortDescription());

        parse( localRoot, fs_root );

        tree.setModel(new DefaultTreeModel(localRoot));
    }

    private void parse(DefaultMutableTreeNode root_node, DirectoryEntry fs_root) throws IOException
    {
        for (Iterator<?> iter = fs_root.getEntries(); iter.hasNext(); )
        {
            Entry entry = (Entry) iter.next();

            if (entry.isDirectoryEntry()) {
                DirectoryEntry de = (DirectoryEntry) entry;

                String name = "<html><body><b style=\"color: blue\">" +  entry.getName() + "</b></body></html>";

                DefaultMutableTreeNode node = new TreeNodeContainer(de,name);
                root_node.add(node);

                parse( node, de );
            } else if( entry.isDocumentEntry() ) {

                DocumentEntry de = (DocumentEntry) entry;

                FieldInformation info = analyzeDocumentEntry(de);

                // the data is accessed by getting an input stream
                // for the given document entry
                DocumentInputStream dstream = new DocumentInputStream(de);

                if( de.getName().equals("__properties_version1.0") )
                {
                    info.setType("0102");
                }

                // create a Java object from the data provided
                // by the input stream. depending on the field
                // information, either a String or a byte[] will
                // be returned. other datatypes are not yet supported
                Object data = this.getData(dstream, info);

                StringBuilder sb = new StringBuilder();

                sb.append("<html><body>");

                if( entry.getName().startsWith("__properties") ) {
                    sb.append("<b>");
                    sb.append(entry.getName());
                    sb.append("</b>");
                } else {
                    // sb.append("<b>");
                    sb.append(entry.getName());
                    // sb.append("</b>");
                }

                sb.append("&nbsp;&nbsp;&nbsp;");


                String property_name =  props.get(info.getClazz());

                if( property_name != null ) {
                    sb.append(" <code style=\"color: green\">").append(property_name).append("</code> ");
                }

                if( setting_show_size ) {
                    sb.append(" ").append(de.getSize()).append("b ");
                }

                if( data instanceof String )
                {
                    String s = (String)data;
                    if( s.length() > 100 ) {
                        s = s.substring(0,100) + "...";
                    }

                     sb.append("     <i>").append(s).append("</i>");
                } else if( data instanceof byte[] ) {
                    byte bytes[] = (byte[]) data;
                    sb.append("     <i style=\"color: dd5555\">byte array</i>");
                }

                sb.append("</body></html>");

                TreeNodeContainer node = new TreeNodeContainer(entry,sb.toString());
                node.setData(data);

                if( de.getName().equals("__properties_version1.0") )
                {
                    try {
                        PropertyParser pp = new PropertyParser(de);
                        for (String tag : pp.getPropertyTags()) {
                            TreeNodeContainer pnode = new PropertyContainer(entry,
                                        "<html><body><pre style=\"font-family: monospace; fonz-size:8px\">"  + tag + "</pre></body></html>",
                                        tag);
                            node.add(pnode);
                        }
                    } catch (IOException ex) {
                        logger.error(ex, ex);
                    }
                }

                root_node.add(node);
            }
        }
    }




    /**
     * Reads the information from the InputStream and
     * creates, based on the information in the
     * {@link FieldInformation} object, either a String
     * or a byte[] (e.g., for attachments) Object
     * containing this data.
     *
     * @param dstream The InputStream of the Document Entry.
     * @param info The field information that is needed to
     *  determine the data type of the input stream.
     * @return The String/byte[] object representing
     *  the data.
     * @throws IOException Thrown if the .msg file could not
     *  be parsed.
     * @throws UnsupportedOperationException Thrown if
     *  the .msg file contains unknown data.
     */
    protected Object getData(DocumentInputStream dstream, FieldInformation info) throws IOException {
        // if there is no field information available, we simply
        // return null. in that case, we're not interested in the
        // data anyway
        if (info == null || FieldInformation.UNKNOWN.equals(info.getType())) {
            return null;
        }
        // if the type is 001e (we know it is lower case
        // because analyzeDocumentEntry stores the type in
        // lower case), we create a String object from the data.
        // the encoding of the binary data is most probably
        // ISO-8859-1 (not pure ASCII).
        if (info.getType().equals("001e")) {
            // we put the complete data into a byte[] object...
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int read; (read = dstream.read(buffer)) > 0;) {
                baos.write(buffer, 0, read);
            }
            // ...and create a String object from it
/*
            byte bytes[] = baos.toByteArray();

            for( int i = 0; i < bytes.length; i++ )
            {
            System.out.print(String.format("%d %c,", (int)bytes[i], (char)bytes[i]));
            }
            System.out.println();
             */
            String text = new String(baos.toByteArray(), "ISO-8859-1");
            return text;
        } else if (info.getType().equals("001f")) {
            // Unicode encoding with lowbyte followed by hibyte
            // Note: this is arcane guesswork, but it works
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int read; (read = dstream.read(buffer)) > 0;) {
                baos.write(buffer, 0, read);
            }
            byte[] bytes = baos.toByteArray();

            String text = new String(bytes, "UTF-16LE");
            return text;
        } else if (info.getType().equals("0102")) {
            // the data is read into a byte[] object
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int read; (read = dstream.read(buffer)) > 0;) {
                baos.write(buffer, 0, read);
            }
            return baos.toByteArray();
        } else if (info.getType().equals("0000")) {
            // the data is read into a byte[] object
            byte[] buffer = new byte[1024];

            StringBuilder sb = new StringBuilder();

            for (int read; (read = dstream.read(buffer)) > 0;) {
                for (int i = 0; i < read; i++) {
                    sb.append(buffer[i]);
                    sb.append(" ");
                }
            }
            return sb.toString();
        }

        // this should not happen
        logger.trace("Unknown field type " + info.getType());
        return null;
    }

    protected FieldInformation analyzeDocumentEntry(DocumentEntry de) {
        String name = de.getName();
        // we are only interested in document entries
        // with names starting with __substg1.
        logger.trace("Document entry: " + name);
        String key = "__substg1.";
        if (name.startsWith(key)) {
            String clazz = FieldInformation.UNKNOWN;
            String type = FieldInformation.UNKNOWN;
            try {
                String val = name.substring(key.length() + 2).toLowerCase();
                // the first 4 digits of the remainder
                // defines the field class (or field name)
                // and the last 4 digits indicate the
                // data type.
                clazz = val.substring(0, 4);
                type = val.substring(4);
                logger.trace("  Found document entry: class=" + clazz + ", type=" + type + " size: " + de.getSize());
            } catch (RuntimeException re) {
                logger.trace("Could not parse directory entry " + name + " " + re);
            }
            return new FieldInformation(clazz, type);
        } else {
            logger.trace("Ignoring entry with name " + name + " size: " + de.getSize());
        }

        // we are not interested in the field
    	// and return an empty FieldInformation object
    	return new FieldInformation();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(250, 250));

        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void treeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeMouseClicked

        if( evt.getButton() == MouseEvent.BUTTON3 )
        {

            TreePath path = tree.getPathForLocation(evt.getX(), evt.getY());

            if( path == null ) {
                // retry it with lower X value
                for( int x = evt.getX(); x > 0 && path == null; x -= 10 ) {
                    path = tree.getPathForLocation(x, evt.getY());
                }
            }

            if (path != null) {
                tree.setSelectionPath(path);
            }


            JPopupMenu popup = new NavActionPopup(this);

            popup.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_treeMouseClicked

    public void deleteSelectedElement()
    {
        new AutoMBox(this.getClass().getName()) {

            @Override
            public void do_stuff() throws Exception {
                TreePath path = tree.getSelectionPath();

                if (path == null) {
                    return;
                }

                TreeNodeContainer cont = (TreeNodeContainer) path.getLastPathComponent();

                logger.info("deleting : " + cont.getEntry().getName());

                TopLevelPropertyStream tops = new TopLevelPropertyStream(fs.getRoot());
                tops.delete(cont.getEntry());

                cont.removeFromParent();
                tree.updateUI();

                if( StringUtils.isYes(root.getSetup().getLocalConfig(SETTING_AUTOSAVE,"false")) )
                {
                    save();
                    reload();
                }
            }
        };
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables

    void save() throws FileNotFoundException, IOException
    {
       if( fs == null ) {
           return;
       }

        try (OutputStream out = new FileOutputStream(file)) {
            fs.writeFilesystem(out);
        }
    }

    @Override
    public void close()
    {
        root.getSetup().setLocalConfig(SETTING_SHOW_SIZE, String.valueOf(setting_show_size));
        super.close();
    }

    void showSelectedElement()
    {
        TreePath path = tree.getSelectionPath();

        if( path == null ) {
            return;
        }

        TreeNodeContainer cont = (TreeNodeContainer) path.getLastPathComponent();

        invokeDialog(new ShowNode(root, cont, props));
    }


    void editSelectedElement() {
        TreePath path = tree.getSelectionPath();

        if( path == null ) {
            return;
        }

        TreeNodeContainer cont = (TreeNodeContainer) path.getLastPathComponent();

        invokeDialog(new EditNode(root, this, cont));
    }

    void edited() throws FileNotFoundException, IOException {

        if (StringUtils.isYes(root.getSetup().getLocalConfig(SETTING_AUTOSAVE, "false"))) {
            save();
            reload();
        }
    }

}
