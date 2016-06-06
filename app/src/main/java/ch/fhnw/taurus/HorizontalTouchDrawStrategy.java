package ch.fhnw.taurus;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * Created by nozdormu on 06/06/2016.
 */
public class HorizontalTouchDrawStrategy implements DrawStrategy {
    // FIXME Make it configurable
    private static final float INNER_CURSOR_RADIUS = 30;
    private static final float OUTER_CURSOR_RADIUS = INNER_CURSOR_RADIUS + 10;


    @Override
    public void drawBackground(Canvas canvas) {
        float height = canvas.getHeight();
        float width = canvas.getWidth();

        // canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5);

        float horizontalMid = (float)width/2.0f;
        float verticalMid = (float)height/2.0f;
        canvas.drawLine(0,verticalMid,width,verticalMid,paint);
        canvas.drawCircle( horizontalMid,verticalMid,OUTER_CURSOR_RADIUS+5,paint);


    }

    @Override
    public void drawCursor(Canvas canvas, float x, float y) {
        // canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);

        float height = canvas.getHeight();
        float verticalMid = (float)height/2.0f;

        canvas.drawCircle(x,verticalMid,INNER_CURSOR_RADIUS,paint);
        if(INNER_CURSOR_RADIUS != OUTER_CURSOR_RADIUS) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);

            canvas.drawCircle(x,verticalMid,OUTER_CURSOR_RADIUS,paint);
        }
    }
}
