import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		Parser parser = new Parser("data//corpus.txt", "data//toInputRead");
		parser.generateInput();
        new TreeBuilder("data//toInputRead");
	}

}
