package edu.fsu.cs.goodtiming.User;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import edu.fsu.cs.goodtiming.MyContentProvider;
import edu.fsu.cs.goodtiming.R;
import edu.fsu.cs.goodtiming.Utils.NewEventFragment;


public class UserFragment extends Fragment {

    private OnUserFragmentInteractionListener mListener;
    private Bundle mBundle;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        // If you want to find a view in here, use rootView.findViewById instead of getActivity().findViewById

        // Button - creates new activity where user can write and store a journal entry when clicked
        Button addEntry = rootView.findViewById(R.id.new_journalEntry_button);
        addEntry.setOnClickListener(view -> {
            int entryID = User_JournalFragment.entries.size();
            Intent intent = new Intent(getActivity(), User_JournalEntry.class);
            startActivity(intent);
        });

        // Create and Initialize settings button
        ImageButton settings = rootView.findViewById(R.id.user_settings_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), settings);
                menu.getMenuInflater().inflate(R.menu.settings_popup_menu, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Create the settings menu
                        switch (menuItem.getItemId()){
                            case R.id.settings_delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Delete All Entries");
                                builder.setMessage("Would you like to delete all entries from this device?\n(Warning: Deletion is permanent)")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int j) {
                                                SharedPreferences sharedPreferences = getContext()
                                                        .getSharedPreferences("edu.fsu.cs.goodtiming.entry", Context.MODE_PRIVATE);
                                                sharedPreferences.edit().clear();
                                                User_JournalFragment.entries.clear();
                                                User_JournalFragment.arrayAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //Do Nothing
                                            }
                                        }).create().show();
                                return true;
                        }
                        return false;
                    }
                });
                menu.show();
            }
        });

        // Get Data from Content Provider
        ArrayList<Integer> data = getData();
        ArrayList<Date> dates = new ArrayList<>(7);

        // Get Dates
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++){
            dates.add(i, calendar.getTime());
            calendar.add(Calendar.DATE, -1);
        }

        // Imported library that displays graph
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        // Put data into graph
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(dates.get(6), data.get(6)/60),
                new DataPoint(dates.get(5), data.get(5)/60),
                new DataPoint(dates.get(4), data.get(4)/60),
                new DataPoint(dates.get(3), data.get(3)/60),
                new DataPoint(dates.get(2), data.get(2)/60),
                new DataPoint(dates.get(1), data.get(1)/60),
                new DataPoint(dates.get(0), data.get(0)/60)
        });

        graph.setTitle("Time Spent Studying Past Week (minutes)");
        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        graph.getViewport().setMinX(dates.get(6).getTime());
        graph.getViewport().setMaxX(dates.get(0).getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        //Starts a child fragment where the journal entries are listed
        Fragment childFrag = new User_JournalFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_user_fragment_container, childFrag).commit();
    }

    // Initializes mListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserFragmentInteractionListener) {
            mListener = (OnUserFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUserFragmentInteractionListener {
        void ShowSessionFragment(Bundle bundle);
        void ShowCalendarFragment(Bundle bundle);
        void ShowEventFragment(Bundle bundle);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        //inflate menu
        menu.clear();
        inflater.inflate(R.menu.eventmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();
        if (id == R.id.addevent) {
            //jump here
            NewEventFragment NewEventFragment = new NewEventFragment();
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame,NewEventFragment);
            fragmentTransaction.commit();
        }

            return super.onOptionsItemSelected(item);
    }

    // Get analytics for last seven days
    public ArrayList getData() {
        ArrayList<Integer> data = new ArrayList();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String getDate;

        String[] projection = new String[] {
                MyContentProvider.COLUMN_ANALYTICS_NAME,
                MyContentProvider.COLUMN_ANALYTICS_TIME,
                MyContentProvider.COLUMN_ANALYTICS_DATA};

        for (int i = 0; i < 7; i++){
            getDate = format.format(calendar.getTime());

            String s = "(( " + MyContentProvider.COLUMN_ANALYTICS_TIME + " = \"" + getDate + "\" ))";
            Cursor c = getActivity().getContentResolver().query(MyContentProvider.ANALYTICS_CONTENT_URI, projection, s, null, null);

            data.add(0);
            if (c != null) {
                while (c.moveToNext())
                    data.add(i, Integer.parseInt(c.getString(c.getColumnIndexOrThrow(MyContentProvider.COLUMN_ANALYTICS_DATA))));
            }
            calendar.add(Calendar.DATE, -1);
        }

        return data;
    }


}
