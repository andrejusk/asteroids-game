package uk.ac.reading.student.akostarevas.asteroids;


import android.graphics.Bitmap;
import android.graphics.Canvas;

abstract class GameObject {

    Bitmap bitmap;

    float x;
    float y;

    GameObject(Bitmap bitmap, float x, float y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, null);
    }

}
