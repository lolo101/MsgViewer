package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.DesEncrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EncryptedDBPasswd {
    protected static Logger logger = LogManager.getLogger(EncryptedDBPasswd.class);

    public static String encryptDBPassword(final String DBPasswd, final String password) {
        return new AutoLogger<>(EncryptedDBPasswd.class.getName(),
                () -> new DesEncrypt(password).encrypt(DBPasswd)
        ).resultOrElse(null);
    }

    public static String decryptDBPassword(final String DBPasswd, final String password) {
        if ((DBPasswd.length() % 4) == 0) {
            return new AutoLogger<>(EncryptedDBPasswd.class.getName(),
                    () -> new DesEncrypt(password).decrypt(DBPasswd)
            ).resultOrElse(null);
        }
        return null;
    }
}
