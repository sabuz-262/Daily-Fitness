package com.wsu.dailyfitness;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.content.DialogInterface;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;

/**
 * a background service.
 */
public class SensorBackgroundService extends Service implements SensorEventListener {

    public static final String ACTION = "com.wsu.dailyfitness";

    /**
     *  we need the sensor manager and sensor reference
     */
    private SensorManager mSensorManager = null;
    /**
     * keep track of the previous value
     */
    public static int previousValue = 0;
    private int totalStep = 0;
    private float mAcheive = 1000;
    private  updateStatsTask mstatsTask = null;
    private DatabaseHelper database;
    private String mToday = "";
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

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

        //set alarm at 9AM and then repeat alarm every 1 hour
        setAlarm();
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

        // previousValue = sensors previos data, before the application starts for first time
        // current = todays' data
        int current = (int)event.values[0] - previousValue;
        totalStep = totalStep + current;

        //Feedback on achieving milestones (multiples of 1000 feet)
        if(totalStep*Constants.step_length == mAcheive){

            //next achievement
            mAcheive = mAcheive + Constants.multiples_acheive;
            Intent intent = new Intent(ACTION);
            // Put extras into the intent as usual
            intent.putExtra("resultCode", Activity.RESULT_OK);
            intent.putExtra("resultValue", " Congrates for achieving  " + totalStep + " distance.");
            // Fire the broadcast with intent packaged
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        if(mToday.isEmpty()){
            mToday = getDateTime();
        }else if(!mToday.equals(getDateTime())){
            // new day start, aggerate with the previos data
            previousValue = previousValue + current;
            mToday = getDateTime();
            return;
        }

        mstatsTask = new updateStatsTask(LoginActivity.mUsername,getDateTime(),  current);
        mstatsTask.execute((Void) null);

    }

    private void setAlarm(){

        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ReminderBroadcastReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        // Set the alarm to start at 9.00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 00);

        //repeat the alarm after evry 1 hour
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60, alarmIntent);
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
            /*
            if(success)
                Toast.makeText(getApplicationContext(), "Updated" + " " + msteps + "", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Faileld", Toast.LENGTH_SHORT).show();*/
        }

        @Override
        protected void onCancelled() {
        }
    }
}
