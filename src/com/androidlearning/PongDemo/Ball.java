package com.androidlearning.PongDemo;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

public class Ball extends Sprite{

    private final float speedX = 0.3f;
    private final float speedY = 0.3f;

    private int directionX = 1;
    private int directionY = 1;

    public Ball(int screenHeight, int screenWidth) {
        super(screenHeight, screenWidth);
    }

    @Override
    public void init(Bitmap image) {
        super.init(image);
        ballReset();
    }

    public void ballReset() {
        setX(getScreenWidth() / 2 - getRect().width() / 2) ;
        setY(getScreenHeight()/2 - getRect().height()/2);

        directionX = new Random().nextInt(2)*2 -1;
        directionY = new Random().nextInt(2)*2 -1;
    }

    public void update (long elapsed) {

        float x = getX();
        float y = getY();

        Rect screenRect  = getScreenRect();

//        if (!( (0 <= getScreenRect().left) && (getScreenRect().right <= getScreenWidth()) )) {
//            directionX = -directionX;
//        }
//
//        if (!( 0 <= getScreenRect().top && getScreenRect().bottom <= getScreenHeight()) ) {
//            directionY = -directionY;
//        }

        if (getScreenRect().left <= 0) directionX = 1;
        else if (getScreenRect().right >= getScreenWidth()) directionX = -1;
        else if (getScreenRect().top <= 0 ) directionY = 1;
        else if (getScreenRect().bottom >= getScreenHeight()) directionY = -1;

        x += directionX * speedX * elapsed;
        y += directionY * speedY * elapsed;

        setX(x);
        setY(y);

    }

    public void changeDirectionX() {
        directionX = -directionX;
    }

    public void changeDirectionY() {
        directionY = -directionY;
    }

    public void moveLeft() {
        directionX = -1;
    }
    public void moveRight() {
        directionX = 1;
    }
}
