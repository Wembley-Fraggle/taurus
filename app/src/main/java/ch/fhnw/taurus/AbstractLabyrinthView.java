package ch.fhnw.taurus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by nozdormu on 08/06/2016.
 */
public abstract class AbstractLabyrinthView extends SurfaceView implements SurfaceHolder.Callback{

    public AbstractLabyrinthView(Context context) {
        super(context);
    }

    public AbstractLabyrinthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractLabyrinthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
