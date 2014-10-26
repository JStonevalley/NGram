import java.util.*;

public class NextWordPredictor {
	
	private static final String UNKNOWN = "<unk>";

	private int maxN;
	private HashMap<String, Node> model;
	private ArrayList<String> mostCommonWords; // The most common words in the model, used when n = 1
	private List<Category> categories;

	public NextWordPredictor(HashMap<String, Node> model, List<Category> categories, int maxN) {
		this.model = model;
		this.categories = categories;
		this.maxN = maxN;

		// Read in most common words
		mostCommonWords = new ArrayList<String>();
		int max_occurences = -1;
		for (Node child : model.values()) {
			if (child.occurences > max_occurences) {
				max_occurences = child.occurences;
				mostCommonWords.clear();
				mostCommonWords.add(child.word);
			} else if (child.occurences == max_occurences) {
				mostCommonWords.add(child.word);
			}
		}
	}

	public ArrayList<String> nextWord(List<String> previousWords) {
		if (previousWords.size() > maxN - 1) {
			throw new RuntimeException("Too many previous words.");
		} else if (previousWords.size() == 0) {
			return mostCommonWords; // No previous words, return most common words overall
		}

		Node node = null;
		String word = previousWords.get(0);
		Category category = null;

		// Get the first node
		node = model.get(word);
		if (node == null && (category = Category.categorize(word, categories)) != null)
			node = model.get(category.getName());
		if (node == null)
			node = model.get(UNKNOWN);
		if (node == null)
			return nextWord(previousWords.subList(1, previousWords.size())); // Backoff

		// Iterate over nodes to visit
		for (int i = 1; i < previousWords.size(); i++) {

			Node parent = node;
			word = previousWords.get(i);

			node = parent.getChildWithWord(word);
			if (node == null && (category = Category.categorize(word, categories)) != null)
				node = parent.getChildWithWord(category.getName());
			if (node == null)
				node = parent.getChildWithWord(UNKNOWN);
			if (node == null)
				return nextWord(previousWords.subList(1, previousWords.size())); // Backoff

		}

		// We're at the last node, return words of the most frequent children
		ArrayList<Node> children = node.getMostFrequentChildren(false);
		ArrayList<String> ret = new ArrayList<String>(children.size());
		for (Node child : children) {
			ret.add(child.word);
		}
		return ret;
	}


}

