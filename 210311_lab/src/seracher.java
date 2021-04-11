import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class seracher {
	
	public static void CalcSim(double[] a_r,Iterator<String> its,HashMap hm,ArrayList<String> w) {
		
		double[] v= {0,0,0,0,0}; //inner product
		double[] d_v= {0,0,0,0,0};
		int[] d_i= {0,0,0,0,0};
		
		while(its.hasNext()) {
			String key=its.next();
			Object value=hm.get(key);
			//System.out.println(key+"->"+value); //test
			
			if(w.contains(key)) {
				//System.out.println(key+value);
				ArrayList<String> row=(ArrayList<String>) value;
				for(int k=0;k<row.size();k=k+2) {
					v[Integer.parseInt(row.get(k))]=v[Integer.parseInt(row.get(k))]+Double.parseDouble(row.get(k+1));
					//v[Integer.parseInt(row.get(k))]=Math.round(v[Integer.parseInt(row.get(k))]*100)/100.0;
					
					d_i[Integer.parseInt(row.get(k))]++;
					d_v[Integer.parseInt(row.get(k))]=d_v[Integer.parseInt(row.get(k))]+Math.pow(Double.parseDouble(row.get(k+1)), 2);
				}
			}
		}
		for(int i=0;i<5;i++) {
			if(v[i]!=0) 
				a_r[i]=v[i]/((Math.sqrt(d_v[i]))*(Math.sqrt(d_i[i])));
		}
	}
	
	public static void make_serach(String file,String q,String query) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {

		ArrayList<String> word =new ArrayList();
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		for(int i=0;i<kl.size();i++) {
			Keyword kwrd = kl.get(i);
			//System.out.println(kwrd.getString()+'\t'+kwrd.getCnt());
			word.add(kwrd.getString());
		}
		
		FileInputStream fileinputStream = new FileInputStream("./index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileinputStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		HashMap hashMap2=(HashMap)object;
		Iterator<String> it =hashMap2.keySet().iterator();
		
		double[] arr_value= {0,0,0,0,0};
		
		CalcSim(arr_value,it,hashMap2,word);
//		for(int k=0;k<arr_value.length;k++)
//			System.out.println(arr_value[k]);
		
		 int[] index = {0,1,2,3,4}; ArrayList<String> index_name=new ArrayList<String>();
	     double temp; int temp2;
	        
	        for(int i=arr_value.length; i>0; i--) {
	            for (int j=0; j<i-1; j++) {
	                if(arr_value[j] < arr_value[j+1]) {
	                    temp = arr_value[j]; temp2=index[j];
	                    arr_value[j] = arr_value[j+1]; index[j] = index[j+1];
	                    arr_value[j+1] = temp; index[j+1] = temp2;
	                }
	            }
	        }
//	        for(int i=0;i<index.length;i++)
//	        	System.out.println(index[i]);
	        
			File xmlfile = new File("./collection.xml");
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.parse(xmlfile);
			doc.getDocumentElement().normalize();

			String newbodyContent = "";
			NodeList nList = doc.getElementsByTagName("doc");
			for(int i=0; i<nList.getLength(); i++) {
				Node node = nList.item(i).getFirstChild();
		                if(node.getNodeName().equals("title")){
		                    index_name.add(node.getTextContent());
		                }
			}
//			for(int i=0;i<index_name.size();i++)
//				System.out.println(index_name.get(i));
			for(int i=0;i<3;i++)
				System.out.println("rank"+(i+1)+": "+index_name.get(index[i]));
	}
}