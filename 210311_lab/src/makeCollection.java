
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
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

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class makeCollection {

	public static void make_Collection(String path) throws IOException, ParserConfigurationException {
		System.out.println("test collcetion");
		
		if(path.length() < 0)
			path = "./data/";
		
		String p = ".html";
		File files = new File(path);
		
		String filelist[] =files.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(p);
			}
		});
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Document doc=docBuilder.newDocument();
		
		
		Element docs = doc.createElement("docs");
	    doc.appendChild(docs);

		
		
	    if(filelist.length > 0) {
	         for(int i=0; i<filelist.length; i++) {
	            
	        	 Element e = doc.createElement("doc");
		            docs.appendChild(e);
		            e.setAttribute("id", Integer.toString(i));
		            
		            org.jsoup.nodes.Document document = null;
					try {
						document =  Jsoup.parse(new File(path + "/" + filelist[i]), "UTF-8");

					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
		            
		            Element title = doc.createElement("title");
		            Elements titles =  ((org.jsoup.nodes.Element) document).select("title");
		            title.appendChild(doc.createTextNode(titles.html()));
		            e.appendChild(title);
		            
		            Element body = doc.createElement("body");
		            Elements ps = document.select("#content p");
		            for(org.jsoup.nodes.Element e1: ps)
		               body.appendChild(doc.createTextNode(e1.html()));
		            e.appendChild(body);
		         }
		      }		 
		
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = null;
		try {
			result = new StreamResult(new FileOutputStream(new File("./collection.xml")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
