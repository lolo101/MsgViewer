/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.proxy;

import at.redeye.FrameWork.utilities.StringUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author martin
 */
public class SimpleProxySelector extends ProxySelector
{
    public static Logger logger = Logger.getLogger(SimpleProxySelector.class.getName());
    private static ArrayList<Proxy> no_proxy_list;

    public static class WhiteListEntry
    {
        public String entry;
        public boolean is_ip_address;

        public WhiteListEntry(String white_list_url_entry )
        {
            this.entry = white_list_url_entry.trim();

            is_ip_address = entry.matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+");

            if( entry.contains("*") )
            {
                entry = entry.replace(".", "\\.");
                entry = entry.replace("*", ".*");
            }
        }
    };

    ArrayList<Proxy> proxy_list = new ArrayList<Proxy>();
    ArrayList<WhiteListEntry> white_list = new ArrayList<WhiteListEntry>();

    public SimpleProxySelector( String host, int port )
    {
        super();
        
        Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));        
        proxy_list.add(proxy);

        if( no_proxy_list == null )
        {
            no_proxy_list = new ArrayList<Proxy>();
            no_proxy_list.add(Proxy.NO_PROXY);
        }

    }

    @Override
    public List<Proxy> select(URI uri) {

        logger.info("request for downloading: " + uri);

        try {

            throw new Exception("test");
        } catch( Exception ex ) {
            String res = StringUtils.exceptionToString(ex);

            if( res.contains("org.apache.commons.httpclient.HttpMethodDirector") )
                return no_proxy_list;
        }

        String suri = uri.toString();

        // Urls kommen meißtens mit einen / am Schluss daher

        if( suri.endsWith("/") )
            suri = suri.substring(0,suri.length()-1);

        // dann kommt auch noch ein zweiter request, der dann so aussieht:
        // socket://sis.salomon.at:80

        if( suri.matches(".*:[0-9]+$") )
        {
            int i = suri.lastIndexOf(':');
            suri = suri.substring(0,i);
        }        

        logger.info("suri: " + suri);

        for (WhiteListEntry white_host : white_list)
        {
            if (white_host.entry.equals(suri))
            {
                logger.debug("not using proxy because this host is on the white list");
                return no_proxy_list;
            }            
            else if (!white_host.is_ip_address )
            {
                try
                {
                    if( suri.matches(white_host.entry) )
                    {
                       logger.debug("not using proxy because this host is on the white list");
                       return no_proxy_list;
                    }
                } catch( Exception ex ) {
                    logger.error("regex: failed when matching: " + suri);
                    logger.error( StringUtils.exceptionToString(ex) );
                }
            }
        }

        return proxy_list;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe)
    {
        
    }

    public void exludeFromProxy( String url )
    {
        String urls[] = url.split("[,; ]");

        for( String peace : urls )
        {
            white_list.add(new WhiteListEntry(peace));
        }
    }

}
