package utils;

/**
 * Interface for Stack data structure (LIFO).
 */
public interface MyStack<T> {
    void push(T item);
    T pop();
    T peek();
    boolean isEmpty();
    int size();
}
