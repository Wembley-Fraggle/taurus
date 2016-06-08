package ch.fhnw.taurus;

import android.util.Log;

import java.text.MessageFormat;

import netP5.NetAddress;
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
    private NetAddress remote;

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

    void sendMessage(int angleX, int angleY) {
        if(oscP5 == null) {
            throw new IllegalStateException("Not connected yet");
        }

        OscMessage oscMessage = new OscMessage("/lab");
        oscMessage.add(0);
        oscMessage.add(angleX);
        oscMessage.add(1);
        oscMessage.add(angleY);

        Log.d(LOG_TAG, MessageFormat.format("Updating osc angles [{0}/{1}]",angleX,angleY));
        oscP5.send(oscMessage);
    }
}
