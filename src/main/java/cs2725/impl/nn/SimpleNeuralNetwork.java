/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl.nn;

import cs2725.api.List;
import cs2725.api.Map;
import cs2725.api.Queue;
import cs2725.api.Set;
import cs2725.api.nn.NeuralNetwork;
import cs2725.api.nn.Neuron;
import cs2725.impl.ArrayList;
import cs2725.impl.ArrayQueue;
import cs2725.impl.HashMap;
import cs2725.impl.HashSet;
import cs2725.util.NNLoader;
import cs2725.util.NNLoader.NetworkParameters;

public class SimpleNeuralNetwork implements NeuralNetwork {

    // The neural network represented as an adjacency list.
    private Map<Neuron, List<Neuron>> network = new HashMap<>();

    // Neurons in the input layer.
    Neuron[] inputLayer;

    // Neurons in the output layer.
    Neuron[] outputLayer;

    public SimpleNeuralNetwork(String path) {
        initNetwork(path);
    }

    /**
     * Initialize the neural network with the weights from the given path.
     */
    private void initNetwork(String path) {
        Neuron[][] hiddenLayers;

        NetworkParameters networkParameters = NNLoader.load(path);

        // The number of non-input layers in the neural network.
        int layerCount = networkParameters.W.length;

        // The number of output neurons.
        int numberOfOutputNeurons = networkParameters.W[layerCount - 1].length;

        // Initialize the hiddenLayers.
        int outputLayerIdx = layerCount - 1;
        hiddenLayers = new Neuron[outputLayerIdx][];

        // The number of weights in the first neuron in the first hidden layer.
        int inputSize = networkParameters.W[0][1].length;

        // Initialize the input neurons.
        inputLayer = new InputNeuron[inputSize];
        for (int i = 0; i < inputSize; ++i) {
            inputLayer[i] = new InputNeuron(i);
        }

        // Initialize hidden layers.
        for (int i = 0; i < layerCount - 1; ++i) {
            hiddenLayers[i] = new Neuron[networkParameters.W[i].length];

            // Initialize hidden layer i.
            for (int j = 0; j < hiddenLayers[i].length; ++j) {
                hiddenLayers[i][j] = new HiddenNeuron(j, networkParameters.W[i][j], networkParameters.b[i][j]);
            }
        }

        // Initialize the output neurons.
        outputLayer = new Neuron[numberOfOutputNeurons];
        for (int i = 0; i < numberOfOutputNeurons; ++i) {
            outputLayer[i] = new LinearNeuron(i, networkParameters.W[outputLayerIdx][i],
                    networkParameters.b[outputLayerIdx][i]);
        }

        // Print the layer stats.
        System.out.println("Input layer #neurones: " + inputLayer.length);
        for (int i = 0; i < layerCount - 1; ++i) {
            System.out.println("Hidden layer " + i + " #neurones: " + hiddenLayers[i].length);
        }
        System.out.println("Output layer #neurones: " + outputLayer.length);

        // Wire the neural network.
        wireNetwork(inputLayer, hiddenLayers, outputLayer);
    }

    /**
     * Adds directed edges from every neuron in 'from' layer to every neuron in 'to'
     * layer.
     */
    private void connect(Neuron[] from, Neuron[] to) {
        for (Neuron u : from) {
            network.putIfAbsent(u, new ArrayList<>());
            List<Neuron> adj = network.get(u);
            for (Neuron v : to) {
                adj.insertItem(v); // u -> v
            }
        }
    }

    /**
     * Constructs the connectivity of a fully-connected feed-forward neural network.
     *
     * Given arrays of neurons representing the input layer, a set of hidden layers,
     * and an output layer, this method populates the network adjacency list such
     * that each neuron in layer L has directed edges to all neurons in layer L+1.
     * It ensures all neurons appear in the adjacency map, even those with no
     * outgoing edges, such as output neurons.
     *
     * @param inputLayer   the array of input neurons
     * @param hiddenLayers a 2D array of hidden layers; each sub-array
     *                     hiddenLayers[i] is one hidden layer
     * @param outputLayer  the array of output neurons
     */
    private void wireNetwork(Neuron[] inputLayer, Neuron[][] hiddenLayers, Neuron[] outputLayer) {
        // Connect Input -> First hidden (or directly to output if no hidden layers)
        if (hiddenLayers.length == 0) {
            connect(inputLayer, outputLayer);
        } else {
            connect(inputLayer, hiddenLayers[0]);
        }

        // Todo: Project 3: To be implemented

        // Connect Hidden[i] -> Hidden[i+1]
        // Establish the connections following the snippet above.

        // Connect Last hidden -> Output
        // Establish the connections following the snippet above.

        // Ensure every output neuron appears in the map (with empty adjacency list)
        for (Neuron out : outputLayer) {
            network.putIfAbsent(out, new ArrayList<>());
        }
    }

    @Override
    public float[] predict(float[] input) {
        // Todo: Project 3: To be implemented

        // Step 1: Check if the input size matches the number of input neurons.
        // If not, throw a runtime exception.

        // Step 2: Create an ArrayQueue for BFS traversal of the network.
        // Also, create a set to keep track of visited neurons.

        // Step 3: Initialize input neurons.
        // For each input neuron, set its input value using the input array.
        // Each input i should be set into each input neuron with index i.
        // Mark each input neuron as visited.
        // Enqueue each input neuron into the queue.
        // Note that this is different from typical BFS where you only enqueue one
        // starting node. Discuss with your team members as to why we do this. Include a
        // summary of your discussion in the readme.

        // Step 4: Perform a breadth-first traversal of the network:
        // While the queue is not empty:
        // - Dequeue a neuron from the queue.
        // - Compute its output.
        // - For each adjacent neuron in the network:
        // - If the neuron hasn't been visited:
        // - Mark it as visited and enqueue it.
        // - Pass the current neuron's output as input to the adjacent neuron
        // at the index corresponding to the current neuron's index.

        // Manually perform this modified BFS traversal on the example neural network
        // image provided in the instructions document. Provide the order that you visit
        // each neuron in your readme. Specify each neuron in terms of its layer and the
        // neuron index.

        // Step 5: Prepare the final output:
        // For each output neuron:
        // - Compute its output and store it in the result array.

        // Step 6: Return the output array.

        throw new UnsupportedOperationException("To be implemented");
    }

}
