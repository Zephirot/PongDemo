package com.androidlearning.PongDemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {

    public float x,y;

    private int screenHeight, screenWidth;

    Bitmap image;

    private Rect bounds;

    public Sprite(int screenHeight, int screenWidth) {

        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void init(Bitmap image) {
        this.image = image;

        bounds = new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public Rect getRect () {
        return bounds;
    }

    public Rect getScreenRect () {
        return new Rect((int) x, (int) y, (int) x + getRect().width(), (int) y + getRect().height());
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
