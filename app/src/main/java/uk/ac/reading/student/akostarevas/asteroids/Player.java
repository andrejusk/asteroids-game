package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

@SuppressWarnings("unused")
class Player extends MotionObject {

    private final static float angleMultiplier = 100;
    private final static double thrustSpeed = (float) 0.15;

    private final static int directionMultiplier = 5;

    private final static float maxVelocity = 25;

    private final static float playerScale = 16;

    private final Bitmap normal, thrust;
    private final Paint noAA;

    double thrustAngle;

    boolean turningLeft;
    boolean turningRight;
    boolean thrusting;

    Player(int canvasWidth, int canvasHeight, Bitmap normal, Bitmap thrust) {
        super(canvasWidth / 2, canvasHeight / 2, ((float) canvasHeight) / playerScale, canvasWidth, canvasHeight);

        turningLeft = false;
        turningRight = false;

        thrustAngle = 0;
        thrusting = false;

        this.normal = Bitmap.createScaledBitmap(normal, (int) size, (int) size, false);
        this.thrust = Bitmap.createScaledBitmap(thrust, (int) size, (int) size, false);

        /* No anti-alias scaling */
        noAA = new Paint();
        noAA.setAntiAlias(false);
        noAA.setFilterBitmap(false);
        noAA.setDither(false);
    }

    void updateAngle(Object reference, Object target) {

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
            motion = Vector.multiply(motion, new Vector(thrustSpeed, thrustAngle));
            if (motion.velocity > maxVelocity) {
                motion.setVelocity(maxVelocity);
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
        Bitmap bitmap = (thrusting) ? thrust : normal;

        /* Rotate player */
        Matrix matrix = new Matrix();
        matrix.postTranslate(-bitmap.getWidth()/2, -bitmap.getHeight()/2);
        matrix.postRotate((float) (180.0 - thrustAngle));
        matrix.postTranslate(x + (float) (size / 2.0), y + (float) (size / 2.0));

        /* Draw main player */
        canvas.drawBitmap(bitmap, matrix, noAA);
    }

}
