/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author martin
 */
public class DownloadUrl
{
    protected static Logger logger = LogManager.getLogger(DownloadUrl.class);

    URL from;
    File to;

    public DownloadUrl( URL url, File file )
    {
        from = url;
        to = file;
    }

    public DownloadUrl(URL url) {
        from = url;
    }

    public boolean download()
    {
        File file = null;
        OutputStream out = null;
        BufferedInputStream bis = null;
        InputStream stream = null;

        boolean failed = true;

        try
        {
            file = File.createTempFile( to.getName(), ".part" );

            stream = from.openStream();

            out = new FileOutputStream(file);

            bis = new BufferedInputStream( stream );

            byte[] buf = new byte[1024 * 4];
            int len;

            while ((len = bis.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            bis.close();

            File old_one = to;

            boolean success = true;

            if( old_one.exists() )
            {
              if( !old_one.delete() )
              {
                  logger.error("cannot delete file " + to.toString() );
                  success = false;
              }
            }

            if( success )
            {
                // umbenennen über verschiedene Verzeichnisse hinweg funktioniert anscheinend eh nie
                /*
                if( !file.renameTo(old_one) )
                {
                    logger.error("renaming from " + file.getAbsolutePath() + " to " + to + " failed!");
                    logger.error("trying copying");
                */
                    if( !CopyFile.copy(file, old_one) )
                    {
                        logger.error("Cannot copy file");
                        success = false;
                    }

                    file.delete();
                //}
            }

            if( success )
                failed = false;

        } catch( IOException ex ) {

            logger.error(ex);

        } finally {
            try {
                if( stream != null )
                    stream.close();

                if( out != null )
                    out.close();

                if( bis != null )
                    bis.close();
            } catch( IOException ex ) {
                logger.error(ex);
            }
        }

        return !failed;
    }

    public static boolean downloadUrl( String url, String target )
    {
        URL from = null;

        try
        {
             from = new URL( url );

        } catch( MalformedURLException ex ) {
            System.err.println(ex);
            logger.error(ex);

            return false;
        }

        DownloadUrl durl = new DownloadUrl( from, new File(target) );

        return durl.download();
    }

   public static String downloadUrl(URL url)
   {
        DownloadUrl durl = new DownloadUrl( url );

        StringBuffer buf = new StringBuffer();

        if( durl.download(buf) )
            return buf.toString();

        return null;
   }

   public boolean download(StringBuffer buffer) {
           return download(buffer, null);
   }

    public boolean download(StringBuffer buffer, String encoding) {
        BufferedReader bis = null;
        InputStream stream = null;

        boolean failed = true;

        try {
            stream = from.openStream();

            if( encoding != null )
            {
                bis = new BufferedReader(new InputStreamReader(stream, encoding));
            }
            else
            {
                bis = new BufferedReader(new InputStreamReader(stream));
            }

            int len;

            String line;

            while( (line = bis.readLine()) != null )
            {
                buffer.append(line);
                buffer.append("\n");
            }

            failed = false;

        } catch (IOException ex) {

            logger.error(ex);

        } finally {
            try {
                if( stream != null )
                    stream.close();

                if( bis != null )
                    bis.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }

        return !failed;
    }
}
