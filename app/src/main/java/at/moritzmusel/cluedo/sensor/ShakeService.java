package at.moritzmusel.cluedo.sensor;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.app.Service;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ShakeService extends Service implements SensorEventListener {
    private float accel; //acceleration ignoring gravity
    private float accelGrav; //acceleration including gravity

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float accelGravLast = accelGrav; //assigning current acceleration to latest acceleration
        accelGrav = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = accelGrav - accelGravLast;
        accel = accel * 0.9f + delta; // perform low-cut filter
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
