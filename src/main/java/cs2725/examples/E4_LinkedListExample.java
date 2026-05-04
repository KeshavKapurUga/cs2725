/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.api.List;
import cs2725.impl.LinkedList;
import cs2725.viz.Graph;

public class E4_LinkedListExample {
    
    public static void main(String[] args) {
        List<String> lst = new LinkedList<>();

        Graph.enable();
        Graph.i().makeLeftToRight();
        Graph.i().setRef("lst", lst); // For visualization.

        lst.insertItem("A");
        lst.insertItem("B");
        lst.insertItem("C");
        lst.insertItem("D");
        lst.insertItem("E");
    }

}
