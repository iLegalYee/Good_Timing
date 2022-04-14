package edu.fsu.cs.goodtiming;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShowDayChildFragment extends Fragment {

    Bundle bundle;
    TextView text;

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

        Cursor cursor = getActivity().getContentResolver().query(CalendarContract.Events.CONTENT_URI,
                null, null, null, null);
        text = (TextView) rootView.findViewById(R.id.show_event_text);
        if(bundle == null || !bundle.containsKey("year") || !bundle.containsKey("month") || !bundle.containsKey("day"))
            text.setText("Bundle passed is null or does not contain the date");
        else {
            text.setText("Year: " + bundle.getString("year") + "  Month: " +
                    bundle.getString("month") + "  Day: " + bundle.getString("day") +
                    "\nCursor Count: " + cursor.getCount());
        }

        return rootView;
    }
}