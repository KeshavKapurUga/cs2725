/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api;

import java.util.Iterator;
import java.util.Objects;

/**
 * A generic Map interface that supports basic key-value operations.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public interface Map<K, V> extends Iterable<K> {

    /**
     * A key-value pair entry in the map.
     *
     * @param <K> The type of keys.
     * @param <V> The type of values.
     */
    class Entry<K, V> {
        private final K key;
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
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry<?, ?> other = (Entry<?, ?>) obj;
            return Objects.equals(key, other.key) && Objects.equals(value, other.value);
        }

    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of entries in this map
     */
    int size();

    /**
     * Returns true if this map contains no key-value mappings.
     *
     * @return true if this map is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key the key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the key, false otherwise
     */
    boolean containsKey(K key);

    /**
     * Returns the value to which the specified key is mapped, or null if this map
     * contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null
     *         if there is no mapping for the key
     */
    V get(K key);

    /**
     * Returns an iterator over the keys in this map. The iterator can become
     * invalid if the map is modified during or before the use of the iterator.
     * 
     * @return an iterator over the keys
     */
    @Override
    Iterator<K> iterator();

    /**
     * Returns a set of the keys contained in this map. The keyset can become
     * invalid if the map is modified during or before the use of the keyset.
     *
     * @return a set containing all the keys in the map
     */
    Set<K> keySet();

    /**
     * Returns a set of the entries contained in this map. The entry set can become
     * invalid if the map is modified during or before its usage.
     *
     * @return a set containing all the entries in the map
     */
    Set<Entry<K, V>> entrySet();

    /**
     * Associates the specified value with the specified key in this map. If the map
     * previously contained a mapping for the key, the old value is replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with key, or null if there
     *         was no mapping for key
     */
    V put(K key, V value);

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was
     *         no mapping for key
     */
    V remove(K key);

    /**
     * Removes all of the mappings from this map. The map will be empty
     * after this call returns.
     */
    void clear();

    /**
     * If the specified key is not already in the map or the value of the key is
     * currently null, adds the key with the given value and returns null, otherwise
     * returns the current value.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key if the key is not
     *              already in the map
     * @return the previous value associated with key if it was already present,
     *         otherwise null
     */
    V putIfAbsent(K key, V value);

    /**
     * Returns the value to which the specified key is mapped, or defaultValue if
     * this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the value to be returned if there is no mapping
     * @return the value to which the specified key is mapped, or defaultValue if
     *         there is no mapping
     */
    V getOrDefault(K key, V defaultValue);

    /**
     * Returns a new map containing the same key-value mappings as this map.
     *
     * @return a copy of this map
     */
    Map<K, V> copy();

}
