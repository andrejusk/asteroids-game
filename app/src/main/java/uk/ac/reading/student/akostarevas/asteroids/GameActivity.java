package uk.ac.reading.student.akostarevas.asteroids;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Main activity for Asteroids.
 */
public class GameActivity extends AppCompatActivity {

    private GameThread mGameThread;
    private GameView mGameView;

    /**
     * Create game function.
     * Called when the activity is first created.
     *
     * @param savedInstanceState Saved Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        mGameView = findViewById(R.id.gamearea);
        mGameView.mStatusView = findViewById(R.id.text);
        mGameView.mScoreView = findViewById(R.id.score);
        mGameView.setup();

        this.startGame(savedInstanceState);
    }

    /**
     * Start game function.
     *
     * @param savedInstanceState Saved Bundle.
     */
    @SuppressWarnings("unused")
    private void startGame(Bundle savedInstanceState) {
        //TODO: savedInstance
        mGameThread = new Game(mGameView);
        mGameView.setThread(mGameThread);
        mGameThread.setState(GameThread.STATE_READY);
    }

    /**
     * Pause state function.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (mGameThread.getMode() == GameThread.STATE_RUNNING) {
            mGameThread.setState(GameThread.STATE_PAUSE);
        }
    }


    /**
     * Destroy function.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mGameView.cleanup();
        mGameThread = null;
        mGameView = null;
    }

}