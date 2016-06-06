package ch.fhnw.taurus;

import android.graphics.Canvas;

/**
 * Created by nozdormu on 06/06/2016.
 */
public interface DrawStrategy {
    void drawBackground(Canvas canvas);
    void drawCursor(Canvas canvas, float pitch, float rotation);
}
