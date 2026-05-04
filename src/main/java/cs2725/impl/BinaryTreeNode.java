/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import cs2725.viz.Left;
import cs2725.viz.Right;
import cs2725.viz.TreeNode;
import cs2725.viz.Value;

/**
 * A binary tree node.
 * 
 * @param <T> The type of data stored in the node.
 */
@TreeNode // For visualization.
public class BinaryTreeNode<T> {

    @Value // For visualization.
    private T data;

    @Left // For visualization.
    private BinaryTreeNode<T> left;

    @Right // For visualization.
    private BinaryTreeNode<T> right;

    public BinaryTreeNode(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public T getData() {
        return data;
    }

    public BinaryTreeNode<T> getLeft() {
        return left;
    }

    public BinaryTreeNode<T> getRight() {
        return right;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setLeft(BinaryTreeNode<T> left) {
        this.left = left;
    }

    public void setRight(BinaryTreeNode<T> right) {
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

}
