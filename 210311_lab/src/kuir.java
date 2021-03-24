import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		
		try {
			System.out.println("test main");
			switch(args[0]) {
			case "-c":
				makeCollection.make_Collection(args[1]);
				break;
			case "-k":
				makeKeyword.make_keyword(args[1]);
				break;
			}

		}
		catch(IndexOutOfBoundsException e) {
		}
		

	}

}