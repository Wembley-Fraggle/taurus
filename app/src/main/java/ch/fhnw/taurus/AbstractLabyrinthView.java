package ch.fhnw.taurus;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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
    private List<TouchEventListener> touchEventListenerList;
    protected static final float RADIUS=30;
    protected static final float OUTER_RADIUS = RADIUS + 10;


    public AbstractLabyrinthView(Context context) {
        super(context);
    }

    public AbstractLabyrinthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractLabyrinthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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


    public void addTouchEventListener(TouchEventListener listener) {
        touchEventListenerList.add(listener);
    }

    public void removeTouchEventListener(TouchEventListener listener) {
        touchEventListenerList.remove(listener);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(LOG_TAG,"surfaceCreated() called");
        if(drawTask != null) {
            stopCurrentTask();
            drawTask = null;
        }

        drawTask = new LabyrinthDrawThread(this,RADIUS, OUTER_RADIUS, getDrawStrategy());
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

    protected void fireTouchEvent(MotionEvent event) {
        List<TouchEventListener> localListeners = new LinkedList<>(touchEventListenerList);
        for(TouchEventListener listener : localListeners) {

            // Ensure that the cursor is always displayed within the draw berders
            float x = Math.max(OUTER_RADIUS,Math.min(getWidth()-OUTER_RADIUS,event.getX()));
            float y = Math.max(OUTER_RADIUS,Math.min(getHeight()-OUTER_RADIUS,event.getY()));
            if(x != event.getX() || y != event.getY()) {
                event.setLocation(x,y);
            }

            listener.onTouchEvent(event);
        }
    }


    private void sendPosToDrawTask(float x, float y) {
        if(drawTask != null && drawTask.isAlive()) {
            Handler handler = drawTask.getHandler();
            Message msg = handler.obtainMessage();
            msg.what = LabyrinthDrawThread.WHAT_SET_POS;
            Bundle bundle = msg.getData();
            bundle.putFloat("x",x);
            bundle.putFloat("y",y);
            msg.sendToTarget();
        }
    }

    private void stopCurrentTask() {
        if(drawTask != null && drawTask.isAlive()) {
            Handler handler = drawTask.getHandler();
            Message msg = handler.obtainMessage();
            msg.what = LabyrinthDrawThread.WHAT_STOP;
            msg.sendToTarget();
        }
    }

    private void init() {
        touchEventListenerList = new LinkedList<>();
        touchEventListenerList.add(new TouchEventListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                // Draw Positions in another thread
                sendPosToDrawTask(event.getX(),event.getY());
            }
        });
    }


    private Labyrinth getLabyrinth() {
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
