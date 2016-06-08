package ch.fhnw.taurus;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by nozdormu on 08/06/2016.
 */
public abstract class AbstractLabyrinthView extends SurfaceView implements SurfaceHolder.Callback{
    private static final String LOG_TAG = AbstractLabyrinthView.class.getName();
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
    protected void onAttachedToWindow() {
        Log.v(LOG_TAG,"onAttachedToWindow() called");
        getHolder().addCallback(this);
        super.onAttachedToWindow();
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
