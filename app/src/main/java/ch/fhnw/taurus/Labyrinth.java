package ch.fhnw.taurus;

import java.text.MessageFormat;
import java.util.Observable;

/**
 * Created by nozdormu on 03/05/2016.
 */
public final class Labyrinth extends Observable {
    private static final String LOG_TAG = Labyrinth.class.getName();
    private Object lock = new Object();
    private int maxPitch;
    private int maxRoll;
    private int pitch;
    private int roll;

    public Labyrinth(int maxPitch, int maxRoll) {
        this.maxPitch = maxPitch;
        this.maxRoll = maxRoll;
        pitch = maxRoll / 2;
        roll = maxRoll / 2;
    }

    public  void setAngles(int pitch, int roll) {
        synchronized (lock) {
            validatePitch(pitch);
            validateRoll(roll);

            boolean positionChanged = this.pitch != pitch || this.roll != roll;

            this.pitch= pitch;
            this.roll = roll;
            if (positionChanged) {
                setChanged();
                notifyObservers();
            }
        }
    }

    public void setPitch(int pitch) {
        synchronized (lock) {
            validatePitch(pitch);
            if(this.pitch != pitch) {
                this.pitch = pitch;
                setChanged();
                notifyObservers();
            }
        }
    }

    public void setRoll(int roll) {
        synchronized (lock) {
            validateRoll(roll);
            if(this.roll != roll) {
                this.roll = roll;
                setChanged();
                notifyObservers();
            }
        }
    }

    public int getPitch() {
        synchronized (lock) {
            return roll;
        }
    }

    public int getRoll() {
        synchronized (lock) {
            return pitch;
        }
    }

    public int getMaxPitch() {
        synchronized (lock) {
            return maxPitch;
        }
    }

    public int getMaxRoll() {
        synchronized (lock) {
            return maxRoll;
        }
    }

    private void validatePitch(int pitch) {
        if (pitch < 0) {
            throw new IllegalArgumentException("pitch angle must be positive");
        }
        if(pitch > maxPitch) {
            throw new IllegalStateException(MessageFormat.format("Max allowed pitch-angle: {0}", maxPitch));
        }
    }

    private void validateRoll(int roll) {
        if (roll < 0) {
            throw new IllegalArgumentException("roll angle must be positive");
        }
        if(roll > maxRoll) {
            throw new IllegalStateException(MessageFormat.format("Max allowed roll-angle: {0}", maxRoll));
        }
    }


}
