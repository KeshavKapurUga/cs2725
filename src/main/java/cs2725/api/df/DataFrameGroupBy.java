/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.df;

import cs2725.api.List;
import cs2725.api.Map;

/**
 * Represents a grouped view of a DataFrame. Grouping is done based on one
 * designated column called the group-by column. The grouping is determined by
 * that column's unique values.
 *
 * Each group corresponds to a unique value from the group-by column. Any given
 * group consists of the row indices where the group-by column has the
 * corresponding unique value for that group.
 *
 * @param <T> the type of the column used to group the DataFrame
 */
public interface DataFrameGroupBy<T> {

    /**
     * @return the original DataFrame used to form this group
     */
    DataFrame dataFrame();

    /**
     * @return the Series<T> representing the column used for grouping
     */
    Series<T> groupingColumn();

    /**
     * Returns a copy of the group-to-indices mapping.
     * - The map's key is a unique value in the grouping column.
     * - The map's value is a list of row positions that match that key.
     *
     * @return a copy of the group-to-row-indices mapping
     */
    Map<T, List<Integer>> groups();

    /**
     * Returns an immutable list of the group index. Each element is one unique
     * value from the group by column. This index ensures the order of the groups.
     *
     * @return an immutable list of the ordered unique group values
     */
    List<T> index();

    /**
     * Performs one or more column-level aggregations for each group
     * and returns a new DataFrame that each column in the DataFrame correspond to
     * each aggregate in the aggregates list. See the documentation in the
     * ColumnAggregate class for more information about aggregates.
     * The DataFrame has one additional column for the group-by column with its
     * unique values. The number of rows in the result DataFrame is equal to the
     * number of unique values in the group-by column.
     *
     * The result DataFrame has:
     * - One row per group (in the same order as index returned by the index()
     * function.
     * - One output column per requested aggregate.
     * - One additional column for the group-by column.
     *
     * @param aggregates a list of column-aggregate definitions
     * @return a new DataFrame of aggregated results
     * @throws IllegalArgumentException if a source column is missing or
     *                                  the aggregator is incompatible with the
     *                                  column type
     * @throws NullPointerException     if aggregates or any entry within it
     *                                  is null
     */
    DataFrame aggregate(List<ColumnAggregate<?, ?>> aggregates);

}
