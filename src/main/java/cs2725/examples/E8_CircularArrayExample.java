/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.viz.Graph;

public class E8_CircularArrayExample {
    
    public static void main(String[] args) {
        String[] arr = new String[4];

        int start = 0, end = 0, size = 0;

        // addLast.
        arr[end] = "A";
        end = (++end) % arr.length;
        ++size;

        // addLast.
        arr[end] = "B";
        end = (++end) % arr.length;
        ++size;

        // addLast.
        arr[end] = "C";
        end = ++end % arr.length;
        ++size;

        // removeFirst
        String val = arr[start];
        arr[start] = null;
        start = ++start % arr.length;
        --size;

        // removeFirst
        val = arr[start];
        arr[start] = null;
        start = ++start % arr.length;
        --size;

        // addFirst
        start = (--start + arr.length) % arr.length;
        arr[start] = "X";
        ++size;

        // addLast
        arr[end] = "Y";
        end = ++end % arr.length;
        ++size;

        // addLast
        arr[end] = "Z";
        end = ++end % arr.length;
        ++size;

        String[] newArr = new String[8];
        for (int i = 0; i < size; ++i) {
            newArr[i] = arr[(start + i) % arr.length];
        }
        arr = newArr;
        start = 0;
        end = size;

        Graph.enable();
        Graph.i().makeLeftToRight();
        Graph.i().setRef("val", val);
        Graph.i().setRef("arr", arr);
        Graph.i().setRef("start", start);
        Graph.i().setRef("end", end);
        Graph.i().setRef("size", size);
    }

}
