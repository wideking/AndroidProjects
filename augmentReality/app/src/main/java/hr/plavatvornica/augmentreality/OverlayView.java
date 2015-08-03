package hr.plavatvornica.augmentreality;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

/**
 * Created by widek on 1.8.2015..
 * Class for drawing sensors data on top of camera preview.
 */
public class OverlayView extends View implements SensorEventListener {

    public static final String DEBUG_TAG = "OverlayView Log";
    String accelData = "Accelerometer Data";
    String compassData = "Compass Data";
    String gyroData = "Gyro Data";

    public OverlayView(Context context) {
        super(context);
        SensorManager sensors = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelSensor = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor compassSensor = sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor gyroSensor = sensors.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        boolean isAccelAvailable = sensors.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        boolean isCompassAvailable = sensors.registerListener(this, compassSensor, SensorManager.SENSOR_DELAY_NORMAL);
        boolean isGyroAvailable = sensors.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onDraw(Canvas canvas) {//draw data in canvas on Paint object
        super.onDraw(canvas);

        Paint contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentPaint.setTextAlign(Paint.Align.CENTER);
        contentPaint.setTextSize(20);
        contentPaint.setColor(Color.RED);
        canvas.drawText(accelData, canvas.getWidth() / 2, canvas.getHeight() / 4, contentPaint);
        canvas.drawText(compassData, canvas.getWidth() / 2, canvas.getHeight() / 2, contentPaint);
        canvas.drawText(gyroData, canvas.getWidth() / 2, (canvas.getHeight() * 3) / 4, contentPaint);
    }

    /**
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder msg = new StringBuilder(event.sensor.getName()).append(" ");//Creates text with sensor name and value.
        for (float value : event.values) {
            msg.append("[").append(value).append("]");
        }

        switch (event.sensor.getType())//set's text to appropriate sensor String.
        {
            case Sensor.TYPE_ACCELEROMETER:
                accelData = msg.toString();
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroData = msg.toString();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                compassData = msg.toString();
                break;
        }

        this.invalidate();

    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     * <p/>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
