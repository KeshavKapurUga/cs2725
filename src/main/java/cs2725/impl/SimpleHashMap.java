/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import java.util.Objects;
import cs2725.api.SimpleMap;

public class SimpleHashMap<K, V> implements SimpleMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    private LinkedNode<Entry<K, V>>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public SimpleHashMap() {
        buckets = new LinkedNode[INITIAL_CAPACITY];
        size = 0;
    }

    private int hash(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return (hash & 0x7FFFFFFF) % buckets.length;
    }

    private double loadFactor() {
        return (double) size / buckets.length;
    }

    private Entry<K, V> getEntry(K key) {
        int index = hash(key);
        LinkedNode<Entry<K, V>> node = buckets[index];

        while (node != null) {
            if (Objects.equals(node.getValue().getKey(), key)) {
                return node.getValue();
            }
            node = node.getNext();
        }
        return null;
    }

    @Override
    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        return (entry != null) ? entry.getValue() : null;
    }

    @Override
    public void put(K key, V value) {
        Entry<K, V> entry = getEntry(key);

        if (entry != null) {
            entry.setValue(value);
            return;
        }
        
        if (loadFactor() >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int index = hash(key);
        buckets[index] = new LinkedNode<>(new Entry<>(key, value), buckets[index]);
        size++;
    }

    @Override
    public boolean containsKey(K key) {
        return getEntry(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedNode<Entry<K, V>>[] oldBuckets = buckets;
        buckets = new LinkedNode[oldBuckets.length * 2];
        size = 0;

        for (LinkedNode<Entry<K, V>> node : oldBuckets) {
            while (node != null) {
                put(node.getValue().getKey(), node.getValue().getValue());
                node = node.getNext();
            }
        }
    }

    @Override
    public V remove(K key) {
        int index = hash(key);
        LinkedNode<Entry<K, V>> node = buckets[index];
        LinkedNode<Entry<K, V>> prev = null;

        while (node != null) {
            if (Objects.equals(node.getValue().getKey(), key)) {
                if (prev == null) {
                    buckets[index] = node.getNext();
                } else {
                    prev.setNext(node.getNext());
                }
                size--;
                return node.getValue().getValue();
            }
            prev = node;
            node = node.getNext();
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (LinkedNode<Entry<K, V>> bucket : buckets) {
            LinkedNode<Entry<K, V>> node = bucket;
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.getValue().toString());
                first = false;
                node = node.getNext();
            }
        }

        sb.append("]");
        return sb.toString();
    }

}
