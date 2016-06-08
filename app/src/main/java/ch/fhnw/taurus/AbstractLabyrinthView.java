package ch.fhnw.taurus;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nozdormu on 08/06/2016.
 */
public abstract class AbstractLabyrinthView extends SurfaceView implements SurfaceHolder.Callback{
    private static final String LOG_TAG = AbstractLabyrinthView.class.getName();
    private LabyrinthDrawThread drawTask;
    private DrawStrategy drawStrategy;
    private List<PositionChangedListener> angleChangedListeners;
    private Handler drawHandler;
    protected static final float RADIUS=30;
    protected static final float OUTER_RADIUS = RADIUS + 10;


    public AbstractLabyrinthView(Context context) {
        super(context);
        init();
    }

    public AbstractLabyrinthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbstractLabyrinthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DrawStrategy getDrawStrategy() {
        return drawStrategy;
    }

    public void setDrawStrategy(DrawStrategy drawStrategy) {
        this.drawStrategy = drawStrategy;
    }

    public float getMaxCursorRadius() {
        return OUTER_RADIUS;
    }

    public void addTouchEventListener(PositionChangedListener listener) {
        angleChangedListeners.add(listener);
    }

    public void removeTouchEventListener(PositionChangedListener listener) {
        angleChangedListeners.remove(listener);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(LOG_TAG,"surfaceCreated() called");
        if(drawTask != null) {
            stopCurrentTask();
            drawTask = null;
        }

        drawTask = new LabyrinthDrawThread(this, RADIUS, OUTER_RADIUS, getDrawStrategy(), new ThreadInitCallback() {
            @Override
            public void onHandlerInitialized(Handler handler) {
                drawHandler = handler;
            }
        });
        drawTask.start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(LOG_TAG,"surfaceChanged() called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(LOG_TAG,"surfaceDestroyed() called");
        if(drawTask != null) {
            stopCurrentTask();
            drawTask = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        Log.v(LOG_TAG,"onAttachedToWindow() called");
        getHolder().addCallback(this);
        super.onAttachedToWindow();
    }

    protected void firePositionChanged(float x, float y) {
        List<PositionChangedListener> localListeners = new LinkedList<>(angleChangedListeners);
        for(PositionChangedListener listener : localListeners) {
            listener.onPositionChanged(x,y);
        }
    }


    protected void sendPosToDrawTask(float x, float y) {
        if(drawTask != null && drawTask.isAlive() && drawHandler != null) {
            Message msg = drawHandler.obtainMessage();
            msg.what = LabyrinthDrawThread.WHAT_SET_POS;
            Bundle bundle = msg.getData();
            bundle.putFloat("x",x);
            bundle.putFloat("y",y);
            msg.sendToTarget();
        }
    }

    private void stopCurrentTask() {
        if(drawTask != null && drawTask.isAlive() && drawHandler != null) {
            Message msg = drawHandler.obtainMessage();
            msg.what = LabyrinthDrawThread.WHAT_STOP;
            msg.sendToTarget();
        }
    }

    private void init() {
        angleChangedListeners = new LinkedList<>();
    }


    protected Labyrinth getLabyrinth() {
        return getApp().getLabyrinth();
    }

    private App getApp() {
        Context context = this.getContext().getApplicationContext();
        if(context instanceof  App) {
            return (App)context;
        }
        throw new IllegalStateException("Application is not of correct type");
    }

}
