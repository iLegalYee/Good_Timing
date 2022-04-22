package edu.fsu.cs.goodtiming.Calendar;

import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import edu.fsu.cs.goodtiming.MyContentProvider;
import edu.fsu.cs.goodtiming.R;

// This is a bad prototype for showing the events in a day, mainly to be used for code than actual use
// MUST PASS IN A BUNDLE CONTAINING:
//     Int with key "year" of value 4 digit year
//     Int with key "month" of value month number
//     Int with key "day" of value day of the month
public class ShowDayChildFragment extends Fragment {

    Bundle bundle;
    TextView header;
    RelativeLayout relative;
    Button left;
    Button right;

    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    ClickListener listiner;

    public ShowDayChildFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_show_day_child, container, false);

        // Use this commented code to retrieve the current year, month, and day,
        // which can be passed into the Query function below

//        Date date = Calendar.getInstance().getTime();
//        int year = Integer.parseInt(DateFormat.format("yyyy", date).toString());
//        int month = Integer.parseInt(DateFormat.format("MM", date).toString());
//        int day = Integer.parseInt(DateFormat.format("dd", date).toString());

        header = (TextView) rootView.findViewById(R.id.show_day_header);
        relative = (RelativeLayout) rootView.findViewById(R.id.show_day_layout);
        left = (Button) rootView.findViewById(R.id.left_button);
        right = (Button) rootView.findViewById(R.id.right_button);

        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(bundle.getInt("year"), bundle.getInt("month") - 1, bundle.getInt("day"));
        final long timeShown = tempCalendar.getTimeInMillis();

        // Makes it so that the arrow buttons navigate to different days to show their events
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date tempDate = new Date(timeShown - (24 * 60 * 60 * 1000));
                Bundle tempBundle = new Bundle();
                tempBundle.putInt("year", Integer.parseInt(DateFormat.format("yyyy", tempDate).toString()));
                tempBundle.putInt("month", Integer.parseInt(DateFormat.format("MM", tempDate).toString()));
                tempBundle.putInt("day", Integer.parseInt(DateFormat.format("dd", tempDate).toString()));
                ((CalendarFragment) getParentFragment()).ShowDayChild(tempBundle);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date tempDate = new Date(timeShown + (24 * 60 * 60 * 1000));
                Bundle tempBundle = new Bundle();
                tempBundle.putInt("year", Integer.parseInt(DateFormat.format("yyyy", tempDate).toString()));
                tempBundle.putInt("month", Integer.parseInt(DateFormat.format("MM", tempDate).toString()));
                tempBundle.putInt("day", Integer.parseInt(DateFormat.format("dd", tempDate).toString()));
                ((CalendarFragment) getParentFragment()).ShowDayChild(tempBundle);
            }
        });

        // Checks that a bundle was passed in and the specified keys were included
        // Note that the year, month, and day should be passed in as ints, not strings
        if(bundle == null || !bundle.containsKey("year") || !bundle.containsKey("month") || !bundle.containsKey("day"))
            header.setText("Bundle passed is null or does not contain the date");
        else {
            header.setText(bundle.getInt("month") + "/" + bundle.getInt("day") +
                    "/" + bundle.getInt("year"));
        }

        // Making the recycler view
        List<ExamData> list = new ArrayList<ExamData>();
        list = getData(bundle.getInt("year"),
                bundle.getInt("month"), bundle.getInt("day"));

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        listiner = new ClickListener() {
            @Override
            public void click(int index, int id, String calendar){
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("calendar", calendar);
                ((CalendarFragment) getParentFragment()).ShowEventChild(bundle);
            }
        };
        adapter = new RecyclerAdapter(list, getActivity(),listiner);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    // This only queries the default android calendar for events on a specific day
    // Thus, a full list of the events and tasks for the day would require a
    // call to QueryAppEvents() and QueryAppTasks() as well
    // The "month - 1" is necessary because queries use 1 less for the month
    //    (e.g. must query using month = 3 for april, not 4 like you would think)
    public Cursor QueryAndroidCalendar(int year, int month, int day) {
        String[] projection = new String[] {
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.RDATE,
                CalendarContract.Events.RRULE,
                CalendarContract.Events.ALL_DAY,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.DURATION };

        Calendar startTime = Calendar.getInstance();
        startTime.set(year, month - 1, day, 0, 0, 0);
        Calendar endTime= Calendar.getInstance();
        endTime.set(year, month - 1, day, 23, 59, 59);

        String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ) AND ( deleted != 1 ))";
        return getActivity().getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection, selection, null, null);
    }

    // Returns a cursor pointing to the events in the local app calendar for the specified day
    public Cursor QueryAppEvents(int year, int month, int day) {
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

        Calendar startTime = Calendar.getInstance();
        startTime.set(year, month - 1, day, 0, 0, 0);
        Calendar endTime= Calendar.getInstance();
        endTime.set(year, month - 1, day, 23, 59, 59);

        String selection = "(( " + MyContentProvider.COLUMN_EVENTS_TIME + " >= " + startTime.getTimeInMillis() + " ) AND ( " + MyContentProvider.COLUMN_EVENTS_TIME + " <= " + endTime.getTimeInMillis() + " ))";
        return getActivity().getContentResolver().query(MyContentProvider.EVENTS_CONTENT_URI, projection, selection, null, null);
    }

    // *********UNTESTED***********
    public Cursor QueryAppTasks(int year, int month, int day) {
        String[] projection = new String[] {
                MyContentProvider.COLUMN_TASKS_ID,
                MyContentProvider.COLUMN_TASKS_NAME,
                MyContentProvider.COLUMN_TASKS_DESCRIPTION,
                MyContentProvider.COLUMN_TASKS_DEADLINE};

        String selection = "( " + MyContentProvider.COLUMN_TASKS_DEADLINE + " == " + month + "/" + day + "/" + year + " )";
        return getActivity().getContentResolver().query(MyContentProvider.TASKS_CONTENT_URI, projection, selection, null, null);
    }

    // Returns data for the RecyclerView
    private List<ExamData> getData(int year, int month, int day)
    {
        List<ExamData> list = new ArrayList<>();

        // Querying the android calendar and adding those events for the day to the list
        Cursor cursor = QueryAndroidCalendar(year, month, day);
        if(cursor != null) {
            while(cursor.moveToNext()) {
                // Sorting through the data returned
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String datetime = cursor.getString(cursor.getColumnIndexOrThrow("dtstart"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String allday = cursor.getString(cursor.getColumnIndexOrThrow("allDay"));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(CalendarContract.Events._ID));
                if(title == null || title.equals(""))
                    title = "No Title";
                if(allday != null && allday.equals("1"))
                    datetime = "All Day Event";
                else {
                    if(datetime == null || datetime.equals(""))
                        datetime = "No Date or Time";
                    else{
                        Date date = new Date(Long.parseLong(datetime));
                        if(date != null)
                            datetime = "Start at " + DateFormat.format("HH", date).toString() +
                                    ":" + DateFormat.format("mm", date).toString();
                        else
                            datetime = "No Start Time";
                    }
                }
                if(description == null || description.equals(""))
                    description = "No Description";
                // Add the event to the list
                list.add(new ExamData(title, datetime, description, id, "androidcalendar"));
            }
        }

        // Querying the local app calendar and putting those events in the list like above
        Cursor appcursor = QueryAppEvents(year, month, day);
        if(appcursor != null) {
            Log.d("Inside showday", "App query count: " + appcursor.getCount());
            while(appcursor.moveToNext()) {
                String title = appcursor.getString(appcursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_NAME));
                String datetime = appcursor.getString(appcursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_TIME));
                String description = appcursor.getString(appcursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_DESCRIPTION));
                int id = appcursor.getInt(appcursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_ID));
                if(title == null || title.equals(""))
                    title = "No Title";
                else {
                    if(datetime == null || datetime.equals(""))
                        datetime = "No Start Time";
                    else{
                        Date date = new Date(Long.parseLong(datetime));
                        if(date != null)
                            datetime = "Start at " + DateFormat.format("HH", date).toString() +
                                    ":" + DateFormat.format("mm", date).toString();
                        else
                            datetime = "No Start Time";
                    }
                }
                if(description == null || description.equals(""))
                    description = "No Description";
                list.add(new ExamData(title, datetime, description, id, "appcalendar"));
            }
        }

        return list;
    }
}


