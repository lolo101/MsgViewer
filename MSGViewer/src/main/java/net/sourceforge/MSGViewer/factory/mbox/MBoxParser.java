/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.mbox;

import net.sourceforge.MSGViewer.factory.mbox.headers.HeaderParser;
import net.sourceforge.MSGViewer.factory.mbox.headers.FromEmailHeader;
import net.sourceforge.MSGViewer.factory.mbox.headers.SubjectHeader;
import net.sourceforge.MSGViewer.factory.mbox.headers.DateHeader;
import net.sourceforge.MSGViewer.factory.mbox.headers.ToEmailHeader;
import net.sourceforge.MSGViewer.factory.mbox.headers.MessageIdHeader;
import at.redeye.FrameWork.utilities.ReadFile;
import com.auxilii.msgparser.Message;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author martin
 */
public class MBoxParser 
{        
    List<HeaderParser> header_parsers = new ArrayList();
    
    public MBoxParser()
    {
        header_parsers.add(new FromEmailHeader());
        header_parsers.add(new ToEmailHeader());
        header_parsers.add(new DateHeader());
        header_parsers.add(new SubjectHeader());
        header_parsers.add(new MessageIdHeader());
    }
    
    public Message parse( File file ) throws IOException, Exception
    {
        byte bytes[] = ReadFile.getBytesFromFile(file);
        
        String content = new String(bytes,"ASCII");           
        
        return parse(content);
    }    
    
    public Message parse( String content ) throws Exception
    {
        Message msg = new Message();
        
        int idx_unix = content.indexOf("\n\n");
        int idx_win = content.indexOf("\r\n\r\n");
        
        String header = null;
        
        int start = 0;
        
        if( content.startsWith("From ") )       
            start = content.indexOf("\n");  
        
        int header_end = 0;
        
        if( idx_unix > 0 )
            header_end = idx_unix;
        else if ( idx_win > 0 )
            header_end = idx_win;
        
        header =  content.substring(start,header_end);
        
        msg.setHeaders(header);
        
        parseHeader(msg, header);
        parseBody( msg, content.substring(header_end+1));
        
        return msg;
    }

    private void parseHeader(Message msg, String header) throws Exception 
    {
       String lines[] = header.split("\n");
       
       for( String line : lines )
       {
           for( HeaderParser parser : header_parsers )
           {
               String header_prefix = parser.getHeader() + ": ";
                if( line.startsWith(header_prefix) )
                {
                   String header_content = line.substring(header_prefix.length());
                   parser.parse(msg, header_content);
                }
           }
       }
    }

    private void parseBody(Message msg, String body) 
    {
       msg.setBodyText(body);        
       msg.setBodyRTF("");
                    
    }
    
    
    
}
