import java.util.*;


public class Node {
	
	public String word;
	public int occurences;
	public Node parent;
	public TreeMap<String, Node> children; // TODO ineffective, suggestion: TreeSet<Node>(<comparator on word>) or TreeMap<String, Node> where word is the key
	
	public Node(Node parent, String word) {
		this.word = word;
		this.parent = parent;
		occurences = 1;
		children = new TreeMap<String, Node>();
	}
	
	public void addChild(Node node) {
		children.put(node.word, node);
	}

    /**
     * Returns the child of this node that contains the parameter
     */
	public Node getChildWithWord(String word) {
		return children.get(word);
	}

	public ArrayList<Node> getMostFrequentChildren(boolean includeCategories) {
		ArrayList<Node> ret = new ArrayList<Node>();
		int max_occurences = -1;
		for (Node child : children.values()) {
			if (!includeCategories && Category.isCategory(child.word))
				continue;

			if (child.occurences > max_occurences) {
				max_occurences = child.occurences;
				ret.clear();
				ret.add(child);
			} else if (child.occurences == max_occurences) {
				ret.add(child);
			}
		}
		return ret;
	}
}

