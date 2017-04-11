/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.Message;

/**
 *
 * @author martin
 */
public class SubjectHeader extends HeaderParser
{
    public SubjectHeader()
    {
        super("Subject");
    }

    @Override
    public void parse(Message message, String line) {
        message.setSubject(line);
    }
    
}
