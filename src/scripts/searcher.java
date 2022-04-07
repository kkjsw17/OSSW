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

            double innerProductResult = innerProduct(indexMap, id, kl);
            double distanceQuery = 0.0;
            double distanceDocument = 0.0;

            for (Keyword keyword : kl) {
                String term = keyword.getString();
                double weightQuery = keyword.getCnt();

                if(!indexMap.containsKey(term)) {
                    continue;
                }

                HashMap weightMap = (HashMap) indexMap.get(term);
                double weightDocument = (double) weightMap.get(id);

                distanceQuery += Math.pow(weightQuery, 2);
                distanceDocument += Math.pow(weightDocument, 2);
            }

            double distance = Math.sqrt(distanceQuery) * Math.sqrt(distanceDocument);

            if (distance == 0) {
                distance = 1e-6;
            }

            double cosineSimilarity = innerProductResult / distance;

            similarityMap.put(id, cosineSimilarity);
        }

        HashMap<String, Double> sortedSimilarityMap = sortByValue(similarityMap);
        ArrayList<String> keys = new ArrayList(sortedSimilarityMap.keySet());

        System.out.println("\"" + query + "\" 와의 유사도 측정 결과 상위 3개 문서 제목\n");

        boolean flag = false;

        for (int i = 0; i < 3; i++) {
            String key = keys.get(i);

            if (sortedSimilarityMap.get(key) != 0) {
                String title = titleMap.get(key);
                flag = true;

                System.out.println(sortedSimilarityMap.get(key) + " " + title);
            }
        }

        if (!flag) {
            System.out.println("검색된 문서가 없습니다.");
        }
    }

    public double innerProduct(HashMap indexMap, String id, KeywordList kl) {
        double result = 0.0;

        for (Keyword keyword : kl) {
            String term = keyword.getString();
            double weightQuery = keyword.getCnt();

            if (!indexMap.containsKey(term)) {
                continue;
            }

            HashMap weightMap = (HashMap) indexMap.get(term);
            double weightDocument = (double) weightMap.get(id);

            double multiply = weightQuery * weightDocument;
            result += multiply;
        }

        return result;
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
