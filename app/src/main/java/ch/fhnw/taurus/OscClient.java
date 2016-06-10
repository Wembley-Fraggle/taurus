package ch.fhnw.taurus;

import android.util.Log;

import java.text.MessageFormat;

import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscProperties;

/**
 * Created by nozdormu on 04/05/2016.
 */
public class OscClient {

    private static final String LOG_TAG = OscClient.class.getName();
    private OscP5 oscP5;
    private ConnectionModel model;

    public OscClient(ConnectionModel model) {
        this.model = model;
    }

    void connect() {

        OscProperties properties = new OscProperties();
        properties.setRemoteAddress(model.getIp(),model.getPort());
        properties.setNetworkProtocol(OscP5.TCP);

        oscP5 = new OscP5(this,properties);
    }

    void disconnect() {
        oscP5.stop();
    }

    void sendMessage(int roll,int pitch) {
        if(oscP5 == null) {
            throw new IllegalStateException("Not connected yet");
        }

        OscMessage oscMessage = new OscMessage("/lab");
        oscMessage.add(0);
        oscMessage.add(roll);
        oscMessage.add(1);
        oscMessage.add(pitch);

        Log.d(LOG_TAG, MessageFormat.format("Updating osc angles [{0}/{1}]",roll,pitch));
        oscP5.send(oscMessage);
    }
}
