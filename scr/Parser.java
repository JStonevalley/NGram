import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;

public class Parser {
	private Kattio io;
	private Kattio dictionaryIo;
    private HashSet<String> dictionary;
	private final int n = 4;

	public Parser(String filePathIn, String filePathOut) throws FileNotFoundException {
        dictionaryIo = new Kattio(new FileInputStream(new File("data//dictionary.txt")));
		io = new Kattio(new FileInputStream(new File(filePathIn)), new FileOutputStream(new File(
				filePathOut)));
	}

    private void populateDictionary(){
        dictionary = new HashSet<String>();
       String word = dictionaryIo.getWord();
       while(word != null){
           dictionary.add(word);
           word = dictionaryIo.getWord();
       }
    }

	public void generateInput() {
        populateDictionary();
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
                String potentialWord = token.replaceAll("^[.!?:;,\"%#()-_\\*\\^]*[.!?:;,\"%#()-_*^]*$", "");
                if(!dictionary.contains(potentialWord)){
                    potentialWord = "<unk>";
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
