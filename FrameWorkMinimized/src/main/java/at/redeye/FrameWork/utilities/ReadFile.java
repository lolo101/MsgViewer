/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author martin
 */


/**
 * This program reads a text file line by line and print to the console. It uses
 * FileOutputStream to read the file.
 *
 */
public class ReadFile
{

public static String read_file_string(String file_name)
  {

    File file = new File(file_name);
    FileReader fis = null;
    BufferedReader bis = null;

    StringBuilder res = new StringBuilder();

    try {
      fis = new FileReader(file);

      // Here BufferedInputStream is added for fast reading.
      bis = new BufferedReader(fis);

      int len = 10;

      char[] buff = new char[(int)Math.max(len,file.length())];

      // dis.available() returns 0 if the file does not have more lines.
      while (bis.ready()) {
        len = bis.read(buff);

        res.append(buff,0,len);
        //System.out.println(dis.readLine());
      }

      // dispose all the resources after using them.
      fis.close();
      bis.close();

    } catch (FileNotFoundException e) {
      System.out.println("File Not Found: " + file_name);
    } catch (IOException e) {
      System.out.println(e);
      // e.printStackTrace();
    }

    return res.toString();
  }

  public static StringBuilder read_file_builder(String file_name)
  {

    File file = new File(file_name);
    FileReader fis = null;
    BufferedReader bis = null;

    StringBuilder res = new StringBuilder();

    try {
      fis = new FileReader(file);

      // Here BufferedInputStream is added for fast reading.
      bis = new BufferedReader(fis);

      int len = 10;

      char[] buff = new char[(int)Math.max(len,file.length())];

      // dis.available() returns 0 if the file does not have more lines.
      while (bis.ready()) {
        len = bis.read(buff);

        res.append(buff, 0, len);
        //System.out.println(dis.readLine());
      }

      // dispose all the resources after using them.
      fis.close();
      bis.close();

    } catch (FileNotFoundException e) {
      System.out.println("File Not Found: " + file_name);
    } catch (IOException e) {
      System.out.println(e);
      // e.printStackTrace();
    }

    return res;
  }

  public static String read_file(String file_name)
  {
    return read_file_string( file_name );
  }

  public static byte[] getBytpesFromFile( String file_name ) throws IOException
  {
      File file = new File( file_name );

      byte[] bytes = null;

      bytes = getBytesFromFile(file);

      return bytes;
  }

      // Returns the contents of the file in a byte array.
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static byte[] getBytesResource(Class loader, String resource) throws IOException {

        InputStream stream = loader.getResourceAsStream(resource);

        if (stream == null) {
            System.out.println("Failed loading resource:" + resource);
            return null;
        }

        ByteBuffer out_buffer = ByteBuffer.allocate(10);

        int c;
        byte[] buffer = new byte[5];

        while ((c = stream.read(buffer)) != -1) {

            // enlarge if required
            while( out_buffer.remaining() < c )
            {
                ByteBuffer out2_buffer = ByteBuffer.allocate(out_buffer.capacity()*2);
                out2_buffer.put(out_buffer.array());
                out_buffer = out2_buffer;
            }

            out_buffer.put(buffer, 0, c);
        }

        stream.close();

        return out_buffer.array();
    }

}