package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Player class.
 */
class Player extends MotionObject {

    /* Constant flags */
    private final static float angleMultiplier = 100;
    private final static double thrustSpeed = (float) 0.15;
    private final static float maxVelocity = 25;
    private final static float playerScale = 16;

    private final Bitmap normal, thrust;

    float thrustAngle;

    boolean turningLeft;
    boolean turningRight;
    boolean thrusting;

    /**
     * Player constructor.
     * @param canvasWidth
     * @param canvasHeight
     * @param normal
     * @param thrust
     */
    Player(int canvasWidth, int canvasHeight, Bitmap normal, Bitmap thrust) {
        super(canvasWidth / 2, canvasHeight / 2, ((float) canvasHeight) / playerScale, canvasWidth, canvasHeight, normal);

        turningLeft = false;
        turningRight = false;

        thrustAngle = 0;
        thrusting = false;

        this.normal = Bitmap.createScaledBitmap(normal, (int) size, (int) size, false);
        this.thrust = Bitmap.createScaledBitmap(thrust, (int) size, (int) size, false);
    }

    /**
     * Update angle from Joystick.
     * @param reference Joystick centre.
     * @param target Joystick target.
     */
    void updateAngle(Object reference, Object target) {

        double xVector = target.x - reference.x;
        double yVector = target.y - reference.y;

        /* Quick maths */
        thrustAngle = (float) (Math.atan(xVector / yVector) * 180.0 / Math.PI);

        if (yVector < 0) {
            thrustAngle = 180 + thrustAngle;
        }

    }

    /**
     * Moves player.
     * @param secondsElapsed Seconds elapsed.
     */
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

    /**
     * Draws and wraps player.
     * @param canvas Canvas to draw to.
     */
    @Override
    void draw(Canvas canvas) {
        drawPlayer(canvas, x, y);

        /* Wrap around */
        drawPlayer(canvas, x + canvasWidth, y);
        drawPlayer(canvas, x - canvasWidth, y);
        drawPlayer(canvas, x, y + canvasHeight);
        drawPlayer(canvas, x, y - canvasHeight);
    }

    /**
     * Draws Player.
     * @param canvas Canvas to draw to.
     * @param x X coordinate to use.
     * @param y Y coordinate to use.
     */
    private void drawPlayer(Canvas canvas, float x, float y) {
        /* Select correct bitmap */
        this.bitmap = (thrusting) ? thrust : normal;

        float tempX = this.x;
        float tempY = this.y;

        this.x = x;
        this.y = y;

        /* Draw main player */
        super.draw(canvas, thrustAngle);

        this.x = tempX;
        this.y = tempY;
    }

}
