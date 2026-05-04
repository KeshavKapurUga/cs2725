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

public class DataFrameExample {

    public static void main(String[] args) {
        /**
         * Load data from a CSV file. After loading all columns are of type String.
         */
        DataFrame dataFrame = new DataFrameImpl()
                .readCsv("resources" + File.separator + "movies.csv");

        /**
         * Convert the String columns to their appropriate types as needed.
         */
        dataFrame = dataFrame.mapValues("Month", (v) -> Integer.parseInt(v), String.class, Integer.class)
                .mapValues("Day", (v) -> Integer.parseInt(v), String.class, Integer.class)
                .mapValues("Year", (v) -> Integer.parseInt(v), String.class, Integer.class)
                .mapValues("Rating", (v) -> Integer.parseInt(v), String.class, Integer.class);

        /*
         * Print the number of rows.
         */
        System.out.println("Printing the size:");
        System.out.println(dataFrame.size());

        /*
         * Extract the first 10 rows.
         */
        DataFrame first10 = dataFrame.rows(0, 10);
        System.out.println("Printing first 10 rows:");
        System.out.println(first10);

        /*
         * Extract only the Title and Year columns.
         */
        DataFrame titleYear = first10.columns(List.of("Title", "Year"));
        System.out.println("Printing first 10 rows with only Title and Year columns:");
        System.out.println(titleYear);

        /*
         * Combine title and genre into a single column. ie: Title - [Genre]
         */
        Series<String> titleColumn = first10.getColumn("Title", String.class);
        Series<String> genreColumn = first10.getColumn("Genres", String.class);
        Series<String> titleGenreCombinedColumn = titleColumn.combineWith(genreColumn,
                (title, genre) -> title + " - [" + genre + "]");
        DataFrame titleGenreCombined = new DataFrameImpl()
                .addColumn("Title and Genre", String.class, titleGenreCombinedColumn);
        System.out.println("Printing first 10 rows with Title and genre combined:");
        System.out.println(titleGenreCombined);

        /*
         * Find the average rating for the movies in the first 10 rows.
         */
        // As the first step extract the titles column from the first 10 rows dataFrame.
        Series<String> first10TitlesColumn = first10.getColumn("Title", String.class);
        // Find the unique titles in the first 10 rows.
        Set<String> first10TitlesSet = first10TitlesColumn.unique();
        // Create a boolean mask to select those titles from the original dataFrame.
        Series<Boolean> first10TitlesMask = dataFrame.getColumn("Title", String.class)
                .mapValues((v) -> first10TitlesSet.contains(v));
        // Extract the rows that correspond to the title mask.
        DataFrame first10TitlesFiltered = dataFrame.selectByMask(first10TitlesMask);
        // Define a column aggregate to calculate the average rating.
        ColumnAggregate<Integer, Double> avgRating = new ColumnAggregate<>("Rating", "AvgRating",
                (g) -> g.mean(), Integer.class, Double.class);
        // Group the dataFrame by Title and then apply the column aggregate.
        DataFrame first10TitlesAvgRating = first10TitlesFiltered.groupBy("Title")
                .aggregate(List.of(avgRating));
        System.out.println("Printing average rating for the first 10 titles:");
        System.out.println(first10TitlesAvgRating);

        // A column aggregate to count the number of ratings in each group after
        // applying a 'group by' operation.
        ColumnAggregate<Integer, Long> genreRatingCount = new ColumnAggregate<>("Rating", "RatingCount",
                (g) -> g.count(), Integer.class, Long.class);

        // A column aggregate to calculate the average rating in each group after
        // applying a 'group by' operation.
        ColumnAggregate<Integer, Double> genreAvgRating = new ColumnAggregate<>("Rating", "AvgRating",
                (g) -> g.mean(), Integer.class, Double.class);

        // A column aggregate to calculate the minimum rating in each group after
        // applying a 'group by' operation.
        ColumnAggregate<Integer, Double> genreMinRating = new ColumnAggregate<>("Rating", "MinRating",
                (g) -> g.min(), Integer.class, Double.class);

        // A column aggregate to calculate the maximum rating in each group after
        // applying a 'group by' operation.
        ColumnAggregate<Integer, Double> genreMaxRating = new ColumnAggregate<>("Rating", "MaxRating",
                (g) -> g.max(), Integer.class, Double.class);

        /*
         * Group the dataFrame by Genres and then apply the column aggregates.
         */
        DataFrame genreStats = dataFrame.groupBy("Genres")
                .aggregate(List.of(genreRatingCount, genreAvgRating, genreMinRating, genreMaxRating));
        System.out.println("Printing rating statistics for each genre:");
        System.out.println(genreStats);

        /*
         * Sort by AvgRating in descending order and get top 3 genres.
         */
        genreStats = genreStats.sortBy("AvgRating", Comparator.reverseOrder(), Double.class);
        DataFrame genresStatsTop5 = genreStats.rows(0, 3);
        System.out.println("Printing the rating stats of top 3 genres by AvgRating:");
        System.out.println(genresStatsTop5);
    }

}
