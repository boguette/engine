package org.example.gfx;

public class ImageTile extends Image {

    private int tileWidth, tileHeight;


    public ImageTile(String path, int tileWidth, int tileHeight) {
        super(path);
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;

    }
    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }


    public Image getTileImage(int tileX, int tileY) {

        int[] p = new int[tileWidth * tileHeight];

        for (int y = 0; y < tileWidth; y++) {

            for (int x = 0; x < tileWidth; x++) {
                p[x + y * tileWidth] =  this.getP() [(x + tileX * tileWidth) + (y + tileY * tileHeight) * this.getTileWidth()];

            }

        }
        return new Image(p, tileWidth, tileHeight);
    }

}
