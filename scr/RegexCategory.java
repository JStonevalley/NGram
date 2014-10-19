import java.util.regex.Pattern;

public class RegexCategory extends Category {
	
	private Pattern pattern;

	public RegexCategory(String name, String regex) {
		super(name);
		pattern = Pattern.compile(regex);
	}

	public boolean includes(String word) {
		return pattern.matcher(word).matches();
	}
}