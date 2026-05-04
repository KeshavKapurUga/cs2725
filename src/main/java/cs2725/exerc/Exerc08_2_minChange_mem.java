/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.exerc;

public class Exerc08_2_minChange_mem {

    /**
     * Calculate the minimum count of coins needed to change the given amount.
     * Assume you have an infinite count of each coin in the set of coins.
     * 
     * @param amount The amount you need to change.
     * @param coins  The set of coins you have.
     * @param mem    Memory table of length amount + 1.
     * @return Minimum count of coins needed to change the amount or
     *         Integer.MAX_VALUE if changing is not possible
     */
    private static int minChange(int amount, int[] coins, Integer[] mem) {
        // Todo: To be implemented.
        return 0;
    }

    public static void main(String[] args) {
        int amount = 6;
        int[] coins = { 1, 3, 4 };
        Integer[] mem = new Integer[amount + 1];
        int count = minChange(amount, coins, mem);
        System.out.println("count: " + count);
    }

}
