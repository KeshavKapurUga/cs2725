/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.nn;

/**
 * Represents a neural network capable of producing predictions
 * based on a given input array.
 */
public interface NeuralNetwork {

    /**
     * Computes the network's output for a given input array.
     *
     * @param input an array of input values
     * @return an array of output values
     */
    public float[] predict(float[] input);

    /**
     * Returns the index of the largest value in the output array.
     * 
     * This is typically used to convert the network's output into a class label,
     * assuming the index with the highest value represents the predicted class.
     *
     * @param output the array of output values from the network
     * @return the index of the maximum value in the array
     */
    default int toLabel(float[] output) {
        int maxIdx = 0;
        for (int i = 1; i < output.length; ++i) {
            if (output[maxIdx] < output[i]) {
                maxIdx = i;
            }
        }
        return maxIdx;
    }

}