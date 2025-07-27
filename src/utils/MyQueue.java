package utils;

/**
 * Interface for Queue data structure (FIFO).
 */
public interface MyQueue<T> extends Iterable<T> {
    void enqueue(T item);   // add to rear
    T dequeue();            // remove from front
    T peek();               // view front item
    boolean isEmpty();
    int size();
}
