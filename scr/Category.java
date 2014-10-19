import java.util.List;

public abstract class Category {

	private String name;

	Category(String name) {
		this.name = "<" + name + ">";
	}

	public String getName() {
		return name;
	}

	public abstract boolean includes(String word); 

	public static boolean isCategory(String word) {
		return word.startsWith("<") && word.endsWith(">");
	}

	public static Category categorize(String word, List<Category> categories) {
		for (Category category : categories) {
			if (category.includes(word)) {
				return category;
			}
		}
		return null;
	}
}