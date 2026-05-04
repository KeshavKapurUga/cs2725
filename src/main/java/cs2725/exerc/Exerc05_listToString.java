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

public class Exerc05_listToString {

    /**
     * Converts a linked list to a String
     */
    private static String listToString(LinkedNode<String> head) {
        if (head == null) {
            return "";
        }
        String firstNode = head.getValue() + " ";
        String restOfTheNodes = listToString(head.getNext());
        return firstNode + restOfTheNodes;
    }

    public static void main(String[] args) {
        LinkedNode<String> head = Util.makeList(5);
        String listString = listToString(head);
        System.out.println(listString);

        Graph.enable(); // Visualization.
        Graph.i().makeLeftToRight(); // Visualization.
        Graph.i().setRef("head", head); // Visualization.
    }

}