// Calls the query function below
// This is only a query to the default android calendar
// Our local app event and task tables will be queried and listed later
//            cursor = QueryAndroidCalendar(bundle.getInt("year"),
//                    bundle.getInt("month"), bundle.getInt("day"));
//            if(cursor == null)
//                Log.d("Inside showdayfragment", "cursor from query is null");
//            if(cursor != null) {
//                Log.d("Inside showdayfragment", "cursor from query is not null");

// This following loop is very important!!
// It loops through the values returned from the query and dynamically
// creates a CLICKABLE TextView for each entry
// This loop links each TextView to an OnClickListener so that
// clicking on it opens the ShowEventChildFragment with the corresponding
// event id passed in
//                TextView currentText;
//                TextView previousText = (TextView) rootView.findViewById(R.id.show_day_text);
//            String[] projection = new String[] {
//                    MyContentProvider.COLUMN_EVENTS_ID,
//                    MyContentProvider.COLUMN_EVENTS_NAME,
//                    MyContentProvider.COLUMN_EVENTS_DESCRIPTION,
//                    MyContentProvider.COLUMN_EVENTS_TIME,
//                    MyContentProvider.COLUMN_EVENTS_DATE,
//                    MyContentProvider.COLUMN_EVENTS_REPEAT,
//                    MyContentProvider.COLUMN_EVENTS_LOCATION,
//                    MyContentProvider.COLUMN_EVENTS_DURATION,
//                    MyContentProvider.COLUMN_EVENTS_IS_SESSION};
//            Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.EVENTS_CONTENT_URI,
//                    projection, null, null, null);
//                while(cursor.moveToNext()) {
//                    final int id = cursor.getInt(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_ID));
//                    currentText = new TextView(getContext());
//                    currentText.setId(View.generateViewId());
//                    currentText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
//                    currentText.setClickable(true);
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                        currentText.setElevation(10f);
//                    else
//                        currentText.setShadowLayer(1.5f, -1, 1, Color.LTGRAY);
//                    currentText.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Bundle tempBundle = new Bundle();
//                            tempBundle.putString("calendar", "appcalendar");
//                            tempBundle.putInt("id", id);
//                            ((CalendarFragment) getParentFragment()).ShowEventChild(tempBundle);
//                        }
//                    });
//                    String datetime = cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.COLUMN_EVENTS_TIME));
//                    if(datetime == null || datetime.equals(""))
//                        datetime = "No Date or Time";
//                    else{
//                        Date date = new Date(Long.parseLong(datetime));
//                        if(date != null)
//                            datetime = "Start at " + DateFormat.format("MM", date).toString() +
//                                    "/" + DateFormat.format("dd", date).toString() + "/" +
//                                    DateFormat.format("yyyy", date).toString();
//                        else
//                            datetime = "No Start Time";
//                    }
//
//                    currentText.setText(datetime);
//                    RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(
//                            RelativeLayout.LayoutParams.WRAP_CONTENT,
//                            RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.addRule(RelativeLayout.BELOW, previousText.getId());
//                    relative.addView(currentText, layoutParams);
//                    previousText = currentText;
//                }
//            }