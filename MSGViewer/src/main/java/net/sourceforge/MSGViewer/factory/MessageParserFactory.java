/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory;

import net.sourceforge.MSGViewer.factory.mbox.EMLWriterViaJavaMail;
import net.sourceforge.MSGViewer.factory.mbox.JavaMailParser;
import net.sourceforge.MSGViewer.factory.mbox.MBoxParser;
import net.sourceforge.MSGViewer.factory.mbox.MBoxWriterViaJavaMail;
import net.sourceforge.MSGViewer.factory.msg.MsgWriter;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author martin
 */
public class MessageParserFactory 
{
    MsgParser msg_parser;
    MBoxParser mbox_parser;
    JavaMailParser jmail_parser;
    MBoxWriterViaJavaMail mbox_writer;
    EMLWriterViaJavaMail eml_writer;
    MsgWriter msg_writer;
    
    public  Message parseMessage( File file) throws FileNotFoundException, IOException, Exception
    {
        int idx = file.getName().lastIndexOf(".");
        
        if( idx < 0 )
            throw new FileNotFoundException("Cannot identify file type");
        
        String suffix = file.getName().substring(idx+1).toLowerCase();
        
        if( suffix.equals("msg") )
        {
            return parseMsgFile( file );
        } else if( suffix.equals("mbox") ) {
            return paserJavaMailFile( file );
        } else if( suffix.equals("eml") ) {
            return paserJavaMailFile( file );
        }
         
        return null;
    }    
    
    private Message parseMsgFile( File file ) throws IOException 
    {
        if( msg_parser == null )
            msg_parser = new MsgParser();
        
        return msg_parser.parseMsg(file);
    }

    private Message paserMBoxFile(File file) throws IOException, Exception {
        
        if( mbox_parser == null )
            mbox_parser = new MBoxParser();
        
        return mbox_parser.parse(file);
    }
    
    private Message paserJavaMailFile(File file) throws IOException, Exception {
        
        if( jmail_parser == null )
            jmail_parser = new JavaMailParser();
        
        return jmail_parser.parse(file);
    }    
    
    public void saveMessage( Message msg, File file ) throws FileNotFoundException, Exception
    {
        int idx = file.getName().lastIndexOf(".");
        
        if( idx < 0 )
            throw new FileNotFoundException("Cannot identify file type");
        
        String suffix = file.getName().substring(idx+1).toLowerCase();
        
        if( suffix.equals("msg") )
        {
            saveMsgFile( msg, file );
        } else if( suffix.equals("mbox") ) {
            saveMBoxFile( msg, file );
        } else if( suffix.equals("eml") ) {
            saveEMLFile( msg, file );
        }
                 
    }

    private void saveMsgFile(Message msg, File file) throws FileNotFoundException, IOException {
        if( msg_writer == null )
            msg_writer = new MsgWriter();
        
        msg_writer.write(msg, new FileOutputStream(file));       
    }

    private void saveMBoxFile(Message msg, File file) throws Exception
    {
        if( mbox_writer == null )
            mbox_writer = new MBoxWriterViaJavaMail();
        
        try {
            mbox_writer.write(msg, new FileOutputStream(file));
        } catch( Exception ex ) {
            mbox_writer.close();
            throw ex;
        }
    }
    
    private void saveEMLFile(Message msg, File file) throws Exception
    {
        if( eml_writer == null )
            eml_writer = new EMLWriterViaJavaMail();
        
        try {
            eml_writer.write(msg, new FileOutputStream(file));
        } catch( Exception ex ) {
            eml_writer.close();
            throw ex;
        }
    }    
}
