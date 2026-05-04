/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.viz;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizJdkEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Viz {
    private static final int POLL_MS = 300;

    private final Path dotPath;
    private final JLabel status = new JLabel(" ");
    private final ImagePanel panel = new ImagePanel();

    private final ExecutorService worker = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "viz-render");
        t.setDaemon(true);
        return t;
    });

    private final AtomicBoolean rendering = new AtomicBoolean(false);

    private volatile Stamp lastStamp;
    private volatile BufferedImage lastGood;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        Graphviz.useEngine(new GraphvizJdkEngine());

        Path p = args.length > 0 ? Paths.get(args[0]) : Paths.get("ds_viz.dot");
        SwingUtilities.invokeLater(() -> new Viz(p).start());
    }

    private Viz(Path dotPath) {
        this.dotPath = dotPath.toAbsolutePath().normalize();
    }

    private void start() {
        JFrame f = new JFrame("Viz — " + dotPath.getFileName());
        f.setAlwaysOnTop(true);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        status.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JPanel root = new JPanel(new BorderLayout());
        root.add(panel, BorderLayout.CENTER);
        root.add(status, BorderLayout.SOUTH);
        root.setBackground(Color.WHITE);

        f.setContentPane(root);
        f.setSize(900, 700);
        f.setLocationRelativeTo(null);

        Timer timer = new Timer(POLL_MS, e -> poll());
        timer.setRepeats(true);
        timer.start();

        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) {
                timer.stop();
                worker.shutdownNow();
            }
        });

        f.setVisible(true);
        poll();
    }

    private void poll() {
        if (rendering.get()) return;

        try {
            if (!Files.exists(dotPath)) {
                setStatus("Missing: " + dotPath);
                return;
            }

            RenderTarget target = computeRenderTarget();
            if (target.widthPx <= 0) return;

            Stamp s = Stamp.of(dotPath, target.widthPx);
            if (Objects.equals(s, lastStamp) && lastGood != null) return;

            rendering.set(true);
            worker.submit(() -> render(s, target));
        } catch (Exception ex) {
            setStatus("I/O error " + Instant.now());
            ex.printStackTrace();
        }
    }

    private RenderTarget computeRenderTarget() {
        int cw = panel.getWidth();
        int ch = panel.getHeight();
        if (cw <= 0 || ch <= 0) return new RenderTarget(0, 1.0, 1.0);

        AffineTransform tx = panel.getGraphicsConfiguration() != null
                ? panel.getGraphicsConfiguration().getDefaultTransform()
                : new AffineTransform();

        double sx = tx.getScaleX();
        double sy = tx.getScaleY();

        int wPx = (int) Math.ceil(cw * sx);
        int hPx = (int) Math.ceil(ch * sy);
        wPx = Math.max(1, wPx);
        hPx = Math.max(1, hPx);

        BufferedImage lg = lastGood;
        if (lg != null && lg.getWidth() > 0 && lg.getHeight() > 0) {
            double r = (double) lg.getWidth() / (double) lg.getHeight();
            int wPxByHeight = Math.max(1, (int) Math.floor(hPx * r));
            wPx = Math.min(wPx, wPxByHeight);
        }

        // Set max width.
        wPx = Math.min(wPx, 12_000);

        return new RenderTarget(wPx, sx, sy);
    }

    private void render(Stamp s, RenderTarget target) {
        try {
            String dot = Files.readString(dotPath, StandardCharsets.UTF_8);

            if (dot.trim().isEmpty()) throw new RuntimeException("Empty dot file.");

            BufferedImage img = Graphviz.fromString(dot)
                    .width(target.widthPx)
                    .render(Format.PNG)
                    .toImage();

            lastStamp = s;
            lastGood = img;

            SwingUtilities.invokeLater(() -> panel.setImage(img));
            // setStatus("OK " + Instant.now());
            setStatus("");
        } catch (Throwable ex) {
            setStatus("Invalid DOT (kept last good) " + Instant.now());
            // ex.printStackTrace();
        } finally {
            rendering.set(false);
        }
    }

    private void setStatus(String text) {
        SwingUtilities.invokeLater(() -> status.setText(text));
    }

    private record Stamp(long mtimeMillis, long sizeBytes, int targetWidthPx) {
        static Stamp of(Path p, int targetWidthPx) throws Exception {
            return new Stamp(Files.getLastModifiedTime(p).toMillis(), Files.size(p), targetWidthPx);
        }
    }

    private record RenderTarget(int widthPx, double sx, double sy) { }

    private static final class ImagePanel extends JComponent {
        private volatile BufferedImage img;

        void setImage(BufferedImage img) {
            this.img = img;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            BufferedImage i = img;
            if (i == null) return;

            int cw = getWidth(), ch = getHeight();
            int iw = i.getWidth(), ih = i.getHeight();
            if (cw <= 0 || ch <= 0 || iw <= 0 || ih <= 0) return;

            double scale = Math.min((double) cw / iw, (double) ch / ih);

            scale = Math.min(1.0, scale);

            int w = (int) Math.round(iw * scale);
            int h = (int) Math.round(ih * scale);
            int x = (cw - w) / 2;
            int y = (ch - h) / 2;

            Graphics2D g2 = (Graphics2D) g.create();
            try {
                if (scale < 1.0) {
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                }
                g2.drawImage(i, x, y, w, h, null);
            } finally {
                g2.dispose();
            }
        }
    }
}
