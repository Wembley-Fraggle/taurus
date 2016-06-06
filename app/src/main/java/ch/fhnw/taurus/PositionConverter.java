package ch.fhnw.taurus;

import android.view.View;

/**
 * Created by nozdormu on 12/05/2016.
 */
public class PositionConverter {
    private View view;
    private Labyrinth labyrinth;
    private float cursorRadius;

    public PositionConverter(Labyrinth labyrinth, View view, float cursorRadius) {
        this.labyrinth = labyrinth;
        this.view = view;
        this.cursorRadius = cursorRadius;
    }

    // FIXME Pitch and Roll
    public int convertToXAngle(float displayXPos) {
        return convertToAngle(displayXPos,view.getWidth(),labyrinth.getMaxPitch());
    }

    public int convertToYAngle(float displayXPos) {
        return convertToAngle(displayXPos,view.getHeight(),labyrinth.getMaxRoll());
    }

    private int convertToAngle(float displayPos, float displayLen,int maxAngle) {
        float len = displayLen-2*cursorRadius;
        float pos = displayPos - 2*cursorRadius;
        pos = Math.max(0,pos);

        // The canvas size - the radius padding
        int angle = Math.round(pos/len*maxAngle);

        return Math.max(Math.min(angle,maxAngle),0);
    }
}