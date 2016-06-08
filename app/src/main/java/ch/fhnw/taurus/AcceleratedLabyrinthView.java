package ch.fhnw.taurus;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

/**
 * Created by nozdormu on 08/06/2016.
 */
public class AcceleratedLabyrinthView extends  AbstractLabyrinthView {

    private static final String LOG_TAG = AcceleratedLabyrinthView.class.getName();
    private static final int SAMPLING_PERIOD = 10;
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
        SensorManager manager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        manager.registerListener(sensorEventListener,sensor,SAMPLING_PERIOD);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        SensorManager manager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        manager.unregisterListener(sensorEventListener);
        sensorEventListener = null;
        super.surfaceDestroyed(holder);
    }
}
