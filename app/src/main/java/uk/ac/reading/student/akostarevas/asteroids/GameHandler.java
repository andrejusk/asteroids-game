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
    private TextView mScoreView;
    private TextView mStatusView;

    GameHandler(TextView mScoreView, TextView mStatusView) {
        this.mScoreView = mScoreView;
        this.mStatusView = mStatusView;
    }

    @Override
    public void handleMessage(Message m) {
        if(m.getData().getBoolean("score")) {
            mScoreView.setText(m.getData().getString("text"));
        }
        else {
            //So it is a status
            int i = m.getData().getInt("viz");
            switch(i) {
                case View.VISIBLE:
                    this.mStatusView.setVisibility(View.VISIBLE);
                    break;
                case View.INVISIBLE:
                    this.mStatusView.setVisibility(View.INVISIBLE);
                    break;
                case View.GONE:
                    this.mStatusView.setVisibility(View.GONE);
                    break;
            }

            mStatusView.setText(m.getData().getString("text"));
        }
    }
}