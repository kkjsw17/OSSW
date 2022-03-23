package scripts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class makeKeyword {
	private String input_path;
	private String output_path = "src/data/index.xml";

	public makeKeyword(String path) {
		this.input_path = path;
	}

	public void convertXml() throws ParserConfigurationException, IOException, SAXException, TransformerException {
		File file = new File(input_path);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document document = docBuilder.parse(file);

		NodeList nlist = document.getElementsByTagName("body");
		for (int i = 0; i < nlist.getLength(); i++) {
			Node nNode = nlist.item(i);

			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(nNode.getTextContent(), true);

			String output = "";
			for (int j = 0; j < kl.size(); j++) {
				Keyword kwrd = kl.get(j);
				output += kwrd.getString() + ":" + kwrd.getCnt() + "#";
				nNode.setTextContent(output);
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new FileOutputStream(new File(output_path)));

		transformer.transform(source, result);
	}
	
	public static File[] makeFileList(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}
}
