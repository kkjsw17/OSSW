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
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class indexer {
    private String input_file;
    private String output_file = "index.post";

    public indexer(String file) {
        this.input_file = file;
    }

    public void makeIndex() throws IOException, ParserConfigurationException, SAXException {
        File file = new File(input_file);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.parse(file);

        NodeList nlist = document.getElementsByTagName("body");
        int documentNum = nlist.getLength();

        HashMap<String, Integer> DocumentFrequencyMap = new HashMap<>();
        ArrayList<HashMap<String, Integer>> TermFrequencyMapList = new ArrayList<>();

        for (int i = 0; i < documentNum; i++) {
            Node nNode = nlist.item(i);
            HashMap<String, Integer> TermFrequencyMap = new HashMap<>();

            String[] termList = nNode.getTextContent().split("#");

            for (String s : termList) {
                String[] termAndFrequency = s.split(":");
                String term = termAndFrequency[0];
                int frequency = Integer.parseInt(termAndFrequency[1]);

                if (DocumentFrequencyMap.containsKey(term)) {
                    DocumentFrequencyMap.put(term, DocumentFrequencyMap.get(term) + 1);
                } else {
                    DocumentFrequencyMap.put(term, 1);
                }

                TermFrequencyMap.put(term, frequency);
            }

            TermFrequencyMapList.add(TermFrequencyMap);
        }

        HashMap<String, String> IndexMap = new HashMap<>();

        for (String term : DocumentFrequencyMap.keySet()) {
            int documentFrequency = DocumentFrequencyMap.get(term);
            StringBuilder output = new StringBuilder();

            for (int i = 0; i < documentNum; i++) {
                HashMap<String, Integer> TermFrequencyMap = TermFrequencyMapList.get(i);
                int termFrequency;

                termFrequency = TermFrequencyMap.getOrDefault(term, 0);

                double tf_idf = termFrequency * Math.log(documentNum / documentFrequency);
                output.append(String.format("%d %.2f ", i, tf_idf));
            }

            IndexMap.put(term, output.toString());
        }

        FileOutputStream fileStream = new FileOutputStream(output_file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

        objectOutputStream.writeObject(IndexMap);
        objectOutputStream.close();
    }

    public void showIndex() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("./index.post");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap) object;
        Iterator<String> it = hashMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            String value = (String) hashMap.get(key);
            System.out.println(key + " -> " + value);
        }
    }
}
