package ch.fhnw.taurus;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nozdormu on 12/05/2016.
 */
public class OscThread extends HandlerThread {

    public static final int WHAT_CONNECT = 0;
    public static final int WHAT_SEND_POS = 1;
    public static final int WHAT_DISCONNECT = 2;
    public static final int WHAT_STOP = 3;

    private OscClient oscClient;
    private Handler handler;
    private ConnectionModel connectionModel;
    private List<ThreadInitCallback> callbackList;

    public OscThread(ConnectionModel connectionModel) {
        super(OscThread.class.getSimpleName(), Process.THREAD_PRIORITY_BACKGROUND);
        this.connectionModel = connectionModel;
        this.callbackList = new LinkedList<>();
    }

    public void addThreadInitCallback(ThreadInitCallback callback) {
        this.callbackList.add(callback);
    }

    public void removeThreadInitCallback(ThreadInitCallback callback) {
        this.callbackList.remove(callback);
    }

    public Handler getHandler() {
        return this.handler;
    }

    @Override
    protected void onLooperPrepared() {
        handler = createDispatcher();
        fireHandleInitialized(handler);

        // Once initialized, we don't need it anymore
        callbackList.clear();
    }

    private void fireHandleInitialized(Handler handler) {
        List<ThreadInitCallback> localCallbackList = new LinkedList<>(callbackList);
        for(ThreadInitCallback callback : localCallbackList) {
            callback.onHandlerInitialized(handler);
        }
    }

    private Handler createDispatcher() {
        return new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT_CONNECT) {
                    doConnect(msg);
                }
                else if(msg.what == WHAT_DISCONNECT) {
                    doDisconnect(msg);
                }
                else if(msg.what == WHAT_SEND_POS) {
                    doSendPos(msg);
                }
                else if(msg.what == WHAT_STOP) {
                    doStop(msg);
                }
            }
        };
    }

    private void doConnect(Message msg) {
        if(oscClient != null) {
            oscClient.disconnect();
        }
        oscClient = new OscClient(connectionModel);
        oscClient.connect();
    }

    private void doSendPos(Message msg) {
        if(oscClient == null) {
            throw new IllegalStateException("Not connected yet");
        }

        Bundle bundle = msg.getData();
        int x = bundle.getInt("x");
        int y = bundle.getInt("y");

        oscClient.sendMessage(x,y);

    }

    private void doDisconnect(Message msg) {
        if(oscClient != null) {
            oscClient.disconnect();
            oscClient = null;
        }
    }

    private void doStop(Message msg) {
        if(oscClient != null) {
            oscClient.disconnect();
            oscClient = null;
        }
        quit();
    }
}
