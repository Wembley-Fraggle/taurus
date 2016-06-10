package ch.fhnw.taurus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by nozdormu on 08/05/2016.
 */
public class OscAngleChangedListener implements Observer {
    private static final String LOG_TAG = OscAngleChangedListener.class.getName();
    private Handler  oscHandler;

    public OscAngleChangedListener(Handler oscHandler) {
        this.oscHandler = oscHandler;
    }

    @Override
    public void update(Observable observable, Object data) {
        if(!(observable instanceof  Labyrinth)) {
            // Not the business of this class
            return;
        }
        if(oscHandler!= null) {
            Labyrinth labyrinth = (Labyrinth)observable;
            Message msg = oscHandler.obtainMessage();
            msg.what = OscThread.WHAT_SEND_POS;
            Bundle bundle = msg.getData();
            bundle.putInt("pitch",labyrinth.getPitch());
            bundle.putInt("roll",labyrinth.getRoll());

            msg.sendToTarget();
        }

    }
}
