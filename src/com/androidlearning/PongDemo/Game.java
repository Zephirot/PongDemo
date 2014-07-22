package com.androidlearning.PongDemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashMap;
import java.util.Map;

public class Game {

    private SurfaceHolder holder;
    private Resources resource;

    //Text paints
    private Paint textPaint;
    private Paint scorePaint;

    //Sounds
    private SoundPool soundPool;
    private Map<String,Integer> soundsMap;

    private Ball ball;
    private Bat playerBat;
    private Bat aiBat;

    private final boolean freeDrag = true; //touch the bat to move it or just touch any point on screen

    private enum State {PAUSED, WON, LOST, RUNNING}

    volatile State state;

    private Context context;

    //    ArrayList<Integer> score = new ArrayList<Integer>(2);
    private int playerScore = 0;
    private int aiScore=0;

    public Game(Context context, SurfaceHolder holder, Resources resource, int screenHeight, int screenWidth) {
        this.holder = holder;
        this.resource = resource;
        this.context = context;

        //Images
        ball = new Ball(screenHeight, screenWidth);
        playerBat = new Bat(screenHeight, screenWidth, Bat.Position.LEFT);
        aiBat = new Bat(screenHeight, screenWidth, Bat.Position.RIGHT);

        state = State.PAUSED;
    }

    private void soundsInit() {
        //Sounds
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        soundsMap = new HashMap<String, Integer>();

        soundsMap.put("START", soundPool.load(context, R.raw.gamestart, 1));
        soundsMap.put("VICTORY", soundPool.load(context, R.raw.victory, 1));
        soundsMap.put("DEFEAT", soundPool.load(context, R.raw.defeat, 1));
        soundsMap.put("BOUNCE", soundPool.load(context, R.raw.bounce, 1));
    }

    private void paintInit() {
        //General messages
        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.MAGENTA);
        textPaint.setTextSize(16);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        //Top-screen score
        scorePaint = new Paint();
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setTextSize(20);
        scorePaint.setColor(Color.RED);
    }

    public void init() {
        Bitmap ballImage = BitmapFactory.decodeResource(resource, R.drawable.pong_ball);
//        ballImage.setHeight(40);
//        ballImage.setWidth(40);
        Bitmap batImage = BitmapFactory.decodeResource(resource, R.drawable.pong_bar);
//        batImage.setHeight(80);
//        batImage.setWidth(20);

        ball.init(ballImage);
        playerBat.init(batImage);
        aiBat.init(batImage);

        //TODO check if works, or move back to constructor
        soundsInit();
        paintInit();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                if (soundsMap.get("START") == i) {
                    soundPool.play(soundsMap.get("START"), 0.2f, 0.4f, 1, 0, 1.0f);
                }
            }
        });
    }

    public void update(long elapsed) {

        switch (state) {
            case RUNNING:
                updateGame(elapsed);
                break;
        }
    }

    private void updateGame(long elapsed) {
        Rect ballScreenRect = ball.getScreenRect();
        Rect plScreenRect = playerBat.getScreenRect();
        Rect aiScreenRect = aiBat.getScreenRect();

        if (plScreenRect.contains(ballScreenRect.left, ballScreenRect.centerY())) {
            ball.moveRight();
            soundPool.play(soundsMap.get("BOUNCE"),0.2f, 0.4f, 1, 0, 1.0f);
        }
        else if (aiScreenRect.contains(ballScreenRect.right, ballScreenRect.centerY())) {
            ball.moveLeft();
            soundPool.play(soundsMap.get("BOUNCE"), 0.2f, 0.4f, 1, 0, 1.0f);
        }

        //Win/Lose conditions
        if (ballScreenRect.left <= plScreenRect.left) {
            state = State.LOST;
            soundPool.play(soundsMap.get("DEFEAT"), 0.2f, 0.4f, 1, 0, 1.0f);
        } else if (ballScreenRect.right >= aiScreenRect.right) {
            state = State.WON;
            soundPool.play(soundsMap.get("VICTORY"), 0.2f, 0.4f, 1, 0, 1.0f);
        }

        ball.update(elapsed);
        aiBat.update(elapsed, ball);
    }

    private void drawStateText(Canvas canvas, String text) {
        canvas.drawText(text, canvas.getWidth() / 2, canvas.getHeight() / 2, textPaint);
    }

    private void drawScoreText(Canvas canvas, String score) {
        canvas.drawText(score, canvas.getWidth() / 2, 20, scorePaint);
    }

    private void drawGame(Canvas canvas) {
        ball.draw(canvas);
        playerBat.draw(canvas);
        aiBat.draw(canvas);
    }

    public void draw() {

//        SurfaceHolder holder = getHolder();
        Canvas canvas = holder.lockCanvas();

        if (canvas != null) {

            canvas.drawColor(Color.WHITE);

            switch (state) {
                case PAUSED: {
                    drawStateText(canvas, "Tap screen to start...");
                    break;
                }
                case WON: {
                    drawStateText(canvas, "VICTORY!!!");
                    objectsReset();
                    break;
                }
                case LOST: {
                    drawStateText(canvas, String.format("LOOOSER."));
                    objectsReset();
                    break;
                }
                case RUNNING: {
                    drawScoreText(canvas, String.format("%d - %d", playerScore, aiScore));
                    drawGame(canvas);
                    break;
                }
            }
        } else {
            Log.d("DBG", "no canvas");
        }

        holder.unlockCanvasAndPost(canvas);
    }

    private void objectsReset() {
        ball.ballReset();
        aiBat.batReset();
        playerBat.batReset();
    }


    public void onTouchEvent(MotionEvent event) {

        switch (state) {
            case PAUSED:
                state = State.RUNNING;
                break;
            case WON:
                state = State.RUNNING;
                playerScore++;
                break;
            case LOST:
                state = State.RUNNING;
                aiScore++;
                break;
            case RUNNING:

                boolean buttTouched = playerBat.getScreenRect().contains((int) event.getX(), (int) event.getY()) || freeDrag; //TODO: set to always true

                if (buttTouched) {

                    playerBat.setPosition(event.getY());
                }
                break;
        }

        state = State.RUNNING;

    }
}
