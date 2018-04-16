package uk.ac.reading.student.akostarevas.asteroids;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundThread extends Thread{

    private Context context;

    private MediaPlayer engine;

    boolean stop = false;

    boolean
            startEngine = false,
            stopEngine = false,
            playFire = false,
            playHit = false,
            playDeath = false;

    SoundThread(Context context) {
        this.context = context;
        engine = MediaPlayer.create(context, R.raw.sfx_vehicle_engineloop);
    }

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

    private void startEngine() {
        engine = MediaPlayer.create(context, R.raw.sfx_vehicle_engineloop);
        engine.setLooping(true);
        engine.start();
    }

    private void stopEngine() {
        try {
            if (engine.isPlaying()) {
                engine.stop();
                engine.release();
            }
        } catch(Exception ignored) {}
    }

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
