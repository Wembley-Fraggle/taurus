package ch.fhnw.taurus;

import android.os.Handler;

/**
 * Created by nozdormu on 12/05/2016.
 */
public interface ThreadInitCallback {
    void onHandlerInitialized(Handler handler);
}
