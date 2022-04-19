package edu.fsu.cs.goodtiming.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.fsu.cs.goodtiming.EventFragment;
import edu.fsu.cs.goodtiming.R;
import edu.fsu.cs.goodtiming.UserFragment;

public class Todomain extends AppCompatActivity implements OnDialogCloseListner{


    FragmentManager fManager;
    EventFragment eventFragment = null;
    SessionFragment sessionFragment = null;
    CalendarFragment calendarFragment = null;
    UserFragment userFragment = null;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;
    Button event;
    Button session;
    Button calendar;
    Button user;

    int indexOfShownFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todomain);

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


        mRecyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        myDB = new DataBaseHelper(Todomain.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB, Todomain.this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }



    public void ShowEventFragment(Bundle bundle) {
        Log.d("Inside Main", "ShowEventFragment");
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
            transaction.add(R.id.main_frame, eventFragment).commit();
            indexOfShownFragment = 1;
        }
        else {
            transaction.show(eventFragment).commit();
            indexOfShownFragment = 1;
        }
    }


    public void ShowSessionFragment(Bundle bundle) {
        Log.d("Inside Main", "ShowSessionFragment");
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
            transaction.add(R.id.main_frame, sessionFragment).commit();
            indexOfShownFragment = 2;
        }
        else {
            transaction.show(sessionFragment).commit();
            indexOfShownFragment = 2;
        }
    }


    public void ShowCalendarFragment(Bundle bundle) {
        Log.d("Inside Main", "ShowCalendarFragment");
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
            transaction.add(R.id.main_frame, calendarFragment).commit();
            indexOfShownFragment = 3;
        }
        else {
            transaction.show(calendarFragment).commit();
            indexOfShownFragment = 3;
        }

    }


    public void ShowUserFragment(Bundle bundle) {
        Log.d("Inside Main", "ShowUserFragment");
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
            transaction.add(R.id.main_frame, userFragment).commit();
            indexOfShownFragment = 4;
        }
        else {
            transaction.show(userFragment).commit();
            indexOfShownFragment = 4;
        }
    }

    private void HideOpenFragment() {
        switch(indexOfShownFragment) {
            case 0:
                break;
            case 1:
                fManager.beginTransaction().hide(eventFragment).commit();
                break;
            case 2:
                fManager.beginTransaction().hide(sessionFragment).commit();
                break;
            case 3:
                fManager.beginTransaction().hide(calendarFragment).commit();
                break;
            case 4:
                fManager.beginTransaction().hide(userFragment).commit();
                break;
        }

    }

}