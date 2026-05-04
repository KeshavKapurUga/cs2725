/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.exerc;

import java.util.Arrays;

public class Exerc01 {

    /**
     * Computes the product of all elements in the array except
     * the current element at each index.
     * You are not allowed to use division.
     * 
     * Note: You can assume the products fit within int data type.
     *
     * @param nums an array of integers
     * @return an array where each element at index i is the
     *         product of all elements in nums except nums[i]
     * @throws IllegalArgumentException if nums is null or has
     *                                  fewer than 2 elements
     */
    public static int[] productExceptSelf(int[] nums) {
        if (nums == null || nums.length < 2) {
            throw new IllegalArgumentException("Input array must have at least 2 elements.");
        }

        int n = nums.length;
        int[] result = new int[n];

        for (int i = 0; i < n; ++i) {
            int ans = 1;
            for (int j = 0; j < n; ++j) {
                if (i == j)
                    continue;
                ans = ans * nums[j];
            }
            result[i] = ans;
        }

        return result;
    }

    /**
     * Computes the product of all elements in the array except
     * the current element at each index.
     * You are not allowed to use division.
     * 
     * Note: You can assume the products fit within int data type.
     *
     * @param nums an array of integers
     * @return an array where each element at index i is the
     *         product of all elements in nums except nums[i]
     * @throws IllegalArgumentException if nums is null or has
     *                                  fewer than 2 elements
     */
    public static int[] productExceptSelfImproved(int[] nums) {
        if (nums == null || nums.length < 2) {
            throw new IllegalArgumentException("Input array must have at least 2 elements.");
        }

        int n = nums.length;
        int[] result = new int[n];

        result[0] = 1;

        for (int i = 1; i < n; ++i) {
            result[i] = result[i - 1] * nums[i - 1];
        }

        int suffix = 1;
        for (int i = n - 1; i >= 0; --i) {
            result[i] = result[i] * suffix;
            suffix = suffix * nums[i];
        }

        return result;
    }

    private static void testLargeInputForProductExceptSelf() {
        // Input 6 (even larger array).
        int[] nums6 = new int[1_000_000];
        Arrays.fill(nums6, 1);

        // The larger input for the improved version.
        // (The naive version is not included because it took too much time).
        System.out.println("Starting the large array (1M) processing...");

        long startTime = System.nanoTime();
        productExceptSelf(nums6);
        long endTime = System.nanoTime();

        System.out.println("Done!");
        System.out.println("Duration: " + (endTime - startTime) / 1_000_000_000.0 + " seconds");
        System.out.println();
    }

    private static void testLargeInputForProductExceptSelfImproved() {
        // Input 6 (even larger array).
        int[] nums6 = new int[1_000_000];
        Arrays.fill(nums6, 1);

        // The larger input for the improved version.
        // (The naive version is not included because it took too much time).
        System.out.println("Starting the large array (1M) processing...");

        long startTime = System.nanoTime();
        productExceptSelfImproved(nums6);
        long endTime = System.nanoTime();

        System.out.println("Done!");
        System.out.println("Duration: " + (endTime - startTime) / 1_000_000_000.0 + " seconds");
        System.out.println();
    }

    public static void main(String[] args) {
        // Input 1.
        int[] nums1 = { 1, 2, 3, 4 }; // {24, 12, 8, 6}
        System.out.println("Input: " + Arrays.toString(nums1));
        System.out.println("Output from naive: " + Arrays.toString(productExceptSelf(nums1)));
        System.out.println("Output from improved: " + Arrays.toString(productExceptSelfImproved(nums1)));
        System.out.println();

        // Input 2.
        int[] nums2 = { 0, 1, 2, 3 }; // Expected output: [6, 0, 0, 0]
        System.out.println("Input: " + Arrays.toString(nums2));
        System.out.println("Output from naive: " + Arrays.toString(productExceptSelf(nums2)));
        System.out.println("Output from improved: " + Arrays.toString(productExceptSelfImproved(nums2)));
        System.out.println();

        // Input 3.
        int[] nums3 = { 1, 1, 1, 1 }; // Expected output: [1, 1, 1, 1]
        System.out.println("Input: " + Arrays.toString(nums3));
        System.out.println("Output from naive: " + Arrays.toString(productExceptSelf(nums3)));
        System.out.println("Output from improved: " + Arrays.toString(productExceptSelfImproved(nums3)));
        System.out.println();

        // Input 4.
        int[] nums4 = { 2, 2, 1, 2, 2 }; // Expected output: [8, 8, 16, 8, 8]
        System.out.println("Input: " + Arrays.toString(nums4));
        System.out.println("Output from naive: " + Arrays.toString(productExceptSelf(nums4)));
        System.out.println("Output from improved: " + Arrays.toString(productExceptSelfImproved(nums4)));
        System.out.println();

        // Running the improved implementation with a large input.
        testLargeInputForProductExceptSelfImproved();

        // Running the initial implementation with a large input.
        testLargeInputForProductExceptSelf();
    }
}