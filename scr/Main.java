import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		Parser parser = new Parser("input", "toInputRead");
		parser.generateInput();
        new TreeBuilder("toInputRead");
	}

}
