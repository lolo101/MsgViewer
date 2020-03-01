package net.sourceforge.MSGViewer.MSGNavigator;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.utilities.StringUtils;
import com.auxilii.msgparser.FieldInformation;
import org.apache.poi.poifs.filesystem.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MSGNavigator extends BaseDialog {

    public static final String SETTING_SHOW_SIZE = "MSG_NAVIGATOR_SHOW_SIZE";
    public static final String SETTING_AUTOSAVE = "MSG_SETTING_AUTOSAVE";
    public static final String PROPERTIES_ENTRY = "__properties_version1.0";
    public static final String SUBSTORAGE_PREFIX = "__substg1.0_";

    private POIFSFileSystem fs;
    private final File file;

    private boolean setting_show_size = false;

    public static class TreeNodeContainer extends DefaultMutableTreeNode {
        private final Entry entry;
        private Object data;

        public TreeNodeContainer(Entry entry, String name) {
            super(name);
            this.entry = entry;
        }

        public Entry getEntry() {
            return entry;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

    }

    /**
     * Creates new form MSGNavigator
     */
    public MSGNavigator(Root root, File file) {
        super(root, root.MlM("Navigate:") + " " + file.getName());

        initComponents();

        this.file = file;

        reload();
    }

    final void reload() {

        setting_show_size = StringUtils.isYes(root.getSetup().getLocalConfig(SETTING_SHOW_SIZE, "true"));

        EventQueue.invokeLater(() -> new AutoMBox(MSGNavigator.class.getName()) {
            @Override
            public void do_stuff() throws Exception {
                parse(file);
            }
        });
    }

    void parse(File file) throws IOException {
        try (InputStream in = new FileInputStream(file)) {
            fs = new POIFSFileSystem(in);
            DirectoryNode fs_root = fs.getRoot();

            DefaultMutableTreeNode localRoot = new DefaultMutableTreeNode(fs_root.getShortDescription());

            parse(localRoot, fs_root);

            tree.setModel(new DefaultTreeModel(localRoot));
        }
    }

    private void parse(DefaultMutableTreeNode root_node, DirectoryEntry fs_root) throws IOException {
        for (Entry entry : fs_root) {
            root_node.add(toNode(entry));
        }
    }

    private MutableTreeNode toNode(Entry entry) throws IOException {
        if (entry.isDirectoryEntry()) {
            return parseDirectory((DirectoryEntry) entry);
        }
        DocumentEntry de = (DocumentEntry) entry;
        if (de.getName().startsWith(SUBSTORAGE_PREFIX)) {
            return parseSubStrorage(de);
        }
        if (de.getName().equals(PROPERTIES_ENTRY)) {
            return parseProperties(de);
        }
        return new DefaultMutableTreeNode(de.getName());
    }

    private MutableTreeNode parseDirectory(DirectoryEntry entry) throws IOException {
        DefaultMutableTreeNode node = new TreeNodeContainer(entry,
                "<html><body><b style=\"color: blue\">" + entry.getName() + "</b></body></html>");
        parse(node, entry);
        return node;
    }

    private MutableTreeNode parseSubStrorage(DocumentEntry de) throws IOException {
        try (DocumentInputStream dstream = new DocumentInputStream(de)) {
            FieldInformation info = analyzeSubStorage(de);

            StringBuilder sb = new StringBuilder()
                    .append("<html><body>")
                    .append(de.getName())
                    .append("&nbsp;&nbsp;&nbsp;")
                    .append("<code style=\"color: green\">")
                    .append(info.getId())
                    .append("</code> ");

            if (setting_show_size) {
                sb.append(" ").append(de.getSize()).append("b ");
            }

            Object data = getData(dstream, info);
            if (data instanceof String) {
                String s = (String) data;
                if (s.length() > 100) {
                    s = s.substring(0, 100) + "...";
                }

                sb.append("     <i>").append(s).append("</i>");
            }
            if (data instanceof byte[]) {
                sb.append("     <i style=\"color: dd5555\">byte array</i>");
            }
            sb.append("</body></html>");
            TreeNodeContainer node = new TreeNodeContainer(de, sb.toString());
            node.setData(data);
            return node;
        }
    }

    private static MutableTreeNode parseProperties(DocumentEntry de) throws IOException {
        TreeNodeContainer node = new TreeNodeContainer(de, "<html><body><b>" + de.getName() + "</b></body></html>");
        PropertyParser pp = new PropertyParser(de);
        for (String tag : pp.getPropertyTags()) {
            TreeNodeContainer pnode = new TreeNodeContainer(de,
                    "<html><body><pre style=\"font-family: monospace; fonz-size:8px\">" + tag + "</pre></body></html>");
            node.add(pnode);
        }
        return node;
    }

    public static FieldInformation analyzeSubStorage(DocumentEntry de) {
        String name = de.getName();
        String val = name.substring(SUBSTORAGE_PREFIX.length()).toLowerCase();
        String tag = val.substring(0, 4);
        String type = val.substring(4, 8);
        return new FieldInformation(tag, type);
    }

    private static Object getData(DocumentInputStream dstream, FieldInformation info) throws IOException {
        switch (info.getType()) {
            case PtypString8:
                return new String(read(dstream), StandardCharsets.ISO_8859_1);
            case PtypString:
                return new String(read(dstream), StandardCharsets.UTF_16LE);
            case PtypBinary:
                return read(dstream);
        }
        logger.warn("Unsupported field type " + info.getType());
        return null;
    }

    private static byte[] read(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int read; (read = stream.read(buffer)) > 0; ) {
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(250, 250));

        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(tree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void treeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeMouseClicked

        if (evt.getButton() == MouseEvent.BUTTON3) {

            TreePath path = tree.getPathForLocation(evt.getX(), evt.getY());

            if (path == null) {
                // retry it with lower X value
                for (int x = evt.getX(); x > 0 && path == null; x -= 10) {
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

    public void deleteSelectedElement() {
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

                if (StringUtils.isYes(root.getSetup().getLocalConfig(SETTING_AUTOSAVE, "false"))) {
                    save();
                    reload();
                }
            }
        };
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables

    void save() throws IOException {
        if (fs == null) {
            return;
        }

        try (OutputStream out = new FileOutputStream(file)) {
            fs.writeFilesystem(out);
        }
    }

    @Override
    public void close() {
        root.getSetup().setLocalConfig(SETTING_SHOW_SIZE, String.valueOf(setting_show_size));
        super.close();
    }

    void showSelectedElement() {
        TreePath path = tree.getSelectionPath();

        if (path == null) {
            return;
        }

        TreeNodeContainer cont = (TreeNodeContainer) path.getLastPathComponent();

        invokeDialog(new ShowNode(root, cont));
    }


    void editSelectedElement() {
        TreePath path = tree.getSelectionPath();

        if (path == null) {
            return;
        }

        TreeNodeContainer cont = (TreeNodeContainer) path.getLastPathComponent();

        invokeDialog(new EditNode(root, this, cont));
    }

    void edited() throws IOException {

        if (StringUtils.isYes(root.getSetup().getLocalConfig(SETTING_AUTOSAVE, "false"))) {
            save();
            reload();
        }
    }

}
