package ch.fhnw.taurus;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * Created by nozdormu on 06/06/2016.
 */
public class VerticalTouchDrawStrategy implements  DrawStrategy{

    // FIXME Make it configurable
    private static final float INNER_CURSOR_RADIUS = 30;
    private static final float OUTER_CURSOR_RADIUS = INNER_CURSOR_RADIUS + 10;


    @Override
    public void drawBackground(Canvas canvas) {
        float height = canvas.getHeight();
        float width = canvas.getWidth();

        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5);

        float horizontalMid = (float)width/2.0f;
        float verticalMid = (float)height/2.0f;
        canvas.drawLine(horizontalMid,0,horizontalMid,height,paint);
        canvas.drawCircle( horizontalMid,verticalMid,OUTER_CURSOR_RADIUS+5,paint);


    }

    @Override
    public void drawCursor(Canvas canvas, float x, float y) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);

        float width = canvas.getWidth();
        float horizontalMid = (float)width/2.0f;

        canvas.drawCircle(horizontalMid,y,INNER_CURSOR_RADIUS,paint);

        if(INNER_CURSOR_RADIUS != OUTER_CURSOR_RADIUS) {

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            canvas.drawCircle(horizontalMid,y,OUTER_CURSOR_RADIUS,paint);
        }
    }
}
