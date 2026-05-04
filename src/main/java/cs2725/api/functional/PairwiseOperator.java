/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.functional;

/**
 * A functional interface for combining corresponding elements from two Series
 * into a single output value (element-wise).
 *
 * @param <L> the type of elements in the left operand Series
 * @param <R> the type of elements in the right operand Series
 * @param <O> the result type for each pairwise combination
 */
@FunctionalInterface
public interface PairwiseOperator<L, R, O> {
    /**
     * Applies this operation to one element from the left Series and
     * one element from the right Series to produce a new value.
     *
     * @param left  the element from the left Series
     * @param right the element from the right Series
     * @return the result of the pairwise operation
     */
    O apply(L left, R right);
}