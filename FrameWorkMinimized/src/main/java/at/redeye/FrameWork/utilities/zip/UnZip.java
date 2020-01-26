/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author martin
 */
public class UnZip
{
    final static int BUFFER = 2048;

    public static void unzip( File target_dir, File zip_name ) throws FileNotFoundException, IOException
    {
        ZipInputStream z = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip_name.getAbsolutePath())));

        unzip( target_dir, z);

        z.close();
    }

    public static void unzip(File target_dir, String zip_file_name) throws FileNotFoundException, IOException
    {
        unzip( target_dir, new File(zip_file_name) );
    }

    private static void unzip(File target_dir, ZipInputStream z) throws IOException
    {
        ZipEntry entry;

        while( (entry = z.getNextEntry()) != null )
        {
            //System.out.println("Extracting: " +entry);

            String save_entry = entry.getName().replace("..", "" );

            File file = new File( target_dir + File.separator + entry.getName() );

            // System.out.println("Extracting: " + file + " at " + file.getParent() );

            File parent = file.getParentFile();

            if( !parent.exists() )
                parent.mkdirs();

            byte[] buffer = new byte[BUFFER];
            int count;
            FileOutputStream fos = new FileOutputStream( file );
            BufferedOutputStream dest = new BufferedOutputStream( fos, BUFFER );

            while ((count = z.read(buffer, 0, BUFFER))
              != -1) {
               dest.write(buffer, 0, count);
            }
            dest.flush();
            dest.close();
        }
    }

    public static void main(String[] argv)
    {
        try {
            unzip(new File("/home/martin/klo"), new File( "/home/martin/ooo3.zip"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Zip.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Zip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
