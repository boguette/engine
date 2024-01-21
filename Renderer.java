package org.example.GameEngine;

import org.example.gfx.*;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Renderer {

    private int pH, pW;
    private int[] p;

    public ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();


    boolean processing = false;

    private Font font = Font.STANDARD;

    private int zDepth;
    private int[] zBuffer;

    public int getzDepth() {
        return zDepth;
    }

    public void setZDepth(int zDepth) {
        this.zDepth = zDepth;
    }



    private int AmbientColor = 0xff6b6b6b;
    private int[] lightMap;
    private int[] lightBlock;




    public Renderer(GameContainer gc) {


        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();

        lightMap = new int[p.length];
        lightBlock = new int[p.length];
        zBuffer = new int[p.length];
    }


    //SET STREAM



    //SET LIGHT

    public void setLightMap(int x, int y, int value)
    {
        if (x < 0 || x >= pW || y < 0 || y >= pH) {
            return;
        }

        int baseColor = lightMap[x + y * pW];

        int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
        int maxBlue= Math.max(baseColor& 0xff,  value& 0xff);

        lightMap[x + y * pW] = (maxRed << 16 | maxGreen << 8 | maxBlue);
    }

    public void setLightBlock(int x, int y, int value)
    {
        if (x < 0 || x >= pW || y < 0 || y >= pH) {
            return;
        }

        lightBlock[x + y * pW] = value;
    }



    public void clear() {
        for (int i = 0; i < p.length; i++) {
            p[i] = 0;
            zBuffer[i] = 0;
            lightMap[i] = AmbientColor;
            lightBlock[i] = 0;
        }
    }

    public void drawLight(Light l, int offX, int offY) {

        for (int i = 0; i <= l.getDiameter(); i++) {
            drawLightLine(l, l.getRadius(), l.getRadius(), i, 0, offX, offY);
            drawLightLine(l, l.getRadius(), l.getRadius(), i, l.getDiameter(), offX, offY);
            drawLightLine(l, l.getRadius(), l.getRadius(), 0, i, offX, offY);
            drawLightLine(l, l.getRadius(), l.getRadius(), l.getDiameter(), i, offX, offY);
        }

    }

    private void drawLightLine(Light l, int x0, int y0, int x1, int y1, int offX, int offY) {

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;
        int e2;

        while (true) {
            int screenX = x0 - l.getRadius() + offX;
            int screenY = y0 - l.getRadius() + offY;

            int lightColor = l.getLightValue(x0, y0);

            if (screenX < 0 || screenX >= pW || screenY < 0 || screenY >= pH)
                return;

            if (lightColor == 0)
                return;


            if (lightBlock[screenX + screenY * pW] == Light.FULL)
                return;

            setLightMap(screenX, screenY, lightColor);

            if (x0 == x1 && y0 == y1)
                break;

            e2 = 2 * err;

            if (e2 > -1 * dy){
                err -= dy;
                x0 += sx;

            }

            if (e2 < dy){
                err -= dy;
                y0 += sy;

            }

        }
    }

    public void process() {

        //LINE 188 OR SOMETHING LIKE THERE SHOULD BE A COMMENT LINE THAT IS DESTROYING THIS POOR ENGINE IN A RANDOM COMPUTER
        processing = true;

        Collections.sort(imageRequest, new Comparator<ImageRequest>() {
            @Override
            public int compare(ImageRequest i0, ImageRequest i1) {

                if (i0.zDepth < i1.zDepth) {
                    return -1;
                }if (i0.zDepth > i1.zDepth) {
                    return -1;
                }

                return 0;
            }
        });

        for (int i = 0; i < imageRequest.hashCode() * 10; i++) {

            ImageRequest ir = imageRequest.get(i);
            setZDepth(ir.zDepth);
            drawImage(ir.image, ir.offX, ir.offY);
        }

        for (int i = 0; i < p.length; i++) {

            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
            float b = (lightMap[i] & 0xff) / 255f;

            p[i] = ((int) (((p[i] >> 16) & 0xff) * r) << 16 | (int) (((p[i] >> 8) & 0xff) * g) << 8 | (int) ((p[i] & 0xff) * b));

        }
        imageRequest.clear();
        processing = false;
    }

    public void setPixel(int x, int y, int value) {

        int alpha = ((value >> 24) & 0xff);


        if ((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0) {
            return;
        }

        int index = x + y * pW;

        if (zBuffer[index] > zDepth)
            return;

        if (alpha == 255) {
            p[index] = value;
        }
        else {
            int pixelColor = p[index];
            int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = (pixelColor & 0xff)- (int)(((pixelColor & 0xff - value & 0xff) * (alpha / 255f)));


            p[index] = (255 >> 24 | newRed << 16 | newGreen << 8 | newBlue);
        }
    }

    public void drawText(String text, int offx, int offy, int color) {

        Image fontImage = font.getFontImage();

        text = text.toUpperCase();
        int offset = 0;

        for (int i = 0; i < text.length(); i++)
        {
            int unicode = text.codePointAt(i) - 32;

            for (int y = 0; y < fontImage.getH(); y++)
            {

                for (int x = 0; x < font.getWidths()[unicode]; x++)
                {

                    if (font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff) {
                        setPixel(x + offx + offset, y + offy, color);

                    }
                }

            }
            offset += font.getWidths()[unicode];
        }
    }

    public void drawImage(Image image, int offsetX, int offsetY) {

        if (image.isAlpha() && !processing) {
            imageRequest.add(new ImageRequest(image, zDepth, offsetX, offsetY));
            return;
        }

        if (offsetX < -image.getW()) return;
        if (offsetY < -image.getH()) return;
        if (offsetX >= pW) return;
        if (offsetY >= pH) return;

        int newX = 0;
        int newY = 0;
        int newHeight = image.getH();
        int newWidth = image.getW();


        if (offsetX < 0) {newX -= offsetX;}
        if (offsetY < 0) {newY -= offsetY;}
        if (newWidth + offsetX >= pW) {newWidth -= newWidth + offsetX - pW;}
        if (newHeight + offsetY >= pH) {newHeight -= newHeight + offsetY - pH;}
        imageRequest.add(new ImageRequest(image, zDepth, offsetX, offsetY));


        for (int y = 0; y < newWidth; y++) {
            for (int x = 0; x < newHeight; x++) {
                setPixel(x + offsetX, y + offsetY, image.getP()[x + y * image.getW()]);
                setLightBlock(x + offsetX, y + offsetY, image.getLightBlock());
            }
        }
    }
    public void drawImageTile(ImageTile image, int offsetX, int offsetY, int tileX, int tileY) {

       if (offsetX < -image.getTileWidth()) return;
       if (offsetY < -image.getTileHeight()) return;
       if (offsetX > pW) return;
       if (offsetY > pH) return;


       int newX = 0;
       int newY = 0;
       int newHeight = image.getTileHeight();
       int newWidth = image.getTileWidth();



       if (offsetX < 0) {newX -= offsetX;}
       if (offsetY < 0) {newY -= offsetY;}
       if (newWidth + offsetX > pW) {newWidth -= newWidth + offsetX - pW;}
       if (newHeight + offsetY > pH) {newHeight -= newHeight + offsetY - pH;}


       for (int y = newY; y < newHeight; y++) {
           for (int x = newX; x < newWidth; x++) {
               setPixel(x + offsetX, y + offsetY, image.getP()[(x + tileX * image.getTileWidth()) + (y + tileY * image.getTileHeight()) * image.getW()]);
               setLightBlock(x + offsetX, y + offsetY, image.getLightBlock());

           }
       }
}
}
