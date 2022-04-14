package edu.fsu.cs.goodtiming;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

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
        mBundle = getArguments();
        fManager = getChildFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        // If you want to find a view in here, use rootView.findViewById instead of getActivity().findViewById
        ShowCalendarChild(null);

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

    public interface OnCalendarFragmentInteractionListener {
        void ShowSessionFragment(Bundle bundle);
        void ShowEventFragment(Bundle bundle);
        void ShowUserFragment(Bundle bundle);
    }
}
