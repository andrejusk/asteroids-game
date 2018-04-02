package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PlayerBullet extends MotionObject {

    final static int bulletVelocity = 10;
    final Paint paint;

    PlayerBullet(Player player) {
        //TODO: vector addition player speed fast fast
        super(player.x, player.y, player.canvasWidth, player.canvasHeight, player.thrustAngle, bulletVelocity, true);
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, size, paint);
    }

}
