package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.DesEncrypt;
import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.IllegalBlockSizeException;

public class EncryptedDBPasswd {
    protected static Logger logger = LogManager.getLogger(EncryptedDBPasswd.class);

    DesEncrypt cipher;

    public EncryptedDBPasswd(String password) {
        try {
            cipher = new DesEncrypt(password, true);
        } catch (Exception ex) {
            logger.error(StringUtils.exceptionToString(ex));
        }
    }

    public boolean isValid() {
        return cipher != null;
    }

    public static String encryptDBPassword(final String DBPasswd, final String password) {
        final StringBuffer buf = new StringBuffer();

        AutoLogger al = new AutoLogger(EncryptedDBPasswd.class.getName(), () -> {
            DesEncrypt cipher = new DesEncrypt(password);
            String str = cipher.encrypt(DBPasswd);

            buf.append(str);
        });

        if (al.isFailed())
            return null;

        return buf.toString();
    }

    public static String decryptDBPassword(final String DBPasswd, final String password) {
        if ((DBPasswd.length() % 4) != 0)
            return null; // sicher kein Base64 encodeter String
        try {
            DesEncrypt cipher = new DesEncrypt(password);

            return cipher.decrypt(DBPasswd);
        } catch (IllegalBlockSizeException | IllegalArgumentException ex) {
            return null;
        } catch (Exception ex) {
            logger.error(StringUtils.exceptionToString(ex));
            return null;
        }
    }
}
