/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import java.io.File;
import java.util.Comparator;

import cs2725.api.List;
import cs2725.api.Set;
import cs2725.api.df.ColumnAggregate;
import cs2725.api.df.DataFrame;
import cs2725.api.df.Series;
import cs2725.impl.df.DataFrameImpl;

/**
 * This class must be used to demonstrate the completed DataFrame project.
 */
public class DataFrameDemo {

    public static void main(String[] args) {
        // Load data from a CSV file. After loading all columns are of type String.
        DataFrame dataFrame = new DataFrameImpl()
                .readCsv("resources" + File.separator + "movies.csv");

        // Convert the String columns to their appropriate types as needed.
        dataFrame = dataFrame.mapValues("Month", (v) -> Integer.parseInt(v), String.class, Integer.class)
                .mapValues("Day", (v) -> Integer.parseInt(v), String.class, Integer.class)
                .mapValues("Year", (v) -> Integer.parseInt(v), String.class, Integer.class)
                .mapValues("Rating", (v) -> Integer.parseInt(v), String.class, Integer.class);

        // --- Part 1: Top 10 highest rated movies by average rating ---

        ColumnAggregate<Integer, Double> avgRatingAgg = new ColumnAggregate<>(
                "Rating", "AvgRating", (g) -> g.mean(), Integer.class, Double.class);

        DataFrame movieAvgRating = dataFrame.groupBy("Title")
                .aggregate(List.of(avgRatingAgg));

        movieAvgRating = movieAvgRating.sortBy("AvgRating", Comparator.reverseOrder(), Double.class);
        DataFrame top10Movies = movieAvgRating.rows(0, 10);

        System.out.println("Top 10 highest rated movies (Title and average rating):");
        System.out.println(top10Movies);

        // --- Part 2: Average rating of those top 10 movies by age group ---

        Series<String> top10TitlesSeries = top10Movies.getColumn("Title", String.class);
        Set<String> top10TitlesSet = top10TitlesSeries.unique();

        Series<Boolean> top10Mask = dataFrame.getColumn("Title", String.class)
                .mapValues((v) -> top10TitlesSet.contains(v));
        DataFrame top10Filtered = dataFrame.selectByMask(top10Mask);

        // Build combined "Title | AgeGroup" column
        Series<String> titleSeries = top10Filtered.getColumn("Title", String.class);
        Series<String> ageSeries = top10Filtered.getColumn("Age", String.class);
        Series<String> titleAgeSeries = titleSeries.combineWith(ageSeries, (t, a) -> t + " | " + a);

        DataFrame titleAgeRatingDf = new DataFrameImpl()
                .addColumn("TitleAge", String.class, titleAgeSeries)
                .addColumn("Rating", Integer.class, top10Filtered.getColumn("Rating", Integer.class));

        ColumnAggregate<Integer, Double> avgRatingAgg2 = new ColumnAggregate<>(
                "Rating", "AvgRating", (g) -> g.mean(), Integer.class, Double.class);

        DataFrame top10ByAgeGroup = titleAgeRatingDf.groupBy("TitleAge")
                .aggregate(List.of(avgRatingAgg2));

        System.out.println("Average rating for top 10 movies by age group (Title | AgeGroup and AvgRating):");
        System.out.println(top10ByAgeGroup);

        // --- Extra Credit: Which occupations rate highest, and how do those occupations rate each genre? ---
        //
        // Step 1 (groupBy #1): Group all ratings by Occupation and compute average rating.
        //         Identify the top 3 occupations by average rating.
        // Step 2 (groupBy #2): Filter original data to only those top 3 occupations,
        //         create a combined "Occupation | Genre" key, group by it, and compute
        //         average rating — revealing how each top occupation rates each genre.

        ColumnAggregate<Integer, Double> occAvgAgg = new ColumnAggregate<>(
                "Rating", "AvgRating", (g) -> g.mean(), Integer.class, Double.class);

        DataFrame occAvgRating = dataFrame.groupBy("Occupation")
                .aggregate(List.of(occAvgAgg));
        occAvgRating = occAvgRating.sortBy("AvgRating", Comparator.reverseOrder(), Double.class);
        DataFrame top3Occupations = occAvgRating.rows(0, 3);

        System.out.println("Top 3 occupations by average rating (groupBy #1 — Occupation):");
        System.out.println(top3Occupations);

        // Step 2: filter original data to rows belonging to those top 3 occupations
        Series<String> top3OccSeries = top3Occupations.getColumn("Occupation", String.class);
        Set<String> top3OccSet = top3OccSeries.unique();

        Series<Boolean> occMask = dataFrame.getColumn("Occupation", String.class)
                .mapValues((v) -> top3OccSet.contains(v));
        DataFrame top3OccFiltered = dataFrame.selectByMask(occMask);

        // Build combined "Occupation | Genre" column
        Series<String> occSeries = top3OccFiltered.getColumn("Occupation", String.class);
        Series<String> genreSeries = top3OccFiltered.getColumn("Genres", String.class);
        Series<String> occGenreSeries = occSeries.combineWith(genreSeries, (o, g) -> o + " | " + g);

        DataFrame occGenreRatingDf = new DataFrameImpl()
                .addColumn("OccupationGenre", String.class, occGenreSeries)
                .addColumn("Rating", Integer.class, top3OccFiltered.getColumn("Rating", Integer.class));

        ColumnAggregate<Integer, Double> occGenreAvgAgg = new ColumnAggregate<>(
                "Rating", "AvgRating", (g) -> g.mean(), Integer.class, Double.class);

        DataFrame occGenreAvgRating = occGenreRatingDf.groupBy("OccupationGenre")
                .aggregate(List.of(occGenreAvgAgg));
        occGenreAvgRating = occGenreAvgRating.sortBy("AvgRating", Comparator.reverseOrder(), Double.class);

        System.out.println("Average rating by Occupation | Genre for the top 3 occupations (groupBy #2 — Occupation | Genre):");
        System.out.println(occGenreAvgRating);
    }

}
