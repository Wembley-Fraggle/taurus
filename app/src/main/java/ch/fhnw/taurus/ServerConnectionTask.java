package ch.fhnw.taurus;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class ServerConnectionTask extends AsyncTask<ConnectionModel,Void,Boolean> {
    private static final String LOG_TAG = ServerConnectionTask.class.getName();
    private ConnectionResultCallback callback;


    public ServerConnectionTask(ConnectionResultCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(ConnectionModel... params) {
        Log.v(LOG_TAG,"doInBackground() called");
        if(params == null || params.length < 1) {
            throw new IllegalArgumentException("Missing connection model");
        }
        ConnectionModel connectionModel = params[0];

        Boolean connecable = Boolean.FALSE;

        String ip = connectionModel.getIp();
        try {
            InetAddress serverAddr = InetAddress.getByName(ip);
            return true;
        }
        catch (UnknownHostException ex) {
            connecable = false;
        }
        catch(IOException ex) {
            connecable = false;
        }

        return connecable;
    }


    @Override
    protected void onPostExecute(Boolean connecable) {
        Log.v(LOG_TAG,"onPostExecute() called");

        if(callback != null) {
            callback.onConnectionResult(connecable.booleanValue());
        }
        super.onPostExecute(connecable);
    }
}
