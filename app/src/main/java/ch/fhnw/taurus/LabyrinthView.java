package ch.fhnw.taurus;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class LabyrinthView extends AbstractLabyrinthView {
    private static final String LOG_TAG = LabyrinthView.class.getName();
    private LabyrinthDrawThread drawTask;
    private List<TouchEventListener> touchEventListenerList;
    private DrawStrategy drawStrategy;

    public LabyrinthView(Context context) {
        super(context);
        init();
    }

    public LabyrinthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabyrinthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void addTouchEventListener(TouchEventListener listener) {
        touchEventListenerList.add(listener);
    }

    public void removeTouchEventListener(TouchEventListener listener) {
        touchEventListenerList.remove(listener);
    }

    public DrawStrategy getDrawStrategy() {
        return drawStrategy;
    }

    public void setDrawStrategy(DrawStrategy drawStrategy) {
        this.drawStrategy = drawStrategy;
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(LOG_TAG,"surfaceCreated() called");
        if(drawTask != null) {
            stopCurrentTask();
            drawTask = null;
        }

        drawTask = new LabyrinthDrawThread(this,RADIUS, OUTER_RADIUS, drawStrategy);
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

    public float getMaxCursorRadius() {
        return OUTER_RADIUS;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(LOG_TAG,"onTouchEvent() called");
        Log.v(LOG_TAG, MessageFormat.format("Touched at  {0}/{1}", event.getX(), event.getY()));
        fireTouchEvent(event);

        return super.onTouchEvent(event);
    }

    private void fireTouchEvent(MotionEvent event) {
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

    private Labyrinth getLabyrinth() {
        return getApp().getLabyrinth();
    }

    private void stopCurrentTask() {
        if(drawTask != null && drawTask.isAlive()) {
            Handler handler = drawTask.getHandler();
            Message msg = handler.obtainMessage();
            msg.what = LabyrinthDrawThread.WHAT_STOP;
            msg.sendToTarget();
        }
    }

    private App getApp() {
        Context context = this.getContext().getApplicationContext();
        if(context instanceof  App) {
            return (App)context;
        }
        throw new IllegalStateException("Application is not of correct type");
    }
}
