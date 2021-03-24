
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class makeKeyword {

	public static void make_keyword(String file) throws ParserConfigurationException, SAXException, IOException {
		System.out.println("test keyword");
		
		if(file.length() < 0)
			file = "./collection.xml";
		
		File xmlfile = new File(file);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = docBuilder.parse(xmlfile);
		doc.getDocumentElement().normalize();

		String newbodyContent = "";
		NodeList nList = doc.getElementsByTagName("body");
		for(int i=0; i<nList.getLength(); i++) {
			Node nNode = nList.item(i);
			String bodyContent = nNode.getTextContent();
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(bodyContent, true);
			String newbodytemp = "";
			for(int j=0; j<kl.size(); j++) {
				Keyword kwrd = kl.get(j);
				newbodytemp = kwrd.getString() + ":" + kwrd.getCnt() + "#";
				newbodyContent += newbodytemp;
			}
			nNode.setTextContent(newbodyContent);
			newbodyContent = "";
		}
		
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("./index.xml")));
		
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
	}

	
}
