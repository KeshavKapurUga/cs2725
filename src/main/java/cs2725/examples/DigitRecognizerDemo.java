/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import java.io.File;

import cs2725.api.nn.NeuralNetwork;
import cs2725.impl.nn.SimpleNeuralNetwork;
import cs2725.ui.DigitPredictorUI;

public class DigitRecognizerDemo {

    public static void main(String[] args) {
        // Instantiate the neural network
        String weightsPath = "resources" + File.separator + "digits_network_weights.txt";
        NeuralNetwork network = new SimpleNeuralNetwork(weightsPath);

        // Initialize the UI and set the network reference.
        DigitPredictorUI ui = new DigitPredictorUI();
        ui.setNeuralNetwork(network);
        ui.setVisible(true);
    }

}
