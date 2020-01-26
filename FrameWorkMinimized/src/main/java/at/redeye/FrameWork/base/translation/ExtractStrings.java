/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.translation;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author martin
 */
public class ExtractStrings
{
    static private class TabTitleWrapper extends JComponent
    {
        JTabbedPane parent;
        int index;

        public TabTitleWrapper( JTabbedPane parent, int index )
        {
            this.index = index;
            this.parent = parent;
        }

        public String getText()
        {
            return parent.getTitleAt(index);
        }

        public void setText( String text)
        {
            parent.setTitleAt(index, text);
        }
    }

    static private class ToolTipWrapper extends JComponent
    {
        JComponent parent;

        public ToolTipWrapper( JComponent parent )
        {
            this.parent = parent;
        }

        public String getText()
        {
            return parent.getToolTipText();
        }

        public void setText( String text)
        {
            parent.setToolTipText(text);
        }
    }

    static private class FrameTitleWrapper extends JComponent
    {
        TitledBorder border;

        public FrameTitleWrapper( JComponent parent )
        {
            if( parent.getBorder() instanceof TitledBorder )
            {
                border = (TitledBorder) parent.getBorder();
            }
        }

        public String getText()
        {
            return border.getTitle();
        }

        public void setText( String text)
        {
           border.setTitle(text);
        }
    }

    TreeSet<String> strings;
    HashMap<String,List<JComponent>> components;

    public ExtractStrings( Container cont )
    {
        strings = new TreeSet<>();
        components = new HashMap<>();

        extractStrings(cont);
    }

    public TreeSet<String> getStrings()
    {
        return strings;
    }

    public HashMap<String,List<JComponent>>  getComponents()
    {
        return components;
    }

    private void extractStrings( Container cont )
    {
        for( Component comp : cont.getComponents() )
        {
//            System.out.println("com:" + comp);

            if( comp instanceof JComponent )
            {
                JComponent jcomp = (JComponent) comp;

                String text = jcomp.getToolTipText();

                if( text != null && !text.isEmpty() )
                {
                    strings.add(text);
                    addComp( text, new ToolTipWrapper(jcomp));
                }

                Border border = jcomp.getBorder();

                if(border instanceof TitledBorder)
                {
                    TitledBorder tborder = (TitledBorder) border;

                    text = tborder.getTitle();

                    if( text != null && !text.isEmpty() )
                    {
                        strings.add(text);
                        addComp( text, new FrameTitleWrapper(jcomp));
                    }
                }
            }

            if( comp instanceof JLabel )
                addString((JLabel)comp);
            else if( comp instanceof JButton )
                addString((JButton)comp);
            else if( comp instanceof JMenu )
                addString((JMenu)comp);
            else if( comp instanceof JMenuItem )
                addString((JMenuItem)comp);
            else if( comp instanceof JRadioButtonMenuItem )
                addString((JRadioButtonMenuItem)comp);
            else if( comp instanceof JCheckBoxMenuItem )
                addString((JCheckBoxMenuItem)comp);
            else if( comp instanceof JCheckBox )
                addString((JCheckBox)comp);
            else if( comp instanceof JRadioButton )
                addString((JRadioButton)comp);
            else if( comp instanceof JTabbedPane )
            {
                addString((JTabbedPane)comp);

                try {
                    extractStrings((Container) comp);
                } catch (Exception ex) {
                }
            }
            else
            {
                try {
                    extractStrings((Container) comp);
                } catch (Exception ex) {
                }
            }
        }
    }

    private void addComp( String text, JComponent comp )
    {
        List<JComponent> vcomp = components.computeIfAbsent(text, k -> new ArrayList<JComponent>());

        vcomp.add(comp);
    }

    private void addString( JLabel label )
    {
        strings.add(label.getText());

        addComp(label.getText(),label);
    }

    private void addString( JCheckBox box )
    {
        strings.add(box.getText());

        addComp(box.getText(),box);
    }

    private void addString( JRadioButton button )
    {
        strings.add(button.getText());

        addComp(button.getText(),button);
    }

    private void addString( JButton button )
    {
        if( button.getText().isEmpty() )
            return;

        strings.add(button.getText());

        addComp(button.getText(),button);
    }

    private void addString(JMenu menu) {

        if( menu.getText().isEmpty() )
            return;

        strings.add(menu.getText());

        addComp(menu.getText(),menu);

        extractStrings(menu.getPopupMenu());
    }

    private void addString(JMenuItem menu_item) {

        if( menu_item.getText().isEmpty() )
            return;

        strings.add(menu_item.getText());

        addComp(menu_item.getText(),menu_item);
    }

    public static void assign(JComponent comp, String value) {

        if( comp instanceof JButton )
            ((JButton)comp).setText(value);
        else if( comp instanceof JLabel )
            ((JLabel)comp).setText(value);
        else if( comp instanceof JMenuItem )
            ((JMenuItem)comp).setText(value);
        else if( comp instanceof JMenu )
            ((JMenu)comp).setText(value);
        else if( comp instanceof JRadioButton )
            ((JRadioButton)comp).setText(value);
        else if( comp instanceof JCheckBox )
            ((JCheckBox)comp).setText(value);
        else if( comp instanceof TabTitleWrapper )
            ((TabTitleWrapper)comp).setText(value);
        else if( comp instanceof ToolTipWrapper )
            ((ToolTipWrapper)comp).setText(value);
        else if( comp instanceof FrameTitleWrapper )
            ((FrameTitleWrapper)comp).setText(value);
    }

    private void addString(JTabbedPane jTabbedPane)
    {
        for( int i = 0; i < jTabbedPane.getTabCount(); i++ )
        {
            TabTitleWrapper wrapper = new TabTitleWrapper(jTabbedPane, i );
            strings.add(wrapper.getText());
            addComp(wrapper.getText(),wrapper);
        }
    }
}
