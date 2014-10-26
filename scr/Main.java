import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    // static String CORPUS_FILE = "..//data//corpus_train.txt";
    // static String TREE_INPUT_FILE = "..//data//toInputRead_train";
    // static String TREE_OUTPUT_FILE = "..//data//output";


    // Categories
    private ArrayList<Category> categories = new ArrayList<Category>();

    public static void main(String[] args) throws FileNotFoundException {

        List<String> argsList = Arrays.asList(args);
        
        // Categories
        ArrayList<Category> categories = new ArrayList<Category>();
        if (!argsList.contains("nocategory")) {
            categories.add(new ListCategory("country", "..//data//countries.txt"));
            categories.add(new RegexCategory("number", "-?[,\\.\\d]+")); // TODO this has to be included when reading corpus
        }

        // Parse or run algo?   
        if (argsList.get(0).equals("parse")) {

            if (argsList.contains("dontcategorize")) {
                categories = null;
            }

            String dictionary = argsList.get(1);
            String corpusFile = argsList.get(2);
            String outTreeFile = argsList.get(3);

            System.err.println("PARSING CORPUS");

            //parse corpus
            long time = System.currentTimeMillis();
            Parser parser = new Parser(dictionary, corpusFile, outTreeFile, categories);
            parser.generateInput();

            System.err.println(getMillisecondsSinceTime(time)+"ms - CORPUS PARSED");
            // build tree
            // TreeBuilder treeBuilder = new TreeBuilder(TREE_INPUT_FILE);
            // HashMap<String, Node> hashTree = treeBuilder.buildTree(new HashMap<String, Node>());
            // System.out.println(getMillisecondsSinceTime(time)+"ms -TREE BUILT");
            // treeBuilder.printTree(hashTree, null); //print to system.out
            // treeBuilder.printTree(hashTree, new File(TREE_OUTPUT_FILE)); //print to file	
            // System.out.println(getMillisecondsSinceTime(time)+"ms -TREE PRINTED/WRITTEN");
        
        } else {

            String inputFile = argsList.get(0);

            // Tree
            TreeBuilder treeBuilder = new TreeBuilder(inputFile);
            HashMap<String, Node> hashTree = treeBuilder.buildTree(new HashMap<String, Node>());
            
            // n
            int n = 4;

            // Predictor
            NextWordPredictor predictor = new NextWordPredictor(hashTree, categories, n);
            System.out.println("Created predictor. Please, enter words..."); 

            int numCorrectGuesses = 0;
            int numWrongGuesses = 0;

            // Continuous word by word reading
            LinkedList<String> previousWords = new LinkedList<String>();
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.equals(":quit"))
                    break;

                String[] words = line.split("\\s+");

                // Skip lines with only whitespace
                if (words.length == 0)
                    continue;

                // The "previous words" are all words but the last on the line
                previousWords.clear();
                for (int i = 0; i < words.length - 1; i++) {
                    previousWords.add(words[i]);
                }

                // The correct word is the last on the line
                String correctWord = words[words.length - 1];
                
                StringBuilder sb = new StringBuilder(30);
                for (String word : previousWords) {
                    sb.append(word).append(" ");
                }

                // Get the best predictions given the previous words
                ArrayList<String> predictions = predictor.nextWord(previousWords);
                
                // No predictions counts as wrong guess
                if (predictions.isEmpty()) {
                    System.out.printf("Previous words: %-30s Correct: %-12s   Guess: %-12s   ", sb.toString(), correctWord, "");
                    System.out.println("WRONG");
                    numWrongGuesses++;
                    continue;
                }

                // Guess the first among the suggested predictions
                String guess = predictions.get(0);


                // Print the result
                System.out.printf("Previous words: %-30s Correct: %-12s   Guess: %-12s   ", sb.toString(), correctWord, guess);
                
                if (guess.equals(correctWord)) {
                    System.out.print("CORRECT");
                    numCorrectGuesses++;
                } else {
                    System.out.print("WRONG");
                    numWrongGuesses++;
                }

                System.out.println();
            }

            // Sum things up
            int totalGuesses = numCorrectGuesses + numWrongGuesses;
            System.out.println();
            System.out.println("--- DONE ---");
            System.out.printf("Correct guesses:     %6d / %d\n", numCorrectGuesses, totalGuesses);
            System.out.printf("Wrong guesses:       %6d / %d\n", numWrongGuesses, totalGuesses);
            System.out.printf("Ratio: %f\n", (float) numCorrectGuesses / totalGuesses);
        }
    }

    private static long getMillisecondsSinceTime(long time) {
        long t = System.currentTimeMillis();
        return t - time;
    }
}
