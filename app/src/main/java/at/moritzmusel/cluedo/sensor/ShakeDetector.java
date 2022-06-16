package at.moritzmusel.cluedo.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Class for the shake interaction.
 */
public class ShakeDetector implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.5F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener onShakeListener;
    private long shakeTimestamp;
    private int shakeCount;

    public void setOnShakeListener(OnShakeListener listener) {
        this.onShakeListener = listener;
    }

    public interface OnShakeListener {
        void onShake(int count);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * method for the shake event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (onShakeListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;
            float gForce = (float)Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {//only detects shake event if it's a few seconds after a previous shake event

                    return;
                }

                if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {//shake count is reset after a few seconds
                    shakeCount = 0;
                }

                shakeTimestamp = now;
                shakeCount++;

                onShakeListener.onShake(shakeCount);
            }
        }
    }


}