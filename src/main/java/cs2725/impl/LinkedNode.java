/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

/**
 * Class representing a node in the linked list. Each node contains a
 * value of type {@code T} and a reference to the next node in the list.
 *
 * @param <T> The type of the value stored in the node.
 */
@cs2725.viz.ListNode // For visualization.
public class LinkedNode<T> {

    /**
     * The value stored in the node.
     */
    @cs2725.viz.Value // For visualization.
    private T value;

    /**
     * A reference to the next node in the linked list.
     * This is {@code null} if this is the last node in the list.
     */
    @cs2725.viz.Next // For visualization.
    private LinkedNode<T> next;

    /**
     * Constructs a new node with the specified value.
     * The next node reference is initialized to {@code null}.
     *
     * @param value The value to store in the node.
     */
    public LinkedNode(T value) {
        this.value = value;
        this.next = null;
    }

    /**
     * Constructs a new node with the specified value and next node reference.
     *
     * @param value The value to store in the node.
     * @param next  The reference to the next node in the linked list.
     */
    public LinkedNode(T value, LinkedNode<T> next) {
        this.value = value;
        this.next = next;
    }

    /**
     * Returns the value stored in the node.
     *
     * @return The value stored in the node.
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the value stored in the node.
     *
     * @param value The value to store in the node.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Returns the reference to the next node in the linked list.
     *
     * @return The reference to the next node in the linked list.
     */
    public LinkedNode<T> getNext() {
        return next;
    }

    /**
     * Sets the reference to the next node in the linked list.
     *
     * @param next The reference to the next node in the linked list.
     */
    public void setNext(LinkedNode<T> next) {
        this.next = next;
    }
}