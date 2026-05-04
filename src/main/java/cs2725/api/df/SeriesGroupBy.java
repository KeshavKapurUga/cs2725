/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.df;

import cs2725.api.Map;
import cs2725.api.List;
import cs2725.api.functional.AggregateFunction;

/**
 * Represents the result of grouping a Series by unique values.
 * Allows aggregation functions to be applied to each group.
 *
 * @param <T> the type of elements in the original Series
 */
public interface SeriesGroupBy<T> {

    /**
     * Returns a reference to the original Series.
     *
     * @return the original Series
     */
    Series<T> series();

    /**
     * Returns a copy of the group-to-list mapping.
     * Each key is a unique group value, and the associated list contains the
     * position indices of items belonging to that group.
     *
     * @return an immutable mapping of group values to lists of indices
     */
    Map<T, List<Integer>> groups();

    /**
     * Returns an immutable version of the group index, which contains the unique
     * values from the group-by operation. This maintains the group order.
     *
     * @return an immutable version of the group index
     */
    List<T> index();

    /**
     * Applies an aggregation function to each group and returns a new Series with
     * the aggregated values.
     *
     * @param <R>        the result type of the aggregation function
     * @param aggregator an AggregateFunction that takes a Series<T> and produces an
     *                   aggregated result of type R
     * @return a new Series containing the aggregated values, indexed by the order
     *         of the group index
     * @throws IllegalArgumentException if the aggregator is null
     */
    <R> Series<R> aggregate(AggregateFunction<T, R> aggregator);

}