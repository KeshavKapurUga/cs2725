/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility to load neural network weights and biases from a plain text file.
 * The file format is as follows:
 * 
 * Line 1: number of layers (L)
 * Line 2: comma-separated list of layer sizes: input_size, n_1, n_2, ..., n_L
 * Next: for each layer l:
 * - n_(l+1) lines of comma-separated weights for each neuron
 * - 1 line of comma-separated biases for the layer
 */
public class NNLoader {

    /**
     * Container class for network parameters.
     * W[layer][neuron][input] holds the weight matrix for each layer.
     * b[layer][neuron] holds the bias vector for each layer.
     */
    public static final class NetworkParameters {
        public final float[][][] W; // Weights
        public final float[][] b; // Biases

        private NetworkParameters(float[][][] W, float[][] b) {
            this.W = W;
            this.b = b;
        }
    }

    /**
     * Loads weights and biases from a text file.
     * 
     * @param path Path to the text file
     * @return NetworkParameters object containing weights and biases
     */
    public static NetworkParameters load(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            // Read the number of layers
            int L = Integer.parseInt(br.readLine().trim());

            // Read the topology line (layer sizes)
            String[] topoTokens = br.readLine().split(",");
            int[] n = new int[topoTokens.length];
            for (int i = 0; i < topoTokens.length; i++) {
                n[i] = Integer.parseInt(topoTokens[i].trim());
            }

            // Check that topology is consistent with number of layers
            if (n.length != L + 1) {
                throw new IOException("Topology does not match number of layers.");
            }

            // Allocate arrays to store weights and biases
            float[][][] W = new float[L][][];
            float[][] b = new float[L][];

            // Process each layer
            for (int layer = 0; layer < L; layer++) {
                int rows = n[layer + 1]; // Number of neurons in this layer
                int cols = n[layer]; // Number of inputs to each neuron

                // Read weight matrix: one line per neuron
                float[][] wLayer = new float[rows][cols];
                for (int r = 0; r < rows; r++) {
                    String[] weightTokens = br.readLine().split(",");
                    if (weightTokens.length != cols) {
                        throw new IOException("Weight row length mismatch at layer " + layer);
                    }
                    for (int c = 0; c < cols; c++) {
                        wLayer[r][c] = Float.parseFloat(weightTokens[c].trim());
                    }
                }

                // Read bias vector: one line for all neurons
                String[] biasTokens = br.readLine().split(",");
                if (biasTokens.length != rows) {
                    throw new IOException("Bias length mismatch at layer " + layer);
                }
                float[] bLayer = new float[rows];
                for (int i = 0; i < rows; i++) {
                    bLayer[i] = Float.parseFloat(biasTokens[i].trim());
                }

                // Store the layer's parameters
                W[layer] = wLayer;
                b[layer] = bLayer;
            }

            // Return the structured parameters
            return new NetworkParameters(W, b);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to load the weights.", t);
        }
    }

}
