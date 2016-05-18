package ch.fhnw.taurus;

import java.text.MessageFormat;
import java.util.Observable;

/**
 * Created by nozdormu on 03/05/2016.
 */
public final class Labyrinth extends Observable {
    private static final String LOG_TAG = Labyrinth.class.getName();
    private Object lock = new Object();
    private int maxXDegree;
    private int maxYDegree;
    private int xAngle;
    private int yAngle;

    public Labyrinth(int maxXDegree, int maxYDegree) {
        this.maxXDegree = maxXDegree;
        this.maxYDegree = maxYDegree;
        xAngle = maxXDegree / 2;
        yAngle = maxYDegree / 2;
    }

    public  void setAngles(int x, int y) {
        synchronized (lock) {
            if (x < 0) {
                throw new IllegalArgumentException("x-Angle must be positive");
            }
            if (y < 0) {
                throw new IllegalArgumentException("y-Angle must be positive");
            }

            if (x > maxXDegree) {
                throw new IllegalStateException(MessageFormat.format("Max allowed x-angle: {0}", maxXDegree));
            }

            if (y > maxYDegree) {
                throw new IllegalStateException(MessageFormat.format("Max allowed y-angle: {0}", maxYDegree));
            }
            boolean positionChanged = this.xAngle != x || this.yAngle != y;
            this.xAngle = x;
            this.yAngle = y;
            if (positionChanged) {
                setChanged();
                notifyObservers();
            }
        }
    }

    public int getXAngle() {
        synchronized (lock) {
            return xAngle;
        }
    }

    public int getYAngle() {
        synchronized (lock) {
            return yAngle;
        }
    }

    public int getMaxXDegree() {
        synchronized (lock) {
            return maxXDegree;
        }
    }

    public int getMaxYDegree() {
        synchronized (lock) {
            return maxYDegree;
        }
    }
}
