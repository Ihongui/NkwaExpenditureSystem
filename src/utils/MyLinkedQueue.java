package utils;

import java.util.Iterator;

/**
 * Queue implementation using a singly linked list.
 */
public class MyLinkedQueue<T> implements MyQueue<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;

    // Internal node structure
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T item) {
            this.data = item;
            this.next = null;
        }
    }

    public MyLinkedQueue() {
        front = rear = null;
        size = 0;
    }

    @Override
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        T value = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return value;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        return front.data;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = front;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T value = current.data;
                current = current.next;
                return value;
            }
        };
    }
}
