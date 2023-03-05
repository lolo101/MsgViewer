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
import java.util.Properties;

public class ParseJNLP {

    private final Properties properties = new Properties();

    public ParseJNLP(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(file);

        findRedeyeContentProperties(document);
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

    public Properties getProperties()
    {
        return properties;
    }
}
