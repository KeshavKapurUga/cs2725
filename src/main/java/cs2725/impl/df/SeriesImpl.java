/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl.df;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import cs2725.api.List;
import cs2725.api.Set;
import cs2725.api.df.Series;
import cs2725.api.functional.Accumulator;
import cs2725.api.functional.PairwiseOperator;
import cs2725.api.functional.PrefixOperator;
import cs2725.api.functional.ValueMapper;
import cs2725.impl.ArrayList;
import cs2725.impl.HashSet;
import cs2725.impl.ImmutableList;

/**
 * Implementation of a Series that maintains values and an index.
 * 
 * @param <T> the type of elements in the series
 */
public class SeriesImpl<T> implements Series<T> {

    private final List<T> values;
    private final List<Integer> index;

    /**
     * Constructs a Series with given values and default index.
     * 
     * @param values the values in the Series
     */
    public SeriesImpl(List<T> values) {
        if (values == null) {
            throw new IllegalArgumentException("Values list cannot be null.");
        }
        this.values = ImmutableList.of(values);
        this.index = new ArrayList<>(values.size());
        for (int i = 0; i < values.size(); i++) {
            this.index.insertItem(i);
        }
    }

    /**
     * Constructs a Series with explicit index. Note that the index can have more
     * values than the number of values in the values list as long as the index
     * values are within the valid range. On the other hand the number of items in
     * the values can be larger than the number of items in the index as long as the
     * index values are within the valid range for the given values.
     * 
     * @param index  the index mapping to the values
     * @param values the values in the Series
     */
    public SeriesImpl(List<Integer> index, List<T> values) {
        if (values == null || index == null) {
            throw new IllegalArgumentException("Values and index cannot be null.");
        }
        for (int idx : index) {
            if (idx < 0 || idx >= values.size()) {
                throw new IllegalArgumentException("Index values must be within valid range.");
            }
        }
        this.values = ImmutableList.of(values);
        this.index = ImmutableList.of(index);
    }

    @Override
    public int size() {
        return index.size();
    }

    @Override
    public T get(int i) {
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + i);
        }
        return values.getItem(index.getItem(i));
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < size();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in Series.");
                }
                return get(pos++);
            }
        };
    }

    @Override
    public List<Integer> index() {
        return ImmutableList.of(index);
    }

    @Override
    public List<T> values() {
        List<T> result = new ArrayList<>(index.size());
        for (int i = 0; i < index.size(); i++) {
            result.insertItem(values.getItem(index.getItem(i)));
        }
        return ImmutableList.of(result);
    }

    @Override
    public Set<T> unique() {
        Set<T> set = new HashSet<>(index.size());
        for (T val : this) {
            set.add(val);
        }
        return set;
    }

    @Override
    public Series<T> withIndex(List<Integer> newIndex) {
        for (int i = 0; i < newIndex.size(); i++) {
            int idx = newIndex.getItem(i);
            if (idx < 0 || idx >= size()) {
                throw new IllegalArgumentException("Index out of bounds: " + idx);
            }
        }
        List<Integer> translatedIndex = new ArrayList<>(newIndex.size());
        for (int i = 0; i < newIndex.size(); i++) {
            translatedIndex.insertItem(index.getItem(newIndex.getItem(i)));
        }
        return new SeriesImpl<>(translatedIndex, values);
    }

    @Override
    public <R> R reduce(Accumulator<R, ? super T> accumulator, R initialValue) {
        R result = initialValue;
        for (T val : this) {
            result = accumulator.apply(result, val);
        }
        return result;
    }

    @Override
    public <R> Series<R> prefix(PrefixOperator<R, ? super T> prefixOperator, R initialValue) {
        List<R> result = new ArrayList<>(size());
        R previousPrefix = initialValue;
        for (T currentElement : this) {
            R currentPrefix = prefixOperator.apply(previousPrefix, currentElement);
            result.insertItem(currentPrefix);
            previousPrefix = currentPrefix;
        }
        return new SeriesImpl<>(result);
    }

    @Override
    public <U> Series<U> mapValues(ValueMapper<? super T, ? extends U> mapper) {
        List<U> mappedValues = new ArrayList<>(size());
        for (T val : this) { // Note: the iterator follows index order.
            mappedValues.insertItem(mapper.map(val));
        }
        return new SeriesImpl<>(mappedValues);
    }

    @Override
    public Series<T> sortBy(Comparator<? super T> comparator) {
        List<Integer> newIndex = index.copy();
        newIndex.sort((i, j) -> comparator.compare(values.getItem(i), values.getItem(j)));
        return this.withIndex(newIndex);
    }

    @Override
    public Series<T> selectByMask(Series<Boolean> mask) {
        if (mask.size() != size()) {
            throw new IllegalArgumentException("Mask size must match series size.");
        }
        List<Integer> newIdx = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            if (Boolean.TRUE.equals(mask.get(i))) {
                newIdx.insertItem(index.getItem(i));
            }
        }
        return new SeriesImpl<>(newIdx, values);
    }

    @Override
    public <U, R> Series<R> combineWith(Series<U> other, PairwiseOperator<? super T, ? super U, ? extends R> combiner) {
        if (other.size() != size()) {
            throw new IllegalArgumentException("Series sizes must match for combination.");
        }
        List<R> combined = new ArrayList<>(size());
        for (int i = 0; i < size(); i++) {
            combined.insertItem(combiner.apply(get(i), other.get(i)));
        }
        return new SeriesImpl<>(combined);
    }

    @Override
    public double sum() {
        double total = 0.0;
        for (T val : this) {
            total += asDouble(val);
        }
        return total;
    }

    @Override
    public long count() {
        long cnt = 0;
        for (T val : this) {
            asDouble(val);
            cnt++;
        }
        return cnt;
    }

    @Override
    public double mean() {
        return sum() / count();
    }

    @Override
    public double min() {
        double minVal = Double.MAX_VALUE;
        for (T val : this) {
            double d = asDouble(val);
            if (d < minVal) {
                minVal = d;
            }
        }
        return minVal;
    }

    @Override
    public double max() {
        double maxVal = -Double.MAX_VALUE;
        for (T val : this) {
            double d = asDouble(val);
            if (d > maxVal) {
                maxVal = d;
            }
        }
        return maxVal;
    }

    @Override
    public double var() {
        double m = mean();
        double sumSq = 0.0;
        for (T val : this) {
            double d = asDouble(val) - m;
            sumSq += d * d;
        }
        return sumSq / (count() - 1);
    }

    @Override
    public double std() {
        return Math.sqrt(var());
    }

    @Override
    public double median() {
        int n = (int) count();
        if (n == 0) {
            return Double.NaN;
        }
        List<Double> sorted = new ArrayList<>(n);
        for (T val : this) {
            sorted.insertItem(asDouble(val));
        }
        sorted.sort((a, b) -> Double.compare(a, b));
        if (n % 2 == 1) {
            return sorted.getItem(n / 2);
        } else {
            return (sorted.getItem(n / 2 - 1) + sorted.getItem(n / 2)) / 2.0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Index\tValue\n");
        for (int i = 0; i < size(); i++) {
            sb.append(index.getItem(i)).append("\t").append(values.getItem(index.getItem(i))).append("\n");
        }
        return sb.toString();
    }

}
