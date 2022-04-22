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

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.util.Calendar;
import java.util.Date;

import edu.fsu.cs.goodtiming.MyContentProvider;
import edu.fsu.cs.goodtiming.R;
import edu.fsu.cs.goodtiming.Utils.NewEventFragment;
import edu.fsu.cs.goodtiming.Utils.Todomain;

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
                        // TODO: create settings implementation
                        switch (menuItem.getItemId()){
                            case R.id.settings_theme:
                                //TODO: create theme implementation
                                return true;

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
        //in case we want to hide certain items in the future
        //menu.findItem(R.id.addevent).setVisible(false);
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
    public Cursor QueryAnalytics() {
        Date date = Calendar.getInstance().getTime();
        Date startDate = new Date(date.getTime() - (7 * 24 * 60 * 60 * 1000));
        int year = Integer.parseInt(DateFormat.format("yyyy", date).toString());
        int month = Integer.parseInt(DateFormat.format("MM", date).toString());
        int day = Integer.parseInt(DateFormat.format("dd", date).toString());

        int startYear = Integer.parseInt(DateFormat.format("yyyy", startDate).toString());
        int startMonth = Integer.parseInt(DateFormat.format("MM", startDate).toString());
        int startDay = Integer.parseInt(DateFormat.format("dd", startDate).toString());

        String[] projection = new String[] {
                MyContentProvider.COLUMN_ANALYTICS_NAME,
                MyContentProvider.COLUMN_ANALYTICS_TIME,
                MyContentProvider.COLUMN_ANALYTICS_DATA};

        Calendar startTime = Calendar.getInstance();
        startTime.set(startYear, startMonth - 1, startDay, 0, 0, 0);
        Calendar endTime= Calendar.getInstance();
        endTime.set(year, month - 1, day, 23, 59, 59);

        String selection = "(( " + MyContentProvider.COLUMN_EVENTS_TIME + " >= " + startTime.getTimeInMillis() + " ) AND ( " + MyContentProvider.COLUMN_EVENTS_TIME + " <= " + endTime.getTimeInMillis() + " ))";
        return getActivity().getContentResolver().query(MyContentProvider.EVENTS_CONTENT_URI, projection, selection, null, null);
    }


}
