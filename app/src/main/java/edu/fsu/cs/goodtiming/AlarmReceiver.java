package edu.fsu.cs.goodtiming;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {
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

        if(id == -1) {
            Log.d("Inside receiver", "id was not stored");
            return;
        }

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

        if(type.equals("early")) {
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle(cursor.getString(
                            cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_NAME)) +
                            " Starts Soon")
                    .setContentText("Event " + cursor.getString(
                            cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_NAME)) +
                            "starts in 15 minutes")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setOnlyAlertOnce(true);
            manager.notify(1, builder.build());
            Intent notification_intent = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            if(cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_IS_SESSION)).equals("yes")) {
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
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle(cursor.getString(
                            cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_NAME)) +
                            " Starts Right Now")
                    .setContentText("Event " + cursor.getString(
                            cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_NAME)) +
                            "starts now. Click to navigate to app.")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setOnlyAlertOnce(true);
            manager.notify(1, builder.build());
            Intent notification_intent = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            if(cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_IS_SESSION)).equals("yes")) {
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
        else {
            Log.d("Inside Receiver", "Type was not stored correctly");
            cursor.close();
            return;
        }
        cursor.close();
    }
}