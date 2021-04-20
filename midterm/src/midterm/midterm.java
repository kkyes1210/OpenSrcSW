package midterm;

public class midterm {
public static void main(String[] args) {
		try {
			switch(args[0]) {
			case "-f":
				genSnippet.make_Collection(args[1]);
				break;
			}
			

		}
		catch(IndexOutOfBoundsException e) {
		}
}
}
