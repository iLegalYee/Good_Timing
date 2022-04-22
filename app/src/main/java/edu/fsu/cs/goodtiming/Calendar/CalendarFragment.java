package edu.fsu.cs.goodtiming.Calendar;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import edu.fsu.cs.goodtiming.R;
import edu.fsu.cs.goodtiming.Utils.NewEventFragment;

// This fragment is for the main calendar tab and it nests child fragments within it
public class CalendarFragment extends Fragment {
    private OnCalendarFragmentInteractionListener mListener;
    private Bundle mBundle;
    FragmentManager fManager;

    CalendarChildFragment calendarFragment;
    ShowDayChildFragment showDayFragment;
    ShowEventChildFragment showEventFragment;


    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mBundle = getArguments();
        fManager = getChildFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        // If you want to find a view in here, use rootView.findViewById instead of getActivity().findViewById
        ShowCalendarChild(null);

        // Enters this statement if instance is started from clicking on a notification
        if(mBundle != null && mBundle.containsKey("session") && mBundle.containsKey("id")) {
            Bundle tempBundle = new Bundle();
            tempBundle.putInt("id", mBundle.getInt("id"));
            tempBundle.putString("calendar", "appcalendar");
            ShowEventChild(tempBundle);
        }

        return rootView;
    }

    // Initializes mListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCalendarFragmentInteractionListener) {
            mListener = (OnCalendarFragmentInteractionListener) context;
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

    // These functions replace the current shown child fragment with the specified fragment
    public void ShowCalendarChild(Bundle bundle) {
        FragmentTransaction transaction = fManager.beginTransaction();
        calendarFragment = new CalendarChildFragment();
        if(bundle != null)
            calendarFragment.setArguments(bundle);
        if(fManager.findFragmentById(R.id.calendar_frame) == null)
            transaction.add(R.id.calendar_frame, calendarFragment).commit();
        else {
            transaction.replace(R.id.calendar_frame, calendarFragment);
            transaction.addToBackStack(null).commit();
        }
    }

    public void ShowDayChild(Bundle bundle) {
        FragmentTransaction transaction = fManager.beginTransaction();
        showDayFragment = new ShowDayChildFragment();
        if(bundle != null)
            showDayFragment.setArguments(bundle);
        transaction.replace(R.id.calendar_frame, showDayFragment);
        transaction.addToBackStack(null).commit();
    }

    public void ShowEventChild(Bundle bundle) {
        FragmentTransaction transaction = fManager.beginTransaction();
        showEventFragment = new ShowEventChildFragment();
        if(bundle != null)
            showEventFragment.setArguments(bundle);
        transaction.replace(R.id.calendar_frame, showEventFragment);
        transaction.addToBackStack(null).commit();

    }

    // Interface to call MainActivity functions
    public interface OnCalendarFragmentInteractionListener {
        void ShowSessionFragment(Bundle bundle);
        void ShowEventFragment(Bundle bundle);
        void ShowUserFragment(Bundle bundle);
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

}
