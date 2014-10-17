import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Main {

    static String CORPUS_FILE = "data//corpus.txt";
    static String TREE_INPUT_FILE = "data//toInputRead";
    static String TREE_OUTPUT_FILE = "data//output";

    public static void main(String[] args) throws FileNotFoundException {
        //parse corpus
        long time = System.currentTimeMillis();
        Parser parser = new Parser(CORPUS_FILE, TREE_INPUT_FILE);
        parser.generateInput();

        System.out.println(getMillisecondsSinceTime(time)+"ms - CORPUS PARSED");
        //build tree
        TreeBuilder treeBuilder = new TreeBuilder(TREE_INPUT_FILE);
        HashMap<String, Node> hashTree = treeBuilder.buildTree(new HashMap<String, Node>());
        System.out.println(getMillisecondsSinceTime(time)+"ms -TREE BUILT");
//        treeBuilder.printTree(hashTree, null); //print to system.out
        treeBuilder.printTree(hashTree, new File(TREE_OUTPUT_FILE)); //print to file	}
        System.out.println(getMillisecondsSinceTime(time)+"ms -TREE PRINTED/WRITTEN");
    }

    private static long getMillisecondsSinceTime(long time) {
        long t = System.currentTimeMillis();
        return t - time;
    }
}
