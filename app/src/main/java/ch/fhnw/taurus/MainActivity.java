package ch.fhnw.taurus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private LabyrinthView surfaceView;
    private AsyncTask connectionTask;
    private boolean connectionTested;  // FIXME Requried?
    private ConnectionModel connectionModel;
    private OscAngleChangedListener angleChangedListener;
    private OscThread oscThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onCreate() called");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        surfaceView = (LabyrinthView)this.findViewById(R.id.labyrinth);
        surfaceView.getHolder();

        final PositionConverter converter = new PositionConverter(getLabyrinth(),surfaceView,surfaceView.getMaxCursorRadius());
        surfaceView.addTouchEventListener(new TouchEventListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                int xDegree = converter.convertToXAngle(event.getX());
                int yDegree = converter.convertToXAngle(event.getY());
                getLabyrinth().setAngles(xDegree,yDegree);
            }
        });

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
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

        connectionTask = new ServerConnectionTask(new ConnectionResultCallback() {
            @Override
            public void onConnectionResult(boolean connectable) {
                if(connectable) {
                    Log.w(LOG_TAG,"Connection possible");
                    OscClient client = new OscClient(connectionModel);
                    oscThread = new OscThread(connectionModel);
                    oscThread.addThreadInitCallback(new ThreadInitCallback() {
                        @Override
                        public void onHandlerInitialized(Handler handler) {
                            connectOsc(handler);
                        }
                    });
                    oscThread.start();
                    angleChangedListener= new OscAngleChangedListener(oscThread);
                    getLabyrinth().addObserver(angleChangedListener);
                }
                else {
                    Log.w(LOG_TAG,"Failed to connect");
                    setResult(Contract.RESULT_CONNECTION_FAILED);
                    finish();

                }
            }
        }).execute(connectionModel);
        connectionTested = true;
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
        if(oscThread != null) {
            stopOscThread();;
            oscThread = null;
        }
        connectionTask = null;
        super.onDestroy();
    }

    private void connectOsc(Handler handler) {
        Message msg = handler.obtainMessage();
        msg.what = OscThread.WHAT_CONNECT;
        msg.sendToTarget();
    }

    private void stopOscThread() {
        if(oscThread != null) {
            Handler handler = oscThread.getHandler();
            Message msg = handler.obtainMessage();
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