package at.redeye.FrameWork.utilities;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.utilities.base64.Base64Coder;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class DesEncrypt {
    private static final byte[] salt = {
            (byte) 0x79, (byte) 0x73, (byte) 0xE3, (byte) 0x67, (byte) 0xE0, (byte) 0xB9, (byte) 0x31, (byte) 0x08
    };

    private Cipher encode;
    private Cipher decode;

    public DesEncrypt(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
        init(password, false);
    }

    public DesEncrypt(String password, boolean decode_only) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
        init(password, decode_only);
    }

    private void init(String password, boolean decode_only) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 19);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

        if (!decode_only)
            encode = Cipher.getInstance(key.getAlgorithm());

        decode = Cipher.getInstance(key.getAlgorithm());
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, 19);

        if (!decode_only)
            encode.init(Cipher.ENCRYPT_MODE, key, paramSpec);

        decode.init(Cipher.DECRYPT_MODE, key, paramSpec);
    }

    /**
     * Ciphers a text with the given password. The resulting cyphered text is base64 encoded.
     */
    public String encrypt(String str) throws IllegalBlockSizeException, BadPaddingException {
        if (str == null || encode == null) {
            return null;
        }

        byte[] utf8_str = str.getBytes(StandardCharsets.UTF_8);
        byte[] encoded = encode.doFinal(utf8_str);

        return new String(Base64Coder.encode(encoded));
    }

    /**
     * deciphers a text, if the wrong password was given a exception will be thrown
     *
     * @param str a ciphered, bas64 encoded text
     */
    public String decrypt(String str) throws IllegalBlockSizeException, BadPaddingException {
        if (str == null) {
            return null;
        }

        byte[] encoded = Base64Coder.decode(str);

        byte[] decoded_utf8_str = decode.doFinal(encoded);

        return new String(decoded_utf8_str, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        final String passwd = "AberHallo";
        final String text = "Ein Hund kam in die Küche";

        new AutoLogger(DesEncrypt.class.getName(), () -> {
            DesEncrypt en_cipher = new DesEncrypt(passwd);
            DesEncrypt de_cipher = new DesEncrypt(passwd);

            String encoded = en_cipher.encrypt(text);

            System.out.println(encoded);

            System.out.println(de_cipher.decrypt(encoded));
        });

    }
}
