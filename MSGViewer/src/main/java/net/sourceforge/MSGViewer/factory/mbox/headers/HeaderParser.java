package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.Message;

public abstract class HeaderParser
{
    private final String header;

    HeaderParser(String header)
        {
            this.header = header;
        }

    public String getHeader()
        {
            return header;
        }

    public abstract void parse(Message message, String line) throws Exception;
}
