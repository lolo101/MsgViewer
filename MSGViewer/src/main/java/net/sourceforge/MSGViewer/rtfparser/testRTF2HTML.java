/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer.rtfparser;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.BaseModuleLauncher;
import at.redeye.FrameWork.base.LocalRoot;
import at.redeye.FrameWork.utilities.ReadFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;

/**
 *
 * @author martin
 */
public class testRTF2HTML extends BaseModuleLauncher
{
    public testRTF2HTML( String args[] )
    {
        super(args);

        root = new LocalRoot( "MSGViewer", "MSGViewer");

        configureLogging();
    }

    public void run()
    {
        for( String arg : args )
        {
            if( arg.toLowerCase().endsWith(".rtf") )
            {
                final File file = new File( arg );

                if( !file.exists() )
                {
                    logger.error("Cannot open file " + file);
                    continue;
                }

                final String content = ReadFile.read_file(arg);

                System.out.println(content);

                new AutoLogger(testRTF2HTML.class.getName()) {

                    @Override
                    public void do_stuff() throws Exception {

                        RTFParser parser = new RTFParser(new FileInputStream(file));

                        String message = null;

                        Exception exc = null;
                        Error err = null;

                        try {

                            StringBuilder sb = new StringBuilder();

                            parser.parse();
                            logger.info("done parsing " + file);

                            List<RTFGroup> groups = parser.getGroups();

                            for( RTFGroup group : groups )
                            {
                                /*
                                for( String cmd : group.getCommands() )
                                {
                                    System.out.println("cmd: " + cmd);

                                }*/

                                if( !group.isEmptyText() )
                                {
                                    String content = group.getTextContent();

                                    System.out.print(content);
                                    sb.append(content);
                                }
                            }

                            String file_name = getStartupParam("writehtml");

                            if( file_name != null )
                            {
                                FileWriter fout = new FileWriter(file_name);
                                fout.write(sb.toString());
                                fout.close();
                            }

                        } catch ( Error ex ) {
                            message = ex.getMessage();                            
                            err = ex;
                        } catch ( Exception ex ) {
                            message = ex.getMessage();
                            exc = ex;
                        }

                        if( message != null)
                        {
                            int start = message.indexOf("at line");
                            int start_col = message.indexOf("column ");

                            if( start >= 0 )
                            {
                                int end =  message.indexOf(",",start);
                                int end_col =  message.indexOf(".",start_col);

                                if( end >= 0 )
                                {
                                    System.out.println(message);

                                    int line = Integer.parseInt(message.substring(start+8,end));
                                    int col = 0;

                                    if( end_col > 0 )
                                        col = Integer.parseInt(message.substring(start_col+7,end_col));
                                    else
                                        col = Integer.parseInt(message.substring(start_col+7));

                                    String lines[] = content.split("\n");

                                    String ll = lines[line-1];

                                    if( ll.length() > 100 )
                                    {
                                        int send = col + 60;
                                        if( ll.length() < send )
                                            send = ll.length() -1;

                                        ll = ll.substring(col - 40, send);
                                        col = 40;
                                    }

                                    StringBuilder sb = new StringBuilder();

                                    for( int i = 0; i < col -1; i++ )
                                        sb.append(' ');
                                    sb.append("^");

                                    logger.error("\n\n" + ll + "\n" + sb.toString());
                                }
                            }
                        }

                        if (err != null || exc != null)
                        {
                            if (err != null) {
                                throw err;
                            } else {
                                throw exc;
                            }
                        }
                        
                    }
                };

            }
        }
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    public static void main(String args[])
    {
        testRTF2HTML test = new testRTF2HTML(args);

        test.run();
    }
}
