/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import cs2725.api.Set;
import cs2725.api.Map;

import java.util.Iterator;

/**
 * A HashSet implementation using a HashMap as the underlying data structure.
 *
 * @param <E> the type of elements maintained by this set
 */
public class HashSet<E> implements Set<E> {

    private static final float DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75f;
    private static final Object DUMMY = new Object();

    private final Map<E, Object> map;

    /**
     * Constructs an empty HashSet with default capacity.
     */
    public HashSet() {
        this.map = new HashMap<>();
    }

    /**
     * Constructs a new HashSet with a given initial capacity.
     *
     * @param initialCapacity the initial number of buckets in the underlying map
     */
    public HashSet(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity, DEFAULT_LOAD_FACTOR_THRESHOLD);
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
    public boolean contains(E element) {
        return map.containsKey(element);
    }

    @Override
    public boolean add(E element) {
        return map.putIfAbsent(element, DUMMY) == null;
    }

    @Override
    public boolean remove(E element) {
        return map.remove(element) != null;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<E> copy() {
        Set<E> clone = new HashSet<>();
        for (E key : map.keySet()) {
            clone.add(key);
        }
        return clone;
    }

    /**
     * Note: The behavior is undefined if the map is modified during or before the
     * use of the returned iterator object.
     */
    @Override
    public Iterator<E> iterator() {
        return map.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set<?> other = (Set<?>) obj;
        if (this.size() != other.size()) {
            return false;
        }
        try {
            for (Object element : other) {
                @SuppressWarnings("unchecked")
                E elementE = (E) element;
                if (!contains(elementE)) {
                    return false;
                }
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
        return true;
    }

}
