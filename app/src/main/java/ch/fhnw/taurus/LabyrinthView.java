package ch.fhnw.taurus;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.MessageFormat;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class LabyrinthView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String LOG_TAG = LabyrinthView.class.getName();
    private AsyncTask drawTask;

    public LabyrinthView(Context context) {
        super(context);
    }

    public LabyrinthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabyrinthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
/*
     public LabyrinthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
*/
    @Override
    protected void onAttachedToWindow() {
        Log.v(LOG_TAG,"onAttachedToWindow() called");
        getHolder().addCallback(this);
        super.onAttachedToWindow();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(LOG_TAG,"surfaceCreated() called");
        if(drawTask != null) {
            drawTask.cancel(true);
        }
        drawTask = new LabyrinthDrawTask(getLabyrinth()).execute(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(LOG_TAG,"surfaceChanged() called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(LOG_TAG,"surfaceDestroyed() called");
        if(drawTask != null) {
            drawTask.cancel(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(LOG_TAG,"onTouchEvent() called");
        Log.v(LOG_TAG, MessageFormat.format("Touched at  {0}/{1}", event.getX(), event.getY()));

        Labyrinth labyrinth = getLabyrinth();
        labyrinth.setAngles(getXDAngle(event.getX()),getYAngle(event.getY()));

        return super.onTouchEvent(event);
    }

    private float getXDAngle(float value) {
        Labyrinth labyrinth = getLabyrinth();

        return value/getWidth()*labyrinth.getMaxXDegree();
    }

    private float getYAngle(float value) {
        Labyrinth labyrinth = getLabyrinth();
        return value/getHeight()*labyrinth.getMaxYDegree();
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
