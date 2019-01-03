package com.wsu.dailyfitness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.widget.Toast;
import android.content.Intent;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.app.PendingIntent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ReminderBroadcastReceiver extends  BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        DateFormat df = new SimpleDateFormat("EEE, HH");
        String date = df.format(Calendar.getInstance().getTime());

        if(isTimeToShowNotification(date)){
            Toast.makeText(context, "Please Walk.", Toast.LENGTH_LONG).show();
            //Did not work
            //showNotification(context, "Please Walk");
        }
    }

    public boolean isTimeToShowNotification(String date){
        String arr[] = {"Sat","Sun"};

        //tokens[0] will be day, token[1] will be hour
        String[] tokens = date.split("[ .,?!]+");
        int hour = Integer.parseInt(tokens[1]);

        // Not a Sat day, Sun day and time is greater than or equal 9AM to 5PM
        if(!tokens[0].equals(arr[0]) && !tokens[0].equals(arr[1]) && hour>=9 && hour <17)
            return true;

        return false;

    }
/*
    public void showNotification(Context context, String message) {
        // Set Notification Title
        String strtitle = context.getString(R.string.notificationtitle);
        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(context, NotificationView.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", message);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
                // Set Icon
                .setSmallIcon(R.drawable.android)
                // Set Ticker Message
                .setTicker(message)
                // Set Title
                .setContentTitle(context.getString(R.string.notificationtitle))
                // Set Text
                .setContentText(message)
                // Add an Action Button below Notification
                .addAction(R.drawable.android, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(1, builder.build());

    }
*/
}