/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.df;

import java.io.UncheckedIOException;
import java.util.Comparator;

import cs2725.api.List;
import cs2725.api.Set;
import cs2725.api.functional.ValueMapper;

/**
 * A DataFrame is a tabular structure. It consists of multiple named columns
 * (Series), all sharing a common index that references positions in their
 * respective value lists. Reordering and subsetting (removing indices) affect
 * the logical order and available rows while keeping the original column data
 * intact.
 * 
 * When loaded from a CSV or other text-based sources, all values are initially
 * stored as Strings. Type conversion should be performed using mappings,
 * allowing each column (Series) to be cast to its appropriate data type. See
 * the `map` function in Series for more details on applying type conversions.
 * 
 * This design decouples the logical row order and subset selection from the
 * physical storage of column values, enabling efficient transformations and
 * operations on subsets of data without modifying the underlying storage.
 * 
 * Implementations should ensure immutability for getters by returning copies.
 */
public interface DataFrame {

    /**
     * Returns a copy of the column names in this DataFrame. A copy is returned so
     * that modifications to the returned list do not affect the DataFrame.
     * 
     * @return a copy of the column names
     */
    Set<String> columns();

    /**
     * Returns an immutable list of the index shared across all Series. An immutable
     * list is returned so that modifications to the index that affects the
     * DataFrame is not possible.
     * 
     * @return an immutable list of the index values
     */
    List<Integer> index();

    /**
     * Loads data from a CSV file and returns a new DataFrame. All values are loaded
     * as Strings into type Series<String> columns. The first row of the CSV file is
     * assumed to contain column names. If column names are repeated in the header
     * only the last such column will be loaded. A default index is created to
     * represent the order of rows in the CSV file.
     * 
     * @param pathToCsv the path to the CSV file
     * @throws IllegalStateException        if the DataFrame has columns or index
     * @throws java.io.UncheckedIOException if there is an error reading the file
     */
    DataFrame readCsv(String pathToCsv) throws UncheckedIOException;

    /**
     * Adds a new column with explicit type and return a new DataFrame with the
     * added column. The new column is added to the end of the DataFrame.
     * 
     * @param columnName the name of the column
     * @param type       the type of the Series elements
     * @param series     the Series object to be added
     * @param <T>        the type of the column
     * @return a new DataFrame with the added column
     * @throws IllegalArgumentException if the columnName, type, or series is null,
     *                                  if the columnName is empty, if the
     *                                  columnName already exists, if the type does
     *                                  not match the Series type, or if the current
     *                                  size of the DataFrame does not match the
     *                                  size of the series.
     */
    <T> DataFrame addColumn(String columnName, Class<T> type, Series<T> series);

    /**
     * Retrieves a column by name.
     * 
     * @param columnName the name of the column to retrieve
     * @param <T>        the type of the column
     * @return the Series of type T
     * @throws IllegalArgumentException if the column does not exist or if the
     *                                  stored type does not match the expected
     *                                  type
     */
    <T> Series<T> getColumn(String columnName, Class<T> type);

    /**
     * Returns a subset of rows between the specified start and end indices
     * (exclusive).
     * 
     * @param start the starting row index (inclusive)
     * @param end   the ending row index (exclusive)
     * @return a new DataFrame containing the selected rows
     * @throws IndexOutOfBoundsException if indices are out of range
     * @throws IllegalArgumentException  if start >= end
     */
    DataFrame rows(int start, int end);

    /**
     * Returns a new DataFrame containing only the rows where the mask Series is
     * true. The mask must be a boolean Series with the same length as the
     * DataFrame's index. null values are treated as false.
     *
     * @param mask a boolean Series indicating which rows to retain
     * @return a new DataFrame containing only the selected rows
     * @throws IllegalArgumentException if the mask length does not match the number
     *                                  of rows
     */
    DataFrame selectByMask(Series<Boolean> mask);

    /**
     * Returns a subset of the DataFrame containing only the specified columns.
     * 
     * @param columnNames the list of column names to retain
     * @return a new DataFrame containing only the specified columns
     * @throws IllegalArgumentException if columnNames is null, columnNames is empty
     *                                  or any specified column does not exist
     */
    DataFrame columns(List<String> columnNames);

    /**
     * Maps the values of a column to a new type and/or value using the provided
     * mapper function.
     * 
     * @param <T>        the target type of the mapping
     * @param columnName the name of the column to map
     * @param mapper     the function to apply to each value
     * @param sourceType the source type of the mapping
     * @param targetType the target type of the mapping
     * @return a new DataFrame with the specified column replaced with the mapped
     *         column
     * @throws IllegalArgumentException if the column does not exist or the mapped
     *                                  series is not of the target type
     * @throws NullPointerException     if columnName, sourceType, targetType, or
     *                                  mapper is null
     */
    <S, T> DataFrame mapValues(String columnName, ValueMapper<S, T> mapper, Class<S> sourceType, Class<T> targetType);

    /**
     * Groups the DataFrame by a specified column, producing a DataFrameGroupBy
     * instance that allows applying aggregation operations on the groups.
     * 
     * @param groupByColumnName the name of the column to group by
     * @return a DataFrameGroupBy instance for performing aggregations
     * @throws IllegalArgumentException if the column does not exist
     * @throws NullPointerException     if groupByColumnName is null
     */
    DataFrameGroupBy<?> groupBy(String groupByColumnName);

    /**
     * Sorts the DataFrame by the specified column name using the provided
     * comparator.
     * 
     * @param columnName the name of the column to sort by
     * @param comparator the comparator to use for sorting
     * @param type       the runtime type of the column
     * @param <T>        the type of the column
     * @return a new DataFrame sorted by the specified column
     * @throws IllegalArgumentException if the column does not exist or the type of
     *                                  the column does not match the expected type
     * @throws NullPointerException     if columnName, comparator, or type is null
     */
    <T> DataFrame sortBy(String columnName, Comparator<? super T> comparator, Class<T> type);

    /**
     * Returns the number of rows in the DataFrame.
     * 
     * @return the number of rows
     */
    int size();

}
