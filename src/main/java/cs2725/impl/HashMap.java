/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import cs2725.api.Map;
import cs2725.api.Set;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Hash based Map implementation using separate chaining.
 * 
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class HashMap<K, V> implements Map<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;

    /*
     * Underlying array of buckets, where each bucket is a singly-linked list
     * of LinkedNode<Map.Entry<K, V>>.
     */
    private LinkedNode<Map.Entry<K, V>>[] table;

    /*
     * The number of key-value entries stored in the map.
     */
    private int size;

    /*
     * Threshold (capacity * loadFactor) at which the table is resized.
     */
    private int sizeThreshold;

    /*
     * Load factor that determines when to resize the table.
     */
    private final double loadFactorThreshold;

    /**
     * Constructs a HashMap with a specified initial capacity and load factor.
     *
     * @param initialCapacity     the initial number of buckets
     * @param loadFactorThreshold the load factor threshold for resizing
     */
    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity, double loadFactorThreshold) {
        if (initialCapacity <= 0 || loadFactorThreshold <= 0) {
            throw new IllegalArgumentException("Capacity and load factor must be positive.");
        }
        this.table = (LinkedNode<Map.Entry<K, V>>[]) new LinkedNode[initialCapacity];
        this.loadFactorThreshold = loadFactorThreshold;
        this.sizeThreshold = (int) (initialCapacity * loadFactorThreshold);
        this.size = 0;
    }

    /**
     * Constructs a HashMap with default capacity (16) and load factor (0.75).
     */
    public HashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR_THRESHOLD);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        return findEntry(key) != null;
    }

    @Override
    public V get(K key) {
        Map.Entry<K, V> entry = findEntry(key);
        return (entry == null) ? null : entry.getValue();
    }

    /**
     * Note: The behavior is undefined if the map is modified during or before the
     * use of the returned iterator object.
     */
    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            private int index;
            private LinkedNode<Map.Entry<K, V>> current;

            {
                index = 0;
                current = table[index];
                advance();
            }

            private void advance() {
                while (current == null && index < table.length - 1) {
                    current = table[++index];
                }
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements are available.");
                }
                K key = current.getValue().getKey();
                current = current.getNext();
                advance();
                return key;
            }
        };
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        cs2725.api.List<K> keysList = new ArrayList<>();
        for (K key : this) {
            keys.add(key);
            keysList.insertItem(key);
        }
        for (K key : keysList) {
            if (!keys.contains(key)) {
                throw new AssertionError("A key is missing from the key set: " + key);
            }
        }
        for (Map.Entry<K, V> entry : entrySet()) {
            if (!keys.contains(entry.getKey())) {
                throw new AssertionError("A key is missing from the key set: " + entry.getKey());
            }
        }
        return keys;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new HashSet<>();
        for (LinkedNode<Map.Entry<K, V>> bucket : table) {
            LinkedNode<Map.Entry<K, V>> current = bucket;
            while (current != null) {
                entries.add(current.getValue());
                current = current.getNext();
            }
        }
        return entries;
    }

    @Override
    public V put(K key, V value) {
        return insertOrUpdate(key, value, false);
    }

    @Override
    public V remove(K key) {
        int index = hash(key);
        LinkedNode<Map.Entry<K, V>> previous = null;
        LinkedNode<Map.Entry<K, V>> current = table[index];

        while (current != null) {
            K currentKey = current.getValue().getKey();
            if (Objects.equals(currentKey, key)) {
                if (previous == null) {
                    table[index] = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }
                size--;
                return current.getValue().getValue();
            }
            previous = current;
            current = current.getNext();
        }
        return null;
    }

    @Override
    public void clear() {
        @SuppressWarnings("unchecked")
        LinkedNode<Map.Entry<K, V>>[] newTable = (LinkedNode<Map.Entry<K, V>>[]) new LinkedNode[table.length];
        table = newTable;
        size = 0;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return insertOrUpdate(key, value, true);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        Map.Entry<K, V> entry = findEntry(key);
        return (entry == null) ? defaultValue : entry.getValue();
    }

    @Override
    public Map<K, V> copy() {
        HashMap<K, V> clone = new HashMap<>(table.length, loadFactorThreshold);
        for (Map.Entry<K, V> entry : entrySet()) {
            clone.put(entry.getKey(), entry.getValue());
        }
        return clone;
    }

    /**
     * Inserts or updates an entry with the specified key-value pair. If the old
     * value is null it will be updated regardless of the onlyIfAbsent flag.
     * 
     * @param key          the key to insert or update
     * @param value        the value associated with the key
     * @param onlyIfAbsent if true, do not update existing values
     * @return the old value if present, otherwise null
     */
    private V insertOrUpdate(K key, V value, boolean onlyIfAbsent) {
        int index = hash(key);
        LinkedNode<Map.Entry<K, V>> current = table[index];

        while (current != null) {
            Map.Entry<K, V> entry = current.getValue();
            if (Objects.equals(entry.getKey(), key)) {
                V oldValue = entry.getValue();
                // Update the value if the old value is null or if this is a put call.
                if (oldValue == null || !onlyIfAbsent) {
                    entry.setValue(value);
                }
                return oldValue;
            }
            current = current.getNext();
        }

        // Key not found, add a new node to the front of the list
        Map.Entry<K, V> mapEntry = new Map.Entry<>(key, value);
        table[index] = new LinkedNode<>(mapEntry, table[index]);
        size++;

        if (size > sizeThreshold) {
            resize();
        }
        return null;
    }

    /**
     * Finds the entry with the specified key in the map.
     * 
     * @param key the key to search for
     * @return the node containing the entry, or null if not found
     */
    private Map.Entry<K, V> findEntry(K key) {
        int index = hash(key);
        LinkedNode<Map.Entry<K, V>> current = table[index];
        while (current != null) {
            if (Objects.equals(current.getValue().getKey(), key)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    /**
     * Computes the bucket index for a given key using modular hashing.
     */
    private int hash(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return (hash & 0x7fffffff) % table.length;
    }

    /**
     * Doubles the capacity of the table and re-hashes existing entries.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        LinkedNode<Map.Entry<K, V>>[] oldTable = table;
        table = (LinkedNode<Map.Entry<K, V>>[]) new LinkedNode[newCapacity];

        // Re-hash each node
        for (LinkedNode<Map.Entry<K, V>> bucket : oldTable) {
            LinkedNode<Map.Entry<K, V>> current = bucket;
            while (current != null) {
                // Record the next node before modifying the current node.
                LinkedNode<Map.Entry<K, V>> next = current.getNext();

                // Compute the new index for the current node.
                K key = current.getValue().getKey();
                int newIndex = hash(key); // Note: new table should be set as the table before calling hash.

                // Reuse the current node to avoid creating new nodes.
                current.setNext(table[newIndex]);
                table[newIndex] = current;

                // Move to the next node.
                current = next;
            }
        }
        sizeThreshold = (int) (newCapacity * loadFactorThreshold);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map<?, ?> other = (Map<?, ?>) obj;
        if (this.size() != other.size()) {
            return false;
        }
        try {
            for (Map.Entry<?, ?> entry : other.entrySet()) {
                @SuppressWarnings("unchecked")
                K key = (K) entry.getKey();
                @SuppressWarnings("unchecked")
                V value = (V) entry.getValue();
                if (!this.containsKey(key) || !Objects.equals(this.get(key), value)) {
                    return false;
                }
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Map.Entry<K, V> entry : entrySet())
            h += entry.hashCode(); // Order independent.
        return h;
    }

}
