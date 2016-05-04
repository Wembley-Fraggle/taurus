package ch.fhnw.taurus;

import android.util.Log;

import java.text.MessageFormat;

/**
 * Created by nozdormu on 04/05/2016.
 */
public class AngleChangedLogger implements AngleChangedListener{
    private static final String LOG_TAG = AngleChangedLogger.class.getName();
    @Override
    public void onAngleChanged(float posX, float posY) {
        Log.d(LOG_TAG, MessageFormat.format("Angle Changed: {0}/{1}",posX,posY));
    }
}
