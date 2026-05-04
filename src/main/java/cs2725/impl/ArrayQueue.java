/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import cs2725.api.Queue;

public class ArrayQueue<T> implements Queue<T> {

    private static final int INITIAL_CAPACITY = 4;

    private T[] data;
    private int front;
    private int back;
    private int size;

    public ArrayQueue() {
        init(INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private void init(int capacity) {
        data = (T[]) new Object[capacity];
        front = 0;
        back = 0;
        size = 0;
    }

    private void grow() {
        @SuppressWarnings("unchecked")
        T[] newData = (T[]) new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[(front + i) % data.length];
        }
        data = newData;
        front = 0;
        back = size;
    }

    public void enqueue(T item) {
        if (size == data.length) {
            grow();
        }
        data[back] = item;
        back = (back + 1) % data.length;
        size++;
    }

    public T dequeue() {
        checkSize();
        T item = data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size--;
        return item;
    }

    public T peek() {
        checkSize();
        return data[front];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        init(INITIAL_CAPACITY);
    }

    private void checkSize() {
        if (size == 0) {
            throw new IllegalStateException("Queue is empty");
        }
    }
}