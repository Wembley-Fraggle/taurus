package ch.fhnw.taurus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private SurfaceView surfaceView;
    private AsyncTask connectionTask;
    private boolean connectionTested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView)this.findViewById(R.id.labyrinth);
        surfaceView.getHolder();

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        // TODO On Back button clicked

        connectionTested = false;
        if(savedInstanceState != null && savedInstanceState.containsKey(Contract.TAG_CONNECTION_TEST_STARTED)) {
            connectionTested = savedInstanceState.getBoolean(Contract.TAG_CONNECTION_TEST_STARTED);
        }
        if(!connectionTested) {
            connectionTask = new ServerConnectionTask(this).execute(getIntent());
            connectionTested = true;
        }
    }

    @Override
    public void onBackPressed() {
        Log.v(LOG_TAG,"onBackPressed() called");
        if(connectionTask != null) {
            connectionTask.cancel(true);
            connectionTask = null;
        }
        connectionTested = false;
        super.onBackPressed();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onRestoreInstanceState() called");
        connectionTested = false;
        if(savedInstanceState.containsKey(Contract.TAG_CONNECTION_TEST_STARTED)) {
            connectionTested = savedInstanceState.getBoolean(Contract.TAG_CONNECTION_TEST_STARTED);

        }
        if(!connectionTested) {
            connectionTask = new ServerConnectionTask(this).execute(getIntent());
            connectionTested = true;
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onSaveInstanceState() called");
        savedInstanceState.putBoolean(Contract.TAG_CONNECTION_TEST_STARTED,connectionTested);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG,"onDestroy() called");
        if(connectionTask != null) {
            connectionTask.cancel(true);
        }
        connectionTask = null;
        super.onDestroy();
    }

}