import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;

public class ListCategory extends Category {
	
	private HashSet<String> words;

	public ListCategory(String name, String filename) {
		super(name);

		words = new HashSet<String>();

		// Read category words from file
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				words.add(line.toLowerCase());
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean includes(String word) {
		return words.contains(word.toLowerCase());
	}
}