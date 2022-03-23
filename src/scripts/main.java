package scripts;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class main {
    public static void main(String args[]) throws IOException, TransformerException, ParserConfigurationException, SAXException {
        String command = args[0];
        String path = args[1];

        if(command.equals("-c")) {
            makeCollection collection = new makeCollection(path);
            collection.makeXml();
        }
        else if(command.equals("-k")) {
            makeKeyword keyword = new makeKeyword(path);
            keyword.convertXml();
        }
    }
}
