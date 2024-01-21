package org.example.GameEngine;


public class GameContainer implements Runnable{

    private Window window;
    private Thread thread;
    private boolean RUNNING = false;
    private final double UPDATE_CAP = 0.1/60;


    private AbstractGame game;



    private Renderer renderer;
    private Input input;

    public Window getWindow() {
        return window;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Input getInput() {
        return input;
    }

    public float getScale() {
        return scale;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private int width = 320, height = 240;
    private float scale = 3f;
    private String title = "Game Engine";


    public GameContainer(AbstractGame game) {
        this.game = game;
    }

    public void start() {
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);

        thread = new Thread(this);
        thread.run();
    }

    public void run() {
        RUNNING = true;

        double FRAME_TIME = 0;
        int FRAME_COUNT = 0;
        int FPS = 0;

        boolean render = false;

        double FIRST_TIME = 0;
        double LAST_TIME = System.nanoTime() / 1000000000.0;
        double PASSED_TIME = 0;
        double UNPROCESSED_TIME = 0;

        while (RUNNING) {

            render = false;

            FIRST_TIME = System.nanoTime() / 1000000000.0;
            PASSED_TIME = FIRST_TIME - LAST_TIME;
            LAST_TIME = FIRST_TIME;

            UNPROCESSED_TIME += PASSED_TIME;
            FRAME_TIME += PASSED_TIME;

            while (UNPROCESSED_TIME >= UPDATE_CAP) {

                UNPROCESSED_TIME -= UPDATE_CAP;
                render = true;

                game.update(this, (float)UPDATE_CAP);


                input.update();

                if (FRAME_TIME >= 1.0) {
                    FRAME_TIME = 0;
                    FPS = FRAME_COUNT;
                    FRAME_COUNT = 0;



                }
            }

            if (render) {
                renderer.clear();
                game.render(this, renderer);
                renderer.process();
                renderer.drawText("FPS:" + FPS, 10, 10, 0xffffffff);
                window.update();
                FRAME_COUNT++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
        dispose();
    }

    public void dispose() {

    }

    public void stop() {
    }

}
