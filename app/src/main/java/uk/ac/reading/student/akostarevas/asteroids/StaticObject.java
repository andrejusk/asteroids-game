package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class StaticObject {

    float size = 10;
    float x;
    float y;

    Paint debugPaint;

    StaticObject(float x, float y) {
        this.x = x;
        this.y = y;
        debugPaint = new Paint();
        debugPaint.setTextSize(45);
        debugPaint.setColor(Color.GREEN);
    }

    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, size, debugPaint);
    }

}
