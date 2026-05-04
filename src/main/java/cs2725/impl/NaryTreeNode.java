/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl;

import java.util.ArrayList;
import java.util.List;

import cs2725.viz.Children;
import cs2725.viz.TreeNode;
import cs2725.viz.Value;

/**
 * A node in an n-ary tree.
 * 
 * @param <T> The type of data stored in the node.
 */
@TreeNode // For visualization.
public class NaryTreeNode<T> {

    @Value // For visualization.
    T data;

    @Children // For visualization.
    List<NaryTreeNode<T>> children;

    public NaryTreeNode() {
        this(null);
    }

    public NaryTreeNode(T data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public List<NaryTreeNode<T>> getChildren() {
        return new ArrayList<>(children);
    }

    public void setData(T data) {
        this.data = data;
    }

    public void addChild(NaryTreeNode<T> child) {
        children.add(child);
    }

    public void removeChild(NaryTreeNode<T> child) {
        children.remove(child);
    }

    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public int getNumChildren() {
        return children.size();
    }

    public NaryTreeNode<T> getChild(int index) {
        return children.get(index);
    }

    public void setChild(int index, NaryTreeNode<T> child) {
        children.set(index, child);
    }

    public void clearChildren() {
        children.clear();
    }

}
