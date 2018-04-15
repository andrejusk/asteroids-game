package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Extends Object.
 * Allows drawing.
 */
class GameObject extends Object {

    Paint debugPaint;

    GameObject(float x, float y, float size) {
        super(x, y, size);

        debugPaint = new Paint();
        debugPaint.setTextSize(45);
        debugPaint.setColor(Color.GREEN);
    }

    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, size, debugPaint);
    }

}
