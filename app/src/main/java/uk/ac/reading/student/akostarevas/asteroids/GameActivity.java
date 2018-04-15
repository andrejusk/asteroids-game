package uk.ac.reading.student.akostarevas.asteroids;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Main activity for Asteroids.
 */
public class GameActivity extends AppCompatActivity {

    private GameThread gameThread;
    private GameView gameView;

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

        gameView = findViewById(R.id.gamearea);
        gameView.statusView = findViewById(R.id.text);
        gameView.liveView = findViewById(R.id.lives);
        gameView.scoreView = findViewById(R.id.score);

        gameView.scores = findViewById(R.id.button_scores);
        gameView.diff = findViewById(R.id.button_difficulty);
        gameView.guide = findViewById(R.id.button_help);

        gameView.name = findViewById(R.id.score_name);

        gameView.setup();

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
        gameThread = new Game(gameView);
        gameView.setThread(gameThread);
        gameThread.setState(GameThread.STATE.MENU);
    }

    /**
     * Pause state function.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (gameThread.gameState == GameThread.STATE.RUNNING) {
            gameThread.setState(GameThread.STATE.PAUSE);
        }
    }


    /**
     * Destroy function.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        gameView.cleanup();
        gameThread = null;
        gameView = null;
    }

}