/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api;

/**
 * Represents a queue data structure. A queue is a collection of elements that
 * supports two main operations: enqueue and dequeue. The enqueue operation adds
 * an element to the back of the queue, and the dequeue operation removes the
 * element at the front of the queue. The queue follows the First-In-First-Out
 * (FIFO) principle.
 * 
 * @param <T> the type of elements in the queue
 */
public interface Queue<T> {

    /**
     * Adds an element to the back of the queue.
     * 
     * @param item the element to be added
     */
    public void enqueue(T item);

    /**
     * Removes the element at the front of the queue and returns it.
     * 
     * @return the element at the front of the queue
     * @throws IllegalStateException if the queue is empty
     */
    public T dequeue();

    /**
     * Retrieves the element at the front of the queue without removing it.
     * 
     * @return the element at the front of the queue
     * @throws IllegalStateException if the queue is empty
     */
    public T peek();

    /**
     * Returns true if the queue is empty, false otherwise.
     * 
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty();

    /**
     * Returns the number of elements in the queue.
     * 
     * @return the number of elements in the queue
     */
    public int size();

}
