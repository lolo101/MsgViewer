package at.redeye.FrameWork.utilities;

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

    private final Cipher encode;
    private final Cipher decode;

    public DesEncrypt(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
        this(password, false);
    }

    private DesEncrypt(String password, boolean decode_only) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 19);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, 19);

        decode = Cipher.getInstance(key.getAlgorithm());
        decode.init(Cipher.DECRYPT_MODE, key, paramSpec);

        if (!decode_only) {
            encode = Cipher.getInstance(key.getAlgorithm());
            encode.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        } else {
            encode = null;
        }
    }

    /**
     * Ciphers a text with the given password. The resulting ciphered text is base64 encoded.
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
     * deciphers a text, if the wrong password was given an exception will be thrown
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
}
