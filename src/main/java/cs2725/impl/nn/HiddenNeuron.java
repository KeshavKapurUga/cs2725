/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.impl.nn;

public class HiddenNeuron extends LinearNeuron {

    public HiddenNeuron(int index, float[] weights, float bias) {
        super(index, weights, bias);
    }

    /**
     * Implementation of relu activation.
     * More information: https://en.wikipedia.org/wiki/Rectifier_(neural_networks)
     */
    private float relu(float val) {
        return Math.max(val, 0);
    }

    @Override
    public float compute() {
        float output = super.compute();
        return relu(output);
    }

}
