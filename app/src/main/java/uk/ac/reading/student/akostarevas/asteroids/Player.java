package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;

@SuppressWarnings("unused")
class Player extends MovableObject {

    private final static float angleMultiplier = 100;
    private final static float thrustSpeed = 5;

    private float thrustAngle;

    boolean turningLeft;
    boolean turningRight;
    boolean thrusting;

    Player(int canvasWidth, int canvasHeight) {
        super(canvasWidth / 2, canvasHeight / 2, canvasWidth, canvasHeight);

        turningLeft = false;
        turningRight = false;

        thrustAngle = 0;
        thrusting = false;
    }

    void move(float secondsElapsed) {

        /* Turning */
        if (turningLeft) {
            this.thrustAngle -= secondsElapsed * angleMultiplier;
        }
        if (turningRight) {
            this.thrustAngle += secondsElapsed * angleMultiplier;
        }

        /* Movement */
        if (thrusting) {

            /* Cross product vectors */
            double movementAngleRadians = (angle / 180.0 * Math.PI);
            double xMove = (velocity) * Math.sin(movementAngleRadians);
            double yMove = (velocity) * Math.cos(movementAngleRadians);

            double thrustAngleRadians = (thrustAngle / 180.0 * Math.PI);
            double xThrust = (thrustSpeed) * Math.sin(thrustAngleRadians);
            double yThrust = (thrustSpeed) * Math.cos(thrustAngleRadians);

            double xFinal = xMove + xThrust;
            double yFinal = yMove + yThrust;

            /* Sum of squares */
            velocity = (float) Math.sqrt(Math.pow(xFinal, 2) + Math.pow(yFinal, 2));

            /* Quick maths */
            angle = (float) Math.atan(xFinal / yFinal);
            
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
