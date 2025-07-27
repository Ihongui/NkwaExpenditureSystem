package utils;

/**
 * Interface for a simple map (key-value store).
 */
public interface MyMap<K, V> {
    void put(K key, V value);             // Add or update
    V get(K key);                         // Retrieve value
    boolean containsKey(K key);           // Check if key exists
    V remove(K key);                      // Remove entry
    int size();                           // Count entries
    boolean isEmpty();                    // Is map empty

    Iterable<Entry<K, V>> entrySet();     // Required for for-each loops

    MyList<V> values();                   // 🔥 Now included!

    /**
     * Interface representing a map entry (key-value pair).
     */
    interface Entry<K, V> {
        K getKey();
        V getValue();
    }
    default void putIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            put(key, value);
        }
    }

    default V getOrDefault(K key, V defaultValue) {
        V val = get(key);
        return (val != null) ? val : defaultValue;
    }

    Iterable<K> keySet();

}
