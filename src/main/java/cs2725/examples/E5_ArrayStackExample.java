/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.api.Stack;
import cs2725.impl.ArrayStack;
import cs2725.viz.Graph;

public class E5_ArrayStackExample {
    
    public static void main(String[] args) {
        // We internally use an ArrayList.
        Stack<String> stack = new ArrayStack<>();

        Graph.enable();
        Graph.i().makeLeftToRight();
        Graph.i().setRef("stack", stack); // For visualization.

        stack.push("A");
        stack.push("B");
        stack.push("C");
        stack.push("D");
        stack.push("E");

        int size0 = stack.size();

        String v1 = stack.pop();
        String v2 = stack.pop();
        String v3 = stack.peek();
        String v4 = stack.peek();

        int size1 = stack.size();

        boolean empty0 = stack.isEmpty();
    }

}
