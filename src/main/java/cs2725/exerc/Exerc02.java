/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.exerc;

public class Exerc02 {

    public static void main(String[] args) {
        char[] arr = { 'F', 'G', 'H', 'A', 'B', 'C', 'D', 'E' };

        // Following is an example of how to consider an array as a circular structure
        // that you can go around clockwise starting from any position.
        // In the example below we start from index 3 where A is and go around clockwise
        // to print all letters in alphabetical order.
        // Expected output: ABCDEFGH
        String index_mapping_info = ""; // This is for informational purposes.
        for (int i = 0; i < arr.length; ++i) {
            int start = 3;
            int idx = (i + start) % arr.length;
            System.out.print(arr[idx]);

            index_mapping_info += String.format("offset_from_start: %d, start: %d, offset_from_index_zero: %d, mapped_idx: %d\n",
                    i, start, i + start, idx); // This is for informational purposes.
        }
        System.out.println();
        // Following line prints how index mapping worked in the loop.
        System.out.println(index_mapping_info); // This is for informational purposes.


        // Following is an example of how to consider an array as a circular structure
        // that you can go around counter clockwise starting from any position.
        // In the example below we start from index 2 where H is and go around counter
        // clockwise to print all letters in reverse alphabetical order.
        // Expected output: HGFEDCBA
        index_mapping_info = ""; // This is for informational purposes.
        for (int i = 0; i < arr.length; ++i) {
            int start = 2;
            int idx = (start - i + arr.length) % arr.length;
            System.out.print(arr[idx]);

            index_mapping_info += String.format("offset_from_start: %d, start: %d, offset_from_index_zero: %d, offset_from_index_zero_as_positive: %d, mapped_idx: %d\n",
                    -i, start, start - i, start - i + arr.length, idx); // This is for informational purposes.
        }
        System.out.println();
        // Following line prints how index mapping worked in the loop.
        System.out.println(index_mapping_info); // This is for informational purposes.
    }

}
