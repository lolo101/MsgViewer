/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.DesEncrypt;
import at.redeye.FrameWork.utilities.StringUtils;
import javax.crypto.IllegalBlockSizeException;
import org.apache.log4j.Logger;

/**
 *
 * @author martin
 */
public class EncryptedDBPasswd
{
   protected static Logger logger = Logger.getLogger(EncryptedDBPasswd.class.getName());

   DesEncrypt cipher;

   public EncryptedDBPasswd( String password )
   {
       try {
        cipher = new DesEncrypt(password,true);
       } catch ( Exception ex ) {
           logger.error(StringUtils.exceptionToString(ex));
       }
   }

   public boolean isValid()
   {
       return cipher != null;
   }

   public String tryDecryptDBPassword(String DBPasswd )
   {
       if( DBPasswd.isEmpty() || !isValid() )
           return DBPasswd;

       if( (DBPasswd.length() % 4) != 0 )
           return DBPasswd;

       String str;

       try {
        str = cipher.decrypt(DBPasswd);

       } catch( IllegalBlockSizeException ex ) {
           return DBPasswd;
       } catch( IllegalArgumentException ex ) {
           return DBPasswd;
       } catch ( Exception ex) {
           logger.error(StringUtils.exceptionToString(ex));
           return DBPasswd;
       }

       if( str == null )
           return DBPasswd;

       return str;
   }
   

   public static String encryptDBPassword( final String DBPasswd, final String password )
   {
       final StringBuffer buf = new StringBuffer();

       AutoLogger al = new AutoLogger(EncryptedDBPasswd.class.getName())
       {
           public void do_stuff() throws Exception
           {
                DesEncrypt cipher = new DesEncrypt( password );
                String str = cipher.encrypt( DBPasswd );

                buf.append(str);
           }
       };

       if( al.isFailed() )
           return null;

       return buf.toString();
   }

   public static String decryptDBPassword(final String DBPasswd, final String password )
   {
       /** mit % 8 getht nicht, weil hin und wieder kann das doch ein verschlüsselter Wert sein. */
       if( (DBPasswd.length() % 4) != 0 )
           return null; // sicher kein Base64 encodeter String
       try {
           DesEncrypt cipher = new DesEncrypt(password);
           String str = cipher.decrypt(DBPasswd);

           return str;
       } catch( IllegalBlockSizeException ex ) {
           return null;
       } catch( IllegalArgumentException ex ) {
           return null;
       } catch (Exception ex) {
           logger.error(StringUtils.exceptionToString(ex));
           return null;
       }
   }

   public static String tryDecryptDBPassword(String DBPasswd, String password )
   {
       if( DBPasswd.isEmpty() )
           return DBPasswd;

       String res = decryptDBPassword(DBPasswd, password);

       if( res == null )
           return DBPasswd;

       return res;
   }

}
