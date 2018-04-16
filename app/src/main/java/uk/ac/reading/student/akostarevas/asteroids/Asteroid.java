package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Asteroids class.
 */
class Asteroid extends MotionObject {

    /* Rotation per time */
    private float rotationMultiplier;

    /**
     * Constructor for collision splitting.
     * @param parent Parent Asteroid.
     * @param target PlayerBullet that hit the Asteroid.
     * @param side Which side to split to.
     */
    Asteroid(Asteroid parent, MotionObject target, boolean side) {
        this(parent.canvasWidth, parent.canvasHeight, parent.bitmap);

        /* Inherit position */
        this.x = parent.x;
        this.y = parent.y;

        /* Resize */
        this.size = parent.size / 2;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) this.size, (int) this.size, false);

        /* Rotate */
        if (side) {
            this.motion.angleDegrees = target.motion.angleDegrees + 90;
        } else {
            this.motion.angleDegrees = target.motion.angleDegrees - 90;
        }

        /* De-spawn if needed */
        if (this.size < canvasHeight / 32) {
            this.exitedBounds = true;
        }
        parent.exitedBounds = true;
        target.exitedBounds = true;
    }

    /**
     * Asteroid constructor, randomly generates values.
     * @param canvasWidth Canvas width.
     * @param canvasHeight Canvas Height.
     * @param bitmap Bitmap image.
     */
    Asteroid(int canvasWidth, int canvasHeight, Bitmap bitmap) {
        super(0, 0, canvasHeight / 8,
                canvasWidth, canvasHeight, new Vector(), true, bitmap);

        Random random = new Random();

        /* Random starting point outside bounds */
        do {
            x = random.nextFloat() * (2 * canvasWidth) - canvasWidth / 2;
            y = random.nextFloat() * (2 * canvasHeight) - canvasHeight / 2;
        } while(inBounds());

        /* Random point in centre of bounds as target */
        Object target = new Object(
                random.nextFloat() * (canvasWidth / 2) + canvasWidth / 4,
                random.nextFloat() * (canvasHeight / 2) + canvasHeight / 4
        );

        /* Generate motion */
        motion = new Vector(this, target);
        motion.setVelocity(random.nextFloat() * 3 + 2);

        /* Random rotation */
        rotation = random.nextFloat() * 360;
        rotationMultiplier = random.nextFloat() * 30;

    }

    /**
     * Draws Asteroid if in bounds.
     * @param canvas Canvas to draw to.
     */
    @Override
    void draw(Canvas canvas) {
        if (exitedBounds) {
            return;
        }
        super.draw(canvas);
    }

    /**
     * Moves and rotates Asteroid if in bounds.
     * @param secondsElapsed Seconds elapsed.
     */
    @Override
    void move(float secondsElapsed) {
        if (exitedBounds) {
            return;
        }

        rotation += secondsElapsed * rotationMultiplier;
        super.move(secondsElapsed);
    }


}
