package ch.fhnw.taurus;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class LabyrinthDrawTask extends AsyncTask<SurfaceHolder,Void,Void>{
    private final Labyrinth labyrinth;

    public LabyrinthDrawTask(Labyrinth labyrinth) {
        if(labyrinth == null) {
            throw new IllegalArgumentException("Invalid number of surface holders received");
        }
        this.labyrinth = labyrinth;
    }

    @Override
    protected Void doInBackground(@NonNull  SurfaceHolder... holders) {
        if(holders == null || holders.length < 1) {
            throw new IllegalArgumentException("Expected at least one Sufaceholder");
        }
        final SurfaceHolder surfaceHolder = holders[0];

        Canvas canvas = null;
        while (!isCancelled()) {
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    onDrawPosition(canvas,labyrinth);
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }

        return null;
    }

    private void onDrawPosition(Canvas canvas,Labyrinth labyrinth) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        float posX = labyrinth.getXAngle()/labyrinth.getMaxXDegree() * canvas.getWidth();
        float posY = labyrinth.getYAngle()/labyrinth.getMaxYDegree() * canvas.getHeight();
        float radius = 25;
        canvas.drawCircle(posX,posY,radius,paint);

        radius += 10;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawCircle(posX,posY,radius,paint);
    }

}

