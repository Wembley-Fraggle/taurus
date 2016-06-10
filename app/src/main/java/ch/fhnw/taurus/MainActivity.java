package ch.fhnw.taurus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private TouchLabyrinthView surfaceView;
    private ServerConnectionTask connectionTask;
    private boolean connectionTested;  // FIXME Requried?
    private ConnectionModel connectionModel;
    private OscAngleChangedListener angleChangedListener;
    private Handler oscHandler;
    private ProgressDialog connectProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onCreate() called");

        setContentView(R.layout.activity_main);

        // Enable the Up button
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        // TODO On Back button

        Intent callerIntent = getIntent();
        if(callerIntent.hasExtra(Contract.TAG_CONNECTION_MODEL)) {
            // Get the model from the caller if existing
            connectionModel = (ConnectionModel)callerIntent.getSerializableExtra(Contract.TAG_CONNECTION_MODEL);
        }

        connectProgressDialog =  new ProgressDialog(this);
        connectProgressDialog.setTitle("Connecting..."); // FIXME get resurce
        connectProgressDialog.setMessage("Please wait");
        connectProgressDialog.setCancelable(false);
        connectProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        if(savedInstanceState == null ) {
            startConnectionTask(savedInstanceState);
        }

        super.onCreate(savedInstanceState);
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
        this.connectionModel = (ConnectionModel)savedInstanceState.get(Contract.TAG_CONNECTION_MODEL);
        startConnectionTask(savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);
    }

    private void startConnectionTask(Bundle savedInstanceState) {
        final ConnectionModel connectionModel = (ConnectionModel) getIntent().getSerializableExtra(Contract.TAG_CONNECTION_MODEL);

        final AsyncTask<Void,Integer,OscClient> waitForClientTask = new AsyncTask<Void,Integer,OscClient>() {

            private ServerConnectionTask connectionTask;
            private CountDownTimer timer;
            private static final long MAX_WAIT_SECONDS = 15;

            @Override
            protected void onPreExecute() {
                connectionTask = new ServerConnectionTask();
                connectionTask.execute(connectionModel);
                int maxWaitMs = (int)TimeUnit.MILLISECONDS.convert(MAX_WAIT_SECONDS, TimeUnit.SECONDS);
                int intervalMs = (int)TimeUnit.MILLISECONDS.convert(1,TimeUnit.SECONDS);

                connectProgressDialog.setMax(maxWaitMs);
                connectProgressDialog.setProgress(maxWaitMs);
                connectProgressDialog.show();

                final AsyncTask<Void,Integer,OscClient> taskToCancel = this;
                timer = new CountDownTimer(maxWaitMs,intervalMs) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if(!isCancelled()) {
                            publishProgress((int)millisUntilFinished);
                            if(millisUntilFinished == 0) {
                                taskToCancel.cancel(true);
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        publishProgress(0);
                        connectionTask.cancel(true);
                        taskToCancel.cancel(true);
                    }
                };
                timer.start();
                super.onPreExecute();
            }

            @Override
            protected OscClient doInBackground(Void[] params) {
                try {
                    return connectionTask.get();
                }
                catch(InterruptedException | ExecutionException | CancellationException ex) {
                    Log.v(LOG_TAG,"Connection failed",ex);
                    return null;
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                int remaining = values[0];
                if(!isCancelled() && connectProgressDialog != null) {
                    connectProgressDialog.setProgress(remaining);
                }

            }

            @Override
            protected void onPostExecute(OscClient oscClient) {
                Log.w(LOG_TAG, "Connection possible");
                if(timer != null) {
                    timer.cancel();
                }
                if(connectProgressDialog != null) {
                    connectProgressDialog.dismiss();
                }

                new OscThread(oscClient, new ThreadInitCallback() {
                    @Override
                    public void onHandlerInitialized(Handler handler) {
                        oscHandler = handler;
                        angleChangedListener = new OscAngleChangedListener(handler);
                        getLabyrinth().addObserver(angleChangedListener);

                    }
                }).start();
                Toast.makeText(MainActivity.this,R.string.successfully_connected,Toast.LENGTH_LONG).show();
                super.onPostExecute(oscClient);
            }

            @Override
            protected void onCancelled() {
                Log.w(LOG_TAG, "Failed to connect");
                connectionTask.cancel(true);
                setResult(Contract.RESULT_CONNECTION_FAILED);
                Toast.makeText(MainActivity.this,R.string.connection_failed,Toast.LENGTH_LONG).show();
                finish();
                super.onCancelled();
            }
        };
        waitForClientTask.execute();
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

        connectProgressDialog.dismiss();
        connectProgressDialog = null;

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