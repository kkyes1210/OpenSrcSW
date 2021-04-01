import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException {
		
		try {
			switch(args[0]) {
			case "-c":
				makeCollection.make_Collection(args[1]);
				break;
			case "-k":
				makeKeyword.make_keyword(args[1]);
				break;
			case "-i":
				indexer.make_index(args[1]);
				break;
			case "-s":
				seracher.make_serach(args[1],args[2],args[3]);
				break;
			}
			

		}
		catch(IndexOutOfBoundsException e) {
		}
		

	}

}