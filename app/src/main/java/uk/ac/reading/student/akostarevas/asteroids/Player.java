package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

@SuppressWarnings("unused")
class Player extends MotionObject {

    private final static float angleMultiplier = 100;
    private final static float thrustSpeed = (float) 0.15;

    private final static int directionMultiplier = 5;

    private final static float maxVelocity = 25;

    private final static int playerScale = 16;

    private final int playerSize;
    private final Bitmap normal, thrust;
    private final Paint noAA;

    private float thrustAngle;

    boolean turningLeft;
    boolean turningRight;
    boolean thrusting;

    Player(int canvasWidth, int canvasHeight, Bitmap normal, Bitmap thrust) {
        super(canvasWidth / 2, canvasHeight / 2, canvasWidth, canvasHeight);

        turningLeft = false;
        turningRight = false;

        thrustAngle = 0;
        thrusting = false;

        playerSize = canvasHeight / playerScale;

        this.normal = normal;
        this.thrust = thrust;

        /* No anti-alias scaling */
        noAA = new Paint();
        noAA.setAntiAlias(false);
        noAA.setFilterBitmap(false);
        noAA.setDither(false);
    }

    void updateAngle(GameObject reference, GameObject target) {

        double xVector = target.x - reference.x;
        double yVector = target.y - reference.y;

        /* Quick maths */
        thrustAngle = (float) (Math.atan(xVector / yVector) * 180.0 / Math.PI);

        if (yVector < 0) {
            thrustAngle = 180 + thrustAngle;
        }

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

            if (velocity > maxVelocity) {
                velocity = maxVelocity;
            }

            /* Quick maths */
            angle = (float) (Math.atan(xFinal / yFinal) * 180.0 / Math.PI);

            if (yFinal < 0) {
                angle = 180 + angle;
            }

        }

        super.move(secondsElapsed);
    }

    @Override
    void draw(Canvas canvas) {
        drawPlayer(canvas, x, y);

        /* Wrap around */
        //TODO: conditionals for whether to bother?
        drawPlayer(canvas, x + canvasWidth, y);
        drawPlayer(canvas, x - canvasWidth, y);
        drawPlayer(canvas, x, y + canvasHeight);
        drawPlayer(canvas, x, y - canvasHeight);
    }

    private void drawPlayer(Canvas canvas, float x, float y) {
        /* Select correct bitmap */
        Bitmap tempBitmap = (thrusting) ? thrust : normal;

        /* Scale player */
        tempBitmap = Bitmap.createScaledBitmap(tempBitmap, playerSize, playerSize, false);

        /* Rotate player */
        Matrix matrix = new Matrix();
        matrix.postTranslate(-tempBitmap.getWidth()/2, -tempBitmap.getHeight()/2);
        matrix.postRotate(180 - thrustAngle);
        matrix.postTranslate(x + (float) (playerSize / 2.0), y + (float) (playerSize / 2.0));

        /* Draw main player */
        canvas.drawBitmap(tempBitmap, matrix, noAA);
    }

    void debugDraw(Canvas canvas) {
        canvas.drawText(String.valueOf(thrusting), x + 50, y + 150, debugPaint);
        canvas.drawText(String.valueOf(thrustAngle), x + 50, y + 200, debugPaint);

        double thrustAngleRadians = (thrustAngle * Math.PI / 180.0);
        double xThrust = (directionMultiplier * tailMultiplier) * Math.sin(thrustAngleRadians);
        double yThrust = (directionMultiplier * tailMultiplier) * Math.cos(thrustAngleRadians);

        canvas.drawLine(x, y, (float) (x + xThrust), (float) (y + yThrust), debugPaint);

        super.draw(canvas);
    }

}
