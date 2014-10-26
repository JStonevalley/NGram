import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jonas on 2014-10-26.
 */
public class noCategoriesDictionary {
    private Kattio io;
    private String path;
    private ArrayList<Category> categories;

    public noCategoriesDictionary(String path, ArrayList<Category> categories) throws FileNotFoundException{
        this.path = path;
        this.categories = categories;
        io = new Kattio(new FileInputStream(new File(path)));
        String potentialWord;
        while((potentialWord = io.getWord()) != null){
            Category category = null;
            if ((category = Category.categorize(potentialWord, categories)) == null) {
                System.out.println(potentialWord);
            }
        }
    }
}
