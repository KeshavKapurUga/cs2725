/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.impl.LinkedNode;
import cs2725.viz.Graph;

public class E3_LinkedNodeExample {

    public static void main(String[] args) {
        LinkedNode<String> head = new LinkedNode<>("A");
        LinkedNode<String> tmp = new LinkedNode<>("B");
        
        Graph.enable();
        Graph.i().makeLeftToRight();
        Graph.i().setRef("head", head);
        Graph.i().setRef("tmp", tmp);

        head.setNext(tmp);
    }
    
}
