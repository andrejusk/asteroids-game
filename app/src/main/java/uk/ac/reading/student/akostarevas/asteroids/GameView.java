package uk.ac.reading.student.akostarevas.asteroids;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


@SuppressWarnings("unused")
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    GameHandler handler;
    /* Pointers to the views */
    TextView scoreView;
    TextView liveView;
    TextView statusView;

    Button scores;
    Button diff;
    Button guide;

    EditText name;
    ImageView logo;
    /* GameThread and communication handler */
    private volatile GameThread thread;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /* Get the holder of the screen and register interest */
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    public void setup() {
        /* Set up a handler for messages from GameThread */
        handler = new GameHandler(scoreView, liveView, statusView, scores, diff, guide, name, logo);
    }

    /**
     * Release any resources.
     */
    public void cleanup() {
        this.thread.running = false;
        this.thread.cleanup();

        this.removeCallbacks(thread);
        thread = null;

        SurfaceHolder holder = getHolder();
        holder.removeCallback(this);
    }

    /**
     * Sets new thread.
     *
     * @param newThread Thread to set to.
     */
    public void setThread(GameThread newThread) {
        thread = newThread;

        setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if (thread == null) {
                    return false;
                }
                if (thread.gameState == GameThread.STATE.MENU) {
                    playSelect(thread);
                }
                return thread.onTouch(event);
            }
        });

        setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return thread != null && thread.onKey(event);
            }
        });

        setClickable(true);
        setFocusable(true);
    }

    static void playSelect(GameThread thread) {
        playSelect(thread.context);
    }

    static void playSelect(Context context) {
        /* Play jingle */
        final MediaPlayer beep = MediaPlayer.create(context, R.raw.sfx_sounds_blip2);
        beep.start();
        beep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                beep.release();
            }
        });
    }

    /**
     * Ensure that we go into pause state if we go out of focus.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (thread != null) {
            if (!hasWindowFocus) {
                thread.pause();
            }
            else {
                thread.unPause();
            }
        }
    }

    /**
     * Called on created surface.
     *
     * @param holder SurfaceHolder
     */
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread != null) {
            thread.running = true;

            if (thread.getState() == Thread.State.NEW) {
                /* Just start the new thread */
                thread.start();
            } else {
                if (thread.getState() == Thread.State.TERMINATED) {
                    /*
                     * Start a new thread
                     * Should be this to update screen with old game: new GameThread(this, thread);
                     * The method should set all fields in new thread to the value of old thread's fields
                     */
                    thread = new Game(this);
                    thread.running = true;
                    thread.start();
                }
            }
        }
    }

    /**
     * Always called once after surfaceCreated.
     * Tell the GameThread the actual size.
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (thread != null) {
            thread.setSurfaceSize(width, height);
        }
    }

    /**
     * Need to stop the GameThread if the surface is destroyed
     * Remember this doesn't need to happen when app is paused on even stopped.
     */
    public void surfaceDestroyed(SurfaceHolder arg0) {

        boolean retry = true;
        if (thread != null) {
            thread.running = false;
        }

        /* Join the thread with this thread */
        while (retry) {
            try {
                if (thread != null) {
                    thread.join();
                }
                retry = false;
            } catch (InterruptedException ignored) {
            }
        }
    }

}