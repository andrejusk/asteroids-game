package uk.ac.reading.student.akostarevas.asteroids;


import android.graphics.Bitmap;

public class Ball extends GameObject {

    /* The speed (pixel/second) of the ball in direction X and Y */
    float xSpeed;
    float ySpeed;

    Ball(Bitmap bitmap, float x, float y, float xSpeed, float ySpeed) {
        super(bitmap, x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    Ball(Bitmap bitmap, float x, float y) {
        this(bitmap, x, y, 0, 0);
    }

    void move(float secondsElapsed) {
        x += secondsElapsed * xSpeed;
        y += secondsElapsed * ySpeed;
    }

}
