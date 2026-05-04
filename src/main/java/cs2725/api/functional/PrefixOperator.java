/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.functional;

/**
 * A functional interface for prefix computations in a Series: each new prefix
 * value is computed from the previous prefix value and the current Series
 * element.
 *
 * @param <A> the type of the accumulated prefix/result
 * @param <E> the type of elements being processed
 */
@FunctionalInterface
public interface PrefixOperator<A, E> {
    /**
     * Computes the current prefix value based on the previous prefix and the
     * current element.
     *
     * @param previous       the previous prefix value
     * @param currentElement the current element from the Series
     * @return the new prefix value
     */
    A apply(A previousPrefix, E currentElement);
}