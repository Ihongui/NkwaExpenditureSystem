package utils;

/**
 * Simple hash map using separate chaining.
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] buckets;
    private int size;

    // Our internal linked list node
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Public interface-compatible entry
    private static class EntryView<K, V> implements MyMap.Entry<K, V> {
        private final K key;
        private final V value;

        EntryView(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];

        // Update value if key exists
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        // Insert new node at head
        Entry<K, V> newNode = new Entry<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;

        // Resize if load factor exceeded
        if ((1.0 * size) / buckets.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V get(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];

        while (head != null) {
            if (head.key.equals(key)) return head.value;
            head = head.next;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V remove(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];
        Entry<K, V> prev = null;

        while (head != null) {
            if (head.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = head.next;
                } else {
                    prev.next = head.next;
                }
                size--;
                return head.value;
            }
            prev = head;
            head = head.next;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = new Entry[oldBuckets.length * 2];
        size = 0;

        for (Entry<K, V> head : oldBuckets) {
            while (head != null) {
                put(head.key, head.value); // rehash all
                head = head.next;
            }
        }
    }

    @Override
    public Iterable<MyMap.Entry<K, V>> entrySet() {
        MyList<MyMap.Entry<K, V>> entries = new MyArrayList<>();

        for (Entry<K, V> head : buckets) {
            while (head != null) {
                entries.add(new EntryView<>(head.key, head.value));
                head = head.next;
            }
        }

        return entries;
    }
    @Override
    public Iterable<K> keySet() {
        MyList<K> keys = new MyArrayList<>();
        for (Entry<K, V> bucket : buckets) {
            while (bucket != null) {
                keys.add(bucket.key);
                bucket = bucket.next;
            }
        }
        return keys;
    }


    @Override
    public MyList<V> values() {
        MyList<V> list = new MyArrayList<>();

        for (Entry<K, V> head : buckets) {
            while (head != null) {
                list.add(head.value);
                head = head.next;
            }
        }

        return list;
    }
}
