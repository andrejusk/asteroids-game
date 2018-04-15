package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Random;

class Asteroid extends MotionObject {

    private final Paint noAA;

    Bitmap bitmap;

    float rotationMultiplier;
    float rotation;

    Asteroid(Asteroid parent, MotionObject target, boolean side) {
        this(parent.canvasWidth, parent.canvasHeight, parent.bitmap);

        /* Inherit position */
        this.x = parent.x;
        this.y = parent.y;

        /* Resize */
        this.size = parent.size / 2;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) this.size, (int) this.size, false);

        /* De-spawn if needed */
        if (this.size < canvasHeight / 32) {
            this.exitedBounds = true;
        }
        parent.exitedBounds = true;
        target.exitedBounds = true;

        /* Rotate */
        if (side) {
            this.angle = target.angle + 90;
        } else {
            this.angle = target.angle - 90;
        }

    }

    Asteroid(int canvasWidth, int canvasHeight, Bitmap bitmap) {
        super(0, 0, canvasHeight / 8, canvasWidth, canvasHeight, 0, 0, true);

        Random random = new Random();

        /* Random point outside bounds */
        float startX, startY;
        do {
            startX = random.nextFloat() * (2 * canvasWidth) - canvasWidth / 2;
            startY = random.nextFloat() * (2 * canvasHeight) - canvasHeight / 2;
        } while(inBounds(startX, startY));

        /* Random point in centre of bounds */
        float targetX = random.nextFloat() * (canvasWidth / 2) + canvasWidth / 4;
        float targetY = random.nextFloat() * (canvasHeight / 2) + canvasHeight / 4;

        /* Apply start position */
        this.x = startX;
        this.y = startY;

        /* Apply angle */
        double xVector = targetX - startX;
        double yVector = targetY - startY;

        angle = (float) (Math.atan(xVector / yVector) * 180.0 / Math.PI);
        if (yVector < 0) {
            angle = 180 + angle;
        }

        /* Random speed */
        velocity = random.nextFloat() * 3 + 2;

        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) this.size, (int) this.size, false);

        /* Random rotation */
        rotation = random.nextFloat() * 360;
        rotationMultiplier = random.nextFloat() * 30;

        /* No anti-alias scaling */
        noAA = new Paint();
        noAA.setAntiAlias(false);
        noAA.setFilterBitmap(false);
        noAA.setDither(false);

    }

    @Override
    void draw(Canvas canvas) {
        if (exitedBounds) {
            return;
        }

        /* Rotate player */
        Matrix matrix = new Matrix();
        matrix.postTranslate(-bitmap.getWidth()/2, -bitmap.getHeight()/2);
        matrix.postRotate(180 - rotation);
        matrix.postTranslate(x + (float) (size / 2.0), y + (float) (size / 2.0));

        /* Draw main player */
        canvas.drawBitmap(bitmap, matrix, noAA);
    }

    void debugDraw(Canvas canvas) {
        if (exitedBounds) {
            return;
        }
        super.draw(canvas);
    }

    @Override
    void move(float secondsElapsed) {
        if (exitedBounds) {
            return;
        }

        rotation += secondsElapsed * rotationMultiplier;
        super.move(secondsElapsed);
    }


}
