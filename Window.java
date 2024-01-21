package org.example.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;

public class Window {

    private JFrame frame;

    private BufferedImage image;
    private Canvas canvas;

    private Graphics GRAPHICS;

    private BufferStrategy bs;


    public JFrame getFrame() {
        return frame;
    }

    public Window (GameContainer gc) {
        image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
        canvas = new Canvas();


        Dimension size = new Dimension(
                (int) (gc.getWidth() * gc.getScale()),
                (int) ( gc.getHeight() * gc.getScale())
        );

        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);
        canvas.setMinimumSize(size);

        frame = new JFrame(gc.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        GRAPHICS = bs.getDrawGraphics();

    }

    public BufferedImage getImage() {
        return image;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void update() {
        GRAPHICS.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        bs.show();
    }

}
