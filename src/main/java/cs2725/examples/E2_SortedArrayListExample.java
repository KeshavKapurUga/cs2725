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

public class E2_SortedArrayListExample {
    
    public static void main(String[] args) {
        Comparator<String> cmp = Comparator.<String>naturalOrder();

        List<String> lst = new SortedArrayList<>(cmp);

        Graph.enable(); // For visualization.
        Graph.i().makeLeftToRight(); // For visualization.
        Graph.i().setRef("lst", lst); // For visualization.

        lst.insertItem("A");
        lst.insertItem("B");
        lst.insertItem("D+");

        lst.insertItem("D+");
        lst.insertItem("E");
        lst.insertItem("F");
        
        lst.insertItem("G");
        lst.insertItem("D+");
        lst.insertItem("C");

        lst.searchItem("D+");
        lst.searchItem("Z");
        lst.searchItem("a");
    }

}
