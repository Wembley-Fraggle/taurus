package ch.fhnw.taurus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private TouchLabyrinthView surfaceView;
    private ServerConnectionTask connectionTask;
    private boolean connectionTested;  // FIXME Requried?
    private ConnectionModel connectionModel;
    private OscAngleChangedListener angleChangedListener;
    private Handler oscHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onCreate() called");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Enable the Up button
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        // TODO On Back button

        Intent callerIntent = getIntent();
        if(callerIntent.hasExtra(Contract.TAG_CONNECTION_MODEL)) {
            // Get the model from the caller if existing
            connectionModel = (ConnectionModel)callerIntent.getSerializableExtra(Contract.TAG_CONNECTION_MODEL);
        }

        if(savedInstanceState == null ) {
            startConnectionTask(savedInstanceState);
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

        this.connectionModel = (ConnectionModel)savedInstanceState.get(Contract.TAG_CONNECTION_MODEL);

        if(savedInstanceState.containsKey(Contract.TAG_CONNECTION_TEST_STARTED)) {
            connectionTested = savedInstanceState.getBoolean(Contract.TAG_CONNECTION_TEST_STARTED);
        }
        if(!connectionTested) {
            startConnectionTask(savedInstanceState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void startConnectionTask(Bundle savedInstanceState) {
        final ConnectionModel connectionModel = (ConnectionModel) getIntent().getSerializableExtra(Contract.TAG_CONNECTION_MODEL);

        connectionTask = new ServerConnectionTask();
        connectionTask.execute(connectionModel);
        try {
            OscClient oscClient  = connectionTask.get(15, TimeUnit.SECONDS);
            Log.w(LOG_TAG, "Connection possible");
            new OscThread(oscClient, new ThreadInitCallback() {
                @Override
                public void onHandlerInitialized(Handler handler) {
                    oscHandler = handler;
                    angleChangedListener = new OscAngleChangedListener(handler);
                    getLabyrinth().addObserver(angleChangedListener);
                }
            }).start();
            connectionTested = true;
        }
        catch(Exception ex) {
            connectionTask.cancel(true);
            Log.w(LOG_TAG, "Failed to connect");
            setResult(Contract.RESULT_CONNECTION_FAILED);
            finish();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onSaveInstanceState() called");
        savedInstanceState.putBoolean(Contract.TAG_CONNECTION_TEST_STARTED,connectionTested);

        savedInstanceState.putSerializable(Contract.TAG_CONNECTION_MODEL,connectionModel);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG,"onDestroy() called");
        if(connectionTask != null) {
            connectionTask.cancel(true);
            connectionTask = null;
        }
        if(angleChangedListener != null) {
            getLabyrinth().deleteObserver(angleChangedListener);
            angleChangedListener = null;
        }
        if(oscHandler != null) {
            stopOscThread();;
            oscHandler = null;
        }
        connectionTask = null;
        super.onDestroy();
    }

    private void stopOscThread() {
        if(oscHandler != null) {
            Message msg = oscHandler.obtainMessage();
            msg.what = OscThread.WHAT_STOP;
            msg.sendToTarget();
        }
    }



    private Labyrinth getLabyrinth() {
        return getApp().getLabyrinth();
    }

    private App getApp() {
        return (App)this.getApplication();
    }

}