package ch.fhnw.taurus;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

/**
 * Created by nozdormu on 12/05/2016.
 */
public class OscThread extends HandlerThread {

    public static final int WHAT_SEND_POS = 1;
    public static final int WHAT_STOP = 2;

    private OscClient oscClient;
    private Handler handler;
    private ThreadInitCallback threadInitCallback;

    public OscThread(OscClient oscClient, ThreadInitCallback callback) {
        super(OscThread.class.getSimpleName(), Process.THREAD_PRIORITY_BACKGROUND);
        this.threadInitCallback = callback;
        this.oscClient = oscClient;
    }

    @Override
    protected void onLooperPrepared() {
        handler = createDispatcher();

        if(threadInitCallback != null) {
            threadInitCallback.onHandlerInitialized(handler);
        }
    }

    private Handler createDispatcher() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == WHAT_SEND_POS) {
                    doSendPos(msg);
                }
                else if(msg.what == WHAT_STOP) {
                    doStop(msg);
                }
            }
        };
    }


    private void doSendPos(Message msg) {
        if(oscClient == null) {
            throw new IllegalStateException("Not connected yet");
        }

        Bundle bundle = msg.getData();
        int pitch = bundle.getInt("pitch");
        int roll = bundle.getInt("roll");

        oscClient.sendMessage(roll,pitch);

    }

    private void doStop(Message msg) {
        if(oscClient != null) {
            oscClient.disconnect();
            oscClient = null;
        }
        quit();
    }
}
