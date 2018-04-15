package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class PlayerInput extends GameObject {

    /* Debug controller paint */
    private Paint controllerPaint;

    enum TYPE {
        THRUST, SHOOT, JOYSTICK
    }

    boolean active;
    int pointerId;

    PlayerInput(float x, float y, TYPE type) {
        super(x, y, 10);

        active = false;
        pointerId = -1;

        controllerPaint = new Paint();
        controllerPaint.setStyle(Paint.Style.STROKE);
        controllerPaint.setStrokeWidth(10);

        switch (type) {
            case THRUST:
                this.size *= 15;
                controllerPaint.setColor(Color.CYAN);
                break;
            case SHOOT:
                this.size *= 15;
                controllerPaint.setColor(Color.RED);
                break;
            case JOYSTICK:
                this.size *= 30;
                controllerPaint.setColor(Color.WHITE);
                break;
        }

        controllerPaint.setAlpha(100);

    }

    void debugDraw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawText(String.valueOf(active), x + 50, y + 50, debugPaint);
        canvas.drawText(String.valueOf(pointerId), x + 50, y + 100, debugPaint);
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, this.size, controllerPaint);
    }

    boolean isAffected(float x, float y) {
        float difX = x - this.x;
        float difY = y - this.y;

        double dif = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));

        return (dif < this.size);
    }

    boolean isPointer(int pointerId) {
        return active && pointerId == this.pointerId;
    }

}
