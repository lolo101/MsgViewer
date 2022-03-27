package at.redeye.FrameWork.base.tablemanipulator;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class RowHeader
{
    static class RowHeaderRenderer extends JLabel implements ListCellRenderer<Integer> {

        final JTable table;
        final Font font;

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

            if (table.getRowCount() > 0)
                setText(String.format(" %d ", table.getRowCount()));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Integer> list, Integer value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            // weil sonst wird plötzlich die Schrift fett
            setFont(font);

            if (value == null)
                setText("");
            else
                setText(value + " ");

            return this;
        }

    }

    static class ListModel extends AbstractListModel<Integer> {
        final JTable table;

        public ListModel(JTable table) {
            this.table = table;
        }

        @Override
        public int getSize() {
            int count = table.getRowCount();

            if (count == 0)
                return 1;

            return count;
        }

        @Override
        public Integer getElementAt(int index) {
            return index + 1;
        }
    }

    final JTable table;
    RowHeaderRenderer header;
    JList<Integer> list;
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
            list = new JList<>(new ListModel(table));

            list.setOpaque(false);

            header = new RowHeaderRenderer(table);

            list.setCellRenderer(header);

            scroll.setRowHeaderView(list);

            JScrollBar scroll_bar = scroll.getVerticalScrollBar();

            if( scroll_bar != null )
            {
                scroll_bar.addComponentListener(new ComponentListener() {

                    @Override
                    public void componentResized(ComponentEvent e) {}

                    @Override
                    public void componentMoved(ComponentEvent e) {}

                    @Override
                    public void componentShown(ComponentEvent e) {
                        vertical_scroll_bar_visible = true;
                        visible_state_listener.run();
                    }

                    @Override
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
        // obriger code funktioniert eh sehr gut, aber
        // performanter ist dieser hier.
        if( list != null )
            list.setFixedCellHeight(height);
    }

    void setVisible(boolean state) {
        if (state == visible_state) {
            return;
        }

        visible_state = state;

        if (state) {
            scroll.setRowHeaderView(list);
        } else {
            scroll.setRowHeaderView(null);
        }

        updateUI();
    }

    boolean isScrollBarVisible()
    {
        return vertical_scroll_bar_visible;
    }
}
