package managers;

import utils.FileStorage;
import utils.MyArrayList;
import utils.MyHashSet;
import utils.MyList;
import utils.MySet;

/**
 * Manages a set of unique expenditure categories using custom MySet.
 */
public class CategoryManager {

    private final MySet<String> categories;

    public CategoryManager() {
        categories = new MyHashSet<>();
    }

    /**
     * Adds a new category if it doesn't already exist.
     * @param category Name of the category to add
     * @return true if added, false if duplicate
     */
    public boolean addCategory(String category) {
        return categories.add(category);
    }

    /**
     * Returns all categories as a MySet.
     * @return MySet of categories
     */
    public MySet<String> getAllCategories() {
        return categories;
    }

    /**
     * Checks if a given category exists.
     * @param category Name to check
     * @return true if category exists
     */
    public boolean containsCategory(String category) {
        return categories.contains(category);
    }

    /**
     * Clears all categories.
     */
    public void clearCategories() {
        categories.clear();
    }

    /**
     * Returns the total number of categories.
     * @return int
     */
    public int size() {
        return categories.size();
    }
    // ✅ Save categories to file
    public void saveToFile(String filepath) {
        MyList<String> list = new MyArrayList<>();
        for (String cat : categories) {
            list.add(cat);
        }
        FileStorage.writeLines(filepath, list);
        
    }

    // ✅ Load categories from file
    public void loadFromFile(String filepath) {
        for (String line : FileStorage.readLines(filepath)) {
            if (!line.isBlank()) {
                categories.add(line.trim().toLowerCase());
            }
        }
    }
}
