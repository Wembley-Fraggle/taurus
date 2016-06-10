package ch.fhnw.taurus;

import android.view.View;

/**
 * Created by nozdormu on 12/05/2016.
 */
public class TouchAngleConverter {
    private View view;
    private Labyrinth labyrinth;
    private float cursorRadius;

    public TouchAngleConverter(Labyrinth labyrinth, View view, float cursorRadius) {
        this.labyrinth = labyrinth;
        this.view = view;
        this.cursorRadius = cursorRadius;
    }

    public int convertToXAngle(float displayXPos) {
        return convertToAngle(displayXPos,view.getWidth(),labyrinth.getMaxRoll());
    }

    public int convertToYAngle(float displayXPos) {
        return convertToAngle(displayXPos,view.getHeight(),labyrinth.getMaxPitch());
    }

    private int convertToAngle(float displayPos, float displayLen,int maxAngle) {
        float len = displayLen-2*cursorRadius;
        float pos = displayPos - cursorRadius;
        pos = Math.max(0,pos);

        // The canvas size - the radius padding
        int angle = Math.round(pos/len*maxAngle);

        return Math.max(Math.min(angle,maxAngle),0);
    }
}