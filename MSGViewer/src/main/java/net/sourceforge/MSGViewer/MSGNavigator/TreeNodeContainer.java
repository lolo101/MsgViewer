package net.sourceforge.MSGViewer.MSGNavigator;

import org.apache.poi.poifs.filesystem.Entry;

import javax.swing.tree.DefaultMutableTreeNode;

public final class TreeNodeContainer extends DefaultMutableTreeNode {
    private final Entry entry;
    private final Object data;

    public TreeNodeContainer(Entry entry, String name, Object data) {
        super(name);
        this.entry = entry;
        this.data = data;
    }

    public Entry getEntry() {
        return entry;
    }

    public Object getData() {
        return data;
    }
}
