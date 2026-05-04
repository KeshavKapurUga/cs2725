/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl.df;

import cs2725.api.List;
import cs2725.api.Map;
import cs2725.api.Set;
import cs2725.api.df.DataFrame;
import cs2725.api.df.DataFrameGroupBy;
import cs2725.api.df.Series;
import cs2725.api.functional.ValueMapper;
import cs2725.impl.ArrayList;
import cs2725.impl.HashMap;
import cs2725.impl.HashSet;
import cs2725.impl.ImmutableList;
import de.vandermeer.asciitable.AsciiTable;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

/**
 * Implementation of the DataFrame interface.
 */
public class DataFrameImpl implements DataFrame {

    // The columns in the DataFrame are stored as Series.
    private final Map<String, Series<?>> columns;

    // The column names are stored here to maintain the column order.
    private final List<String> columnNames;

    // The types of the columns are stored for type checking.
    private final Map<String, Class<?>> types;

    // The index of the DataFrame is stored as a list of integers.
    private final List<Integer> index;

    public DataFrameImpl() {
        this.columns = new HashMap<>();
        this.columnNames = new ArrayList<>();
        this.types = new HashMap<>();
        this.index = new ArrayList<>();
    }

    @Override
    public Set<String> columns() {
        return columns.keySet();
    }

    @Override
    public List<Integer> index() {
        return ImmutableList.of(index);
    }

    @Override
    public DataFrame readCsv(String pathToCsv) throws UncheckedIOException {
        if (!columns.isEmpty() || index.size() > 0) {
            throw new IllegalStateException("DataFrame already contains data.");
        }

        try (Reader reader = new FileReader(pathToCsv);
                CSVParser csvParser = CSVFormat.Builder.create()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setTrim(true)
                        .get()
                        .parse(reader)) {

            // Read column names into a set.
            Set<String> columnNames = new HashSet<>();
            for (String column : csvParser.getHeaderNames()) {
                columnNames.add(column);
            }

            // Create a map for each column to store column values.
            Map<String, List<String>> table = new HashMap<>();
            for (String column : columnNames) {
                table.put(column, new ArrayList<>());
            }

            for (CSVRecord record : csvParser) {
                for (String column : columnNames) {
                    table.get(column).insertItem(record.get(column));
                }
            }

            // Initialize a new DataFrame for the loaded data.
            DataFrameImpl dataFrame = new DataFrameImpl();

            // Add columns to the DataFrame as Series.
            for (String column : columnNames) {
                dataFrame.addColumnHelper(column, String.class, new SeriesImpl<>(table.get(column)));
            }

            // Return the populated DataFrame.
            return dataFrame;
        } catch (IOException e) {
            throw new UncheckedIOException("Error reading CSV file: " + pathToCsv, e);
        }
    }

    private <T> void addColumnHelper(String columnName, Class<T> type, Series<T> series) {
        if (columnName == null || type == null || series == null) {
            throw new IllegalArgumentException("Column name, type, or series cannot be null.");
        }
        if (columnName.isEmpty()) {
            throw new IllegalArgumentException("Column name cannot be empty.");
        }
        if (columns.containsKey(columnName)) {
            throw new IllegalArgumentException("Column already exists: " + columnName);
        }
        if (index.size() > 0 && series.size() != index.size()) {
            throw new IllegalArgumentException("Series size does not match DataFrame row count.");
        }

        Series.checkType(series, type); // Throws if types do not match.

        columns.put(columnName, series);
        columnNames.insertItem(columnName);
        types.put(columnName, type);

        // Add index to match the column if this is the first column added.
        if (index.size() == 0) {
            for (int i = 0; i < series.size(); i++) {
                index.insertItem(i);
            }
        }
    }

    @Override
    public <T> DataFrame addColumn(String columnName, Class<T> type, Series<T> series) {
        DataFrameImpl newFrame = new DataFrameImpl();
        populateSubFrame(newFrame, index);
        newFrame.addColumnHelper(columnName, type, series);
        return newFrame;
    }

