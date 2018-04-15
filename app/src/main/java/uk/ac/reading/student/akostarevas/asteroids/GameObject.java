package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;

/**
 * Extends Object.
 * Keeps track of canvas and allows drawing.
 */
abstract class GameObject extends Object {

    /* Keep track of canvas */
    int canvasWidth;
    int canvasHeight;

    GameObject(float x, float y, float size, int canvasWidth, int canvasHeight) {
        super(x, y, size);

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    abstract void draw(Canvas canvas);

    boolean inBounds() {
        return inBounds(x, y);
    }

    boolean inBounds(double x, double y) {
        return inBounds(x, y, this.canvasWidth, this.canvasHeight, this.size);
    }

    private static boolean inBounds(double x, double y, int canvasWidth, int canvasHeight, float size) {
        return (
                (x) <= canvasWidth &&
                (x + size) >= 0 &&
                (y) <= canvasHeight &&
                (y + size) >= 0
        );
    }

}
