/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api;

import java.util.Iterator;

/**
 * A generic Set interface that supports basic set operations.
 *
 * @param <E> the type of elements maintained by this set
 */
public interface Set<E> extends Iterable<E> {

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of elements in this set
     */
    int size();

    /**
     * Returns true if this set contains no elements.
     *
     * @return true if this set is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns true if this set contains the specified element.
     *
     * @param element the element whose presence in this set is to be tested
     * @return true if this set contains the specified element, false otherwise
     */
    boolean contains(E element);

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param element element to be added to this set
     * @return true if the element was added, false if it was already present
     */
    boolean add(E element);

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param element element to be removed from this set
     * @return true if the element was removed, false if it was not present
     */
    boolean remove(E element);

    /**
     * Removes all elements from this set. The set will be empty after this call
     * returns.
     */
    void clear();

    /**
     * Returns an iterator over the elements in this set. The iterator can become
     * invalid if the set is modified during or before the use of the iterator.
     *
     * @return an iterator over elements in this set
     */
    @Override
    Iterator<E> iterator();

    /**
     * Returns a shallow copy of this set.
     *
     * @return a new set containing the same elements as this set
     */
    Set<E> copy();

}
