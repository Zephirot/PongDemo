package com.androidlearning.PongDemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    GameRunner runner;
    private Game game;

    public GameView(Context context, AttributeSet attrs) {

        super(context, attrs);
        SurfaceHolder  holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        game.onTouchEvent(event);
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        game = new Game(this.getContext(), getHolder(), getResources(), getHeight(), getWidth());
        runner = new GameRunner(game);
        runner.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (runner != null) {
            runner.shutdown();

            while (runner != null) {
                try {
                    runner.join();
                    runner = null;
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
