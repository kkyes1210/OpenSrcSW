import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {
	
	@SuppressWarnings({"rawtypes", "unchecked" , "nls"})
	public static void make_index(String file) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException{
		
		if(file.length() < 0)
			file = "./index.xml";
		
		//body추출
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();
     
        NodeList n_list = root.getElementsByTagName("body");
      
        
        ArrayList<String[]> list = new ArrayList<String[]>();

        for(int i=0; i<n_list.getLength(); i++) {
        	
        	Element sub_el = (Element) n_list.item(i);
        	Node body=sub_el.getFirstChild();
        	String value = body.getNodeValue();
        	//System.out.println("*test*"+value);
        	
        	String[] arr=value.split(":|#");
        	
        	list.add(arr);
        }
        
        
        ArrayList<String> arr_key = new ArrayList<>();
        ArrayList<Integer> count = new ArrayList<>();
        ArrayList<ArrayList<String>> tf = new ArrayList<>();
        
        for(int k=0;k<list.size();k++) {
        	String[] row= list.get(k);
        	for(int j=0;j<row.length;j+=2) {
        		//test list
        		//System.out.print("index"+k+" "+row[j]+"\n");
        		if(arr_key.contains(row[j])) {
        			//System.out.println("존재"); //test
        			//System.out.println(arr_key.indexOf(row[j]));
        			
        			count.set(arr_key.indexOf(row[j]), count.get(arr_key.indexOf(row[j]))+1);
        			ArrayList<String> row2=new ArrayList<>();
        			row2=tf.get(arr_key.indexOf(row[j]));
        			row2.add(Integer.toString(k));
        			row2.add(row[j+1]);
        			
        			tf.set(arr_key.indexOf(row[j]),row2);
        			
        		}else {
        			//System.out.println("존재X"); //test
        			arr_key.add(row[j]);
        			count.add(1);
        			
        			ArrayList<String> row2=new ArrayList<>();
        			//row2.add(row[j]);
        			row2.add(Integer.toString(k));
        			row2.add(row[j+1]);
        			tf.add(row2);
        			
        		}
        	}
        }
        
        //test:df 출력
//        for(int k=0;k<arr_key.size();k++) {
//        	System.out.println(arr_key.get(k)+" "+count.get(k));
//        }
        
        // tf test
//        for(int k=0;k<tf.size();k++) {
//        	ArrayList<String> row= tf.get(k);
//        	
//        	System.out.print(arr_key.get(k));
//        	
//        	for(int j=0;j<row.size();j++) {
//        		System.out.print(":"+row.get(j));
//        	}
//        	System.out.println(" ");
//        	
//        }
        
		//파일에 객체 저장
		FileOutputStream fileStream = new FileOutputStream("./index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		HashMap hashmap = new HashMap();
		
		
		
		for(int i=0;i<arr_key.size();i++) {
			ArrayList<String> row= tf.get(i);
			ArrayList<String> s = new ArrayList<>();
			//System.out.println(row.size()/2);
			for(int j=0;j<row.size();j+=2) {
				
				s.add(row.get(j));
				//System.out.println(arr_key.get(i)+":"+count.get(i));
				
				double tf_idf=(Double.parseDouble(row.get(j+1)))*(Math.log(5.0/(double)count.get(i)));
				
				tf_idf=Math.round(tf_idf*100)/100.0;
				s.add(Double.toString(tf_idf));
			}
			
			hashmap.put(arr_key.get(i),s);
			
			//test
//			for(int k=0;k<s.size();k++) {
//				System.out.print(s.get(k)+" ");
//			}
//			System.out.println();
		}
		
		
		
		objectOutputStream.writeObject(hashmap);
		objectOutputStream.close();
		
		//파일에서 객체 불러오기
		FileInputStream fileinputStream = new FileInputStream("./index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileinputStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		HashMap hashMap2=(HashMap)object;
		Iterator<String> it =hashMap2.keySet().iterator();
		
		while(it.hasNext()) {
			String key=it.next();
			Object value=hashMap2.get(key);
			System.out.println(key+"->"+value);
		}
		
		}
}
