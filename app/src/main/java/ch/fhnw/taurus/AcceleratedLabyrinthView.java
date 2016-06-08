package ch.fhnw.taurus;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import java.text.MessageFormat;

/**
 * Created by nozdormu on 08/06/2016.
 */
public class AcceleratedLabyrinthView extends  AbstractLabyrinthView {

    private static final float G = 9.81f;
    private static final String LOG_TAG = AcceleratedLabyrinthView.class.getName();
    private static final int SAMPLING_PERIOD = 20;
    private SensorEventListener sensorEventListener;


    public AcceleratedLabyrinthView(Context context) {
        super(context);
    }

    public AcceleratedLabyrinthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AcceleratedLabyrinthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        super.surfaceCreated(holder);
        SensorManager manager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                float gX = normalizeGravity(-event.values[0]);
                float gY = normalizeGravity(event.values[1]);

                float x = convertGravityXToPosition(gX);
                float y = convertGravityYToPosition(gY);

                sendPosToDrawTask(x,y);
                firePositionChanged(x,y);

                Log.v(LOG_TAG, MessageFormat.format("onSensorChanged() called [{0}/{1}]", x,y));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // ignore it
            }
        };

        manager.registerListener(sensorEventListener, sensor,SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        SensorManager manager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        manager.unregisterListener(sensorEventListener);
        sensorEventListener = null;
        super.surfaceDestroyed(holder);
    }

    private float normalizeGravity(float g) {
        if(g < 0) {
            return Math.max(-G, g);
        }
        else {
            return Math.min(G, g);
        }
    }

    private float convertGravityXToPosition(float g) {
        return convertGravityToPosition(g, getWidth());
    }

    private float convertGravityYToPosition(float g) {
        return convertGravityToPosition(g, getHeight());
    }

    private float convertGravityToPosition(float g,float displaySize ) {
        return (g+G)/(2*G)*(displaySize-2*OUTER_RADIUS)+OUTER_RADIUS;
    }
}

