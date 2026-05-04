/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import cs2725.api.List;
import cs2725.api.SimpleMap;

/**
 * Unsorted ArrayList based implementation of a map.
 * 
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class SimpleUnsortedMap<K, V> implements SimpleMap<K, V> {

    private List<Entry<K, V>> entries;

    public SimpleUnsortedMap() {
        entries = new ArrayList<>();
    }

    @Override
    public V get(K key) {
        int index = findIndex(key);
        return (index != -1) ? entries.getItem(index).getValue() : null;
    }

    @Override
    public void put(K key, V value) {
        int index = findIndex(key);
        if (index != -1) {
            entries.getItem(index).setValue(value);
        } else {
            entries.insertItem(new Entry<>(key, value));
        }
    }

    @Override
    public V remove(K key) {
        int index = findIndex(key);
        if (index != -1) {
            V value = entries.getItem(index).getValue();
            entries.deleteItemAt(index);
            return value;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return findIndex(key) != -1;
    }

    @Override
    public int size() {
        return entries.size();
    }

    private int findIndex(K key) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.getItem(i).getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return entries.toString();
    }

}