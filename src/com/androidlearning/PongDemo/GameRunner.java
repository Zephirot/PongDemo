package com.androidlearning.PongDemo;

public class GameRunner extends Thread {

    private volatile boolean running = true;
    private Game game;

    public GameRunner(Game game) {
        this.game = game;
    }

    @Override
    public void run() {

        long lastTime = System.currentTimeMillis();

        game.init();

        //game loop
        while (running) {

                //Thread.sleep(200);

                long now = System.currentTimeMillis();
                long elapsed = now - lastTime;

                if (elapsed < 100) {
                    game.update(elapsed);
                    game.draw();
                }

                lastTime = now;

        }

    }

    public void shutdown() {
        running = false;
    }

}
