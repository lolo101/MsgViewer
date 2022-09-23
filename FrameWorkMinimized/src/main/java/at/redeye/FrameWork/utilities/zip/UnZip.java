package at.redeye.FrameWork.utilities.zip;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip
{
    final static int BUFFER = 2048;

    public static void unzip(File target_dir, File zip_name) throws IOException {
        ZipInputStream z = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip_name.getAbsolutePath())));

        unzip(target_dir, z);

        z.close();
    }

    public static void unzip(File target_dir, String zip_file_name) throws IOException {
        unzip(target_dir, new File(zip_file_name));
    }

    private static void unzip(File target_dir, ZipInputStream z) throws IOException
    {
        ZipEntry entry;

        while( (entry = z.getNextEntry()) != null )
        {
            File file = new File(target_dir, entry.getName());
            if (!file.toPath().normalize().startsWith(target_dir.toPath().normalize())) {
                throw new IOException("Bad zip entry");
            }

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
        } catch (IOException ex) {
            Logger.getLogger(Zip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
