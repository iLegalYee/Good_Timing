package edu.fsu.cs.goodtiming.Calendar;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.fsu.cs.goodtiming.R;

// This fragment displays the details for an event
// MUST PASS IN A BUNDLE CONTAINING:
//    String with key "calendar" of value "androidcalendar", "appcalendar", or "task"
//    Int with key "id" which is the _id value of the event in its corresponding content provider table
// These two values are used to locate the specific event to display from one of the three content provider tables

public class ShowEventChildFragment extends Fragment {

    Bundle bundle;
    TextView title;
    TextView description;
    TextView time;
    TextView date;
    TextView repeat;
    TextView location;
    TextView duration;
    TextView session;

    public ShowEventChildFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_show_event_child, container, false);

        // Initializing all of the TextViews
        title = (TextView) rootView.findViewById(R.id.show_event_title);
        description = (TextView) rootView.findViewById(R.id.show_event_description);
        time = (TextView) rootView.findViewById(R.id.show_event_time);
        date = (TextView) rootView.findViewById(R.id.show_event_date);
        repeat = (TextView) rootView.findViewById(R.id.show_event_repeat);
        location = (TextView) rootView.findViewById(R.id.show_event_location);
        duration = (TextView) rootView.findViewById(R.id.show_event_duration);
        session = (TextView) rootView.findViewById(R.id.show_event_session);

        if(bundle != null && bundle.containsKey("id") && bundle.containsKey("calendar")) {
            if (bundle.getString("calendar").equals("androidcalendar")) {
                // Querying the default android calendar
                String[] projection = new String[]{
                        CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DESCRIPTION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.RDATE,
                        CalendarContract.Events.RRULE,
                        CalendarContract.Events.ALL_DAY,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DURATION};
                String selection = "( " + CalendarContract.Events._ID + " == " + bundle.getInt("id") + " )";
                Cursor cursor = getActivity().getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection, selection, null, null);

                if (cursor.moveToNext()) {
                    title.setText("Title is: " + cursor.getString(cursor.getColumnIndexOrThrow("title")));
                    description.setText("Description is: " + cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    time.setText("Time is: " + cursor.getString(cursor.getColumnIndexOrThrow("dtstart")));
                    location.setText("Location is: " + cursor.getString(cursor.getColumnIndexOrThrow("eventlocation")));
                    duration.setText("Duration is: " + cursor.getString(cursor.getColumnIndexOrThrow("duration")));
                }
                else
                    Log.d("Inside showeventfrag", "query did not return anything");
                cursor.close();
            }
            else if(bundle.getString("calendar").equals("appcalendar")) {

                // TODO: Query local app calendar for event and figure out how to display it

            }
            else if(bundle.getString("calendar").equals("task")) {

                // TODO: Query task table and figure out how to display it

            }
        }
        return rootView;
    }
}