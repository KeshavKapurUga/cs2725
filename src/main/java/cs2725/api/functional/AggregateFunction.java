/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.functional;

import cs2725.api.df.Series;

/**
 * Represents an aggregation operation that processes a list of values
 * and produces a single aggregated result.
 *
 * @param <T> the type of input values
 * @param <R> the type of the aggregated result
 */
@FunctionalInterface
public interface AggregateFunction<T, R> {

    /**
     * Applies the aggregation function to a Series.
     *
     * @param series the Series to aggregate
     * @return the aggregated result
     * @throws IllegalArgumentException if series is null
     */
    R apply(Series<T> series);
}
