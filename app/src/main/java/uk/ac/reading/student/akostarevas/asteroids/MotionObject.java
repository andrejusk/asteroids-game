package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;

class MotionObject extends GameObject {

    /* Physics variables */
    final static int tailMultiplier = 10;
    private final static int speedMultiplier = 100;
    private final static int dropOffDivisor = 4;

    /* Keep track of canvas */
    private int canvasWidth;
    private int canvasHeight;

    /* Angle in degrees */
    float angle;
    float velocity;

    /* Special flags */
    private boolean warp;
    private boolean enteringBounds;
    boolean exitedBounds = false;

    MotionObject(float x, float y, int canvasWidth, int canvasHeight) {
        this(x, y, canvasWidth, canvasHeight, 0, 0, false);
    }

    MotionObject(float x, float y, int canvasWidth, int canvasHeight, float angle, float velocity, boolean enteringBounds) {
        super(x, y);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.angle = angle;
        this.velocity = velocity;
        this.enteringBounds = enteringBounds;
        this.warp = !enteringBounds;
    }

    void move(float secondsElapsed) {
        /* Update velocity with drop off */
        velocity -= Math.log(velocity + 1) * secondsElapsed / dropOffDivisor;

        /* Calculate displacement */
        double angleRadians = (angle / 180.0 * Math.PI);
        double speed = velocity * speedMultiplier;
        double xDisplace = speed * Math.sin(angleRadians) * secondsElapsed;
        double yDisplace = speed * Math.cos(angleRadians) * secondsElapsed;

        /* Apply displacement */
        double xRaw = x + xDisplace;
        double yRaw = y + yDisplace;

        /* If out of bounds */
        if (!inBounds(xRaw, yRaw)) {
            /* If not supposed to be out of bounds */
            if (!enteringBounds) {
                exitedBounds = true;
            }
        }

        /* If within bounds */
        if (inBounds(xRaw, yRaw)) {
            /* If started out of bounds */
            if (enteringBounds) {
                enteringBounds = false;
            }
        }

        if (warp) {
            /* Warp bottom and right */
            xRaw = xRaw % canvasWidth;
            yRaw = yRaw % canvasHeight;

            /* Warp top and left */
            xRaw = (xRaw < 0) ? xRaw + canvasWidth : xRaw;
            yRaw = (yRaw < 0) ? yRaw + canvasHeight : yRaw;
        }

        x = (float) xRaw;
        y = (float) yRaw;
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

    boolean inBounds(double x, double y) {
        return inBounds(x, y, this.canvasWidth, this.canvasHeight);
    }

    private static boolean inBounds(double x, double y, int canvasWidth, int canvasHeight) {
        return (x <= canvasWidth && x >= 0 && y <= canvasHeight && y >= 0);
    }

}
