package ch.fhnw.taurus;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class ServerConnectionTask extends AsyncTask<ConnectionModel,Void,OscClient> {
    private static final String LOG_TAG = ServerConnectionTask.class.getName();
    public ServerConnectionTask() {
        super();
    }

    @Override
    protected OscClient doInBackground(ConnectionModel... params) {
        Log.v(LOG_TAG,"doInBackground() called");
        if(params == null || params.length < 1) {
            throw new IllegalArgumentException("Missing connection model");
        }
        ConnectionModel connectionModel = params[0];
        OscClient client = new OscClient(connectionModel);
        client.connect();

        return client;
    }

}
