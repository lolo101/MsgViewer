package net.sourceforge.MSGViewer.rtfparser;

import at.redeye.FrameWork.base.BaseModuleLauncher;
import at.redeye.FrameWork.base.LocalRoot;
import at.redeye.FrameWork.utilities.ReadFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class testRTF2HTML extends BaseModuleLauncher {
    public testRTF2HTML(String[] args) {
        super(args);

        root = new LocalRoot("MSGViewer", "MSGViewer");

        configureLogging();
    }

    public void run() throws Exception {
        for (String arg : args) {
            if (arg.toLowerCase().endsWith(".rtf")) {
                final File file = new File(arg);

                if (!file.exists()) {
                    logger.error("Cannot open file " + file);
                    continue;
                }

                final String content = ReadFile.read_file(arg);

                System.out.println(content);

                RTFParser parser = new RTFParser(new FileInputStream(file));

                String message = null;
                Exception exc = null;
                Error err = null;

                try {

                    parser.parse();
                    logger.info("done parsing " + file);

                    String htmlContent = parser.getHTML();
                    System.out.print(htmlContent);

                    String file_name = getStartupParam("writehtml");

                    if (file_name != null) {
                        try (FileWriter fout = new FileWriter(file_name)) {
                            fout.write(htmlContent);
                        }
                    }

                } catch (Error ex) {
                    message = ex.getMessage();
                    err = ex;
                } catch (Exception ex) {
                    message = ex.getMessage();
                    exc = ex;
                }

                if (message != null) {
                    int start = message.indexOf("at line");
                    int start_col = message.indexOf("column ");

                    if (start >= 0) {
                        int end = message.indexOf(",", start);
                        int end_col = message.indexOf(".", start_col);

                        if (end >= 0) {
                            System.out.println(message);

                            int line = Integer.parseInt(message.substring(start + 8, end));
                            int col;

                            if (end_col > 0)
                                col = Integer.parseInt(message.substring(start_col + 7, end_col));
                            else
                                col = Integer.parseInt(message.substring(start_col + 7));

                            String[] lines = content.split("\n");

                            String ll = lines[line - 1];

                            if (ll.length() > 100) {
                                int send = col + 60;
                                if (ll.length() < send)
                                    send = ll.length() - 1;

                                ll = ll.substring(col - 40, send);
                                col = 40;
                            }

                            String sb = " ".repeat(Math.max(0, col - 1)) + "^";
                            logger.error("\n\n" + ll + "\n" + sb);
                        }
                    }
                }

                if (err != null || exc != null) {
                    if (err != null) {
                        throw err;
                    } else {
                        throw exc;
                    }
                }
            }
        }
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    // FIXME make me a JUnit test class
    public static void main(String[] args) throws Exception {
        testRTF2HTML test = new testRTF2HTML(args);

        test.run();
    }
}
