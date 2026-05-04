/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.api.Queue;
import cs2725.impl.ArrayQueue;
import cs2725.impl.BinaryTreeNode;
import cs2725.impl.NaryTreeNode;
import cs2725.viz.Graph;

public class TreeExample {

    public static <T> int height(BinaryTreeNode<T> root) {
        if (root == null) {
            return -1;
        }
        int leftHeight = height(root.getLeft());
        int rightHeight = height(root.getRight());
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public static <T> int count(BinaryTreeNode<T> root) {
        if (root == null) {
            return 0;
        }
        return count(root.getLeft()) + count(root.getRight()) + 1;
    }

    public static <T> void preOrder(BinaryTreeNode<T> root) {
        if (root == null) {
            return;
        }
        System.out.print(root.getData() + " ");
        preOrder(root.getLeft());
        preOrder(root.getRight());
    }

    public static <T> void inOrder(BinaryTreeNode<T> root) {
        if (root == null) {
            return;
        }
        inOrder(root.getLeft());
        System.out.print(root.getData() + " ");
        inOrder(root.getRight());
    }

    public static <T> void postOrder(BinaryTreeNode<T> root) {
        if (root == null) {
            return;
        }
        postOrder(root.getLeft());
        postOrder(root.getRight());
        System.out.print(root.getData() + " ");
    }

    public static <T> void levelOrder(BinaryTreeNode<T> root) {
        if (root == null) {
            return;
        }

        Queue<BinaryTreeNode<T>> queue = new ArrayQueue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            BinaryTreeNode<T> current = queue.dequeue();
            
            // Process the current node.
            System.out.print(current.getData() + " ");

            // Enqueue left child.
            if (current.getLeft() != null) {
                queue.enqueue(current.getLeft());
            }

            // Enqueue right child.
            if (current.getRight() != null) {
                queue.enqueue(current.getRight());
            }
        }
    }

    private static NaryTreeNode<String> parentArrayToTree(int[] parentArray, String[] values) {
        @SuppressWarnings("unchecked")
        NaryTreeNode<String>[] nodes = new NaryTreeNode[parentArray.length];
        for (int i = 0; i < parentArray.length; i++) {
            nodes[i] = new NaryTreeNode<>(values[i]);
        }
        NaryTreeNode<String> root = null;
        for (int i = 0; i < parentArray.length; i++) {
            if (parentArray[i] == -1) {
                root = nodes[i];
            } else {
                nodes[parentArray[i]].addChild(nodes[i]);
            }
        }
        return root;
    }

    private static BinaryTreeNode<String> makeExampleBinaryTree() {
        // Initialize binary tree nodes.
        BinaryTreeNode<String> a = new BinaryTreeNode<>("A");
        BinaryTreeNode<String> b = new BinaryTreeNode<>("B");
        BinaryTreeNode<String> c = new BinaryTreeNode<>("C");
        BinaryTreeNode<String> d = new BinaryTreeNode<>("D");
        BinaryTreeNode<String> e = new BinaryTreeNode<>("E");
        BinaryTreeNode<String> f = new BinaryTreeNode<>("F");
        BinaryTreeNode<String> g = new BinaryTreeNode<>("G");
        BinaryTreeNode<String> h = new BinaryTreeNode<>("H");
        BinaryTreeNode<String> i = new BinaryTreeNode<>("I");
        BinaryTreeNode<String> j = new BinaryTreeNode<>("J");
        BinaryTreeNode<String> k = new BinaryTreeNode<>("K");

        // Make a binary tree.
        a.setLeft(b);
        a.setRight(c);
        b.setLeft(d);
        b.setRight(e);
        c.setRight(f);
        d.setLeft(g);
        d.setRight(h);
        e.setLeft(i);
        e.setRight(j);
        f.setLeft(k);

        return a;
    }

    public static BinaryTreeNode<Integer> makeExampleBinaryTree2() {
        // Create nodes
        BinaryTreeNode<Integer> node1 = new BinaryTreeNode<>(1);
        BinaryTreeNode<Integer> node2 = new BinaryTreeNode<>(2);
        BinaryTreeNode<Integer> node3 = new BinaryTreeNode<>(3);
        BinaryTreeNode<Integer> node4 = new BinaryTreeNode<>(4);
        BinaryTreeNode<Integer> node5 = new BinaryTreeNode<>(5);
        BinaryTreeNode<Integer> node6 = new BinaryTreeNode<>(6);
        BinaryTreeNode<Integer> node7 = new BinaryTreeNode<>(7);
        BinaryTreeNode<Integer> node8 = new BinaryTreeNode<>(8);

        // Construct the tree.
        node1.setLeft(node3);
        node1.setRight(node2);

        node3.setRight(node6);

        node2.setLeft(node4);
        node2.setRight(node5);

        node5.setLeft(node8);
        node5.setRight(node7);

        // Return the root of the tree.
        return node1;
    }

    private static NaryTreeNode<String> makeExampleNaryTree() {
        // Initialize n-ary tree nodes.
        NaryTreeNode<String> a1 = new NaryTreeNode<>("A");
        NaryTreeNode<String> b1 = new NaryTreeNode<>("B");
        NaryTreeNode<String> c1 = new NaryTreeNode<>("C");
        NaryTreeNode<String> d1 = new NaryTreeNode<>("D");
        NaryTreeNode<String> e1 = new NaryTreeNode<>("E");
        NaryTreeNode<String> f1 = new NaryTreeNode<>("F");
        NaryTreeNode<String> g1 = new NaryTreeNode<>("G");
        NaryTreeNode<String> h1 = new NaryTreeNode<>("H");
        NaryTreeNode<String> i1 = new NaryTreeNode<>("I");

        // Make an n-ary.
        a1.addChild(i1);
        a1.addChild(b1);
        a1.addChild(c1);
        b1.addChild(d1);
        b1.addChild(e1);
        c1.addChild(f1);
        c1.addChild(g1);
        c1.addChild(h1);

        return a1;
    }

    public static void main(String[] args) {
        // Visualizations.
        Graph.enable();
        Graph.i().makeTopDown();

        // BinaryTreeNode<String> a = makeExampleBinaryTree();
        BinaryTreeNode<Integer> a = makeExampleBinaryTree2();

        NaryTreeNode<String> a1 = makeExampleNaryTree(); // Later visualized.

        // Parent array representation of the n-ary tree.
        // Note: The order of children is not preserved in the parent array
        // representation.
        int[] parentArray = new int[] { -1, 0, 0, 1, 1, 2, 2, 2, 0 };
        String[] values = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I" };
        NaryTreeNode<String> converted = parentArrayToTree(parentArray, values); // Later visualized.

        // Binary tree algorithms.
        System.out.println("Binary tree height: " + height(a));
        System.out.println("Binary tree count: " + count(a));
        System.out.print("Binary tree pre-order: ");
        preOrder(a);
        System.out.println();
        System.out.print("Binary tree in-order: ");
        inOrder(a);
        System.out.println();
        System.out.print("Binary tree post-order: ");
        postOrder(a);
        System.out.println();
        System.out.print("Binary tree level-order: ");
        levelOrder(a);
        System.out.println();

        // Visualizations.
        Graph.i().setRef("a", a);
        // Graph.i().setRef("a1", a1);
        // Graph.i().setRef("converted", converted);
    }

}
