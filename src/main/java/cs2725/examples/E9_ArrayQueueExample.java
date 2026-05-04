/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.api.Queue;
import cs2725.impl.ArrayQueue;
import cs2725.viz.Graph;

public class E9_ArrayQueueExample {
    
    public static void main(String[] args) {
        // We internally use a circular array for this.
        Queue<String> queue = new ArrayQueue<>();

        Graph.enable();
        Graph.i().makeLeftToRight();
        Graph.i().setRef("queue", queue); // For visualization.

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        queue.enqueue("E");

        int size0 = queue.size();

        String v1 = queue.dequeue();
        String v2 = queue.dequeue();
        String v3 = queue.dequeue();
        String v4 = queue.dequeue();

        queue.enqueue("F");
        queue.enqueue("G");
        queue.enqueue("H");
        queue.enqueue("I");
        queue.enqueue("J");
        queue.enqueue("K");
        queue.enqueue("L");
        queue.enqueue("M");

        int size1 = queue.size();

        boolean empty0 = queue.isEmpty();
    }

}
