/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.exerc;

import cs2725.impl.LinkedNode;
import cs2725.util.Util;
import cs2725.viz.Graph;

public class Exerc04_printList {

    /**
     * Print a linked list.
     * 
     * @param head The head of the list.
     */
    private static void printList(LinkedNode<String> head) {
        if (head == null) {
            return;
        }
        System.out.print(head.getValue() + " ");
        printList(head.getNext());
    }

    public static void main(String[] args) {
        LinkedNode<String> head = Util.makeList(5);
        printList(head);

        Graph.enable(); // Visualization.
        Graph.i().makeLeftToRight(); // Visualization.
        Graph.i().setRef("head", head); // Visualization.
    }

}
