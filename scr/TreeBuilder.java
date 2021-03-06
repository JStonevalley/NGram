import java.io.*;
import java.util.HashMap;
import java.util.Iterator;


public class TreeBuilder {
	
	private String INPUT_FILE_NAME;
	private static int N = 4;
	
	public TreeBuilder(String fileName) {
        INPUT_FILE_NAME = fileName;
	}
	
	public HashMap<String, Node> buildTree(HashMap<String, Node> treeHash) {
		BufferedReader in = null;
		try {
			File file = new File(INPUT_FILE_NAME);
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
					
					//if first word in sequence
					if (i == 0) {
                        //if word does not exist at top of tree, then add it.
						if (startNode == null) {
							startNode = new Node(null, word);
							treeHash.put(word, startNode);
						} else {
							startNode.occurences++;
						}
                        prevNode = startNode; //update previous node
					} else {
						Node child;
                        //if node does not exist as a child to previous node, then add it
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
        return treeHash;
	}

    /**
     * Prints a text representation of the tree. The printing is done depth-first.
     */
	public void printTree(HashMap<String, Node> treeHash, File file) {
        Kattio out = null;
        if (file != null)
            try {
                out = new Kattio(System.in, new FileOutputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
		Iterator<String> words = treeHash.keySet().iterator();
		for (int i = 0; i < treeHash.size(); i++) {
			String word = words.next();
			Node child = treeHash.get(word);
            if (out == null) {
                printNode(child, 0);
                System.out.println("");
            }
            else {
                printNodeToFile(child, 0, out);
                out.print("\n");
            }
            // add newline between trees
            if (out != null)
                out.write("\n");
            else
                System.out.println("");
		}

        if (out != null)
            out.close();
	}
	
	private void printNode(Node node, int depth) {
		if (depth == N) {
			return;
		}
		System.out.println("{" + node.word + "(" + node.occurences + "), ");
		for (Node child : node.children.values()) {
		// for (int i = 0; i < node.children.size(); i++) {
			printNode(child, depth + 1);
		}
        System.out.print("}");
	}

    private void printNodeToFile(Node node, int depth, Kattio out) {
        if (depth == N) {
            return;
        }
        out.write("{" + node.word + "(" + node.occurences + "), \n");
		for (Node child : node.children.values()) {
        // for (int i = 0; i < node.children.size(); i++) {
			printNodeToFile(child, depth + 1, out);
            // printNodeToFile(node.children.get(i), depth + 1, out);
        }
        out.write("}");
    }
}
