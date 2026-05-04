/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.exerc;

public class Exerc03_towerOfHanoi {

    /**
     * Tower of Hanoi recursive solution.
     * Playable version: https://www.mathsisfun.com/games/towerofhanoi.html
     * 
     * @param n    The number of disks.
     * @param from The source rod.
     * @param to   The destination rod.
     * @param aux  The auxiliary rod.
     */
    private static void towerOfHanoi(int n, char src, char spare, char dst) {
        if (n == 0) return;
        towerOfHanoi(n - 1, src, dst, spare);
        System.out.println("Move disk " + n + " from " + src + " to " + dst);
        towerOfHanoi(n - 1, spare, src, dst);
    }

    public static void main(String[] args) {
        int n = 3;
        towerOfHanoi(n, 'A', 'B', 'C');
    }

}
