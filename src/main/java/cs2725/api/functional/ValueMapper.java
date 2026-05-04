/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.functional;

/**
 * A functional interface for mapping each element of a Series to a new value.
 *
 * @param <I> the input element type
 * @param <O> the output element type
 */
@FunctionalInterface
public interface ValueMapper<I, O> {
    /**
     * Maps the given input element to an output element.
     *
     * @param input the input element
     * @return the mapped output value
     */
    O map(I input);
}