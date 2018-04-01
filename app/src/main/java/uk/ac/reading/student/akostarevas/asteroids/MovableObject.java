package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;

class MovableObject extends StaticObject {

    private final static int tailMultiplier = 10;
    private final static int speedMultiplier = 100;

    private int canvasWidth;
    private int canvasHeight;

    /* Angle in degrees */
    float angle;
    float velocity;

    MovableObject(float x, float y, int canvasWidth, int canvasHeight) {
        this(x, y, canvasWidth, canvasHeight, 0, 0);
    }

    MovableObject(float x, float y, int canvasWidth, int canvasHeight, float angle, float velocity) {
        super(x, y);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.angle = angle;
        this.velocity = velocity;
    }

    void move(float secondsElapsed) {
        double angleRadians = (angle / 180.0 * Math.PI);

        double xSpeed = (velocity * speedMultiplier) * Math.sin(angleRadians);
        double ySpeed = (velocity * speedMultiplier) * Math.cos(angleRadians);

        x = (float) ((x + secondsElapsed * xSpeed) % canvasWidth);
        y = (float) ((y + secondsElapsed * ySpeed) % canvasHeight);
    }

    @Override
    void draw(Canvas canvas) {
        double angleRadians = (angle / 180.0 * Math.PI);

        double xSpeed = (velocity * tailMultiplier) * Math.sin(angleRadians);
        double ySpeed = (velocity * tailMultiplier) * Math.cos(angleRadians);

        canvas.drawLine(x, y, (float) (x - xSpeed), (float) (y - ySpeed), debugPaint);

        canvas.drawText(String.valueOf(angle), x + 50, y + 50, debugPaint);
        canvas.drawText(String.valueOf(velocity), x + 50, y + 100, debugPaint);

        super.draw(canvas);
    }

}
