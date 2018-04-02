package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Controller extends StaticObject {

    private Paint debugControllerPaint;

    enum TYPE {
        BUTTON, JOYSTICK
    }

    boolean active;
    int pointerId;

    float controllerSize;
    TYPE type;

    Controller(float x, float y, TYPE type) {
        super(x, y);

        active = false;
        pointerId = -1;

        debugControllerPaint = debugPaint;
        debugControllerPaint.setAlpha(50);
        this.type = type;
        controllerSize = size;

        switch (type) {
            case BUTTON:
                controllerSize *= 15;
                break;
            case JOYSTICK:
                controllerSize *= 30;
                break;
        }

    }

    @Override
    void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawText(String.valueOf(active), x + 50, y + 50, debugPaint);
        canvas.drawText(String.valueOf(pointerId), x + 50, y + 100, debugPaint);

        canvas.drawCircle(x, y, controllerSize, debugPaint);
    }

    boolean isAffected(float x, float y) {
        float difX = x - this.x;
        float difY = y - this.y;

        double dif = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));

        return (dif < controllerSize);
    }

    boolean isPointer(int pointerId) {
        return active && pointerId == this.pointerId;
    }

}
