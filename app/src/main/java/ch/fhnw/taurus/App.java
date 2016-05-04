package ch.fhnw.taurus;

import android.app.Application;
import android.util.Log;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class App extends Application {

    private static final String LOG_TAG = App.class.getName();
    private Labyrinth labyrinth;

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "onCreate() called");
        labyrinth = new Labyrinth(180,180);
        super.onCreate();
    }

    public Labyrinth getLabyrinth() {
        return labyrinth;
    }
}