    @Override
    public <T> Series<T> getColumn(String columnName, Class<T> type) {
        Series<?> series = columns.get(columnName);
        Class<?> seriesType = types.get(columnName);

        if (series == null) {
            throw new IllegalArgumentException("Column does not exist: " + columnName);
        }

        if (!seriesType.equals(type)) {
            throw new IllegalArgumentException("Column type does not match requested type.");
        }

        // The requested type matched the stored type, so the cast is safe.
        @SuppressWarnings("unchecked")
        Series<T> castedSeries = (Series<T>) series;
        return castedSeries;
    }

    /**
     * Helper method to populate a sub-frame with columns using the given index for
     * each columns.
     * 
     * @param subFrame the sub-frame to populate
     */
    private void populateSubFrame(DataFrameImpl subFrame, List<Integer> index) {
        for (String column : columnNames) {
            Class<?> type = types.get(column);
            @SuppressWarnings("unchecked")
            Class<Object> objectType = (Class<Object>) type;
            @SuppressWarnings("unchecked")
            Series<Object> series = (Series<Object>) getColumn(column, type).withIndex(index);
            // Note: type is checked when columns are added.
            subFrame.addColumnHelper(column, objectType, series);
        }
    }

    @Override
    public DataFrame rows(int start, int end) {
        if (start < 0 || end > index.size() || start >= end) {
            throw new IllegalArgumentException("Invalid row range.");
        }

        // Initialize an index with the selected range.
        List<Integer> newIndex = new ArrayList<>();

        // Populate the index of the subset DataFrame.
        for (int i = start; i < end; i++) {
            newIndex.insertItem(index.getItem(i));
        }

        // Create a DataFrame for the subset of rows.
        DataFrameImpl subFrame = new DataFrameImpl();

        // Populate the columns of the subset DataFrame.
        populateSubFrame(subFrame, newIndex);

        return subFrame;
    }

    @Override
    public DataFrame selectByMask(Series<Boolean> mask) {
        Objects.requireNonNull(mask, "Mask cannot be null.");

        if (mask.size() != index.size()) {
            throw new IllegalArgumentException("Mask size does not match DataFrame size.");
        }

        // Initialize an index with the selected range.
        List<Integer> newIndex = new ArrayList<>();

        for (int i = 0; i < mask.size(); i++) {
            if (mask.get(i) != null && Boolean.TRUE.equals(mask.get(i))) {
                newIndex.insertItem(index.getItem(i));
            }
        }

        // Create a DataFrame for the subset of rows.
        DataFrameImpl filtered = new DataFrameImpl();

        // Populate the columns of the subset DataFrame.
        populateSubFrame(filtered, newIndex);

        return filtered;
    }

    @Override
    public DataFrame columns(List<String> columnNames) {
        if (columnNames == null || columnNames.size() == 0) {
            throw new IllegalArgumentException("Column names cannot be null or empty.");
        }

        for (String column : columnNames) {
            if (!columns.containsKey(column)) {
                throw new IllegalArgumentException("Column does not exist: " + column);
            }
        }

        DataFrameImpl subset = new DataFrameImpl();

        for (String column : columnNames) {
            @SuppressWarnings("unchecked")
            Class<Object> columnType = (Class<Object>) types.get(column);
            @SuppressWarnings("unchecked")
            Series<Object> columnSeries = (Series<Object>) getColumn(column, types.get(column));
            subset.addColumnHelper(column, columnType, columnSeries); // Types are checked when columns are added.
        }

        return subset;
    }

    @Override
    public DataFrameGroupBy<?> groupBy(String groupByColumnName) {
        // Check for null group by column name.
        Objects.requireNonNull(groupByColumnName, "Group by column name cannot be null.");

        // Ensure the column exists in the DataFrame
        if (!columns.containsKey(groupByColumnName)) {
            throw new IllegalArgumentException("Column does not exist: " + groupByColumnName);
        }

        // Construct and return DataFrameGroupBy instance.
        return new DataFrameGroupByImpl<>(this, groupByColumnName, types.get(groupByColumnName));
    }

