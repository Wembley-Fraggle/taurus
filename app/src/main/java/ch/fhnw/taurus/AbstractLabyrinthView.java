package ch.fhnw.taurus;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by nozdormu on 08/06/2016.
 */
public abstract class AbstractLabyrinthView extends SurfaceView implements SurfaceHolder.Callback{
    private static final String LOG_TAG = AbstractLabyrinthView.class.getName();
    private DrawStrategy drawStrategy;
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

    @Override
    protected void onAttachedToWindow() {
        Log.v(LOG_TAG,"onAttachedToWindow() called");
        getHolder().addCallback(this);
        super.onAttachedToWindow();
    }

}
