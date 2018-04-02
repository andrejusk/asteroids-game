package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Controller extends StaticObject {

    private Paint controllerPaint;

    enum TYPE {
        THRUST, SHOOT, JOYSTICK
    }

    boolean active;
    int pointerId;

    private float controllerSize;

    Controller(float x, float y, TYPE type) {
        super(x, y);

        active = false;
        pointerId = -1;

        controllerPaint = new Paint();

        controllerSize = size;

        switch (type) {
            case THRUST:
                controllerSize *= 15;
                controllerPaint.setColor(Color.CYAN);
                break;
            case SHOOT:
                controllerSize *= 15;
                controllerPaint.setColor(Color.RED);
                break;
            case JOYSTICK:
                controllerSize *= 30;
                controllerPaint.setColor(Color.GREEN);
                break;
        }

        controllerPaint.setAlpha(50);

    }

    @Override
    void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawText(String.valueOf(active), x + 50, y + 50, debugPaint);
        canvas.drawText(String.valueOf(pointerId), x + 50, y + 100, debugPaint);

        canvas.drawCircle(x, y, controllerSize, controllerPaint);
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
