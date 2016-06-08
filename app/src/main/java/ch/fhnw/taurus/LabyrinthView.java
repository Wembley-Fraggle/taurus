package ch.fhnw.taurus;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.text.MessageFormat;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class LabyrinthView extends AbstractLabyrinthView {
    private static final String LOG_TAG = LabyrinthView.class.getName();

    public LabyrinthView(Context context) {
        super(context);
    }

    public LabyrinthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabyrinthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(LOG_TAG,"onTouchEvent() called");
        Log.v(LOG_TAG, MessageFormat.format("Touched at  {0}/{1}", event.getX(), event.getY()));
        fireTouchEvent(event);

        return super.onTouchEvent(event);
    }

}
