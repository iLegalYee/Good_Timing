package edu.fsu.cs.goodtiming;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements
        EventFragment.OnEventFragmentInteractionListener,
        SessionFragment.OnSessionFragmentInteractionListener,
        CalendarFragment.OnCalendarFragmentInteractionListener,
        UserFragment.OnUserFragmentInteractionListener
{
    FragmentManager fManager;
    EventFragment eventFragment = null;
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
        RequestPermission(Manifest.permission.READ_CALENDAR);
        RequestPermission(Manifest.permission.WRITE_CALENDAR);
        fManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        ShowEventFragment(null);

        event = findViewById(R.id.main_event_button);
        session = findViewById(R.id.main_session_button);
        calendar = findViewById(R.id.main_calendar_button);
        user = findViewById(R.id.main_user_button);

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEventFragment(null);
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

    @Override
    public void ShowEventFragment(Bundle bundle) {
        String tag = EventFragment.class.getCanonicalName();
        FragmentTransaction transaction = fManager.beginTransaction();
        if(bundle != null && bundle.containsKey("Restart")) {
            if(eventFragment != null)
                transaction.detach(eventFragment);
            eventFragment = null;
        }
        HideOpenFragment();
        if(eventFragment == null) {
            eventFragment = new EventFragment();
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

    private void HideOpenFragment() {
        if(eventFragment != null)fManager.beginTransaction().hide(eventFragment).commit();
        if(sessionFragment != null)fManager.beginTransaction().hide(sessionFragment).commit();
        if(calendarFragment != null)fManager.beginTransaction().hide(calendarFragment).commit();
        if(userFragment != null)fManager.beginTransaction().hide(userFragment).commit();
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
