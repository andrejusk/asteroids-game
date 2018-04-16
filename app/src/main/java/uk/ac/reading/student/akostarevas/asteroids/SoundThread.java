package uk.ac.reading.student.akostarevas.asteroids;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * SouundThread class.
 */
public class SoundThread extends Thread {

    /* Context to play to */
    private Context context;

    /* Global engine sound */
    private MediaPlayer engine;

    boolean stop = false;

    /* Start/stop flags */
    boolean
            startEngine = false,
            stopEngine = false,
            playFire = false,
            playHit = false,
            playDeath = false;

    /**
     * Constructor.
     * @param context Context.
     */
    SoundThread(Context context) {
        this.context = context;
        engine = MediaPlayer.create(context, R.raw.sfx_vehicle_engineloop);
    }

    /**
     * Run method. Listens for flags.
     */
    @Override
    public void run() {
        while (!stop) {
            if (startEngine) {
                startEngine = false;
                startEngine();
            }
            if (stopEngine) {
                stopEngine = false;
                stopEngine();
            }
            if (playFire) {
                playFire = false;
                playFire();
            }
            if (playHit) {
                playHit = false;
                playHit();
            }
            if (playDeath) {
                playDeath = false;
                playDeath();
            }
        }
        stopEngine();
    }

    /**
     * Start engine sound.
     */
    private void startEngine() {
        engine = MediaPlayer.create(context, R.raw.sfx_vehicle_engineloop);
        engine.setLooping(true);
        engine.start();
    }

    /**
     * Stop engine sound if exists.
     */
    private void stopEngine() {
        try {
            if (engine.isPlaying()) {
                engine.stop();
                engine.release();
            }
        } catch(Exception ignored) {}
    }

    /**
     * Play firing noise.
     */
    private void playFire() {
        final MediaPlayer fire = MediaPlayer.create(context, R.raw.sfx_damage_hit1);
        fire.start();
        fire.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                fire.release();
            }
        });
    }

    /**
     * Play death sound.
     */
    private void playDeath() {
        final MediaPlayer death = MediaPlayer.create(context, R.raw.sfx_exp_medium1);
        death.start();
        death.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                death.release();
            }
        });
    }

    /**
     * Play hit sound.
     */
    private void playHit() {
        final MediaPlayer hit = MediaPlayer.create(context, R.raw.sfx_exp_short_soft1);
        hit.start();
        hit.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                hit.release();
            }
        });
    }

}
