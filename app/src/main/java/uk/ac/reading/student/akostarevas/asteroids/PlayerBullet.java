package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

class PlayerBullet extends MotionObject {

    final static int bulletSize = 10;

    final static double bulletVelocity = 10;

    PlayerBullet(Player player, Bitmap bitmap) {
        super(player.x + player.size / 2, player.y + player.size / 2, bulletSize,
                player.canvasWidth, player.canvasHeight,
                new Vector(bulletVelocity, player.thrustAngle), true, bitmap);
    }

}
