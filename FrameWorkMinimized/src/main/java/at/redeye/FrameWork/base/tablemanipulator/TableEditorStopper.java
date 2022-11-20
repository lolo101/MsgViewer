package at.redeye.FrameWork.base.tablemanipulator;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class is a workaround for a bug in Java (4503845)
 * see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4503845">http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4503845</a>
 * Although the state appears as fixed, it does not yet work on 1.5
 * (see the comments at the end on the previous page)
 * <p>
 * The bug refers to tables with editing cells. When the user interacts
 * with other UI element -or other cell in the table-, the editor component
 * should receive a stopCellEditing call.
 * This does not happen if the user selects a UI element outside the table.
 * <p>
 * For example:
 * - A dialog contains a table and a Cancel button that, before closing,
 * verifies whether there are changes to save.
 * -The user is modifying one cell and then presses the Cancel button
 * -Then the cell editor does not receive an stopCellEditing call, so
 * there seems to be no changes to save!
 * <p>
 * A basic solution for this last example would be to call to
 * table.getCellEditor().stopCellEditing() when reacting to events on the
 * Cancel button, but this call should be made on every event handler for
 * each UI element!
 * <p>
 * This class works by listening to focus events when a cell editor is selected.
 * If the focus is transfered to any place outside the table, it calls to
 * stopCellEditing()
 * Why is it not enough to check the focus on just the editor component?
 * => Because the TableCellEditor could be composed of multiple Components
 */
public class TableEditorStopper extends FocusAdapter implements PropertyChangeListener
{
    private boolean paused;
    private Component focused;
    private final JTable table;

    public TableEditorStopper(JTable table)
    {
        this.table=table;
        table.addPropertyChangeListener("tableCellEditor", this);
    }

    /**
     * pause listening on property change event
     * call this function before removing a row
     */
    public void doPause()
    {
        paused = true;
    }

    void doContinue() {
        paused = false;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if (focused!=null)
        {
            focused.removeFocusListener(this);
        }
        focused = table.getEditorComponent();
        if (focused!=null)
        {
            focused.addFocusListener(this);
        }
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        if( paused )
            return;

        if (focused!=null)
        {
            focused.removeFocusListener(this);
            focused = e.getOppositeComponent();
            if (table == focused || table.isAncestorOf(focused)) {
                focused.addFocusListener(this);
            } else {
                focused = null;
                TableCellEditor editor = table.getCellEditor();
                if (editor != null) {
                    editor.stopCellEditing();
                }
            }
        }
    }
}

