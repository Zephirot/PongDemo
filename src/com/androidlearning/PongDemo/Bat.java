package com.androidlearning.PongDemo;

import android.graphics.Bitmap;

import java.util.Random;

public class Bat extends Sprite {

    int batLeftOffset = 20;
    int batRightOffset = 20;

    public enum Position {LEFT, RIGHT}

    private Position pos;
    private float speed = 0.4f;

    private int direction;

    @Override
    public void init(Bitmap image) {
        super.init(image);
        batReset();

        direction = new Random().nextInt(2)*2-1;
    }

    public void batReset() {
        if (pos == Position.LEFT) {
            this.setX(batLeftOffset);
        }
        if (pos == Position.RIGHT) {
            this.setX(getScreenWidth() - batRightOffset - getRect().width());
        }
        this.setY( getScreenHeight()/2 - getRect().height()/2 );
    }


    public void setPosition(float y) {

        int half = getRect().centerY();
        int pos = (int) y - half;

//        if ( pos <= half) { pos = half; }
//        else if (y > getScreenHeight() - getRect().height()) { pos = getScreenHeight() - getRect().height(); }
        this.setY(pos);
    }

    public void update(long elapsed, Ball ball) {

        int decision = new Random().nextInt(10);


        if (getScreenRect().top <= 0) {
            direction = 1; }
        else if (getScreenRect().bottom >= getScreenHeight()) {
            direction = -1;
        }

        float y = getY();
        y += direction*speed*elapsed;
        setY(y);

    }

    public Bat(int screenHeight, int screenWidth, Position position) {
        super(screenHeight, screenWidth);
        this.pos = position;

    }
}
