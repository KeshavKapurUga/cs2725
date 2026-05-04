/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

import cs2725.api.List;

/**
 * Implementation of an unordered array backed list.
 */
public class ArrayList<T> implements List<T> {

    protected static final int DEFAULT_INIT_SIZE = 4;

    protected int size;
    protected T[] store;
    protected int initSize = DEFAULT_INIT_SIZE;

    public ArrayList() {
        init(DEFAULT_INIT_SIZE);
    }

    public ArrayList(int initSize) {
        init(initSize);
    }

    @SuppressWarnings("unchecked")
    private void init(int initSize) {
        this.initSize = initSize;
        size = 0;
        store = (T[]) new Object[this.initSize];
    }

    @Override
    public void insertItem(T item) {
        if (size == store.length) {
            resize(store.length * 2);
        }

        store[size++] = item;
    }

    @Override
    public void deleteItemAt(int index) {
        checkIndex(index);
        for (int i = index + 1; i < size; ++i) {
            store[i - 1] = store[i];
        }
        store[--size] = null;
        if (store.length >= initSize * 2 && size == store.length / 4) {
            resize(store.length / 2);
        }
    }

    @Override
    public void deleteItem(T item) {
        int index = searchItem(item);
        if (index != -1) {
            deleteItemAt(index);
        }
    }

    @Override
    public T getItem(int index) {
        checkIndex(index);
        return store[index];
    }

    @Override
    public void setItem(int index, T item) {
        checkIndex(index);
        store[index] = item;
    }

    @Override
    public void clear() {
        init(initSize);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int searchItem(T item) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(store[i], item)) {
                return i;
            }
        }
        return -1; // Item not found.
    }

    @Override
    public void sort(Comparator<T> comparator) {
        Arrays.sort(store, 0, size, comparator);
    }

    protected void resize(int capacity) {
        store = Arrays.<T>copyOf(store, capacity);
    }

    protected void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(store[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public List<T> copy() {
        ArrayList<T> newList = new ArrayList<>();
        newList.store = Arrays.copyOf(this.store, this.store.length);
        newList.size = this.size;
        newList.initSize = this.initSize;
        return newList;
    }

    /**
     * Note: The behavior is undefined if the map is modified during or before the
     * use of the returned iterator object.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new IndexOutOfBoundsException("No more elements.");
                }
                return store[index++];
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof List<?>)) {
            return false;
        }
        List<?> other = (List<?>) obj;
        if (this.size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!Objects.equals(this.getItem(i), other.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(store), size);
    }

    @Override
    public T[] toArray() {
        return Arrays.copyOf(store, size);
    }

}
