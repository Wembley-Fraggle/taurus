package ch.fhnw.taurus;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * Created by nozdormu on 06/06/2016.
 */
public class AccelerometerDrawStrategy implements DrawStrategy {

    // FIXME Make it configurable
    private static final float INNER_CURSOR_RADIUS = 30;
    private static final float OUTER_CURSOR_RADIUS = INNER_CURSOR_RADIUS + 10;


    @Override
    public void drawCursor(Canvas canvas, float pitch, float roll) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);

        canvas.drawCircle(pitch, roll, INNER_CURSOR_RADIUS, paint);

        if (INNER_CURSOR_RADIUS != OUTER_CURSOR_RADIUS) {

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            canvas.drawCircle(pitch, roll, OUTER_CURSOR_RADIUS, paint);
        }
    }

    @Override
    public void drawBackground(Canvas canvas) {
        float height = canvas.getHeight();
        float width = canvas.getWidth();

        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5);

        float horizontalMid = width/2.0f;
        float verticalMid = height/2.0f;
        canvas.drawCircle( horizontalMid,verticalMid,OUTER_CURSOR_RADIUS+5,paint);

    }
}
