/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.util;

import cs2725.api.List;
import cs2725.impl.ArrayList;
import cs2725.impl.LinkedNode;

public class Util {

    /**
     * Make a linked list of characters.
     * 
     * @param len The length of the list.
     * @return The head of the list.
     */
    public static LinkedNode<String> makeList(int len) {
        LinkedNode<String> head = null;
        for (int i = len - 1; i >= 0; --i) {
            LinkedNode<String> tmp = new LinkedNode<>(Character.toString('A' + i));
            tmp.setNext(head);
            head = tmp;
        }
        return head;
    }

    public static <T> List<T> copyList(List<T> list) {
        List<T> copy = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            copy.insertItem(list.getItem(i));
        }
        return copy;
    }

}
