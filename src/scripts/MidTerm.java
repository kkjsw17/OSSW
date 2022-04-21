package scripts;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class MidTerm {
    private String path;

    public MidTerm(String path) {
        this.path = path;
    }

    public void showSnippet(String query) throws ParserConfigurationException, IOException, SAXException {
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(query, true);
        ArrayList<String> keywordList = new ArrayList<>();

        for (Keyword keyword : kl) {
            keywordList.add(keyword.getString());
        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.parse(path);

        NodeList titleList = document.getElementsByTagName("title");
        NodeList bodyList = document.getElementsByTagName("body");
        HashMap<String, String> docMap = new HashMap<>();

        for (int i = 0; i < titleList.getLength(); i++) {
            Node title = titleList.item(i);
            Node body = bodyList.item(i);

            docMap.put(title.getTextContent(), body.getTextContent());
        }

        HashMap<String, Integer> snippetMap = new HashMap<>();

        for (String key : docMap.keySet()) {
            String body = docMap.get(key);

            int highScore = 0;
            String highSnippet = "";

            for (int i = 0; i < body.length() - 30; i++) {
                String sequence = (String) body.subSequence(i, i + 30);
                int matchingScore = 0;

                for (int j = 0; j < keywordList.size(); j++) {
                    String keyword = keywordList.get(j);

                    if (sequence.contains(keyword)) {
                        matchingScore++;
                    }
                }

                if (matchingScore > highScore) {
                    highScore = matchingScore;
                    highSnippet = sequence;
                }
            }

            if (highScore != 0) {
                snippetMap.put(String.format("%s, %s, %d ", key, highSnippet, highScore), highScore);
            }
        }

        HashMap<String, Integer> sortedSnippetMap = sortByValue(snippetMap);

        for (String content : sortedSnippetMap.keySet()) {
            System.out.println(content.toString());
        }
    }

    private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());

        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        HashMap<String, Integer> temp = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }
}
