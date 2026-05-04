/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api;

/**
 * Represents a deque data structure. A deque is a collection of elements that
 * supports four main operations: addFirst, addLast, removeFirst, and removeLast.
 * The addFirst operation adds an element to the front of the deque, the addLast
 * operation adds an element to the end of the deque, the removeFirst operation
 * removes the element at the front of the deque, and the removeLast operation
 * removes the element at the end of the deque. A deque supports both stack-like
 * and queue-like operations.
 *
 * @param <T> the type of elements in the deque
 */
public interface Deque<T> {

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param element the element to add
     */
    void addFirst(T element);

    /**
     * Inserts the specified element at the end of this deque.
     *
     * @param element the element to add
     */
    void addLast(T element);

    /**
     * Retrieves and removes the first element of this deque.
     *
     * @return the head of this deque
     * @throws IllegalStateException if this deque is empty
     */
    T removeFirst();

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws IllegalStateException if this deque is empty
     */
    T removeLast();

    /**
     * Retrieves, but does not remove, the first element of this deque.
     *
     * @return the head of this deque
     * @throws IllegalStateException if this deque is empty
     */
    T peekFirst();

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws IllegalStateException if this deque is empty
     */
    T peekLast();

    /**
     * Returns {@code true} if this deque contains no elements.
     *
     * @return {@code true} if this deque contains no elements
     */
    boolean isEmpty();

    /**
     * Returns the number of elements in this deque.
     *
     * @return the number of elements in this deque
     */
    int size();

}
