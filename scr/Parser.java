import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Parser {
	Kattio io;
	Kattio outPutIo;
	private final int n = 4;

	public Parser(String filePathIn, String filePathOut) throws FileNotFoundException {
		io = new Kattio(new FileInputStream(new File(filePathIn)), new FileOutputStream(new File(
				filePathOut)));
	}

	public void generateInput() {
		String[] words = new String[n];
		int wordIndex = 0;
		String token = io.getWord();
		while (token != null) {
			if (wordIndex == 0) {
				for (int i = 0; i < words.length - 1; i++) {
					words[i] = "";
				}
				words[words.length - 1] = token.replaceAll("[.!?:;,\"%#()]$", "");
			} else {
				for (int i = 0; i < words.length - 1; i++) {
					words[i] = words[i + 1];
				}
				words[words.length - 1] = token.replaceAll("[.!?:;,\"%#()]$", "");
			}
			wordIndex++;
			if (token.matches("[\\w|\\W]*[.!?]")) {
                if(wordIndex >= n) {
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
            if(wordIndex >= n) {
                io.write(printArray(words));
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
