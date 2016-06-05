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
    private OscThread oscThread;

    public OscAngleChangedListener(OscThread oscThread) {
        this.oscThread= oscThread;
    }

    @Override
    public void update(Observable observable, Object data) {
        if(!(observable instanceof  Labyrinth)) {
            // Not the business of this class
            return;
        }
        Handler handler = oscThread.getHandler();
        if(handler != null) {
            Labyrinth labyrinth = (Labyrinth)observable;
            Message msg = handler.obtainMessage();
            msg.what = OscThread.WHAT_SEND_POS;
            Bundle bundle = msg.getData();
            bundle.putInt("x",labyrinth.getPitch());
            bundle.putInt("y",labyrinth.getRoll());

            msg.sendToTarget();
        }

    }
}
