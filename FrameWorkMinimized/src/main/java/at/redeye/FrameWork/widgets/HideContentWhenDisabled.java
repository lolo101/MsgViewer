/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets;

import javax.swing.JTextField;

/**
 * TextField that does'nt shows the text when setEditabl(false) is called.
 * @author martin
 */
public class HideContentWhenDisabled extends JTextField
{
    String content;

    @Override
    public void setEditable( boolean state )
    {
        if( state == false )
        {
            content = getText();
            super.setText("");
        } else {
            if( content != null )
                super.setText(content);
        }

        super.setEditable(state);
    }

    @Override
    public void setText( String text )
    {
        content = text;
        super.setText(text);
    }
}
