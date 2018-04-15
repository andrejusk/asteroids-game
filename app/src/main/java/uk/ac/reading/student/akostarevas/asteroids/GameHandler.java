package uk.ac.reading.student.akostarevas.asteroids;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

/**
 * Handles messages from GameThread to GameView.
 */
class GameHandler extends Handler {

    /* Pointers to the views */
    private TextView scoreView;
    private TextView livesView;
    private TextView statusView;

    private Button difficulty;
    private Button high;
    private Button help;

    GameHandler(TextView scoreView, TextView livesView, TextView statusView,
                Button difficulty, Button high, Button help) {
        this.scoreView = scoreView;
        this.statusView = statusView;
        this.livesView = livesView;

        this.difficulty = difficulty;
        this.high = high;
        this.help = help;
    }

    @Override
    public void handleMessage(Message m) {
        if (m.getData().getBoolean("score")) {
            scoreView.setText(m.getData().getString("text"));
        }
        else if (m.getData().getBoolean("lives")) {
            livesView.setText(m.getData().getString("text"));
        }
        else {
            int view = m.getData().getInt("viz");
            int buttons = m.getData().getInt("buttons");

            this.statusView.setVisibility(view);
            this.difficulty.setVisibility(buttons);
            this.high.setVisibility(buttons);
            this.help.setVisibility(buttons);

            statusView.setText(m.getData().getString("text"));
        }
    }
}