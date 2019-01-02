package com.wsu.dailyfitness;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * a background service.
 */
public class SensorBackgroundService extends Service implements SensorEventListener {

    /**
     * a tag for logging
     */
    private static final String TAG = SensorBackgroundService.class.getSimpleName();

    /**
     *  we need the sensor manager and sensor reference
     */
    private SensorManager mSensorManager = null;


    /**
     * keep track of the previous value
     */
    public static int previousValue = 0;
    private  updateStatsTask mstatsTask = null;
    private DatabaseHelper database;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get sensor manager on starting the service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        database = new DatabaseHelper(this);

        // we need the step counter sensor
        int sensorType = Sensor.TYPE_STEP_COUNTER;

        // we need the step counter sensor
        Sensor sensor = mSensorManager.getDefaultSensor(sensorType);

        mSensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_UI);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // do nothing
        return null;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(previousValue == 0){
            previousValue = (int)event.values[0];
            return;
        }

        int current = (int)event.values[0] - previousValue;
        Log.d(TAG, "received sensor valures are: " + String.valueOf(event.values[0]));

        mstatsTask = new updateStatsTask(LoginActivity.mUsername,getDateTime(),  current);
        mstatsTask.execute((Void) null);


    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM-dd-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public class updateStatsTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mDate;
        private final int msteps;

        updateStatsTask(String username, String date, int steps) {
            mUsername = username;
            mDate = date;
            msteps = steps;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // insert  steps

            try {
                return  database.insertDailyStats(mUsername, mDate, msteps);

            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mstatsTask = null;
            if(success)
                Toast.makeText(getApplicationContext(), "Updated" + " " + msteps + "", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Faileld", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onCancelled() {
        }
    }


}
