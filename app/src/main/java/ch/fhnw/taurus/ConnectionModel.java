package ch.fhnw.taurus;

import java.io.Serializable;
import java.util.Observable;

/**
 * Created by nozdormu on 04/05/2016.
 */
public class ConnectionModel extends  Observable implements Serializable {
    private int port;
    private String ip;

    public ConnectionModel() {
        this.ip = "127.0.0.1";
        this.port = 12202;
    }

    public void setPort(int port) {
        if(this.port != port) {
            this.port = port;
            setChanged();
            notifyObservers();
        }

    }

    public void setIp(String ip) {
        if(this.ip != ip) {
            this.ip = ip;
            notifyObservers();
        }

    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }



}
