package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.view.KeyEvent;

@SuppressWarnings("unused")
class Player extends MovableObject {

    private final static float angleMultiplier = 100;

    boolean turningLeft;
    boolean turningRight;

    Player(int canvasWidth, int canvasHeight) {
        super(canvasWidth / 2, canvasHeight / 2, canvasWidth, canvasHeight);
        turningLeft = false;
        turningRight = false;
    }

    void move(float secondsElapsed) {
        if (turningLeft) {
            this.angle -= secondsElapsed * angleMultiplier;
        }
        if (turningRight) {
            this.angle += secondsElapsed * angleMultiplier;
        }
        super.move(secondsElapsed);
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawText(String.valueOf(turningLeft), x + 50, y + 150, debugPaint);
        canvas.drawText(String.valueOf(turningRight), x + 50, y + 200, debugPaint);
        super.draw(canvas);
    }

}
