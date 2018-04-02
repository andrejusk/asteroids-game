package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;

import java.util.Random;

public class Asteroid extends MovableObject {

    public Asteroid(int canvasWidth, int canvasHeight) {
        super(0, 0, canvasWidth, canvasHeight, 0, 0, true);

        Random random = new Random();

        /* Random point outside bounds */
        float startX = 0, startY = 0;
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
        velocity = random.nextFloat() * 4 + 6;

    }

    @Override
    void draw(Canvas canvas) {
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
        super.move(secondsElapsed);
    }


}
