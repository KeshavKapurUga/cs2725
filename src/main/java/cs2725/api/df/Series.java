/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.df;

import java.util.Comparator;
import java.util.Iterator;

import cs2725.api.List;
import cs2725.api.Set;
import cs2725.api.functional.Accumulator;
import cs2725.api.functional.PairwiseOperator;
import cs2725.api.functional.PrefixOperator;
import cs2725.api.functional.ValueMapper;

/**
 * A Series is an indexed collection of values. The values are stored in a fixed
 * list and the index references positions in that list. Reordering and removing
 * indices (subsetting) changes the order and the items available in the Series
 * while the original values list remain intact. This design decouples the
 * logical order and subset selection of the Series from the backing storage of
 * its values.
 * 
 * The implementations should ensure immutability for getters by returning
 * immutable lists.
 * 
 * @param <T> the type of elements in the series
 */
public interface Series<T> extends Iterable<T> {

    /**
     * Returns the number of elements in the Series. This is the same as the size of
     * the index.
     *
     * @return the size of the Series
     */
    int size();

    /**
     * Returns the value at the given index position according to the index order.
     *
     * @param index the position in the Series
     * @return the value at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    T get(int index);

    /**
     * Returns an iterator over the elements in the Series in the order of the
     * index.
     *
     * @return an iterator over the elements in the Series
     */
    Iterator<T> iterator();

    /**
     * Returns an immutable version of the index list where each element points to a
     * position in the values list. An immutable list is returned so that
     * modifications to the index is not possible.
     * 
     * @return an immutable version of the index
     */
    List<Integer> index();

    /**
     * Returns an immutable version of the values list that corresponds to the
     * index.
     * 
     * @return an immutable version of the values
     */
    List<T> values();

    /**
     * Returns the unique values in the Series.
     * 
     * @return a new Set containing the unique values
     */
    Set<T> unique();

    /**
     * Returns a copy of the Series with a new index.
     * 
     * The caller of this function only knows that the Series has the items pointed
     * to by the current index in the order they appear in the current index. When
     * the caller gives a new index, the Series should be reorganized based on the
     * new index. This means the existing index should be reorganized based on the
     * new index in the new Series.
     *
     * @param newIndex the new list of indices, where each value must be within the
     *                 valid range of the current Series.
     *                 ie: 0 <= newIndex[i] < size() for each valid i.
     * @return a new Series<T> with the specified index
     * @throws IllegalArgumentException if any index value in the newIndex is not
     *                                  within the valid range for this Series
     */
    Series<T> withIndex(List<Integer> newIndex);

    /**
     * Reduces all elements into a single value by applying the given accumulator in
     * the index order.
     * 
     * This function is similar to Java 8's Stream.reduce method.
     *
     * @param <R>          the type of the final result
     * @param accumulator  a function that takes the previous aggregated result and
     *                     the current element and returns the updated result
     * @param initialValue the starting value for the reduction that is used as the
     *                     seed value for the aggregated result
     * @return the final reduced value after processing all elements
     */
    <R> R reduce(Accumulator<R, ? super T> accumulator, R initialValue);

    /**
     * Computes a prefix series by repeatedly applying a prefix operator in the
     * index order. Each entry in the resulting prefix series is built from the
     * previous prefix value and the current element in the series.
     *
     * @param <R>            the type of elements in the resulting prefix list
     * @param prefixOperator a function that produces the next prefix value from
     *                       the current prefix and the next element
     * @param initialValue   the initial (seed) prefix value
     * @return a list of prefix results, one result per element in the Series
     */
    <R> Series<R> prefix(PrefixOperator<R, ? super T> prefixOperator, R initialValue);

    /**
     * Creates a new Series by applying the given value mapper to each element in
     * the index order.
     *
     * @param <U>    the type of elements in the new Series
     * @param mapper a function mapping each element to a new value
     * @return a new Series containing the mapped values
     */
    <U> Series<U> mapValues(ValueMapper<? super T, ? extends U> mapper);

