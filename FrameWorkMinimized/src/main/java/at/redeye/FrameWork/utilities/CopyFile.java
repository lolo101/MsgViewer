/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author martin
 */
public class CopyFile
{
     protected static Logger logger = Logger.getLogger(CopyFile.class.getName());


    public static boolean copy( File from, File to )
    {
        if( !from.exists() )
            return false;

        try
        {
            FileInputStream fin = new FileInputStream( from );

            FileOutputStream fout = new FileOutputStream(to);

            int len = 0;
            byte[] buf =  new byte[1024*4];

            while ((len = fin.read(buf)) > 0) {
                fout.write(buf, 0, len);
            }

            fin.close();
            fout.close();

            return true;

        } catch( IOException ex ) {
            logger.error(ex);
        }

        return false;
    }

}
