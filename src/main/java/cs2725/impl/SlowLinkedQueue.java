/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import cs2725.api.Queue;

public class SlowLinkedQueue<T> implements Queue<T> {

    private LinkedNode<T> head;
    private int size;

    public SlowLinkedQueue() {
        head = null;
        size = 0;
    }

    @Override
    public void enqueue(T item) {
        LinkedNode<T> newNode = new LinkedNode<>(item);
        if (isEmpty()) {
            head = newNode;
        } else {
            LinkedNode<T> tail = head;
            while (tail.getNext() != null) {
                tail = tail.getNext();
            }
            tail.setNext(newNode);
        }
        size++;
    }

    @Override
    public T dequeue() {
        checkSize(); // Throws if empty.

        T item = head.getValue();
        if (size == 1) {
            head = null;
        } else {
            head = head.getNext();
        }
        size--;
        return item;
    }

    @Override
    public T peek() {
        checkSize(); // Throws if empty.
        return head.getValue();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private void checkSize() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
    }
}