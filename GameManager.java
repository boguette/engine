package org.example.GameEngine;

import org.example.GameEngine.AbstractGame;
import org.example.GameEngine.GameContainer;
import org.example.GameEngine.Renderer;
import org.example.audio.SoundClip;
import org.example.gfx.Image;
import org.example.gfx.ImageTile;
import org.example.gfx.Light;

import java.awt.event.KeyEvent;
import java.util.Random;

public class GameManager extends AbstractGame {

    private ImageTile image;
    private Image image2;
    private Image background;

    private Image skyTop, skyMiddle, skyBottom, skyVeryMiddle, skySomewhatMiddle, skyVeryBottom,
            getSkyTop, skyBelowTop, SkySomewhatVeryTop, skyBelowSomewhatTop, skyMiddleTop,
            skyBottomTop, skyCenterMiddle, skyBelowVeryMiddle, grassTop, cursor, smallCloud;

    private SoundClip clip;

    private Light light;

    public GameManager() {

        image2 = new Image("/chromatic-tile.png");
        image = new ImageTile("/better-sun.png", 16, 16);
        //image2.setLightBlock(Light.FULL);

        skyTop = new Image("/sky-very-top.png");
        skyBottom = new Image("/sky-super-duper-bottom.png");
        skyBelowTop = new Image("/sky-below-top.png");
        SkySomewhatVeryTop = new Image("/sky-somewhat-very-top.png");
        skyBelowSomewhatTop = new Image("/sky-below-somewhat-top.png");
        skyVeryMiddle = new Image("/sky-very-middle.png");
        skySomewhatMiddle = new Image("/sky-somewhat-middle.png");
        skyBottomTop = new Image("/sky-bottom-top.png");
        skyMiddleTop = new Image("/sky-middle-top.png");
        skyVeryBottom = new Image("/sky-very-bottom.png");
        skyBelowVeryMiddle = new Image("/sky-below-very-middle.png");
        cursor = new Image("/cursor.png");


        grassTop = new Image("/grass-top.png");
        smallCloud = new Image("/small-cloud.png");
        background = new Image("/background-sample.png");

        light = new Light(50, 0xffffffff);
    }
    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            System.out.println("A is pressed");
        }
    }
    @Override
    public void render(GameContainer gc, Renderer r) {



        for (int x = 0; x < light.getDiameter(); x++) {
            for (int y = 0; y < light.getDiameter(); y++) {
                r.setLightMap(x, y, light.getLightMap()[x + y * light.getDiameter()]);
            }
        }

        for (int i = 0; i <= 450/16; i++) {
            r.drawImage(skyTop, 16 * i, 0);
            r.drawImage(SkySomewhatVeryTop, 16 * i, 16);
            r.drawImage(skyBelowSomewhatTop, 16 * i, 32);
            r.drawImage(skyVeryMiddle, 16 * i, 48);
            r.drawImage(skyBelowVeryMiddle, 16 * i, 64);
            r.drawImage(skyBottomTop, 16 * i, 80);
            r.drawImage(skyBottom, 16 * i, 96);

        }

        r.drawLight(light, gc.getInput().getMouseX(), gc.getInput().getMouseY());
        r.drawImage(image2, 48, 96);
        r.drawImage(image, 38, 38);
        r.drawImage(cursor, gc.getInput().getMouseX() - 8, gc.getInput().getMouseY() - 8);

    }

    public static void main(String[] args) {
        System.out.println("start");
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }

}

