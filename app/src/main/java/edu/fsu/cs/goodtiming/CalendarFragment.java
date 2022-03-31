package edu.fsu.cs.goodtiming;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalendarFragment extends Fragment {
    private OnCalendarFragmentInteractionListener mListener;
    private Bundle mBundle;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        // If you want to find a view in here, use rootView.findViewById instead of getActivity().findViewById

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

    public interface OnCalendarFragmentInteractionListener {
        void ShowSessionFragment(Bundle bundle);
        void ShowEventFragment(Bundle bundle);
        void ShowUserFragment(Bundle bundle);
    }
}
