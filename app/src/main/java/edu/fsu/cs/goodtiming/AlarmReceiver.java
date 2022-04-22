package edu.fsu.cs.goodtiming;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

// This is meant to send out a notification whenever a broadcast is caught by it
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Inside receiver", "Just got a broadcast");
        // Reschedules all the notifications when the phone reboots
        if(intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            String[] projection = new String[] {
                    MyContentProvider.COLUMN_EVENTS_ID,
                    MyContentProvider.COLUMN_EVENTS_TIME
            };
            Cursor cursor = context.getContentResolver().query(MyContentProvider.EVENTS_CONTENT_URI,
                    projection, null, null, null);
            while(cursor.moveToNext()) {
                String timeString = cursor.getString(
                        cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_TIME));
                if (timeString == null || timeString.equals("")) {
                    continue;
                }
                long time = Integer.parseInt(timeString);
                MainActivity main = new MainActivity();
                main.SetTimedNotification(cursor.getInt(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_ID)));
            }

            cursor.close();
            return;
        }

        int id = intent.getIntExtra("id", -1);
        String type = intent.getStringExtra("type");
        Log.d("Inside receiver", "type is: " + type);

        if(id == -1) {
            Log.d("Inside receiver", "id was not stored");
            return;
        }

        // Find the specified event
        String[] projection = new String[] {
                MyContentProvider.COLUMN_EVENTS_ID,
                MyContentProvider.COLUMN_EVENTS_NAME,
                MyContentProvider.COLUMN_EVENTS_DESCRIPTION,
                MyContentProvider.COLUMN_EVENTS_TIME,
                MyContentProvider.COLUMN_EVENTS_DATE,
                MyContentProvider.COLUMN_EVENTS_REPEAT,
                MyContentProvider.COLUMN_EVENTS_LOCATION,
                MyContentProvider.COLUMN_EVENTS_DURATION,
                MyContentProvider.COLUMN_EVENTS_IS_SESSION};

        String selection = "( " + MyContentProvider.COLUMN_EVENTS_ID + " == " + id + " )";
        Cursor cursor = context.getContentResolver().query(MyContentProvider.EVENTS_CONTENT_URI,
                projection, selection, null, null);
        if(!cursor.moveToNext()) {
            Log.d("Inside receiver", "id not found in table");
            return;
        }

        // Make the notification using the information from the query
        String title = cursor.getString(
                cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_NAME));
        String session = cursor.getString(
                cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_IS_SESSION));
        if (title == null || title.equals(""))
            title = "No Title";
        if(session == null)
            session = "";

        if(type.equals("early")) {
            // Enters here to send a notification 15 minutes before event
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle(title + " Starts Soon")
                    .setContentText("Event " + title + "starts in 15 minutes")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setOnlyAlertOnce(true);
            //manager.notify(1, builder.build());
            Intent notification_intent = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            if(session.equals("yes")) {
                bundle.putString("session", "yes");
                bundle.putInt("id", id);
                notification_intent.putExtras(bundle);
                PendingIntent pend = PendingIntent.getActivity(context, 0, notification_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pend);
                manager.notify(2, builder.build());
            }
            else {
                bundle.putString("session", "no");
                bundle.putInt("id", id);
                PendingIntent pend = PendingIntent.getActivity(context, 0, notification_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pend);
                manager.notify(2, builder.build());
            }
        }

        else if(type.equals("ontime")) {
            // Enters here to send a notification at the time of the event
            Log.d("Inside receiver", "Inside ontime");

            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle(title + " Starts Right Now")
                    .setContentText("Event " + title + "starts now. Click to navigate to app.")
                    .setSmallIcon(R.drawable.clock_icon)
                    .setWhen(System.currentTimeMillis())
                    .setOnlyAlertOnce(true);

            Intent notification_intent = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            Log.d("Inside receiver", "Middle of ontime");
            if(session.equals("1")) {
                bundle.putString("session", "yes");
                bundle.putInt("id", id);
                notification_intent.putExtras(bundle);
                PendingIntent pend = PendingIntent.getActivity(context, 1, notification_intent, PendingIntent.FLAG_IMMUTABLE);
                builder.setContentIntent(pend);
                manager.notify(1, builder.build());
                Log.d("Inside recevier", "after if notify()");
            }
            else {
                bundle.putString("session", "no");
                bundle.putInt("id", id);
                notification_intent.putExtras(bundle);
                PendingIntent pend = PendingIntent.getActivity(context, 1, notification_intent, 0);
                builder.setContentIntent(pend);
                manager.notify(1, builder.build());
                Log.d("Inside Receiver", "After else notify()");
            }
            Log.d("Inside receiver", "sent notification");
        }
        else {
            Log.d("Inside Receiver", "Type was not stored correctly");
            cursor.close();
            return;
        }
        Log.d("Inside Receiver", "At end of onreceive");
        cursor.close();
    }
}