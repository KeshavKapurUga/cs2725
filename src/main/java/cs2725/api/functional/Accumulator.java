/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.functional;

/**
 * A functional interface for aggregating or reducing a value by repeatedly
 * combining the previous accumulated result and the current element in the
 * Series.
 *
 * @param <A> the type of the accumulator/result
 * @param <E> the type of elements being processed
 */
@FunctionalInterface
public interface Accumulator<A, E> {
    /**
     * Applies this accumulator to the given previous accumulated value and current
     * element.
     *
     * @param accumulated    the previous accumulated value
     * @param currentElement the current element from the Series
     * @return the new accumulated value
     */
    A apply(A previousAccumulation, E currentElement);
}