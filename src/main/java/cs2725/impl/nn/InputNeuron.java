/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl.nn;

import cs2725.api.nn.Neuron;

public class InputNeuron implements Neuron {

    private float input;
    private int index;

    public InputNeuron(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getNumberOfInputs() {
        return 1;
    }

    @Override
    public void setInput(float input, int inputIdx) {
        if (inputIdx != 0) {
            throw new RuntimeException("Invalid input index for an input neuron.");
        }
        this.input = input;
    }

    @Override
    public float compute() {
        return input;
    }

}
