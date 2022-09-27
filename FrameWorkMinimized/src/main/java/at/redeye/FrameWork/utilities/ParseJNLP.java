package at.redeye.FrameWork.utilities;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParseJNLP {

    private final Properties properties = new Properties();
    private final List<String> jars = new ArrayList<>();
    private String mainJar;
    private String codeBase;

    public ParseJNLP(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(file);

        findRedeyeContent(document);
    }

    private void findRedeyeContent(Document document)
    {
        findRedeyeContentProperties(document);
        findRedeyeContentJars(document);
        findRedeyeContentCodeBase(document);
    }

    private void findRedeyeContentProperties(Document document)
    {
        // property values
        NodeList props = document.getElementsByTagName("property");

        for( int i = 0; i < props.getLength(); i++ )
        {
            Node node = props.item(i);

            NamedNodeMap attr_list = node.getAttributes();

            Node name = attr_list.getNamedItem("name");
            Node value = attr_list.getNamedItem("value");

            properties.setProperty(name.getNodeValue().toUpperCase(),value.getNodeValue());
        }
    }

    private void findRedeyeContentJars(Document document)
    {
         findRedeyeContentJars(document,"jar");
         findRedeyeContentJars(document,"nativelib");
    }

    private void findRedeyeContentJars(Document document, String tagname)
    {
        // property values
        NodeList props = document.getElementsByTagName(tagname);

        for (int i = 0; i < props.getLength(); i++) {
            Node node = props.item(i);

            NamedNodeMap attr_list = node.getAttributes();

            Node href = attr_list.getNamedItem("href");
            Node is_main = attr_list.getNamedItem("main");

            jars.add(href.getNodeValue());

            if (is_main != null) {
                if (StringUtils.isYes(is_main.getNodeValue())) {
                    mainJar = href.getNodeValue();
                }
            }
        }
    }

    public Properties getProperties()
    {
        return properties;
    }

    public String getMainJar()
    {
        return mainJar;
    }

    public List<String> getJars()
    {
        return jars;
    }

    public String getCodeBase()
    {
        return codeBase;
    }

    private void findRedeyeContentCodeBase(Document document)
    {
        NodeList nodes = document.getElementsByTagName("jnlp");
        NamedNodeMap attributes = nodes.item(0).getAttributes();
        Node n = attributes.getNamedItem("codebase");

        codeBase = n.getNodeValue();
    }
}
