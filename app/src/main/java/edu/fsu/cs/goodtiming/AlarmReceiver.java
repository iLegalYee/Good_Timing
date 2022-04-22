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

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Inside receiver", "Just got a broadcast");
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

        String title = cursor.getString(
                cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_NAME));
        String session = cursor.getString(
                cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_IS_SESSION));
        if (title == null || title.equals(""))
            title = "No Title";
        if(session == null)
            session = "";

        if(type.equals("early")) {
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
            Log.d("Inside receiver", "Inside ontime");

//            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                    new Intent(context, MainActivity.class), 0);
//            Log.d("Inside Receiver", "after get activity");
//            NotificationCompat.Builder mBuilder =
//                    new NotificationCompat.Builder(context)
//                            .setSmallIcon(R.drawable.leftarrow)
//                            .setContentTitle("My notification")
//                            .setContentText("Hello World!");
//            mBuilder.setContentIntent(contentIntent);
//            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//            mBuilder.setAutoCancel(true);
//            Log.d("Inside Receiver", "made the builder");
//            NotificationManager mNotificationManager =
//                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            Log.d("Inside receiver", "about to send notification");
//            mNotificationManager.notify(1, mBuilder.build());
//            Log.d("Inside receiver", "sent notification");

            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle(title + " Starts Right Now")
                    .setContentText("Event " + title + "starts now. Click to navigate to app.")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setOnlyAlertOnce(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }

            //manager.notify(1, builder.build());
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