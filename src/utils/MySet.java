package utils;

/**
 * Interface for a Set (unique values).
 */
public interface MySet<T> extends Iterable<T>{
    boolean add(T item);          // Add unique item
    boolean contains(T item);     // Check existence
    boolean remove(T item);       // Remove item
    int size();                   // Total items
    boolean isEmpty();            // Is set empty
    void clear();                 // Remove all
}
