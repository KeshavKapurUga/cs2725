/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.exerc;

public class Exerc07_5_fib_dp_faster {

    /**
     * Calculate the nth Fibonacci number using dynamic programming with constant
     * space.
     * 
     * @param n The index of the Fibonacci number.
     * @return The nth Fibonacci number.
     */
    private static long fibEvenBetter(int n) {
        // Todo: Apply DP and improve.
        return 0;
    }

    public static void runFibFasterMany() {
        for (int i = 0; i <= 50; ++i) {
            long startTime = System.nanoTime();
            long val = fibEvenBetter(i);
            long endTime = System.nanoTime();
            System.out.printf("fib(%d) = %d \t time: %fs\n", i, val, (endTime - startTime) / 1e9);
        }
    }

    public static void main(String[] args) {
        runFibFasterMany();
    }

}
