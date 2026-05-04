/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import java.util.Comparator;

import cs2725.viz.Graph;

public class SortedArrayList<T> extends ArrayList<T> {

    private Comparator<T> comp;

    public SortedArrayList(Comparator<T> comp) {
        super();
        this.comp = comp;
    }

    @Override
    public void insertItem(T item) {
        if (size == store.length) {
            resize(store.length * 2); // Grow if there is no space.
        }

        // Find the position for the new item.
        int i = 0;
        Graph.i().setRef("i", store, i); // For visualization.

        while (i < size && comp.compare(store[i], item) <= 0) {
            ++i;
            Graph.i().setRef("i", store, i); // For visualization.
        }

        // Make space for the item at position i.
        for (int j = size - 1; j >= i; --j) {
            Graph.i().setRef("j", store, j); // For visualization.
            store[j + 1] = store[j];
        }
        Graph.i().clearRef("j"); // For visualization.

        store[i] = item; // Store the item.
        ++size; // Increment the size.

        Graph.i().clearRef("i"); // For visualization.
    }

    @Override
    public void setItem(int index, T item) {
        deleteItemAt(index);
        insertItem(item);
    }

    @Override
    public int searchItem(T item) {
        int l = 0, r = size - 1, index = -1;

        Graph.i().setRef("l", store, l); // For visualization.
        Graph.i().setRef("r", store, r); // For visualization.
        Graph.i().setRef("index", store, index); // For visualization.

        while (l <= r) {
            int m = l + (r - l) / 2;
            Graph.i().setRef("m", store, m); // For visualization.

            if (comp.compare(store[m], item) < 0) {
                l = m + 1;
                Graph.i().setRef("l", store, l); // For visualization.

            } else if (comp.compare(store[m], item) >= 0) {
                if (comp.compare(store[m], item) == 0) {
                    index = m;
                    Graph.i().setRef("index", store, index); // For visualization.
                }

                r = m - 1;
                Graph.i().setRef("r", store, r); // For visualization.
            }

            Graph.i().clearRef("m");
        }

        Graph.i().clearRef("l"); // For visualization.
        Graph.i().clearRef("r"); // For visualization.
        Graph.i().clearRef("index"); // For visualization.
        return index;
    }
    
}
