package ch.fhnw.taurus;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class TouchLabyrinthView extends AbstractLabyrinthView {
    private static final String LOG_TAG = TouchLabyrinthView.class.getName();
    private GestureDetector gestureDetector;

    public TouchLabyrinthView(Context context) {
        super(context);
    }

    public TouchLabyrinthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchLabyrinthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // ... Respond to touch events
                Log.v(LOG_TAG,"onTouch() called");
                fireTouchEvent(event);
                return true;
            }
        });
    }
}
