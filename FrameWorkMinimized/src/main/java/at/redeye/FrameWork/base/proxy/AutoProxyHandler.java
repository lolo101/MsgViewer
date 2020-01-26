/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.proxy;

import at.redeye.FrameWork.base.EncryptedDBPasswd;
import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.utilities.StringUtils;
import com.github.markusbernhardt.proxy.ProxySearch;
import com.github.markusbernhardt.proxy.selector.whitelist.UseProxyWhiteListSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Dialog.ModalityType;
import java.net.*;

/**
 *
 * @author martin
 */
public class AutoProxyHandler
{
    public static Logger logger = LogManager.getLogger(AutoProxyHandler.class);
    public static final String HTTP_PROXY_USER="HTTP_PROXY_USER";
    public static final String HTTP_PROXY_DOMAIN="HTTP_PROXY_DOMAIN";
    public static final String HTTP_PROXY_PASS="HTTP_PROXY_PASS";

    private String proxy_user;
    private String proxy_pass;

    private String saved_proxy_user;
    private String saved_proxy_pass;
    private String saved_proxy_domain;
    private String saved_proxy_pass_enc;

    Root root;

    ProxySearch proxySearch;


    public AutoProxyHandler(final Root root)
    {
        proxy_user = null;
        proxy_pass = null;
        this.root = root;

        ProxySelector.setDefault(null);

        root.addDllExtractorToCache(new ProxyVoleDLL() );

        long start = System.currentTimeMillis();

        logger.trace("X1 : " + (System.currentTimeMillis() - start ));

        if (!loadFromSettings()) {
            if ((proxySearch = haveProxyVole()) != null) {
                logger.info("proxy vole available");

                // ProxySearch.enableLogging();

                logger.trace("X11 : " + (System.currentTimeMillis() - start));
                ProxySelector myProxySelector = proxySearch.getProxySelector();

                logger.trace("XX111 : " + (System.currentTimeMillis() - start));
                ProxySelector.setDefault(myProxySelector);

                if( myProxySelector == null )
                {
                    //logger.info("no proxy found, disable proxy search automatically");
                    logger.info("no proxy found");
                    // root.getSetup().setLocalConfig(FrameWorkConfigDefinitions.ProxyEnabled.getConfigName(), "false");
                }
            } else {
                logger.info("proxy vole NOT available");
            }
        }

        logger.trace("X2 : " + (System.currentTimeMillis() - start ));

        if (ProxySelector.getDefault() != null) {
            if (!loadSavedPassword()) {
                if (detectUserAndPassFromEnv()) {
                    logger.info("detected user: " + proxy_user);
                } else {
                    logger.info("no proxy env detected");
                }
            }
        }

        logger.trace("Saved Passwords : " + (System.currentTimeMillis() - start ));

        Authenticator.setDefault(new Authenticator() {

             private String last_host;


             void checkWrongPass()
             {
                 if( last_host == null )
                     return;

                 if( proxy_user == null || proxy_pass == null )
                     return;

                 if( last_host.equals(getRequestingHost()))
                 {
                     proxy_user = null;
                     proxy_pass = null;
                 }
             }

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                if (getRequestorType() == RequestorType.PROXY) {

                    logger.info("proxy auth request");

                    if( saved_proxy_pass_enc != null && !saved_proxy_pass_enc.isEmpty() )
                    {
                        saved_proxy_pass = EncryptedDBPasswd.decryptDBPassword(saved_proxy_pass,root.getAppName());
                        saved_proxy_pass_enc = null;

                        if( !saved_proxy_pass.isEmpty() )
                            proxy_pass = saved_proxy_pass;
                    }

                    checkWrongPass();

                    last_host = getRequestingHost();

                    if( proxy_user != null && proxy_pass != null )
                    {
                        return new PasswordAuthentication(proxy_user, proxy_pass.toCharArray());

                    } else {

                        logger.info("here");

                        ProxyAuth auth = new ProxyAuth(root, saved_proxy_domain, saved_proxy_user, saved_proxy_pass);

                        logger.info("here");

                        auth.setModalityType(ModalityType.APPLICATION_MODAL);
                        auth.setModal(true);
                        auth.toFront();

                        auth.setVisible(true);

                        if( !auth.execOk() )
                        {
                            return super.getPasswordAuthentication();
                        }

                        logger.info("here");

                        String user = auth.getUserName();
                        String domain = auth.getDomain();
                        String pass = auth.getPassword();

                        if( auth.savePassword() )
                        {
                            savePassword(user,domain,pass);
                        }

                        if( !domain.isEmpty() )
                        {
                            proxy_user = domain + "\\" + user;
                        }
                        else
                        {
                            proxy_user = user;
                        }

                        proxy_pass = pass;

                        return new PasswordAuthentication(proxy_user, proxy_pass.toCharArray());
                    }

                } else {
                    return super.getPasswordAuthentication();
                }
            }
        });

