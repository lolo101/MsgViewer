package at.redeye.FrameWork.utilities;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.xml.sax.SAXException;

/**
 *
 * @author martin
 */
public class ParseJNLPTest {

    static class TestFile
    {
        private String resource;
        private File tempfile;
        private String main_jar;
        List<String> jar_list;
        String code_base;

        TestFile( String resource , String main_jar)
        {
            this.resource = resource;
            this.main_jar = main_jar;
        }

        public void extractFile() throws IOException
        {
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

        public void cleanUp() {
            if( tempfile != null )
                tempfile.delete();
        }

        public File getFile() {
            return tempfile;
        }

        public Properties getProperties() {
            return new Properties();
        }

        public String getMainJar() {
            return main_jar;
        }

        public List<String> getJars() {
            return jar_list;
        }

        private String getCodeBase() {
            return code_base;
        }
    }

    static class TestSFTPUpload extends TestFile
    {
        TestSFTPUpload()
        {
            super( "/at/redeye/FrameWork/utilities/ParseJNLPTestFiles/launch_sftpupload.jnlp", "SFtpUpload.jar" );

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

    static class TestMSGViewer extends TestFile
    {
        TestMSGViewer()
        {
            super( "/at/redeye/FrameWork/utilities/ParseJNLPTestFiles/launch_msgviewer.jnlp", "MSGViewer.jar" );

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
            jar_list.add("ShellExec.jar");
        }
    }

    static List<TestFile> test_cases = new ArrayList();

    public ParseJNLPTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

        test_cases.add(new TestSFTPUpload());
        test_cases.add(new TestMSGViewer());

        for( TestFile test : test_cases ) {
            test.extractFile();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        for( TestFile test : test_cases ) {
            test.cleanUp();
        }
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getProperties method, of class ParseJNLP.
     */
    @Test
    public void testGetProperties() throws ParserConfigurationException, IOException, SAXException {

        for( TestFile test : test_cases )
        {
            System.out.println("getProperties for " + test.resource);
            ParseJNLP instance = new ParseJNLP(test.getFile());
            Properties expResult = test.getProperties();
            Properties result = instance.getProperties();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getMainJar method, of class ParseJNLP.
     */
    @Test
    public void testGetMainJar() throws ParserConfigurationException, IOException, SAXException {

        for( TestFile test : test_cases )
        {
            System.out.println("getMainJar for "  + test.resource );
            ParseJNLP instance = new ParseJNLP(test.getFile());
            String expResult = test.getMainJar();
            String result = instance.getMainJar();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getJars method, of class ParseJNLP.
     */
    @Test
    public void testGetJars() throws ParserConfigurationException, IOException, SAXException {

        for( TestFile test : test_cases )
        {
            System.out.println("getJars for " + test.resource);
            ParseJNLP instance = new ParseJNLP(test.getFile());
            List<String> expResult = test.getJars();
            List<String> result = instance.getJars();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getCodeBase method, of class ParseJNLP.
     */
    @Test
    public void testGetCodeBase() throws ParserConfigurationException, IOException, SAXException {

        for (TestFile test : test_cases) {
            System.out.println("getCodeBase for " + test.resource);
            ParseJNLP instance = new ParseJNLP(test.getFile());
            String expResult = test.getCodeBase();
            String result = instance.getCodeBase();
            assertEquals(expResult, result);
        }
    }
}
