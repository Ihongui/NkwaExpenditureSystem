package utils;

/**
 * Generic interface for List-like structures.
 */
public interface MyList<T> extends Iterable<T> {
    void add(T item);
    T get(int index);
    void remove(int index);
    int size();
    boolean isEmpty();
    void clear();
    void set(int index, T item);
    

}
