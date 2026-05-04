/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import java.util.Comparator;

import cs2725.api.List;
import cs2725.impl.SortedArrayList;
import cs2725.viz.Graph;

public class SortedArrayListExample {
    
    public static void main(String[] args) {
        Comparator<String> cmp = Comparator.<String>naturalOrder();

        List<String> lst = new SortedArrayList<>(cmp); // For visualization.

        Graph.enable();
        Graph.i().makeLeftToRight();
        Graph.i().setRef("lst", lst);

        lst.insertItem("A");
        lst.insertItem("B");
        lst.insertItem("D1");

        lst.insertItem("D1");
        lst.insertItem("E");
        lst.insertItem("F");
        
        lst.insertItem("G");
        lst.insertItem("D1");
        lst.insertItem("C");

        lst.searchItem("D1");
        lst.searchItem("Z");
        lst.searchItem("1");
    }

}
