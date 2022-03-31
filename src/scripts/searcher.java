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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class searcher {
    private String input_file;
    private String ref_file = "collection.xml";

    public searcher(String file) {
        this.input_file = file;
    }

    public void calcSim(String query) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
        FileInputStream fileStream = new FileInputStream(input_file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap indexMap = (HashMap) object;

        File file = new File(ref_file);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.parse(file);

        NodeList nlist = document.getElementsByTagName("title");
        HashMap<String, String> titleMap = new HashMap<>();

        HashMap<String, Double> similarityMap = new HashMap<>();

        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(query, true);

        for (int i = 0; i < nlist.getLength(); i++) {
            Node title = nlist.item(i);
            Node doc = title.getParentNode();
            String id = doc.getAttributes().getNamedItem("id").getTextContent();

            titleMap.put(id, title.getTextContent());

            double similarity = 0.0;

            for (Keyword keyword : kl) {
                String term = keyword.getString();
                double weightQuery = keyword.getCnt();

                if(!indexMap.containsKey(term)) {
                    continue;
                }

                HashMap weightMap = (HashMap) indexMap.get(term);
                double weightDocument = (double) weightMap.get(id);

                double multiply = weightQuery * weightDocument;
                similarity += multiply;
            }

            similarityMap.put(id, similarity);
        }

        HashMap<String, Double> sortedSimilarityMap = sortByValue(similarityMap);
        ArrayList<String> keys = new ArrayList(sortedSimilarityMap.keySet());

        System.out.println("\"" + query + "\" 와의 유사도 측정 결과 상위 3개 문서 제목");

        for (int i = 0; i < 3; i++) {
            String key = keys.get(i);
            String title = titleMap.get(key);

            System.out.println(title);
        }
    }

    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(hm.entrySet());

        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        HashMap<String, Double> temp = new LinkedHashMap<>();

        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }
}
