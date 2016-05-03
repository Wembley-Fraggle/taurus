package ch.fhnw.taurus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;

import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String TAG_IP_ADDRESS = "ip_address";
    private static final String TAG_IP_PORT    = "ip_port";
    private static final String RESULT_CODE = "RESULT_CODE";
    private static final String RESULT_MESSAGE = "RESULT_MESSAGE";
    private static final int CONNECT_REQUEST_CODE = 1;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView)this.findViewById(R.id.labyrinth);
        new ServerConnectionTask().execute(getIntent());
    }

    private class ServerConnectionTask extends AsyncTask<Intent,Void,Intent> {

        @Override
        protected Intent doInBackground(Intent... params) {
            Log.v(LOG_TAG,"doInBackground() called");
            while(! isCancelled() ) {
                try {
                    Thread.sleep(TimeUnit.MILLISECONDS.convert(3, TimeUnit.SECONDS));
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_CODE,HttpURLConnection.HTTP_BAD_REQUEST);
                    intent.putExtra(RESULT_MESSAGE,"Bad Request");
                    return intent;
                }
                catch(InterruptedException ex ) {
                    // Ignore and retry. We can not interrupt
                    Log.d(LOG_TAG,"Service not interruptible");
                }
            }
            Intent intent = new Intent();
            intent.putExtra(RESULT_CODE,HttpURLConnection.HTTP_RESET);
            intent.putExtra(RESULT_MESSAGE,"User canceled");
            return intent;
        }

        @Override
        protected void onPostExecute(Intent intent) {
            Log.v(LOG_TAG,"onPostExecute() called");
            super.onPostExecute(intent);
            int statusCode = intent.getIntExtra(RESULT_CODE,HttpURLConnection.HTTP_INTERNAL_ERROR);
            String msg = intent.getStringExtra(RESULT_MESSAGE);
            Log.d(LOG_TAG, MessageFormat.format("{0}:{1}",statusCode,msg));
            if(statusCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                setResult(CONNECT_REQUEST_CODE,intent);
            }
        }
    }
}