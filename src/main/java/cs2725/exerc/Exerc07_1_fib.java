/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.exerc;

public class Exerc07_1_fib {

    /**
     * Calculate the nth Fibonacci number.
     * 
     * @param n The index of the Fibonacci number.
     * @return The nth Fibonacci number.
     */
    private static long fib(int n) {
        // First base case.
        if (n == 0) {
            return 0;
        }

        // Second base case.
        if (n == 1) {
            return 1;
        }

        // Recursive case.
        long prev = fib(n - 1);
        long prevPrev = fib(n - 2);
        long ans = prev + prevPrev;

        return ans;
    }

    public static void runFibMany() {
        for (int i = 0; i <= 50; ++i) {
            long startTime = System.nanoTime();
            long val = fib(i);
            long endTime = System.nanoTime();
            System.out.printf("fib(%d) = %d \t time: %fs\n", i, val, (endTime - startTime) / 1e9);
        }
    }

    public static void main(String[] args) {
        runFibMany();
    }

}
