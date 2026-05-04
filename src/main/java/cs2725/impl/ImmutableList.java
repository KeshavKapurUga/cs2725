/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import java.util.Comparator;
import java.util.Iterator;

import cs2725.api.List;

/**
 * This is an immutable list implementation. It does not allow any modification
 * to the list after it is created. All methods that modify the list will throw
 * an UnsupportedOperationException. This does not prevent the user from
 * modifying the objects stored in the list.
 */
public class ImmutableList<T> implements List<T> {

    private final List<T> list;

    /**
     * Constructs an ImmutableList with the given list.
     *
     * @param list The list to be wrapped in the ImmutableList.
     */
    private ImmutableList(List<T> list) {
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
        this.list = list;
    }

    @Override
    public void insertItem(T item) {
        throw new UnsupportedOperationException("The list is immutable. Cannot insert item.");
    }

    @Override
    public void deleteItemAt(int index) {
        throw new UnsupportedOperationException("The list is immutable. Cannot delete item.");
    }

    @Override
    public void deleteItem(T item) {
        throw new UnsupportedOperationException("The list is immutable. Cannot delete item.");
    }

    @Override
    public void setItem(int index, T item) {
        throw new UnsupportedOperationException("The list is immutable. Cannot set item.");
    }

    @Override
    public T getItem(int index) {
        return list.getItem(index);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("The list is immutable. Cannot clear.");
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public int searchItem(T item) {
        return list.searchItem(item);
    }

    @Override
    public void sort(Comparator<T> comparator) {
        throw new UnsupportedOperationException("The list is immutable. Cannot sort.");
    }

    @Override
    public List<T> copy() {
        return list.copy();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public T[] toArray() {
        return list.toArray();
    }

    /**
     * Creates an ImmutableList from a given list.
     *
     * @param list The list to be wrapped in the ImmutableList.
     * @return An ImmutableList containing the elements of the given list.
     */
    public static <T> ImmutableList<T> of(List<T> list) {
        if (list instanceof ImmutableList) {
            // This prevents unnecessary wrapping of an already immutable list.
            return (ImmutableList<T>) list;
        }
        return new ImmutableList<>(list);
    }

}
