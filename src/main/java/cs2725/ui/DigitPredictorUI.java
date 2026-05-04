/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.ui;

import cs2725.api.nn.NeuralNetwork;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Simple Swing frame containing a DigitCanvas, a Predict button, and a label
 * showing the predicted digit.
 *
 * The caller must supply a NeuralNetwork using setNeuralNetwork before
 * predictions can be run.
 */
public final class DigitPredictorUI extends JFrame {

    private final DigitCanvas canvas = new DigitCanvas();
    private final JLabel result = new JLabel("Draw a digit", JLabel.CENTER);
    private NeuralNetwork network;

    public DigitPredictorUI() {
        super("Digit Predictor");

        setLayout(new BorderLayout());

        // Create the digit drawing canvas
        canvas.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Result label
        result.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        result.setText("Draw a digit");
        result.setAlignmentX(LEFT_ALIGNMENT);
        result.setForeground(Color.DARK_GRAY);

        // Predict button
        JButton predictBtn = new JButton("Predict");
        predictBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        predictBtn.addActionListener(e -> onPredict());

        // Clear button
        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        clearBtn.addActionListener(e -> canvas.clear());

        // First row: button panel (horizontal)
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonRow.setAlignmentX(LEFT_ALIGNMENT);
        buttonRow.add(predictBtn);
        buttonRow.add(clearBtn);

        // Second row: result label panel
        JPanel resultRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultRow.setAlignmentX(LEFT_ALIGNMENT);
        resultRow.add(result);

        // Container panel to hold both rows vertically
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.add(buttonRow);
        bottom.add(resultRow);

        // Add to main frame
        add(canvas, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /**
     * Sets the NeuralNetwork used for prediction.
     */
    public void setNeuralNetwork(NeuralNetwork nn) {
        this.network = nn;
    }

    /**
     * Called when the Predict button is pressed.
     */
    private void onPredict() {
        if (network == null) {
            result.setText("NeuralNetwork not set");
            return;
        }
        float[] input = canvas.getNormalizedInput();
        int pred = network.toLabel(network.predict(input));
        result.setText("Prediction: " + pred);
    }

}