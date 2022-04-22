package edu.fsu.cs.goodtiming.Calendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import edu.fsu.cs.goodtiming.R;

public class CalendarChildFragment extends Fragment {

    CalendarView calendar;
    Long date;

    public CalendarChildFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar_child, container, false);

        calendar = (CalendarView) rootView.findViewById(R.id.calendar_view);
        date = calendar.getDate();

        // When a day is clicked on the calendar, this opens the showevent fragment for the clicked date
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                    Bundle dateBundle = new Bundle();
                    dateBundle.putInt("year", i);
                    dateBundle.putInt("month", (i1+ 1));
                    dateBundle.putInt("day", i2);
                    ((CalendarFragment) getParentFragment()).ShowDayChild(dateBundle);

            }
        });

        return rootView;
    }
}