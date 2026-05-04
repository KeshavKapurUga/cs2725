/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl.df;

import cs2725.api.List;
import cs2725.api.Map;
import cs2725.api.df.Series;
import cs2725.api.df.SeriesGroupBy;
import cs2725.api.functional.AggregateFunction;
import cs2725.impl.ArrayList;
import cs2725.impl.HashMap;
import cs2725.impl.ImmutableList;

/**
 * Implementation of SeriesGroupBy.
 *
 * @param <T> the type of elements in the original Series
 */
public class SeriesGroupByImpl<T> implements SeriesGroupBy<T> {

    /*
     * The original Series that is being grouped.
     */
    private final Series<T> series;

    /*
     * A map from unique group values to lists of indices. Each key is a unique
     * group value, and the associated list contains the position indices of items
     * belonging to that group.
     */
    private final Map<T, List<Integer>> groups;

    /*
     * A list of unique values from the group-by operation. This maintains the group
     * order of the groups.
     */
    private final List<T> index;

    /**
     * Constructs a grouped Series.
     *
     * @param series the original Series
     * @throws IllegalArgumentException if series is null
     */
    public SeriesGroupByImpl(Series<T> series) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null.");
        }

        // The 'series' is the original ungrouped Series.
        this.series = series;

        // 'groups' maps each unique value in the Series to the list of index positions
        // where that value appears. For example, if series = [a, b, a], then:
        // groups = { a → [0, 2], b → [1] }
        this.groups = groupByValues();

        // 'index' holds the list of unique values in the Series (group labels),
        // and defines the group ordering in aggregate output.
        this.index = buildIndex();
    }

    /**
     * Groups elements by their values, creating a map from unique values to index
     * positions.
     *
     * @return A map where each unique value from the series is a key, and the
     *         associated value is a list of indices indicating the positions in the
     *         series where this unique value appears.
     */
    private Map<T, List<Integer>> groupByValues() {
        Map<T, List<Integer>> map = new HashMap<>(series.size(), 0.75f);
        for (int i = 0; i < series.size(); i++) {
            T val = series.get(i);
            List<Integer> indices = map.get(val);
            if (indices == null) {
                indices = new ArrayList<>();
                map.put(val, indices);
            }
            indices.insertItem(i);
        }
        return map;
    }

    /**
     * Builds the index of unique group values.
     *
     * @return a list of unique values from the group-by operation
     */
    private List<T> buildIndex() {
        List<T> uniqueValues = new ArrayList<>(groups.size());
        for (T key : groups.keySet()) {
            uniqueValues.insertItem(key);
        }
        return uniqueValues;
    }

    @Override
    public Series<T> series() {
        return series;
    }

    @Override
    public Map<T, List<Integer>> groups() {
        // Return a copy to ensure immutability.
        // Note: Map.copy does not copy the values.
        Map<T, List<Integer>> copy = new HashMap<>(groups.size(), 0.75);
        for (T key : groups.keySet()) {
            List<Integer> indicesImmutable = ImmutableList.of(groups.get(key));
            copy.put(key, indicesImmutable);
        }
        return copy;
    }

    @Override
    public List<T> index() {
        List<T> indexImmutable = ImmutableList.of(index);
        return indexImmutable;
    }

    @Override
    public <R> Series<R> aggregate(AggregateFunction<T, R> aggregator) {
        if (aggregator == null) {
            throw new IllegalArgumentException("Aggregator cannot be null.");
        }
        List<R> results = new ArrayList<>(index.size());
        for (int i = 0; i < index.size(); i++) {
            T key = index.getItem(i);
            List<Integer> groupIndices = groups.get(key);
            Series<T> subSeries = series.withIndex(groupIndices);
            results.insertItem(aggregator.apply(subSeries));
        }
        return new SeriesImpl<>(results);
    }
}
