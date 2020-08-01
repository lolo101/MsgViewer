/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class ParseJNLP {

    private Node rootNode;
    private Properties properties = new Properties();
    private List<String> jars = new ArrayList<>();
    private String mainJar;
    private String codeBase;

    public ParseJNLP( File file ) throws ParserConfigurationException, IOException, SAXException
    {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder  = factory.newDocumentBuilder();

         Document document = builder.parse( file );

         rootNode = document.getDocumentElement();

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

    public String print()
    {
        StringBuilder builder = new StringBuilder();

        print( builder, rootNode, 0 );

        return builder.toString();
    }

    public void print( StringBuilder stream, Node node, int depth )
    {
        if( node == null )
            return;

        String name = node.getNodeName();

        if( name != null && !name.isEmpty() )
        {
            stream.append("\n    ".repeat(Math.max(0, depth)));

            stream.append(name);

            if( node.hasAttributes() )
            {
                NamedNodeMap attributes = node.getAttributes();

                for( int i = 0; i < attributes.getLength(); i++ )
                {
                    stream.append(" ");

                    Node attr = attributes.item(i);

                    stream.append(attr.getNodeName());
                    stream.append("=");
                    stream.append(attr.getNodeValue());
                }
            }
        }

        if( node.hasChildNodes() )
        {
            NodeList list = node.getChildNodes();

            for( int i = 0; i < list.getLength(); i++ )
            {
                print( stream, list.item(i), depth + 1 );
            }
        }
    }

    private void findRedeyeContentCodeBase(Document document)
    {
        NodeList nodes = document.getElementsByTagName("jnlp");
        NamedNodeMap attributes = nodes.item(0).getAttributes();
        Node n = attributes.getNamedItem("codebase");

        codeBase = n.getNodeValue();
    }

    static public void main( final String[] argv)
    {
        for (String arg : argv) {
            try {
                ParseJNLP parseJNLP = new ParseJNLP(new File(arg));

                System.out.println( parseJNLP.print() );

                parseJNLP.getProperties().list(System.out);

                for( String jar : parseJNLP.getJars() )
                {
                    if( jar.equals(parseJNLP.getMainJar()) )
                        System.out.println(jar + " main");
                    else
                        System.out.println(jar);
                }

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(ParseJNLP.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ParseJNLP.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(ParseJNLP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
