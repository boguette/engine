package org.example.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Objects;

public class Image {
    private int height, width;
    private int[] p;

    public int getLightBlock() {
        return lightBlock;
    }

    public void setLightBlock(int lightBlock) {
        this.lightBlock = lightBlock;
    }

    private int lightBlock = Light.NONE;

    private boolean alpha = false;

    public boolean isAlpha() {
        return alpha;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

    public Image(String path)
    {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Image.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        width = image.getWidth();
        height = image.getHeight();
        p = image.getRGB(0, 0, width, height, null, 0, width);

        image.flush();

    }

    public Image(int[] p, int width, int height) {
        this.p = p;
        this.width = width;
        this.height = height;
    }

    public int getH() {
        return height;
    }

    public void setH(int h) {
        this.height = h;
    }

    public int getW() {
        return width;
    }

    public void setW(int w) {
        this.width = w;
    }

    public int[] getP() {
        return p;
    }

    public void setP(int[] p) {
        this.p = p;
    }



}
