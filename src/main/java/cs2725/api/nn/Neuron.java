/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.api.nn;

/**
 * Represents a simple neuron that receives a defined number of inputs
 * and produces an output based on them.
 */
public interface Neuron {

    /**
     * Returns this neuron's position identifier in its containing
     * layer. The index is zero‑based and unique within the containing layer.
     *
     * @return the neuron’s index
     */
    public int getIndex();

    /**
     * Returns the number of input values this neuron accepts.
     *
     * @return the number of inputs
     */
    public int getNumberOfInputs();

    /**
     * Sets the value of a specific input.
     * Must be called separately for each input index before computation.
     *
     * @param input    the input value
     * @param inputIdx the index of the input (zero-based)
     */
    public void setInput(float input, int inputIdx);

    /**
     * Computes the neuron's output using its current input values.
     *
     * @return the output value
     */
    public float compute();

}
