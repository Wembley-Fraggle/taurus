package ch.fhnw.taurus;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class ServerConnectionTask extends AsyncTask<Intent,Void,Intent> {
    private static final String LOG_TAG = ServerConnectionTask.class.getName();
    private Context context;

    public ServerConnectionTask(Context context) {
        this.context = context;
    }

    @Override
    protected Intent doInBackground(Intent... params) {
        Log.v(LOG_TAG,"doInBackground() called");
        while(! isCancelled() ) {
            try {
                Thread.sleep(TimeUnit.MILLISECONDS.convert(3, TimeUnit.SECONDS));
                Intent intent = new Intent();
                intent.putExtra(Contract.TAG_HTTP_STATUS, HttpURLConnection.HTTP_BAD_REQUEST);
                intent.putExtra(Contract.RESULT_MESSAGE,"Bad Request");
                return intent;
            }
            catch(InterruptedException ex ) {
                // Ignore and retry. We can not interrupt
                Log.d(LOG_TAG,"Service not interruptible");
            }
        }
        Intent intent = new Intent();
        intent.putExtra(Contract.TAG_HTTP_STATUS,HttpURLConnection.HTTP_RESET);
        intent.putExtra(Contract.RESULT_MESSAGE,"User canceled");
        return intent;
    }


    @Override
    protected void onPostExecute(Intent intent) {
        Log.v(LOG_TAG,"onPostExecute() called");

        int statusCode = intent.getIntExtra(Contract.TAG_HTTP_STATUS,HttpURLConnection.HTTP_INTERNAL_ERROR);
        String msg = intent.getStringExtra(Contract.RESULT_MESSAGE);
        Log.d(LOG_TAG, MessageFormat.format("{0}:{1}",statusCode,msg));
        if(statusCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        }
        super.onPostExecute(intent);
    }
}
