package edu.fsu.cs.goodtiming.Calendar;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import edu.fsu.cs.goodtiming.MyContentProvider;
import edu.fsu.cs.goodtiming.R;

// This fragment displays the details for an event
// MUST PASS IN A BUNDLE CONTAINING:
//    String with key "calendar" of value "androidcalendar", "appcalendar", or "task"
//    Int with key "id" which is the _id value of the event in its corresponding content provider table
// These two values are used to locate the specific event to display from one of the three content provider tables

public class ShowEventChildFragment extends Fragment {

    Bundle bundle;
    TextView header;
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    TextView text7;
    TextView text8;

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
        header = (TextView) rootView.findViewById(R.id.show_event_header);
        text1 = (TextView) rootView.findViewById(R.id.event_text1);
        text2 = (TextView) rootView.findViewById(R.id.event_text2);
        text3 = (TextView) rootView.findViewById(R.id.event_text3);
        text4 = (TextView) rootView.findViewById(R.id.event_text4);
        text5 = (TextView) rootView.findViewById(R.id.event_text5);
        text6 = (TextView) rootView.findViewById(R.id.event_text6);
        text7 = (TextView) rootView.findViewById(R.id.event_text7);
        text8 = (TextView) rootView.findViewById(R.id.event_text8);

        if(bundle != null && bundle.containsKey("id") && bundle.containsKey("calendar")) {
            if (bundle.getString("calendar").equals("androidcalendar")) {
                // Enters this if the event is located in the android calendar
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

                // Working through the entry returned from the cursor to initialize the textviews
                if (cursor.moveToNext()) {
                    String tempString;
                    tempString = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    if(tempString != null)
                        text1.setText("Title: " + tempString);
                    else
                        text1.setText("No Title");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    if(tempString != null)
                        text2.setText("Description: " + tempString);
                    else
                        text2.setText("No Description");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow("dtstart"));
                    Date tempDate = new Date(Long.parseLong(tempString));
                    if(tempString != null)
                        text3.setText("Start Time: " + DateFormat.format("HH", tempDate).toString() +
                            ":" + DateFormat.format("mm", tempDate).toString());
                    else
                        text3.setText("No Start Time");

                    if(tempString != null)
                        text4.setText("Date: " + DateFormat.format("MM", tempDate).toString() +
                                "/" + DateFormat.format("dd", tempDate).toString() + "/" +
                                DateFormat.format("yyyy", tempDate).toString());
                    else
                        text4.setText("No Date");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow("rrule"));
                    if(tempString != null)
                        text5.setText("Repeatability: " + tempString);
                    else
                        text5.setText("No Repeatability");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow("allDay"));
                    if(tempString != null)
                        text6.setText("All Day Event?: " + tempString);
                    else
                        text6.setText("All Day is null");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
                    if(tempString != null)
                        text7.setText("Duration: " + tempString);
                    else
                        text7.setText("Duration is not set");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow("eventLocation"));
                    if(tempString != null)
                        text8.setText("Location is: " + tempString);
                    else
                        text8.setText("Location is not set");
                }
                else {
                    Log.d("Inside showeventfrag", "query did not return anything");
                    header.setText("No Event Matches That ID in the Android Calendar");
                }
                cursor.close();
            }

            else if(bundle.getString("calendar").equals("appcalendar")) {
                // Enters here if the specified event is in the local app calendar
                //Querying the local app calendar
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
                String selection = "( " + MyContentProvider.COLUMN_EVENTS_ID + " == " +
                        bundle.getInt("id") + " )";
                Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.EVENTS_CONTENT_URI,
                        projection, selection, null, null);

                // Working through the entry returned to initialize the textviews
                if (cursor.moveToNext()) {
                    String tempString;
                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_NAME));
                    if(tempString != null)
                        text1.setText("Title: " + tempString);
                    else
                        text1.setText("No Title");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_DESCRIPTION));
                    if(tempString != null)
                        text2.setText("Description: " + tempString);
                    else
                        text2.setText("No Description");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_TIME));
                    Date tempDate = new Date(Long.parseLong(tempString));
                    if(tempString != null)
                        text3.setText("Start Time: " + DateFormat.format("HH", tempDate).toString() +
                                ":" + DateFormat.format("mm", tempDate).toString());
                    else
                        text3.setText("No Start Time");

                    if(tempString != null)
                        text4.setText("Date: " + DateFormat.format("MM", tempDate).toString() +
                                "/" + DateFormat.format("dd", tempDate).toString() + "/" +
                                DateFormat.format("yyyy", tempDate).toString());
                    else
                        text4.setText("No Date");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_REPEAT));
                    if(tempString != null)
                        text5.setText("Repeatability: " + tempString);
                    else
                        text5.setText("No Repeatability");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_IS_SESSION));
                    if(tempString != null)
                        text6.setText("Is Session?: " + tempString);
                    else
                        text6.setText("Not a session");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_DURATION));
                    if(tempString != null)
                        text7.setText("Duration: " + tempString);
                    else
                        text7.setText("Duration is not set");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_LOCATION));
                    if(tempString != null)
                        text8.setText("Location is: " + tempString);
                    else
                        text8.setText("Location is not set");
                }
                else {
                    Log.d("Inside showeventfrag", "query did not return anything");
                    header.setText("No Event Matches That ID in the App's Calendar");
                }
                cursor.close();
            }

            // Doing the same as above for the tasks
            else if(bundle.getString("calendar").equals("task")) {
                String[] projection = new String[] {
                        MyContentProvider.COLUMN_TASKS_ID,
                        MyContentProvider.COLUMN_TASKS_NAME,
                        MyContentProvider.COLUMN_TASKS_DESCRIPTION,
                        MyContentProvider.COLUMN_TASKS_DEADLINE};
                String selection = "( " + MyContentProvider.COLUMN_TASKS_ID + " == " + bundle.getInt("id") + " )";
                Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.TASKS_CONTENT_URI,
                        projection, selection, null, null);
                if (cursor.moveToNext()) {
                    String tempString;
                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_TASKS_NAME));
                    if(tempString != null)
                        text1.setText("Title: " + tempString);
                    else
                        text1.setText("No Title");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_TASKS_DESCRIPTION));
                    if(tempString != null)
                        text2.setText("Description: " + tempString);
                    else
                        text2.setText("No Description");

                    tempString = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_TASKS_DEADLINE));
                    Date tempDate = new Date(Long.parseLong(tempString));
                    if(tempString != null)
                        text3.setText("Deadline: " + tempString);
                    else
                        text3.setText("No Deadline");
                }
                else {
                    Log.d("Inside showeventfrag", "query did not return anything");
                    header.setText("No Task Matches That ID in the Task Table");
                }
                cursor.close();
            }
        }
        return rootView;
    }
}