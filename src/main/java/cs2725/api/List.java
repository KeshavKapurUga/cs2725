/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api;

import java.util.Comparator;
import java.util.Iterator;

import cs2725.impl.ArrayList;

/**
 * An interface representing a generic list structure.
 *
 * @param <T> the type of elements held in this list
 */
public interface List<T> extends Iterable<T> {

    /**
     * If the list is unsorted, adds an item as the last
     * item of the list.
     * 
     * If the list is sorted the item should be inserted at position i such that
     * after the insertion,
     * - 0 <= i < size()
     * - if i > 0 then getItem(i - 1) <= item
     * - if i < size() - 1 then item < getItem(i + 1).
     * 
     * @param item the item to be added
     */
    public void insertItem(T item);

    /**
     * Deletes the item at the specified index from the list.
     *
     * @param index the position of the item to be deleted
     * @throws IndexOutOfBoundsException if the index is invalid.
     */
    public void deleteItemAt(int index);

    /**
     * Deletes the first occurrence of the item from the list.
     *
     * @param item the item to be removed
     */
    public void deleteItem(T item);

    /**
     * Sets the item at the specified index in the list.
     * 
     * If the list is unsorted, replaces the item at the given index with the
     * specified item.
     * 
     * If the list is sorted, the effect is equivalent to deleting the item at the
     * specified index and then inserting the new item at its correct position to
     * maintain the sorted order. See {@link #insertItem} for details on insertion
     * in a sorted list.
     * 
     * @param index the position of the item to be replaced
     * @param item  the new item to set at the specified position
     * @throws IndexOutOfBoundsException if the index is invalid (index < 0 || index
     *                                   >= size())
     */
    public void setItem(int index, T item);

    /**
     * Retrieves an item from the list at the specified index.
     *
     * @param index the position of the item to retrieve
     * @return the item at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid (index < 0 || index
     *                                   >= size())
     */
    public T getItem(int index);

    /**
     * Clears all items from the list.
     */
    public void clear();

    /**
     * Returns the number of items currently in the list.
     *
     * @return the size of the list
     */
    public int size();

    /**
     * Searches for an item in the list and returns the index of the first
     * occurrence.
     *
     * @param item the item to search for
     * @return the index of the first occurrence if found. -1 if not found.
     */
    public int searchItem(T item);

    /**
     * Sorts the list using the given comparator.
     *
     * @param comparator the comparator to determine the order.
     * @throws NullPointerException if the comparator is null.
     */
    void sort(Comparator<T> comparator);

    /**
     * Creates and returns a copy of this list.
     *
     * @return a new List<T> instance containing the same elements as this list.
     */
    List<T> copy();

    /**
     * Returns an iterator over elements of type T. The iterator can become invalid
     * if the list is modified during or before the use of the iterator.
     *
     * @return an Iterator over the elements in this list.
     */
    @Override
    Iterator<T> iterator();

    /**
     * Returns an array containing all of the elements in this list in the order
     * they appear in the list.
     *
     * @return an array containing all of the elements in this list in proper
     *         sequence
     */
    T[] toArray();

    /**
     * Creates a new List instance with the specified elements.
     *
     * @param <T>      the type of elements in the list
     * @param elements the elements to be added to the list
     * @return a new List instance containing the specified elements
     */
    @SafeVarargs
    static <T> List<T> of(T... elements) {
        List<T> list = new ArrayList<>();
        for (T element : elements) {
            list.insertItem(element);
        }
        return list;
    }

}
