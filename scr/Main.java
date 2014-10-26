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

        if (argsList.get(0).equals("preparedictionary")) {

            noCategoriesDictionary tmp = new noCategoriesDictionary(argsList.get(1), categories);

        } else if (argsList.get(0).equals("parse")) {

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

            String inputFileCat = argsList.get(0);
            String inputFileNocat = argsList.get(1);

            // Tree
            TreeBuilder treeBuilderCat = new TreeBuilder(inputFileCat);
            TreeBuilder treeBuilderNocat = new TreeBuilder(inputFileNocat);
            

            HashMap<String, Node> hashTreeCat = treeBuilderCat.buildTree(new HashMap<String, Node>());
            HashMap<String, Node> hashTreeNocat = treeBuilderNocat.buildTree(new HashMap<String, Node>());
            
            // n
            int n = 4;

            // Predictor
            NextWordPredictor predictorCat = new NextWordPredictor(hashTreeCat, categories, n);
            NextWordPredictor predictorNocat = new NextWordPredictor(hashTreeNocat, new ArrayList<Category>(), n);

            System.out.println("Created predictor. Please, enter words..."); 

            int numCorrectGuessesCat = 0;
            int numWrongGuessesCat = 0;
            int numCorrectGuessesNocat = 0;
            int numWrongGuessesNocat = 0;

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
                ArrayList<String> predictionsCat = predictorCat.nextWord(previousWords);
                ArrayList<String> predictionsNocat = predictorNocat.nextWord(previousWords);
                
                // No predictions counts as wrong guess
                // Guess the first among the suggested predictions or "" if no predition
                String guessCat = predictionsCat.isEmpty() ? "" : predictionsCat.get(0);
                String guessNocat = predictionsNocat.isEmpty() ? "" : predictionsNocat.get(0);

                // Print the result
                String catCorrect;
                if (guessCat.equals(correctWord)) {
                    catCorrect = "C";
                    numCorrectGuessesCat++;
                } else {
                    catCorrect = " ";
                    numWrongGuessesCat++;
                }

                String nocatCorrect;
                if (guessNocat.equals(correctWord)) {
                    nocatCorrect = "C";
                    numCorrectGuessesNocat++;
                } else {
                    nocatCorrect = " ";
                    numWrongGuessesNocat++;
                }

                if (catCorrect != nocatCorrect) {
                    System.out.printf("Previous words: %-30s Correct: %-12s   Cat: %-12s   %s   Nocat: %-12s   %s", 
                        sb.toString(), correctWord, guessCat, catCorrect, guessNocat, nocatCorrect);
                    System.out.println();
                }
            }

            // Sum things up
            int totalGuessesCat = numCorrectGuessesCat + numWrongGuessesCat;
            int totalGuessesNocat = numCorrectGuessesNocat + numWrongGuessesNocat;
            assert (totalGuessesCat == totalGuessesNocat);
            int totalGuesses = totalGuessesCat;

            System.out.println();
            System.out.println("--- DONE ---");
            System.out.printf("Correct guesses cat:       %6d / %d\n", numCorrectGuessesCat, totalGuesses);
            System.out.printf("Wrong guesses cat:         %6d / %d\n", numWrongGuessesCat, totalGuesses);
            System.out.printf("Ratio: %f\n", (float) numCorrectGuessesCat / totalGuesses);
            
            System.out.printf("Correct guesses no cat:    %6d / %d\n", numCorrectGuessesNocat, totalGuesses);
            System.out.printf("Wrong guesses no cat:      %6d / %d\n", numWrongGuessesNocat, totalGuesses);
            System.out.printf("Ratio: %f\n", (float) numCorrectGuessesNocat / totalGuesses);
        }
    }

    private static long getMillisecondsSinceTime(long time) {
        long t = System.currentTimeMillis();
        return t - time;
    }
}
