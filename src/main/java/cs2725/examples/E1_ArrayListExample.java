/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.api.List;
import cs2725.impl.ArrayList;
import cs2725.viz.Graph;

public class E1_ArrayListExample {
    
    public static void main(String[] args) {
        List<String> lst = new ArrayList<>();

        Graph.enable(); // For visualization.
        Graph.i().makeLeftToRight(); // For visualization.
        Graph.i().setRef("lst", lst); // For visualization.

        lst.insertItem("A");
        lst.insertItem("B");
        lst.insertItem("C");
        lst.insertItem("D");
        lst.insertItem("E");

        lst.searchItem("D");
        lst.searchItem("Z");

        lst.deleteItem("E");
        lst.deleteItem("B");

        for (int i = 0; lst.size() <= 8; ++i ) {
            lst.insertItem(String.valueOf(i));
        }

        for (int i = 0; lst.size() > 0; ++i) {
            lst.deleteItemAt(lst.size() - 1);
        }
    }

}
