package ch.fhnw.taurus;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nozdormu on 03/05/2016.
 */
public final class Labyrinth {
    private static final String LOG_TAG = Labyrinth.class.getName();
    private Object lock = new Object();
    private float maxXDegree;
    private float maxYDegree;
    private float xAngle;
    private float yAngle;
    private List<AngleChangedListener> angleChangedListeners;

    public Labyrinth(float maxXDegree, float maxYDegree) {
        this.maxXDegree = maxXDegree;
        this.maxYDegree = maxYDegree;
        xAngle = maxXDegree / 2;
        yAngle = maxYDegree / 2;
        angleChangedListeners = new LinkedList<>();
    }

    public  void setAngles(float x, float y) {
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
                fireAngleChanged();
            }
        }
    }

    public float getXAngle() {
        synchronized (lock) {
            return xAngle;
        }
    }

    public float getYAngle() {
        synchronized (lock) {
            return yAngle;
        }
    }

    public float getMaxXDegree() {
        synchronized (lock) {
            return maxXDegree;
        }
    }

    public synchronized float getMaxYDegree() {
        synchronized (lock) {
            return maxYDegree;
        }
    }

    private void fireAngleChanged() {
        List<AngleChangedListener> localList;
        synchronized (lock) {
            localList = new LinkedList<>(this.angleChangedListeners);
        }
        for (AngleChangedListener listener:localList) {
            try {
                listener.onAngleChanged(this.xAngle,this.yAngle);
            }
            catch(Exception ex) {
                synchronized (lock) {
                    this.angleChangedListeners.remove(listener);
                }
            }
        }

    }
}
