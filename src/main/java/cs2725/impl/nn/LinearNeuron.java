/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl.nn;

import cs2725.api.nn.Neuron;

public class LinearNeuron implements Neuron {

    private float[] inputs;
    private float[] weights;
    private float bias;
    private int index;

    public LinearNeuron(int index, float[] weights, float bias) {
        this.index = index;
        this.weights = weights;
        this.inputs = new float[weights.length];
        this.bias = bias;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getNumberOfInputs() {
        return weights.length;
    }

    @Override
    public void setInput(float input, int inputIdx) {
        inputs[inputIdx] = input;
    }

    @Override
    public float compute() {
        float output = 0;
        for (int i = 0; i < getNumberOfInputs(); ++i) {
            output += (inputs[i] * weights[i]);
        }
        return output + bias;
    }

}
