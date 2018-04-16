package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * PlayerBullet class.
 */
class PlayerBullet extends MotionObject {

    /* Constants */
    private final static int bulletSize = 10;
    private final static double bulletVelocity = 10;

    /**
     * PlayerBullet constructor.
     * Builds itself based on Player.
     * @param player Player.
     * @param bitmap Bitmap.
     */
    PlayerBullet(Player player, Bitmap bitmap) {
        super(player.x + player.size / 2, player.y + player.size / 2, bulletSize,
                player.canvasWidth, player.canvasHeight,
                new Vector(bulletVelocity, player.thrustAngle), true, bitmap);
    }

}
