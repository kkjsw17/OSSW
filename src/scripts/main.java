package scripts;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class main {
    public static void main(String args[]) throws IOException, TransformerException, ParserConfigurationException, SAXException, ClassNotFoundException {
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
        else if(command.equals("-i")) {
            indexer keyword = new indexer(path);
            keyword.makeIndex();
            keyword.showIndex();
        }
        else if (command.equals("-s")) {
            searcher title = new searcher(path);

            if (args.length == 4) {
                String subcommand = args[2];
                String query = args[3];

                if (subcommand.equals("-q")) {
                    title.calcSim(query);
                }
            }
        }
        else if(command.equals("-m")) {
            MidTerm midTerm = new MidTerm(path);

            if (args.length == 4) {
                String subcommand = args[2];
                String query = args[3];

                if (subcommand.equals("-q")) {
                    midTerm.showSnippet(query);
                }
            }
        }
    }
}
