/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author martin
 */
public class CopyFile
{
     protected static Logger logger = LogManager.getLogger(CopyFile.class);


    public static boolean copy( File from, File to )
    {
        if( !from.exists() )
            return false;

        try
        {
            FileInputStream fin = new FileInputStream( from );

            FileOutputStream fout = new FileOutputStream(to);

            byte[] buf =  new byte[1024*4];

            for (int len; (len = fin.read(buf)) > 0;) {
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
