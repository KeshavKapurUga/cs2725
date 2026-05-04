/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.df;

import java.util.Objects;

import cs2725.api.functional.AggregateFunction;

/**
 * Represents an aggregation definition for a column. Such a definitions can be
 * applied to aggregate columns in a DataFrameGroupBy instance. The definition
 * specifies the source column, its type, the target column, its type,
 * and the aggregation function.
 *
 * @param <S> the source column's element type
 * @param <R> the result type of the aggregation
 */
public class ColumnAggregate<S, R> {

    private final String sourceColumnName;
    private final Class<S> sourceType;
    private final String targetColumnName;
    private final Class<R> targetType;
    private final AggregateFunction<S, R> aggregator;

    /**
     * Constructs a ColumnAggregate instance with the specified parameters.
     *
     * @param sourceColumnName the name of the source column
     * @param targetColumnName the name of the target column
     * @param aggregator       the aggregation function that transforms S into R
     * @param sourceType       the runtime type of the source column
     * @param targetType       the runtime type of the aggregated result
     * @throws NullPointerException if any argument is null
     */
    public ColumnAggregate(String sourceColumnName, String targetColumnName, AggregateFunction<S, R> aggregator,
            Class<S> sourceType, Class<R> targetType) {
        // Check for null arguments.
        Objects.requireNonNull(sourceColumnName, "Source column name cannot be null.");
        Objects.requireNonNull(sourceType, "Source type cannot be null.");
        Objects.requireNonNull(targetColumnName, "Target column name cannot be null.");
        Objects.requireNonNull(targetType, "Target type cannot be null.");
        Objects.requireNonNull(aggregator, "Aggregator cannot be null.");

        // Assign the arguments to instance variables.
        this.sourceColumnName = sourceColumnName;
        this.targetColumnName = targetColumnName;
        this.aggregator = aggregator;
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    /**
     * @return the name of the source column
     */
    public String sourceColumnName() {
        return sourceColumnName;
    }

    /**
     * @return the runtime type of the source column
     */
    public Class<S> sourceType() {
        return sourceType;
    }

    /**
     * @return the name of the target column (output)
     */
    public String targetColumnName() {
        return targetColumnName;
    }

    /**
     * @return the runtime type of the aggregated result
     */
    public Class<R> targetType() {
        return targetType;
    }

    /**
     * @return the aggregation function that transforms S into R
     */
    public AggregateFunction<S, R> aggregator() {
        return aggregator;
    }
}