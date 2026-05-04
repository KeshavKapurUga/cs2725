/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.viz;

import java.lang.reflect.Array;

public class ArrayIndex {
    
    private Object array;
    private int index;

    public ArrayIndex(Object array, int index) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("The first parameter is not an array.");
        }
        this.array = array;
        this.index = index;
    }

    public Object getArray() {
        return array;
    }

    public int getIndex() {
        return index;
    }

    public boolean isIndexValid() {
        return index >= 0 && index < Array.getLength(array);
    }

}
