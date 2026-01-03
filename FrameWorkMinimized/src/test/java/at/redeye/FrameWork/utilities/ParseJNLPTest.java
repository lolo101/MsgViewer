package at.redeye.FrameWork.utilities;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseJNLPTest {

    static class TestFile
    {
        private final String resource;
        private File tempfile;
        List<String> jar_list;
        String code_base;

        TestFile(String resource) {
            this.resource = resource;
        }

        private void extractFile() throws IOException {
            try (InputStream source = getClass().getResourceAsStream(resource)) {
                tempfile = File.createTempFile("testcase", ".jnlp");

                try (FileOutputStream fout = new FileOutputStream(tempfile)) {
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while (read >= 0) {
                        fout.write(buffer, 0, read);
                        read = source.read(buffer);
                    }
                }
            }
        }

        private void cleanUp() {
            if (tempfile != null)
                tempfile.delete();
        }

        private File getFile() {
            return tempfile;
        }

        private static Properties getProperties() {
            return new Properties();
        }
    }

    private static class TestSFTPUpload extends TestFile {
        private TestSFTPUpload() {
            super("/at/redeye/FrameWork/utilities/ParseJNLPTestFiles/launch_sftpupload.jnlp");

            code_base = "http://redeye.hoffer.cx/sftpupload/";

            jar_list = new ArrayList<>();

            jar_list.add("SFtpUpload.jar");
            jar_list.add("lib/FrameWork.jar");
            jar_list.add("lib/log4j-1.2.12.jar");
            jar_list.add("lib/jsch-0.1.44.jar");
            jar_list.add("lib/jzlib.jar");
            jar_list.add("lib/jshortcut-0_4_repacked.jar");
        }
    }

    private static class TestMSGViewer extends TestFile {
        private TestMSGViewer() {
            super("/at/redeye/FrameWork/utilities/ParseJNLPTestFiles/launch_msgviewer.jnlp");

            code_base = "http://redeye.hoffer.cx/MSGViewer/";

            jar_list = new ArrayList<>();

            jar_list.add("MSGViewer.jar");
            jar_list.add("msgparser.jar");
            jar_list.add("poi-3.2-FINAL-20081019.jar");
            jar_list.add("tnef-1.3.1.jar");
            jar_list.add("MSGViewer.jar");
            jar_list.add("FrameWork.jar");
            jar_list.add("log4j-1.2.12.jar");
            jar_list.add("proxy-vole.jar");
            jar_list.add("js.jar");
            jar_list.add("jshortcut-0_4_repacked.jar");
        }
    }

    private static final Collection<TestFile> test_cases = new ArrayList<>();

    @BeforeAll
    static void setUpClass() throws Exception {

        test_cases.add(new TestSFTPUpload());
        test_cases.add(new TestMSGViewer());

        for (TestFile test : test_cases) {
            test.extractFile();
        }
    }

    @AfterAll
    static void tearDownClass() {
        for (TestFile test : test_cases) {
            test.cleanUp();
        }
    }

    @Test
    void testGetProperties() throws ParserConfigurationException, IOException, SAXException {

        for (TestFile test : test_cases) {
            System.out.println("getProperties for " + test.resource);
            ParseJNLP instance = new ParseJNLP(test.getFile());
            Properties expResult = TestFile.getProperties();
            Properties result = instance.getProperties();
            assertEquals(expResult, result);
        }
    }
}
