/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.tablemanipulator;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

/**
 *
 * @author martin
 */
public class RowHeader
{
    static class RowHeaderRenderer extends JLabel implements ListCellRenderer {

        JTable table;
        Font font;        

        RowHeaderRenderer(JTable table) {
            this.table = table;
            JTableHeader header = table.getTableHeader();
            setOpaque(true);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setHorizontalAlignment(RIGHT);
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            font = header.getFont();
            setFont(font);

            if( table.getRowCount() > 0 )
                setText(String.format(" %d ",table.getRowCount()));
        }

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            // weil sonst wird plötzlich die Schrift fett
            setFont(font);           

            if( value == null )
                setText( "" );
            else
                setText( value.toString() + " " );
            
            return this;
        }

        private void setPreferedHeight(int height)
        {
            if( table.getRowCount() > 0 )
            {
                setText(String.format(" %d ",table.getRowCount()));
            }

            Dimension dim = getPreferredSize();
            dim.height = height;

            if( dim.width == 0 )
                dim.width = 50;

            setPreferredSize(dim);            
        }
    }

    static class ListModel extends AbstractListModel
    {
        JTable table;

        public ListModel(JTable table)
        {
            this.table = table;
        }

        public int getSize()
        {
            int count = table.getRowCount();

            if( count == 0 )
                return 1;

            return count;
        }

        public Object getElementAt(int index)
        {
            return (Integer)index+1;
        }
    }

    JTable table;
    RowHeaderRenderer header;
    JList list;
    JScrollPane scroll;
    boolean visible_state = true;
    boolean vertical_scroll_bar_visible = false;
    Runnable visible_state_listener;

    public RowHeader( JTable table, final Runnable visible_state_listener )
    {
        this.table = table;
        this.visible_state_listener = visible_state_listener;

         scroll = null;

        for( Container cont = table.getParent(); cont != null; cont = cont.getParent() )
        {
            if( cont instanceof JScrollPane )
            {
                scroll = (JScrollPane) cont;
                break;
            }
        }

        if( scroll != null )
        {
            list = new JList(new ListModel(table));

            list.setOpaque(false);

            header = new RowHeaderRenderer(table);

            list.setCellRenderer(header);
            //rowHeader.setFixedCellWidth(20);

            scroll.setRowHeaderView(list);

            JScrollBar scroll_bar = scroll.getVerticalScrollBar();

            if( scroll_bar != null )
            {
                scroll_bar.addComponentListener(new ComponentListener() {

                    public void componentResized(ComponentEvent e) {}
                    public void componentMoved(ComponentEvent e) {}

                    public void componentShown(ComponentEvent e) {
                        vertical_scroll_bar_visible = true;
                        visible_state_listener.run();
                    }

                    public void componentHidden(ComponentEvent e) {
                        vertical_scroll_bar_visible = false;
                        visible_state_listener.run();
                    }
                });
            }
        }
    }

    public void updateUI()
    {
        list.updateUI();
        scroll.updateUI();
    }

    public void setCellHeight( int height )
    {
        /*
        if( header != null )
            header.setPreferedHeight( height );
         */
        // obriger code funktioniert eh sehr gut, aber
        // performanter ist dieser hier.
        if( list != null )
            list.setFixedCellHeight(height);
    }

    void setVisible(boolean state)
    {
        if( state == visible_state ) {
            return;
        }

        visible_state = state;

        if( state == false )
        {
            scroll.setRowHeaderView(null);
        }
        else
        {
             scroll.setRowHeaderView(list);
        }

        updateUI();
    }

    boolean isScrollBarVisible()
    {
        return vertical_scroll_bar_visible;
    }
}
