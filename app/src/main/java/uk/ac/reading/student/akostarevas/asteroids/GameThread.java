package uk.ac.reading.student.akostarevas.asteroids;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import static uk.ac.reading.student.akostarevas.asteroids.GameThread.STATE.*;

@SuppressWarnings("WeakerAccess")
public abstract class GameThread extends Thread {
    /* Different gameState states */
    enum STATE {
        LOSE, PAUSE, READY, RUNNING, WIN
    }

    /* Used to ensure appropriate threading */
    static final Integer monitor = 1;

    /* The view */
    public GameView gameView;

    /* Control variable for the gameState of the game (e.g. STATE.WIN) */
    protected STATE gameState;

    protected int canvasWidth;
    protected int canvasHeight;

    /* Last time we updated the game physics */
    protected long lastUpdate = 0;

    /* Control of the actual running inside run() */
    boolean running = false;

    /* Score tracking */
    long score = 0;

    /* Background */
    private Paint background;

    /* The surface this thread (and only this thread) writes upon */
    private SurfaceHolder surfaceHolder;

    /* The message handler to the View/Activity thread */
    private Handler handler;

    /* Android Context - this stores almost all we need to know */
    private Context context;

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
            setupBeginning();
            lastUpdate = System.currentTimeMillis() + 100;
            setState(RUNNING);
            updateScore(0);
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
                    if (gameState == RUNNING) {
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
            if (gameState == READY || gameState == LOSE || gameState == WIN) {
                setup();
                return true;
            }
            if (gameState == PAUSE) {
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
            if (gameState == RUNNING) {
                setState(PAUSE);
            }
        }
    }

    /**
     * UnPause game.
     */
    public void unPause() {
        /* Move the real time clock up to now */
        synchronized (monitor) {
            lastUpdate = System.currentTimeMillis();
        }
        setState(RUNNING);
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

            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();

            b.putInt("viz", View.VISIBLE);

            if (this.gameState == RUNNING) {
                b.putString("text", "");
                b.putBoolean("showAd", false);
            } else {
                Resources res = context.getResources();
                CharSequence str;

                switch (this.gameState) {
                    case READY:
                        str = res.getText(R.string.mode_ready);
                        break;
                    case PAUSE:
                        str = res.getText(R.string.mode_pause);
                        break;
                    case LOSE:
                        str = res.getText(R.string.mode_lose);
                        break;
                    case WIN:
                        str = res.getText(R.string.mode_win);
                        break;
                    default:
                        str = "";
                        break;
                }

                if (message != null) {
                    str = message + "\n" + str;
                }

                b.putString("text", str.toString());
            }

            msg.setData(b);
            handler.sendMessage(msg);
        }
    }



    /**
     * Update and send a score to the View.
     * Would it be better to do this inside this thread writing it manually on the screen?
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