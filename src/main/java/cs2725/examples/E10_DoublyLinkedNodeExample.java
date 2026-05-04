/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.impl.DoublyLinkedNode;
import cs2725.viz.Graph;

public class E10_DoublyLinkedNodeExample {

    public static void main(String[] args) {
        DoublyLinkedNode<String> head = null;
        DoublyLinkedNode<String> tail = null;

        DoublyLinkedNode<String> tmp = null;
        
       // tmp = new DoublyLinkedNode<String>("A");

        // Adding the first item.
        // head = tmp;
        // tail = tmp;

        // addLast
        // tmp = new DoublyLinkedNode<String>("B");
        // tail.setNext(tmp);
        // tmp.setPrev(tail);
        // tail = tmp;

        // addLast
        // tmp = new DoublyLinkedNode<String>("C");
        // tail.setNext(tmp);
        // tmp.setPrev(tail);
        // tail = tmp;

        // addFirst
        // tmp = new DoublyLinkedNode<String>("1");
        // tmp.setNext(head);
        // head.setPrev(tmp);
        // head = tmp;

        Graph.enable();
        Graph.i().makeLeftToRight();
        Graph.i().setRef("head", head);
        Graph.i().setRef("tail", tail);
        Graph.i().setRef("tmp", tmp);
    }
    
}