        logger.trace("X3 Authentificator : " + (System.currentTimeMillis() - start ));
    }

    public static ProxySearch haveProxyVole()
    {
        try {
            long start = System.currentTimeMillis();
            ProxySearch proxySearch = ProxySearch.getDefaultProxySearch();
            logger.trace("                       waited for proxy search "
                    + (System.currentTimeMillis() - start));

            return proxySearch;

        } catch ( NoClassDefFoundError ex ) {
            return null;
        }
    }

    private boolean detectUserAndPassFromEnv()
    {
        String env = System.getenv("http_proxy");

        if( env == null )
        {
            logger.info("no env is set");
            return false;
        }

        URL proxy_url;

        try {
            proxy_url = new URL( env );
        } catch( MalformedURLException ex ) {
            logger.error("malformed url: " + StringUtils.exceptionToString(ex));
            return false;
        }

        String auth = proxy_url.getAuthority();

        if( auth == null )
            return false;

        int index = auth.indexOf('@');

        if( index < 0 )
            return false;

        auth = auth.substring(0,index);

        String[] sl = auth.split(":");

        if( sl.length != 2 )
            return false;

        proxy_user = sl[0];
        proxy_pass = sl[1];

        return true;
    }

    private void savePassword(String user, String domain, String pass)
    {
        root.getSetup().setLocalConfig(HTTP_PROXY_DOMAIN, domain);
        root.getSetup().setLocalConfig(HTTP_PROXY_USER, user);
        root.getSetup().setLocalConfig(HTTP_PROXY_PASS, EncryptedDBPasswd.encryptDBPassword(pass, root.getAppName()));
        root.saveSetup();
    }

    private boolean loadSavedPassword()
    {
        saved_proxy_domain = root.getSetup().getLocalConfig(HTTP_PROXY_DOMAIN,"");
        saved_proxy_user = root.getSetup().getLocalConfig(HTTP_PROXY_USER,"");
        saved_proxy_pass = root.getSetup().getLocalConfig(HTTP_PROXY_PASS,"");

        if( !saved_proxy_user.isEmpty() &&
            !saved_proxy_pass.isEmpty() )
        {
            saved_proxy_pass_enc = saved_proxy_pass;
        }

        if( !saved_proxy_user.isEmpty() &&
            !saved_proxy_domain.isEmpty() )
        {
            proxy_user = saved_proxy_domain + "\\" + saved_proxy_user;
        }

        if( !saved_proxy_pass.isEmpty() )
            proxy_pass = saved_proxy_pass;

        if( !saved_proxy_user.isEmpty() ||
            !saved_proxy_pass.isEmpty() )
            return true;

        return false;
    }

    public void exludeFromProxy( String url )
    {
        ProxySelector sel = ProxySelector.getDefault();

        if( sel != null )
        {
            if( proxySearch != null )
            {
                new UseProxyWhiteListSelector(url, sel );
            }
            else
            {
                if( sel instanceof SimpleProxySelector )
                {
                    SimpleProxySelector simple_selector = (SimpleProxySelector) sel;
                    simple_selector.exludeFromProxy(url);
                }
            }
        }
    }

    private boolean loadFromSettings()
    {
        if (!StringUtils.isYes(root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.ProxyEnabled))) {
            logger.info("proxy disabled at all");
            return true;
        }

        if (StringUtils.isYes(root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.ProxyAutoDetect))) {
            logger.info("proxy autodetection enabled");
            return false;
        }

        String proxy_host = root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.ProxyHost);

        proxy_host = proxy_host.trim();

        if( proxy_host.isEmpty() )
            return false;

        int proxy_port = 8080;

        try
        {
            proxy_port = Integer.parseInt(root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.ProxyPort));

        } catch( NumberFormatException ex ) {
            logger.error(StringUtils.exceptionToString(ex));
            return false;
        }

        SimpleProxySelector sel = new SimpleProxySelector(proxy_host, proxy_port);

        ProxySelector.setDefault(sel);

         String proxy_disabled_for = root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.ProxyDisabledFor);

         if( proxy_disabled_for != null && !proxy_disabled_for.isEmpty() )
         {
             sel.exludeFromProxy(proxy_disabled_for);
         }

        return true;
    }

}
