package ch.fhnw.taurus;

import android.util.Log;

import java.text.MessageFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by nozdormu on 04/05/2016.
 */
public class AngleChangedLogger implements Observer{
    private static final String LOG_TAG = AngleChangedLogger.class.getName();

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof  Labyrinth) {
            Labyrinth labyrinth = (Labyrinth)observable;
            Log.d(LOG_TAG, MessageFormat.format("Angle Changed: {0}/{1}",labyrinth .getXAngle(),labyrinth.getYAngle()));
        }
    }
}
