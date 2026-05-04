/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.api.Deque;
import cs2725.impl.LinkedDeque;
import cs2725.viz.Graph;

public class E11_LinkedDequeExample {
    
    public static void main(String[] args) {
        // We internally use a doubly linked list.
        Deque<String> deque = new LinkedDeque<>();

        Graph.enable();
        Graph.i().makeLeftToRight();
        Graph.i().setRef("deque", deque); // For visualization.


        deque.addLast("A");
        deque.addLast("B");
        deque.addLast("C");
        deque.addLast("D");
        deque.addLast("E");

        
        int size0 = deque.size();

        String v1 = deque.removeLast();
        String v2 = deque.removeLast();
        String v3 = deque.removeLast();
        String v4 = deque.removeLast();

        int size1 = deque.size();

        boolean empty0 = deque.isEmpty();

        deque.addFirst("A1");
        deque.addFirst("B1");
        deque.addFirst("C1");
        deque.addFirst("D1");
        deque.addFirst("E1");

        int size2 = deque.size();

        String v5 = deque.removeLast();
        String v6 = deque.removeLast();
        String v7 = deque.removeFirst();
        String v8 = deque.removeFirst();

        int size3 = deque.size();

        boolean empty1 = deque.isEmpty();
    }

}
