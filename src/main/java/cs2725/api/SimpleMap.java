/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api;

/**
 * Interface representing a simplified map.
 * 
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public interface SimpleMap<K, V> {

    /**
     * A simple key-value pair entry.
     */
    class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key + ": " + value;
        }
    }

    /**
     * Looks up the value associated with the given key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if the key is
     *         not in the map
     */
    V get(K key);

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is
     * replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    void put(K key, V value);

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was no
     *         mapping for key
     */
    V remove(K key);

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key
     */
    boolean containsKey(K key);

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    int size();

}
