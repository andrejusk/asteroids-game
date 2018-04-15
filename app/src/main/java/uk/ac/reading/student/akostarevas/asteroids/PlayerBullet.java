package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

class PlayerBullet extends MotionObject {

    private final Paint noAA;

    Bitmap bitmap;
    float rotation;

    final static int bulletSize = 5;

    final static double bulletVelocity = 10;
    final Paint paint;

    PlayerBullet(Player player, Bitmap bitmap) {
        super(player.x + player.size / 2, player.y + player.size / 2, bulletSize,
                player.canvasWidth, player.canvasHeight,
                new Vector(bulletVelocity, player.thrustAngle), true);

        paint = new Paint();
        paint.setColor(Color.WHITE);

        rotation = (float) player.thrustAngle;

        /* Scale bitmap */
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) this.size, (int) this.size, false);

        /* No anti-alias scaling */
        noAA = new Paint();
        noAA.setAntiAlias(false);
        noAA.setFilterBitmap(false);
        noAA.setDither(false);
    }

    @Override
    void draw(Canvas canvas) {
        /* Rotate asteroid */
        Matrix matrix = new Matrix();
        matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
        matrix.postRotate(180 - rotation);
        matrix.postTranslate(x + (float) (size / 2.0), y + (float) (size / 2.0));

        /* Draw main player */
        canvas.drawBitmap(bitmap, matrix, noAA);
    }

}
