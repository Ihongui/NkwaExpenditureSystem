package utils;

import java.util.Iterator;

/**
 * HashSet implementation backed by custom MyHashMap.
 */
public class MyHashSet<T> implements MySet<T> {

    private MyMap<T, Boolean> map;

    public MyHashSet() {
        this.map = new MyHashMap<>();
    }

    @Override
    public boolean add(T item) {
        if (!map.containsKey(item)) {
            map.put(item, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    @Override
    public boolean remove(T item) {
        if (map.containsKey(item)) {
            map.remove(item);
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        this.map = new MyHashMap<>();
    }

    @Override
    public Iterator<T> iterator() {
        // Wrap the map.entrySet() into a key-only iterator
        return new Iterator<T>() {
            private final Iterator<MyMap.Entry<T, Boolean>> entryIterator = map.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            @Override
            public T next() {
                return entryIterator.next().getKey();
            }
        };
    }
}
