package utils;

/**
 * Stack implementation using a singly linked list.
 */
public class MyLinkedStack<T> implements MyStack<T> {
    private Node<T> top;
    private int size;

    // Node class for linked list
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T value) {
            this.data = value;
            this.next = null;
        }
    }

    public MyLinkedStack() {
        top = null;
        size = 0;
    }

    @Override
    public void push(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
        size++;
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }
        T value = top.data;
        top = top.next;
        size--;
        return value;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }
        return top.data;
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public int size() {
        return size;
    }
}
