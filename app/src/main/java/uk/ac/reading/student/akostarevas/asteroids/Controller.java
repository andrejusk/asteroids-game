package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Controller extends StaticObject {

    static int SIZE = 100;

    Paint debugControllerPaint;

    enum TYPE {
        BUTTON, JOYSTICK
    }

    TYPE type;

    Controller(float x, float y, TYPE type) {
        super(x, y);
        debugControllerPaint = debugPaint;
        debugControllerPaint.setAlpha(50);
        this.type = type;
    }

    @Override
    void draw(Canvas canvas) {
        super.draw(canvas);

        int size = SIZE;
        switch (type) {
            case BUTTON:
                size *= 1.5;
            case JOYSTICK:
                size *= 3;

        }

        canvas.drawCircle(x, y, size, debugPaint);
    }

}
