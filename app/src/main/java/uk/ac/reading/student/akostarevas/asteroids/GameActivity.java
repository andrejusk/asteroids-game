package uk.ac.reading.student.akostarevas.asteroids;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

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

        /* Give gameView variables */
        gameView = findViewById(R.id.gamearea);
        gameView.statusView = findViewById(R.id.text);
        gameView.liveView = findViewById(R.id.lives);
        gameView.scoreView = findViewById(R.id.score);
        gameView.diff = findViewById(R.id.button_difficulty);
        gameView.scores = findViewById(R.id.button_scores);
        gameView.guide = findViewById(R.id.button_help);
        gameView.name = findViewById(R.id.score_name);

        /* Menu buttons */
        gameView.diff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(R.layout.popup_diff);
            }
        });
        gameView.scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(R.layout.popup_score);
            }
        });
        gameView.guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(R.layout.popup_guide);
            }
        });

        gameView.setup();

        this.startGame(savedInstanceState);
    }

    /**
     * Shows popup.
     * @param popup Popup to show.
     */
    private void showPopup(int popup) {
        /* Get main layout */
        RelativeLayout mainLayout = findViewById(R.id.gameLayout);

        /* Get inflater */
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        if (inflater == null) {
            return;
        }

        /* Create popup */
        View popupView = inflater.inflate(popup, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        /* Show popup */
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        /* Listen for dismissal */
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
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