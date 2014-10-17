import java.util.ArrayList;


public class Node {
	
	public String word;
	public int occurences;
	public Node parent;
	public ArrayList<Node> children;
	
	public Node(Node parent, String word ) {
		this.word = word;
		this.parent = parent;
		occurences = 1;
		children = new ArrayList<Node>();
	}
	
	public void addChild(Node node) {
		children.add(node);
	}

    /**
     * Returns the child of this node that contains the parameter
     */
	public Node getChildWithWord(String word) {
		for (Node childNode : children) {
			if (childNode.word.equals(word)) {
				return childNode;
			}
		}
		return null;
	}
	
}

