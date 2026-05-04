/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

import cs2725.api.List;
import cs2725.viz.Graph;

/**
 * Implementation of a singly linked list.
 *
 * @param <T> The type of elements stored in this linked list.
 */
public class LinkedList<T> implements List<T> {

    private LinkedNode<T> head; // Points to the first node
    private int size; // Tracks the size of the list

    /**
     * Constructs an empty LinkedList.
     */
    public LinkedList() {
        head = null;
        size = 0;
    }

    @Override
    public void insertItem(T item) {
        LinkedNode<T> newNode = new LinkedNode<>(item);

        Graph.i().setRef("newNode", newNode); // Visualization.

        if (head == null) {
            head = newNode;
        } else {
            LinkedNode<T> current = head;

            Graph.i().setRef("current", current); // Visualization.

            while (current.getNext() != null) {
                current = current.getNext();

                Graph.i().setRef("current", current); // Visualization.

            }
            current.setNext(newNode);

            Graph.i().clearRef("current"); // Visualization.
        }
        size++;

        Graph.i().clearRef("newNode"); // Visualization.
    }

    @Override
    public void deleteItemAt(int index) {
        checkIndex(index);
        if (index == 0) {
            head = head.getNext();
        } else {
            LinkedNode<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            LinkedNode<T> nextNext = current.getNext().getNext();
            current.setNext(nextNext);
        }
        size--;
    }

    @Override
    public void deleteItem(T item) {
        if (head == null) {
            return;
        }
        if (Objects.equals(item, head.getValue())) {
            head = head.getNext();
            --size;
            return;
        }
        LinkedNode<T> current = head;
        while (current.getNext() != null) {
            if (Objects.equals(item, current.getNext().getValue())) {
                LinkedNode<T> nextNext = current.getNext().getNext();
                current.setNext(nextNext);
                --size;
                return;
            }
            current = current.getNext();
        }
    }

    @Override
    public T getItem(int index) {
        checkIndex(index);
        LinkedNode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getValue();
    }

    @Override
    public void setItem(int index, T item) {
        checkIndex(index);
        LinkedNode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        current.setValue(item);
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int searchItem(T item) {
        LinkedNode<T> current = head;
        int index = 0;
        while (current != null) {
            if (Objects.equals(item, current.getValue())) {
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1; // Item not found
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    @Override
    public void sort(Comparator<T> comparator) {
        if (head == null || head.getNext() == null) {
            return; // List is empty or has only one element
        }

        // New sorted list reference.
        LinkedNode<T> sorted = null;

        while (head != null) {
            // Detach the first node from "head"
            LinkedNode<T> current = head;
            head = head.getNext();
            current.setNext(null);

            // Insert 'current' into the correct position of the 'sorted' list
            if (sorted == null || comparator.compare(current.getValue(), sorted.getValue()) <= 0) {
                // Insert before the sorted list's head
                current.setNext(sorted);
                sorted = current;
            } else {
                // Walk through sorted until the correct insertion point
                LinkedNode<T> search = sorted;
                while (search.getNext() != null
                        && comparator.compare(current.getValue(), search.getNext().getValue()) > 0) {
                    search = search.getNext();
                }
                // Insert 'current' after 'search'
                current.setNext(search.getNext());
                search.setNext(current);
            }
        }

        // Set head to the sorted list.
        head = sorted;
    }

    @Override
    public List<T> copy() {
        LinkedList<T> newList = new LinkedList<>();
        LinkedNode<T> current = head;
        LinkedNode<T> tail = null;

        while (current != null) {
            LinkedNode<T> newNode = new LinkedNode<>(current.getValue());

            if (tail == null) {
                newList.head = newNode;
            } else {
                tail.setNext(newNode);
            }

            tail = newNode;
            current = current.getNext();
        }

        newList.size = this.size;
        return newList;
    }

    /**
     * Note: The behavior is undefined if the map is modified during or before the
     * use of the returned iterator object.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private LinkedNode<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T value = current.getValue();
                current = current.getNext();
                return value;
            }
        };
    }

    @Override
    public T[] toArray() {
        throw new UnsupportedOperationException("The method 'toArray' is not supported.");
    }

}