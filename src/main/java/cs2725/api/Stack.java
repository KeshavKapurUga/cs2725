/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api;

/**
 * Represents a stack data structure. A stack is a collection of elements that
 * supports two main operations: push and pop. The push operation adds an
 * element to the top of the stack, and the pop operation removes the element at
 * the top of the stack. The stack follows the Last-In-First-Out (LIFO)
 * principle.
 * 
 * @param <T> the type of elements in the stack
 */
public interface Stack<T> {

    /**
     * Pushes an element onto the top of the stack.
     * 
     * @param item the element to be pushed
     */
    public void push(T item);

    /**
     * Removes the element at the top of the stack and returns it.
     * 
     * @return the element at the top of the stack
     * @throws IllegalStateException if the stack is empty
     */
    public T pop();

    /**
     * Retrieves the element at the top of the stack without removing it.
     * 
     * @return the element at the top of the stack
     * @throws IllegalStateException if the stack is empty
     */
    public T peek();

    /**
     * Returns true if the stack is empty, false otherwise.
     * 
     * @return true if the stack is empty, false otherwise
     */
    public boolean isEmpty();

    /**
     * Returns the number of elements in the stack.
     * 
     * @return the number of elements in the stack
     */
    public int size();

}
