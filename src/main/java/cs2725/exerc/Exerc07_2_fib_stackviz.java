/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.exerc;

import cs2725.util.StackViz;

public class Exerc07_2_fib_stackviz {

    /**
     * Calculate the nth Fibonacci number.
     * 
     * @param n The index of the Fibonacci number.
     * @return The nth Fibonacci number.
     */
    static int fCount = 0;
    static int timeStep = 0;

    private static long fibTraceable(int n) {
        // Stack frame visualization.
        StackViz.start("fib(" + n + ")");

        // First base case.
        if (n == 0) {
            // Stack frame visualization.
            StackViz.end("fib(" + n + ")", String.valueOf(0));

            return 0;
        }

        // Second base case.
        if (n == 1) {
            // Stack frame visualization.
            StackViz.end("fib(" + n + ")", String.valueOf(1));

            return 1;
        }

        // Recursive case.
        long prev = fibTraceable(n - 1);
        long prevPrev = fibTraceable(n - 2);
        long ans = prev + prevPrev;

        // Stack frame visualization.
        StackViz.end("fib(" + n + ")", String.valueOf(ans));

        return ans;
    }

    public static void main(String[] args) {
        long val = fibTraceable(4);
        System.out.println("\n\nfib(4) = " + val);
    }

}
