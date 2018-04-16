package uk.ac.reading.student.akostarevas.asteroids;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

@SuppressWarnings("WeakerAccess")
public abstract class GameThread extends Thread {
    /*

        Different gameState states

        MENU - Game is in menu
        PAUSE - Game is paused (due to focus loss)
        RUNNING - Game is running
        DEAD - Player has died, still has lives
        FINISH - Player is out of lives

     */
    enum STATE {
        MENU, PAUSE, RUNNING, DEAD, FINISH
    }
    /* Control variable for the gameState of the game (e.g. STATE.WIN) */
    protected STATE gameState;

    /*
        Different difficulties
     */
    enum DIFFICULTY {
        EASY, MEDIUM, HARD
    }
    protected DIFFICULTY difficulty = DIFFICULTY.MEDIUM;

    /* Used to ensure appropriate threading */
    static final Integer monitor = 1;

    /* The view */
    public GameView gameView;

    protected int canvasWidth;
    protected int canvasHeight;

    /* Last time we updated the game physics */
    protected long lastUpdate = 0;

    /* Control of the actual running inside run() */
    boolean running = false;

    /* Score tracking */
    long score = 0;

    /* Lives */
    protected int lives = 3;

    /* Background */
    private Paint background;

    /* The surface this thread (and only this thread) writes upon */
    private SurfaceHolder surfaceHolder;

    /* The message handler to the View/Activity thread */
    private Handler handler;

    /* Android Context - this stores almost all we need to know */
    Context context;

    public GameThread(GameView gameView) {
        this.gameView = gameView;

        surfaceHolder = gameView.getHolder();
        handler = gameView.handler;
        context = gameView.getContext();

        background = new Paint();
        background.setColor(Color.BLACK);
    }

    /**
     * Called when app is destroyed, so not really that important here
     * But if (later) the game involves more thread, we might need to stop a thread, and then we would need this
     * Dare I say memory leak...
     */
    public void cleanup() {
        this.context = null;
        this.gameView = null;
        this.handler = null;
        this.surfaceHolder = null;
    }

    /**
     * Pre-begin a game
     */
    abstract public void setupBeginning();

    /**
     * Starting up the game
     */
    public void setup() {
        synchronized (monitor) {
            /* Save lives/score between lives */
            if (gameState != STATE.DEAD) {
                updateScore(0);
                updateLives(3);
            }
            setupBeginning();
            lastUpdate = System.currentTimeMillis();
            setState(STATE.RUNNING);
        }
    }

    /**
     * Start thread.
     */
    @Override
    public void run() {
        Canvas canvasRun;
        while (running) {
            canvasRun = null;
            try {
                canvasRun = surfaceHolder.lockCanvas(null);
                synchronized (monitor) {
                    if (gameState == STATE.RUNNING) {
                        updatePhysics();
                    }
                    draw(canvasRun);
                }
            } finally {
                if (canvasRun != null) {
                    if (surfaceHolder != null)
                        surfaceHolder.unlockCanvasAndPost(canvasRun);
                }
            }
        }
    }

    /**
     * Set surface size.
     *
     * @param width  Width.
     * @param height Height.
     */
    public void setSurfaceSize(int width, int height) {
        synchronized (monitor) {
            canvasWidth = width;
            canvasHeight = height;
        }
    }


    /**
     * Draw background.
     *
     * @param canvas Canvas to draw to.
     */
    protected void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, background);
    }

    /**
     * Update physics.
     */
    private void updatePhysics() {
        long now = System.currentTimeMillis();
        float elapsed = (now - lastUpdate) / 1000.0f;
        this.updateGame(elapsed);
        lastUpdate = now;
    }

    abstract protected void updateGame(float secondsElapsed);

    /**
     * On touch event.
     *
     * @param e MotionEvent
     * @return Successful handle.
     */
    public boolean onTouch(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (gameState == STATE.MENU || gameState == STATE.DEAD) {
                setup();
                return true;
            }
            if (gameState == STATE.FINISH) {
                //TODO: Save score, go to STATE.MENU
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putBoolean("submit", true);
                msg.setData(bundle);
                handler.sendMessage(msg);
                setState(STATE.MENU);
                return true;
            }
            if (gameState == STATE.PAUSE) {
                unPause();
                return true;
            }
        }
        synchronized (monitor) {
            return this.actionOnTouch(e);
        }
    }

    abstract boolean actionOnTouch(MotionEvent e);

    boolean onKey(KeyEvent event) {
        synchronized (monitor) {
            this.actionOnKey(event);
        }
        return false;
    }

    abstract void actionOnKey(KeyEvent event);

    /**
     * Pause game.
     */
    public void pause() {
        synchronized (monitor) {
            if (gameState == STATE.RUNNING) {
                setState(STATE.PAUSE);
            }
        }
    }

    /**
     * UnPause game.
     */
    public void unPause() {
        if (gameState == STATE.PAUSE) {
            /* Move the real time clock up to now */
            lastUpdate = System.currentTimeMillis();
            setState(STATE.RUNNING);
        }
    }



    /**
     * Send messages to View/Activity thread.
     *
     * @param state State to set to.
     */
    public void setState(STATE state) {
        synchronized (monitor) {
            setState(state, null);
        }
    }

    /**
     * Sets game state.
     *
     * @param state    State to set to.
     * @param message Message.
     */
    public void setState(STATE state, CharSequence message) {
        synchronized (monitor) {
            this.gameState = state;

            cleanState();
            if (state == STATE.MENU) {
                playJingle();
            }

            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();

            if (this.gameState == STATE.RUNNING) {
                bundle.putInt("viz", View.GONE);
                bundle.putInt("buttons", View.GONE);
                bundle.putString("text", "");
            } else {
                bundle.putInt("viz", View.VISIBLE);
                Resources res = context.getResources();
                switch (this.gameState) {
                    case MENU:
                        bundle.putString("text", res.getText(R.string.mode_menu).toString());
                        bundle.putInt("buttons", View.VISIBLE);
                        break;
                    case PAUSE:
                        bundle.putString("text", res.getText(R.string.mode_pause).toString());
                        bundle.putInt("buttons", View.INVISIBLE);
                        break;
                    case DEAD:
                        bundle.putString("text", res.getText(R.string.mode_dead).toString());
                        bundle.putInt("buttons", View.INVISIBLE);
                        break;
                    case FINISH:
                        bundle.putString("text", res.getText(R.string.mode_finish).toString());
                        bundle.putInt("buttons", View.INVISIBLE);
                        break;
                    default:
                        bundle.putString("text", "");
                        break;
                }
            }
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }

    abstract void cleanState();

    private void playJingle() {
        /* Play jingle */
        final MediaPlayer jingle = MediaPlayer.create(context, R.raw.sfx_sounds_fanfare3);
        jingle.start();
        jingle.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                jingle.release();
            }
        });
    }


    /**
     * Update and send a score to the View.
     */
    private void updateScore(long score) {
        this.score = score;

        synchronized (monitor) {
            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putBoolean("score", true);
            b.putString("text", getScoreString().toString());
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }

    /**
     * Update and send a lives to the View.
     */
    protected void updateLives(int lives) {
        this.lives = lives;

        synchronized (monitor) {
            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putBoolean("lives", true);
            b.putString("text", String.valueOf(lives));
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }

    /**
     * Increments score.
     *
     * @param score Score to increment by.
     */
    public void increaseScore(long score) {
        this.updateScore(this.score + score);
    }



    /**
     * Returns score as a String.
     *
     * @return score.
     */
    protected CharSequence getScoreString() {
        return Long.toString(Math.round(this.score));
    }

}