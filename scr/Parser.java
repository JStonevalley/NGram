import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Parser {
	private Kattio io;
	// private Kattio countryIo;
    private Kattio dictionaryIo;
    //Change for another depth of the ngrams ex: 3 trigram, 4 quadrigram
	private final int n = 4;
    private List<Category> categories;

	public Parser(String fileDictionary, String filePathIn, String filePathOut, List<Category> categories) throws FileNotFoundException {
        dictionaryIo = new Kattio(new FileInputStream(new File(fileDictionary)));
        // countryIo = new Kattio(new FileInputStream(new File("..//data//countries.txt")));
		io = new Kattio(new FileInputStream(new File(filePathIn)), new FileOutputStream(new File(
				filePathOut)));
        this.categories = categories;
	}

    private HashSet<String> populateHashSet(Kattio ioData){
       HashSet<String> set = new HashSet<String>();
       String word = ioData.getWord();
       while(word != null){
           set.add(word);
           word = ioData.getWord();
       }
        return set;
    }

	public void generateInput() {
        HashSet<String> dictionary = populateHashSet(dictionaryIo);
        // HashSet<String> countries = populateHashSet(countryIo);
		String[] words = new String[n];
		int wordIndex = 0;
		String token = io.getWord();
		while (token != null) {
            token = token.toLowerCase();
            if (!token.matches("[\\d|*^,.#()%-_+=!?]*")) {
                if (wordIndex == 0) {
                    for (int i = 0; i < words.length - 1; i++) {
                        words[i] = "";
                    }
                } else {
                    for (int i = 0; i < words.length - 1; i++) {
                        words[i] = words[i + 1];
                    }
                }
                String potentialWord = token.replaceAll("^[.!?:;,\"%#()-_\\*\\^]*", "");
                potentialWord = potentialWord.replaceAll("[.!?:;,\"%#()-_*^]*$", "");
                potentialWord = potentialWord.replace("'", "");
                if (!dictionary.contains(potentialWord) && categories != null) {
                    Category category = null;
                    if ((category = Category.categorize(potentialWord, categories)) != null) {
                        potentialWord = category.getName();
                    } else {
                        potentialWord = "<unk>";
                    }
                }

                words[words.length - 1] = potentialWord;
                wordIndex++;
                if (token.matches("[\\w|\\W]*[.!?]")) {
                    if (wordIndex >= n) {
                        io.write(printArray(words));
                    }
                    for (int i = 0; i < words.length - 1; i++) {
                        words[i] = words[i + 1];
                    }
                    words[words.length - 1] = "<end>";
                    io.write(printArray(words));
                    for (int i = 0; i < words.length - 2; i++) {
                        words[i] = "";
                        io.write(printArray(words));
                    }
                    wordIndex = 0;
                }
                if (wordIndex >= n) {
                    io.write(printArray(words));
                }
            }
			token = io.getWord();
		}
		io.flush();
		io.close();
	}

	public String printArray(String[] array) {
		String reString = "";
		for (int i = 0; i < array.length; i++) {
			if (i != array.length - 1) {
                if(array[i] != "") {
                    reString = reString + array[i] + " ";
                }
			} else {
				reString = reString + array[i] + "\n";
			}
		}
		return reString;
	}
}
