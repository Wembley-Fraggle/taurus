package ch.fhnw.taurus;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class LabyrinthDrawThread extends HandlerThread{
    private SurfaceView view;
    private boolean isCancelded;
    private final float innerRadius;
    private final float outerRadius;
    private final DrawStrategy drawStrategy;
    private Handler handler;
    private ThreadInitCallback threadInitCallback;

    public static final int WHAT_STOP = 0;
    public static final int WHAT_SET_POS = 1;

    public LabyrinthDrawThread(SurfaceView view, float innerRadius, float outerRadius, DrawStrategy drawStrategy, ThreadInitCallback callback) {
        super(LabyrinthDrawThread.class.getSimpleName(), Process.THREAD_PRIORITY_BACKGROUND);
        if(view == null) {
            throw new IllegalArgumentException("View must not be null");
        }
        this.view = view;
        this.innerRadius= innerRadius;
        this.outerRadius=  Math.max(innerRadius,outerRadius);
        this.drawStrategy = drawStrategy;
        this.threadInitCallback = callback;
    }

    @Override
    protected void onLooperPrepared() {
        handler = createDispatcher();
        threadInitCallback.onHandlerInitialized(handler);
        draw(new DrawCallback() {

            @Override
            public void draw(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                drawStrategy.drawBackground(canvas);
            }
        });
    }

    private Handler createDispatcher() {
        return new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT_STOP) {
                    doStop(msg);
                }
                else if(msg.what == WHAT_SET_POS) {
                    doSetPos(msg);
                }
            }
        };
    }


    private void doStop(Message msg) {
        quit();
    }

    private void doSetPos(Message msg) {
        Bundle bundle = msg.getData();
        final float posX =  bundle.getFloat("x");
        final float posY = bundle.getFloat("y");

        draw(new DrawCallback() {

            @Override
            public void draw(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                drawStrategy.drawBackground(canvas);
                drawStrategy.drawCursor(canvas,posX,posY);
            }
        });

    }

    interface DrawCallback {
        void draw(Canvas canvas);
    }

    private void draw(DrawCallback drawCallback) {

        final SurfaceHolder surfaceHolder = view.getHolder();
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {
                if(canvas != null && drawStrategy != null) {
                    drawCallback.draw(canvas);
                }
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}

