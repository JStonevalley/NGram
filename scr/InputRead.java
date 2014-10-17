import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class InputRead {
	
	public HashMap<String, Node> treeHash;
	private static String FILE_NAME;
	private static int N = 4;
	
	public InputRead(String fileName) {
        FILE_NAME = fileName;
		treeHash = new HashMap<String, Node>();
		readInput();
		System.out.println("F'cking done!");
		printTree();
	}
	
	private void readInput() {
		BufferedReader in = null;
		try {
			File file = new File(FILE_NAME);
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
		}
		
		try {
			String line;
			while ((line = in.readLine()) != null) {
				String[] words = line.split(" ");
				Node prevNode = null;
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					Node startNode = treeHash.get(word);
					
					//if word doesn't exist at top of tree, add it
					if (i == 0) {
						if (startNode == null) {
							startNode = new Node(null, word);
							treeHash.put(word, startNode);
						} else {
							startNode.occurences++;
						}
                        prevNode = startNode;
					} else {
						Node child;
						if ((child = prevNode.getChildWithWord(word)) == null) {
							Node newNode = new Node(prevNode, word);
							prevNode.addChild(newNode);
							prevNode = newNode;
						} else {
							child.occurences++;
                            prevNode = child;
						}
					}
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printTree() {
		Iterator<String> words = treeHash.keySet().iterator();
		for (int i = 0; i < treeHash.size(); i++) {
			String word = words.next();
			Node child = treeHash.get(word);
			printNode(child, 0);
            System.out.println("");
		}
	}
	
	private void printNode(Node node, int depth) {
		if (depth == N) {
			return;
		}
		System.out.println(node.word+"("+node.occurences+"), ");
		for (int i = 0; i < node.children.size(); i++) {
			printNode(node.children.get(i), depth+1);
		}
	}

}
