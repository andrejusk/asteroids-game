package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PlayerBullet extends MotionObject {

    final static int bulletSize = 5;

    final static double bulletVelocity = 10;
    final Paint paint;

    PlayerBullet(Player player) {
        super(player.x + player.size / 2, player.y + player.size / 2, bulletSize,
                player.canvasWidth, player.canvasHeight,
                new Vector(bulletVelocity, player.thrustAngle), true);

        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, size, paint);
    }

}
