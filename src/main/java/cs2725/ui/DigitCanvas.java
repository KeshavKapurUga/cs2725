/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.ui;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Interactive square canvas for drawing 28 × 28 handwritten digits.
 *
 * Strokes are antialiased. The digit is cropped, scaled to fit a 20 × 20 box,
 * centred by its centre-of-mass, and padded to 28 × 28. Intensities are
 * inverted (black -> high) and normalized with mean 0.1307 and standard
 * deviation 0.3081.
 */
public final class DigitCanvas extends JPanel {

    private static final int PIXELS = 28; // logical target size
    private static final int SCALE = 10; // GUI zoom factor
    private static final int SIZE = PIXELS * SCALE;
    private static final int BRUSH_RADIUS = SCALE * 2;

    /** Off-screen image. Gray 0 = black, 255 = white. */
    private final BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_BYTE_GRAY);

    private int lastX = -1, lastY = -1; // previous mouse point

    /**
     * Creates a blank white canvas and installs mouse listeners.
     */
    public DigitCanvas() {
        clear();

        MouseAdapter m = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                beginStroke(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                continueStroke(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                endStroke();
            }
        };
        addMouseListener(m);
        addMouseMotionListener(m);

        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    /**
     * Fills the canvas with white.
     */
    public void clear() {
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, SIZE, SIZE);
        g.dispose();
        repaint();
    }

    /**
     * Returns a 784-length vector suitable for an MNIST network.
     * Performs crop-resize-centre-pad as described in the class header.
     */
    public float[] getNormalizedInput() {

        /* Locate non-white pixels (in high-res space) */
        Rectangle box = findBoundingBox();
        if (box == null) { // blank canvas
            return new float[PIXELS * PIXELS];
        }

        /* Crop to the bounding box */
        BufferedImage cropped = img.getSubimage(box.x, box.y, box.width, box.height);

        /* Scale so that the longer side becomes 20 */
        int target = 20;
        int w = cropped.getWidth();
        int h = cropped.getHeight();
        double scale = target / (double) Math.max(w, h);
        int sw = (int) Math.round(w * scale);
        int sh = (int) Math.round(h * scale);

        BufferedImage scaled = new BufferedImage(sw, sh, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D gs = scaled.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gs.drawImage(cropped, 0, 0, sw, sh, null);
        gs.dispose();

        /* Paste into a blank 28 × 28, centred by centre-of-mass */
        BufferedImage digit = new BufferedImage(PIXELS, PIXELS, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D gd = digit.createGraphics();
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, PIXELS, PIXELS);

        Point com = centreOfMass(scaled);
        int dx = PIXELS / 2 - com.x;
        int dy = PIXELS / 2 - com.y;
        gd.drawImage(scaled, dx, dy, null);
        gd.dispose();

        /* Flatten, invert, normalize */
        float[] vec = new float[PIXELS * PIXELS];
        int k = 0;
        for (int y = 0; y < PIXELS; ++y) {
            for (int x = 0; x < PIXELS; ++x) {
                int gray = digit.getRGB(x, y) & 0xFF;
                int inv = 255 - gray;
                vec[k++] = (float) ((inv / 255.0 - 0.1307) / 0.3081);
            }
        }
        return vec;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    private void beginStroke(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        drawDot(lastX, lastY);
    }

    private void continueStroke(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        drawSegment(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
    }

    private void endStroke() {
        lastX = lastY = -1;
    }

    private void drawDot(int x, int y) {
        Graphics2D g = img.createGraphics();
        enableStrokeAA(g);
        g.setColor(Color.BLACK);
        g.fillOval(x - BRUSH_RADIUS, y - BRUSH_RADIUS,
                BRUSH_RADIUS * 2, BRUSH_RADIUS * 2);
        g.dispose();
        repaint();
    }

    private void drawSegment(int x0, int y0, int x1, int y1) {
        Graphics2D g = img.createGraphics();
        enableStrokeAA(g);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(BRUSH_RADIUS * 2,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        g.drawLine(x0, y0, x1, y1);
        g.dispose();

        int minX = Math.min(x0, x1) - BRUSH_RADIUS;
        int minY = Math.min(y0, y1) - BRUSH_RADIUS;
        int w = Math.abs(x1 - x0) + BRUSH_RADIUS * 2;
        int h = Math.abs(y1 - y0) + BRUSH_RADIUS * 2;
        repaint(minX, minY, w, h);
    }

    private static void enableStrokeAA(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
    }

    /**
     * Returns the tight bounding box of all pixels whose intensity is
     * darker than 250. Null if the image is blank.
     */
    private Rectangle findBoundingBox() {
        int minX = SIZE, minY = SIZE, maxX = -1, maxY = -1;
        for (int y = 0; y < SIZE; ++y)
            for (int x = 0; x < SIZE; ++x) {
                if ((img.getRGB(x, y) & 0xFF) < 250) {
                    if (x < minX)
                        minX = x;
                    if (y < minY)
                        minY = y;
                    if (x > maxX)
                        maxX = x;
                    if (y > maxY)
                        maxY = y;
                }
            }
        return maxX == -1 ? null : new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    /**
     * Centre of mass of the inverted intensities of a grayscale image.
     * Coordinates are in the image’s own reference frame.
     */
    private static Point centreOfMass(BufferedImage img) {
        double sum = 0, sx = 0, sy = 0;
        int w = img.getWidth();
        int h = img.getHeight();
        for (int y = 0; y < h; ++y)
            for (int x = 0; x < w; ++x) {
                int inv = 255 - (img.getRGB(x, y) & 0xFF);
                sum += inv;
                sx += inv * x;
                sy += inv * y;
            }
        if (sum == 0)
            return new Point(w / 2, h / 2);
        return new Point((int) Math.round(sx / sum),
                (int) Math.round(sy / sum));
    }
}
