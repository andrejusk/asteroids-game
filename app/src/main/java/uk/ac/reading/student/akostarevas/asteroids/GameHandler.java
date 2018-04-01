package uk.ac.reading.student.akostarevas.asteroids;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

/**
 * Handles messages from GameThread to GameView.
 */
class GameHandler extends Handler {

    /* Pointers to the views */
    private TextView scoreView;
    private TextView statusView;

    GameHandler(TextView scoreView, TextView statusView) {
        this.scoreView = scoreView;
        this.statusView = statusView;
    }

    @Override
    public void handleMessage(Message m) {
        if(m.getData().getBoolean("score")) {
            scoreView.setText(m.getData().getString("text"));
        }
        else {
            //So it is a status
            int i = m.getData().getInt("viz");
            switch(i) {
                case View.VISIBLE:
                    this.statusView.setVisibility(View.VISIBLE);
                    break;
                case View.INVISIBLE:
                    this.statusView.setVisibility(View.INVISIBLE);
                    break;
                case View.GONE:
                    this.statusView.setVisibility(View.GONE);
                    break;
            }

            statusView.setText(m.getData().getString("text"));
        }
    }
}