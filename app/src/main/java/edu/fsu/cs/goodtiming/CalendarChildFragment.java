package edu.fsu.cs.goodtiming;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CalendarView;

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

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                    Bundle dateBundle = new Bundle();
                    dateBundle.putString("year", "" + i);
                    dateBundle.putString("month", "" + (i1+ 1));
                    dateBundle.putString("day", "" + i2);
                    ((CalendarFragment) getParentFragment()).ShowDayChild(dateBundle);

            }
        });

        return rootView;
    }
}