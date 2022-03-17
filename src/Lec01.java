package lec01;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

public class Lec01 {
	public static void main(String args[]) throws ParserConfigurationException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document document = docBuilder.newDocument();
		
		Element docs = document.createElement("docs");
		document.appendChild(docs);
		
		String path = "data/html";
		File[] files = makeFileList(path);
		                          
		for (int i = 0; i < files.length; i++) {
			org.jsoup.nodes.Document html = Jsoup.parse(files[i], "UTF-8");
			String titleData = html.title();
			String bodyData = html.body().text();
			
			Element doc = document.createElement("doc");
			doc.setAttribute("id", Integer.toString(i));
			
			Element title = document.createElement("title");
			title.appendChild(document.createTextNode(titleData));
			doc.appendChild(title);
			
			Element body = document.createElement("body");
			body.appendChild(document.createTextNode(bodyData));
			doc.appendChild(body);
			
			docs.appendChild(doc);
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new FileOutputStream(new File("data/file.xml")));
		
		transformer.transform(source, result);
	}
	
	public static File[] makeFileList(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}
}
