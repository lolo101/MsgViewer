package at.redeye.FrameWork.utilities;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



/**
 * @author Mario Mattl
 *
 */
public class MD5Calc {
	
	private MessageDigest md5;
	private byte[] digest;

	public MD5Calc() {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
	}

	public MD5Calc(String algorithm) {
		try {
			md5 = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
	}

	public static String toHexString(byte b) {
		int value = (b & 0x7F) + (b < 0 ? 128 : 0);

		String ret = (value < 16 ? "0" : "");
		ret += Integer.toHexString(value).toUpperCase();

		return ret;
	}

	public String calcChecksum(String data) {
		
		StringBuffer strbuf = new StringBuffer();

		md5.update(data.getBytes(), 0, data.length());
		digest = md5.digest();

		for (int i = 0; i < digest.length; i++) {
			strbuf.append(toHexString(digest[i]));
		}

		return strbuf.toString();
	}
	
	public String calcChecksum(byte [] data) {
		StringBuffer strbuf = new StringBuffer();

		md5.update(data, 0, data.length);
		digest = md5.digest();

		for (int i = 0; i < digest.length; i++) {
			strbuf.append(toHexString(digest[i]));
		}

		return strbuf.toString();
	}

        /**
         * converts byte values to a hey string
         * @param data
         * @return
         */
        public static String bytes2hex( byte [] data )
        {
            StringBuffer strbuf = new StringBuffer();

            for (int i = 0; i < data.length; i++) {
                strbuf.append(toHexString(data[i]));
            }

            return strbuf.toString();
        }

        /**
         * Calcs Checksum for a file
         * @param file
         * @return MD5 String Hex coded
         */
    public String calcCheckSum(File file) throws FileNotFoundException, IOException {

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

        byte[] buffer = new byte[2048];

        int numRead;
        do {
            numRead = in.read(buffer);
            if (numRead > 0) {
                md5.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        in.close();

        return bytes2hex(md5.digest() ).toLowerCase();
    }


    public static void main( String argv[] )
    {
        MD5Calc calc = new MD5Calc("MD5");

            try
            {
                System.out.println("md5: " + calc.calcCheckSum(new File("/home/martin/hmcp.zip")) );
            } catch( IOException ex ) {
                System.out.println(ex);
            }
        
    }
}
