/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl.df;

import cs2725.api.df.DataFrame;
import cs2725.api.df.DataFrameGroupBy;
import cs2725.api.df.ColumnAggregate;
import cs2725.api.List;
import cs2725.api.Map;
import cs2725.api.df.Series;
import cs2725.api.df.SeriesGroupBy;
import cs2725.api.functional.AggregateFunction;
import cs2725.impl.ArrayList;
import cs2725.impl.ImmutableList;

import java.util.Objects;

/**
 * Implementation of DataFrameGroupBy<T> that supports grouping a DataFrame by
 * a specified column and applying aggregation functions on groups.
 *
 * @param <T> The type of the column used for grouping.
 */
public class DataFrameGroupByImpl<T> implements DataFrameGroupBy<T> {

    private final DataFrame dataFrame;
    private final SeriesGroupBy<T> groupedColumn;
    private final String groupByColumnName;
    private final Class<T> groupByType;
    private final Map<T, List<Integer>> groups;
    private final List<T> index;

    /**
     * Constructs a DataFrameGroupByImpl instance by grouping a DataFrame
     * based on the specified column.
     *
     * @param dataFrame         The original DataFrame.
     * @param groupByColumnName The name of the column to group by.
     * @param groupByType       The type of the column to group by.
     * @throws NullPointerException     If dataFrame or groupByColumnName is null.
     * @throws IllegalArgumentException If groupByColumnName does not exist.
     */
    public DataFrameGroupByImpl(DataFrame dataFrame, String groupByColumnName, Class<T> groupByType) {
        // Validate input arguments.
        Objects.requireNonNull(dataFrame, "DataFrame cannot be null.");
        Objects.requireNonNull(groupByColumnName, "Group by column name cannot be null.");
        Objects.requireNonNull(groupByType, "Group by type cannot be null.");

        // Get column ensures that the column exists and is of the correct type.
        Series<T> groupingColumn = dataFrame.getColumn(groupByColumnName, groupByType);

        // Validate type safety.
        Series.checkType(groupingColumn, groupByType); // Throws if types do not match.

        // Initialize fields.
        this.dataFrame = dataFrame;
        this.groupByColumnName = groupByColumnName;
        this.groupByType = groupByType;
        this.groupedColumn = new SeriesGroupByImpl<>(groupingColumn);
        this.groups = groupedColumn.groups();
        this.index = groupedColumn.index();
    }

    @Override
    public DataFrame dataFrame() {
        return dataFrame;
    }

    @Override
    public Series<T> groupingColumn() {
        return groupedColumn.series();
    }

    @Override
    public Map<T, List<Integer>> groups() {
        return groupedColumn.groups();
    }

    @Override
    public List<T> index() {
        return ImmutableList.of(index);
    }

    private <U, V> Series<V> applyAggregator(Series<?> sourceSeries, AggregateFunction<?, ?> aggregator,
            Class<U> sourceType, Class<V> targetType) {
        List<V> aggregatedValues = new ArrayList<>(index.size());
        for (T key : index) {
            List<Integer> indices = groups.get(key);

            // The source column is guaranteed to be of type U because the sourceSeries was
            // retrieved from the DataFrame using the sourceType.
            @SuppressWarnings("unchecked")
            Series<U> series = (Series<U>) sourceSeries.withIndex(indices);

            @SuppressWarnings("unchecked")
            AggregateFunction<U, V> castedAggregator = (AggregateFunction<U, V>) aggregator;

            V aggregatedValue = castedAggregator.apply(series);

            if (targetType.isInstance(aggregatedValue)) {
                aggregatedValues.insertItem(aggregatedValue);
            } else {
                throw new IllegalArgumentException("Aggregator returned an incompatible type: " + aggregatedValue);
            }
        }
        return new SeriesImpl<>(aggregatedValues);
    }

    @Override
    public DataFrame aggregate(List<ColumnAggregate<?, ?>> aggregates) {
        // Validate aggregates list.
        if (aggregates == null || aggregates.size() == 0) {
            throw new IllegalArgumentException("Aggregates list cannot be null or empty.");
        }

        // Validate each aggregator in the list.
        for (ColumnAggregate<?, ?> agg : aggregates) {
            Objects.requireNonNull(agg, "Aggregator cannot be null.");
            Objects.requireNonNull(agg.sourceColumnName(), "Source column name cannot be null.");
            Objects.requireNonNull(agg.sourceType(), "Source type cannot be null.");
            Objects.requireNonNull(agg.targetColumnName(), "Target column name cannot be null.");
            Objects.requireNonNull(agg.targetType(), "Target type cannot be null.");
            Objects.requireNonNull(agg.aggregator(), "Aggregator function cannot be null.");

            // Validate source column name.
            if (agg.sourceColumnName().isEmpty()) {
                throw new IllegalArgumentException("Source column name cannot be empty.");
            }

            // Validate target column name.
            if (agg.targetColumnName().isEmpty()) {
                throw new IllegalArgumentException("Target column name cannot be empty.");
            }
        }

        // Initialize the result DataFrame.
        DataFrame result = new DataFrameImpl();

        // Add group by column to the result DataFrame.
        result = result.addColumn(groupByColumnName, groupByType, new SeriesImpl<>(groupedColumn.index()));

        // Apply each aggregator to the source column specified in the aggregator and
        // add the aggregated column to the result DataFrame with the target column name
        // specified in the aggregator.
        for (ColumnAggregate<?, ?> agg : aggregates) {
            String sourceColumnName = agg.sourceColumnName();
            Class<?> sourceType = agg.sourceType();
            String targetColumnName = agg.targetColumnName();
            Class<?> targetType = agg.targetType();

            // Access the source column from the original DataFrame.
            Series<?> sourceSeries = dataFrame.getColumn(sourceColumnName, sourceType);

            // Apply the aggregator to the source column.
            Series<?> aggregated = applyAggregator(sourceSeries, agg.aggregator(), sourceType, targetType);

            Series.checkType(aggregated, targetType); // Throws if types do not match.
            @SuppressWarnings("unchecked")
            Series<Object> castedSeries = (Series<Object>) aggregated;
            @SuppressWarnings("unchecked")
            Class<Object> castedTargetType = (Class<Object>) targetType;
            result = result.addColumn(targetColumnName, castedTargetType, castedSeries);
        }

        return result;
    }

}
