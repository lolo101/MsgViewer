package at.redeye.FrameWork.base.proxy;

import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SimpleProxySelector extends ProxySelector
{
    private static final Pattern HOST_AND_PORT = Pattern.compile(".*:[0-9]+$");
    public static Logger logger = LogManager.getLogger(SimpleProxySelector.class);
    private static final ArrayList<Proxy> no_proxy_list = new ArrayList<>(List.of(Proxy.NO_PROXY));

    public static class WhiteListEntry
    {
        private static final Pattern IP_ADDRESS = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+");
        public String entry;
        public boolean is_ip_address;

        public WhiteListEntry(String white_list_url_entry )
        {
            this.entry = white_list_url_entry.trim();

            is_ip_address = IP_ADDRESS.matcher(entry).matches();

            if( entry.contains("*") )
            {
                entry = entry.replace(".", "\\.");
                entry = entry.replace("*", ".*");
            }
        }
    }

    ArrayList<Proxy> proxy_list = new ArrayList<>();
    ArrayList<WhiteListEntry> white_list = new ArrayList<>();

    public SimpleProxySelector( String host, int port )
    {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));
        proxy_list.add(proxy);
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

        if (HOST_AND_PORT.matcher(suri).matches()) {
            int i = suri.lastIndexOf(':');
            suri = suri.substring(0, i);
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
        String[] urls = url.split("[,; ]");

        for( String peace : urls )
        {
            white_list.add(new WhiteListEntry(peace));
        }
    }

}
