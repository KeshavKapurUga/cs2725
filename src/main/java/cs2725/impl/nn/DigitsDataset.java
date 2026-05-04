/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl.nn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Loads a dataset of 28x28 handwritten digit images and their labels.
 * Each line in the input file contains 784 pixel values followed by the label,
 * all separated by commas. Pixel values are integers in [0, 255].
 * 
 * Inputs are normalized using the formula:
 * x = (x / 255.0 - 0.1307) / 0.3081
 */
public final class DigitsDataset {

    private static final String DATA_PATH = "resources" + File.separator + "mnist_test_data.txt";

    private final float[][] inputs; // inputs[i][784], normalized floats
    private final int[] labels; // labels[i] in [0, 9]

    /**
     * Private constructor to enforce static loading.
     */
    private DigitsDataset(float[][] inputs, int[] labels) {
        this.inputs = inputs;
        this.labels = labels;
    }

    /**
     * Loads the dataset from the default path.
     * 
     * @return a DigitsDataset instance containing inputs and labels
     * @throws RuntimeException if the file cannot be read or parsed
     */
    public static DigitsDataset load() {
        ArrayList<float[]> inputList = new ArrayList<>();
        ArrayList<Integer> labelList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split(",");
                if (tokens.length != 785) {
                    throw new IOException("Invalid line length: " + tokens.length);
                }

                float[] input = new float[784];
                for (int i = 0; i < 784; i++) {
                    int raw = Integer.parseInt(tokens[i].trim());
                    input[i] = normalize(raw);
                }
                int label = Integer.parseInt(tokens[784].trim());

                inputList.add(input);
                labelList.add(label);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Failed to load the dataset!", t);
        }

        float[][] inputs = inputList.toArray(new float[0][0]);
        int[] labels = new int[labelList.size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = labelList.get(i);
        }

        return new DigitsDataset(inputs, labels);
    }

    /**
     * Returns the number of samples in the dataset.
     * 
     * @return dataset size
     */
    public int size() {
        return labels.length;
    }

    /**
     * Returns the normalized input vector for a given sample.
     * 
     * @param index index of the sample
     * @return normalized float[784] input vector
     */
    public float[] getInput(int index) {
        return inputs[index];
    }

    /**
     * Returns the label for a given sample.
     * 
     * @param index index of the sample
     * @return integer label in [0, 9]
     */
    public int getLabel(int index) {
        return labels[index];
    }

    /**
     * Normalizes a pixel value using:
     * (x / 255.0 - 0.1307) / 0.3081
     * 
     * @param value raw pixel in [0, 255]
     * @return normalized float
     */
    private static float normalize(int value) {
        return (float) ((value / 255.0 - 0.1307) / 0.3081);
    }
}