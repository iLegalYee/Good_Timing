package edu.fsu.cs.goodtiming;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

import edu.fsu.cs.goodtiming.Calendar.CalendarFragment;
import edu.fsu.cs.goodtiming.Timer.SessionFragment;
import edu.fsu.cs.goodtiming.User.UserFragment;
import edu.fsu.cs.goodtiming.Utils.NewEventFragment;
import edu.fsu.cs.goodtiming.Utils.Todomain;

// TODO: Makes sure to reset all notifications scheduled when booting up phone
public class MainActivity extends AppCompatActivity implements
        EventFragment.OnEventFragmentInteractionListener,
        SessionFragment.OnSessionFragmentInteractionListener,
        CalendarFragment.OnCalendarFragmentInteractionListener,
        UserFragment.OnUserFragmentInteractionListener,
        NewEventFragment.OnNewEventFragmentInteractionListener
{
    FragmentManager fManager;
    NewEventFragment eventFragment = null;
    SessionFragment sessionFragment = null;
    CalendarFragment calendarFragment = null;
    UserFragment userFragment = null;

    Button event;
    Button session;
    Button calendar;
    Button user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.Light_Theme);
        setContentView(R.layout.activity_main);


        RequestPermission(Manifest.permission.READ_CALENDAR);
        RequestPermission(Manifest.permission.WRITE_CALENDAR);
        RequestPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
        fManager = getSupportFragmentManager();
        ShowEventFragment(null);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null && bundle.containsKey("show")) {
            String tempString = bundle.getString("show");
            if(tempString.equals("event")) ShowEventFragment(null);
            else if(tempString.equals("session")) ShowSessionFragment(null);
            else if(tempString.equals("calendar")) ShowCalendarFragment(null);
            else if(tempString.equals("user")) ShowUserFragment(null);
        }
        else {
            Intent intent = new Intent(this, Todomain.class);
            startActivity(intent);
        }

        // Enters this statement when a notification is clicked to enter the app
        if(bundle != null && bundle.containsKey("session") && bundle.containsKey("id")) {
//            if(bundle.getString("session").equals("yes")) {
//
//            }
//            else if(bundle.getString("session").equals("no")) {
//
//            }
            // TODO: Use the above commented code to show the timer if event is a session and
            //  show the event details if event is not a session
            ShowCalendarFragment(bundle);
        }

        // Make buttons change fragments
        event = findViewById(R.id.main_event_button);
        session = findViewById(R.id.main_session_button);
        calendar = findViewById(R.id.main_calendar_button);
        user = findViewById(R.id.main_user_button);

        final Intent intent = new Intent(this, Todomain.class);

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowSessionFragment(null);
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowCalendarFragment(null);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowUserFragment(null);
            }
        });

    }

    // These functions hide the current fragments and open the specified fragment
    @Override
    public void ShowEventFragment(Bundle bundle) {
        String tag = NewEventFragment.class.getCanonicalName();
        FragmentTransaction transaction = fManager.beginTransaction();
        if(bundle != null && bundle.containsKey("Restart")) {
            if(eventFragment != null)
                transaction.detach(eventFragment);
            eventFragment = null;
        }
        HideOpenFragment();
        if(eventFragment == null) {
            eventFragment = new NewEventFragment();
            if (bundle != null)
                eventFragment.setArguments(bundle);
            transaction.add(R.id.main_frame, eventFragment, tag).commit();
        }
        else {
            transaction.show(eventFragment).commit();
        }
    }

    @Override
    public void ShowSessionFragment(Bundle bundle) {
        String tag = SessionFragment.class.getCanonicalName();
        FragmentTransaction transaction = fManager.beginTransaction();
        if(bundle != null && bundle.containsKey("Restart")) {
            if(sessionFragment != null)
                transaction.detach(sessionFragment);
            sessionFragment = null;
        }
        HideOpenFragment();
        if(sessionFragment == null) {
            sessionFragment = new SessionFragment();
            if (bundle != null)
                sessionFragment.setArguments(bundle);
            transaction.add(R.id.main_frame, sessionFragment, tag).commit();
        }
        else {
            transaction.show(sessionFragment).commit();
        }
    }

    @Override
    public void ShowCalendarFragment(Bundle bundle) {
        String tag = CalendarFragment.class.getCanonicalName();
        FragmentTransaction transaction = fManager.beginTransaction();
        if(bundle != null && bundle.containsKey("Restart")) {
            if(calendarFragment != null)
                transaction.detach(calendarFragment);
            calendarFragment = null;
        }
        HideOpenFragment();
        if(calendarFragment == null) {
            calendarFragment = new CalendarFragment();
            if (bundle != null)
                calendarFragment.setArguments(bundle);
            transaction.add(R.id.main_frame, calendarFragment, tag).commit();
        }
        else {
            transaction.show(calendarFragment).commit();
        }

    }

    @Override
    public void ShowUserFragment(Bundle bundle) {
        String tag = UserFragment.class.getCanonicalName();
        FragmentTransaction transaction = fManager.beginTransaction();
        if(bundle != null && bundle.containsKey("Restart")) {
            if(userFragment != null)
                transaction.detach(userFragment);
            userFragment = null;
        }
        HideOpenFragment();
        if(userFragment == null) {
            userFragment = new UserFragment();
            if (bundle != null)
                userFragment.setArguments(bundle);
            transaction.add(R.id.main_frame, userFragment, tag).commit();
        }
        else {
            transaction.show(userFragment).commit();
        }
    }

    // Called to hide whatever fragment is open
    private void HideOpenFragment() {
        if(eventFragment != null)fManager.beginTransaction().hide(eventFragment).commit();
        if(sessionFragment != null)fManager.beginTransaction().hide(sessionFragment).commit();
        if(calendarFragment != null)fManager.beginTransaction().hide(calendarFragment).commit();
        if(userFragment != null)fManager.beginTransaction().hide(userFragment).commit();
    }

    // Sets notifications for 15 minutes before event and at time of event
    @Override
    public void SetTimedNotification(int id) {
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
        Cursor cursor = getContentResolver().query(MyContentProvider.EVENTS_CONTENT_URI,
                projection, selection, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            long time = Long.parseLong(cursor.getString(
                    cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_TIME)));
            long earlytime = time - 900000;
            long timenow = Calendar.getInstance().getTimeInMillis();

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if(earlytime > timenow) {
                Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                alarmIntent.putExtra("id", id);
                alarmIntent.putExtra("type", "early");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), 0, alarmIntent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, earlytime, pendingIntent);
            }
            if(time > timenow) {
                Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                alarmIntent.putExtra("id", id);
                alarmIntent.putExtra("type", "ontime");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), 0, alarmIntent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }
        Log.d("Inside main", "at end of set notification");
    }

    // Cancels notifications for an event
    public void CancelTimedNotification(int id) {
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
        Cursor cursor = getContentResolver().query(MyContentProvider.EVENTS_CONTENT_URI,
                projection, selection, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            String timeString = cursor.getString(
                    cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_TIME));
            if (timeString == null || timeString.equals("")) {
                return;
            }
            long time = Long.parseLong(timeString);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent alarmIntent1 = new Intent(getApplicationContext(), AlarmReceiver.class);
            alarmIntent1.putExtra("id", id);
            alarmIntent1.putExtra("type", "early");
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                    getApplicationContext(), 0, alarmIntent1, 0);
            alarmManager.cancel(pendingIntent1);

            Intent alarmIntent2 = new Intent(getApplicationContext(), AlarmReceiver.class);
            alarmIntent2.putExtra("id", id);
            alarmIntent2.putExtra("type", "ontime");
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                    getApplicationContext(), 0, alarmIntent2, 0);
            alarmManager.cancel(pendingIntent2);
        }
    }

    // From https://stackoverflow.com/questions/33347809/android-marshmallow-sms-received-permission
    private void RequestPermission(String permission) {
        //String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }
}
