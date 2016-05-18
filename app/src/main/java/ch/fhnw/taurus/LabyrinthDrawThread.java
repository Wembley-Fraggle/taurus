package ch.fhnw.taurus;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private Handler handler;

    public static final int WHAT_STOP = 0;
    public static final int WHAT_SET_POS = 1;

    // FIXME Wait until initialized, use the callback

    public LabyrinthDrawThread(SurfaceView view, float innerRadius, float outerRadius) {
        super(LabyrinthDrawThread.class.getSimpleName(), Process.THREAD_PRIORITY_BACKGROUND);
        if(view == null) {
            throw new IllegalArgumentException("View must not be null");
        }
        this.view = view;
        this.innerRadius= innerRadius;
        this.outerRadius=  Math.max(innerRadius,outerRadius);
    }

    @Override
    protected void onLooperPrepared() {
        handler = createDispatcher();
    }

    public Handler getHandler() {
        return handler;
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
        float posX =  bundle.getFloat("x");
        float posY = bundle.getFloat("y");

        drawCursor(posX,posY);


    }

    private void drawCursor(float x, float y) {

        final SurfaceHolder surfaceHolder = view.getHolder();
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {
                if(canvas != null) {
                    drawCursor(x,y,canvas);
                }
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawCursor(float posX, float posY,Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);

        canvas.drawCircle(posX,posY,innerRadius,paint);

        if(innerRadius != outerRadius) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            canvas.drawCircle(posX,posY,outerRadius,paint);
        }
    }


}