    @Override
    public <T> DataFrame sortBy(String columnName, Comparator<? super T> comparator, Class<T> type) {
        // Ensure parameters are not null.
        Objects.requireNonNull(columnName, "Column name cannot be null.");
        Objects.requireNonNull(comparator, "Comparator cannot be null.");
        Objects.requireNonNull(type, "Type cannot be null.");

        // Access the column to sort by.
        Series<T> original = getColumn(columnName, type);

        // Sort the column by the comparator.
        Series<T> sorted = original.sortBy(comparator);

        DataFrameImpl sortedFrame = new DataFrameImpl();
        for (String existingColumnName : columnNames) {
            if (existingColumnName.equals(columnName)) {
                sortedFrame.addColumnHelper(existingColumnName, type, sorted);
            } else {
                @SuppressWarnings("unchecked")
                Class<Object> columnType = (Class<Object>) types.get(existingColumnName);
                @SuppressWarnings("unchecked")
                Series<Object> columnSeries = (Series<Object>) getColumn(existingColumnName,
                        types.get(existingColumnName));

                // Reorder the column to match the sorted column.
                columnSeries = columnSeries.withIndex(sorted.index());

                // Types are checked when columns are added.
                sortedFrame.addColumnHelper(existingColumnName, columnType, columnSeries);
            }
        }

        return sortedFrame;
    }

    @Override
    public <S, T> DataFrame mapValues(String columnName, ValueMapper<S, T> mapper,
            Class<S> sourceType, Class<T> targetType) {
        // Ensure parameters are not null.
        Objects.requireNonNull(columnName, "Column name cannot be null.");
        Objects.requireNonNull(mapper, "Mapper function cannot be null.");
        Objects.requireNonNull(sourceType, "Source type cannot be null.");
        Objects.requireNonNull(targetType, "Target type cannot be null.");

        // Get column ensures that the column exists and is of the correct type.
        Series<S> original = getColumn(columnName, sourceType);
        ValueMapper<S, T> mapperCasted = (ValueMapper<S, T>) mapper;
        Series<T> mapped = original.mapValues(mapperCasted);

        Series.checkType(mapped, targetType); // Throws if types do not match.

        DataFrameImpl mappedFrame = new DataFrameImpl();
        for (String existingColumnName : columnNames) {
            if (existingColumnName.equals(columnName)) {
                mappedFrame.addColumnHelper(existingColumnName, targetType, mapped);
            } else {
                @SuppressWarnings("unchecked")
                Class<Object> columnType = (Class<Object>) types.get(existingColumnName);
                @SuppressWarnings("unchecked")
                Series<Object> columnSeries = (Series<Object>) getColumn(existingColumnName,
                        types.get(existingColumnName));

                // Types are checked when columns are added.
                mappedFrame.addColumnHelper(existingColumnName, columnType, columnSeries);
            }
        }

        return mappedFrame;
    }

    @Override
    public int size() {
        return index.size();
    }

    @Override
    public String toString() {
        // Create an AsciiTable and add the header row.
        AsciiTable at = new AsciiTable();
        at.addRule();
        Object[] headerRow = columnNames.toArray();
        at.addRow(headerRow);
        at.addRule();

        // Create a list of iterators for each column.
        List<Iterator<?>> columnIterators = new ArrayList<>();
        for (String column : columnNames) {
            columnIterators.insertItem(columns.get(column).iterator());
        }

        // Keep reading as long as at least one iterator still has data
        while (true) {
            boolean hasData = false;
            Object[] rowData = new Object[columnIterators.size()];

            for (int i = 0; i < columnIterators.size(); i++) {
                Iterator<?> it = columnIterators.getItem(i);

                // If the iterator has data, add it to the row.
                if (it.hasNext()) {
                    rowData[i] = it.next();
                    hasData = true; // Mark that we have data.
                } else {
                    // Add a placeholder if the iterator is empty.
                    rowData[i] = "N/A";
                }
            }

            // If no iterator had data, break the loop.
            if (!hasData) {
                break;
            }

            // Add the row to the table and add a rule.
            at.addRow(rowData);
            at.addRule();
        }

        return at.render(100);
    }

}