    /**
     * Sorts this Series based on the supplied comparator and return a new sorted
     * Series. General approach is create a new sorted the index list based on the
     * values in the Series and return a new Series with the sorted index list and
     * the original unmodified values list.
     *
     * @param comparator the comparator used to order the elements
     */
    Series<T> sortBy(Comparator<? super T> comparator);

    /**
     * Returns a new Series containing only the elements at indices where
     * the mask has true as the value. The size of the mask must match the
     * size of this Series. null values are treated as false.
     *
     * @param mask a Series of booleans indicating which elements to retain
     * @return a new Series containing only the masked elements
     * @throws IllegalArgumentException if the mask size differs from the size of
     *                                  this Series
     */
    Series<T> selectByMask(Series<Boolean> mask);

    /**
     * Applies a pairwise operation to each element in this Series and the
     * corresponding element in the other Series and produce a new Series. The size
     * of both Series must match.
     *
     * @param <U>      the type of elements in the other Series
     * @param <R>      the type of elements in the resulting Series
     * @param other    another Series with the same length and compatible indices
     * @param combiner a function that takes one element from this Series and one
     *                 element from the other Series and returns a new value
     * @return a new Series of type R with the same length
     * @throws IllegalArgumentException if the two Series differ in size
     */
    <U, R> Series<R> combineWith(Series<U> other, PairwiseOperator<? super T, ? super U, ? extends R> combiner);

    /**
     * Computes the sum of elements as a double. The function only works if the type
     * of the series is numeric.
     */
    double sum();

    /**
     * Computes the count of elements. The function only works if the type
     * of the series is numeric.
     */
    long count();

    /**
     * Computes the arithmetic mean (average). The function only works if the type
     * of the series is numeric.
     * 
     * More info: https://en.wikipedia.org/wiki/Arithmetic_mean
     */
    double mean();

    /**
     * Finds the minimum value. The function only works if the type
     * of the series is numeric.
     */
    double min();

    /**
     * Finds the maximum value. The function only works if the type
     * of the series is numeric.
     */
    double max();

    /**
     * Computes the sample variance. The function only works if the type
     * of the series is numeric.
     * 
     * More info: https://en.wikipedia.org/wiki/Variance#Unbiased_sample_variance
     */
    double var();

    /**
     * Computes the sample standard deviation. The function only works if the type
     * of the series is numeric.
     * 
     * More info:
     * https://en.wikipedia.org/wiki/Unbiased_estimation_of_standard_deviation
     */
    double std();

    /**
     * Computes the median value. The function only works if the type
     * of the series is numeric.
     * 
     * More info: https://en.wikipedia.org/wiki/Median#Finite_set_of_numbers
     */
    double median();

    /**
     * Converts a given value to a double or 0.0 if null. This function can be used
     * to convert the values of type T to double values to calculate the statistics
     * in the implementation classes.
     * 
     * @throws UnsupportedOperationException if the value is a non-null and non
     *                                       numeric value.
     */
    default double asDouble(T val) {
        if (val == null) {
            return 0.0;
        }
        if (!(val instanceof Number)) {
            throw new UnsupportedOperationException(
                    "Series contains non-numeric type: " + val.getClass().getName());
        }
        return ((Number) val).doubleValue();
    }

    /**
     * Checks if the series contain only values of the specified type. If any value
     * is not of the specified type, an IllegalArgumentException is thrown.
     *
     * @param series the Series to check
     * @param type   the expected type of the elements
     * @param <U>    the type of the elements
     * @throws IllegalArgumentException if the series contains a value that is not
     *                                  of the specified type
     */
    static <U> void checkType(Series<?> series, Class<U> type) {
        for (int i = 0; i < series.size(); i++) {
            Object value = series.get(i);
            if (value != null && !type.isInstance(value)) {
                throw new IllegalArgumentException("Series value type mismatch: " + value.getClass().getName());
            }
        }
    }
}
