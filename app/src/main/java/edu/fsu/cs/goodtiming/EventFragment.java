package edu.fsu.cs.goodtiming;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import edu.fsu.cs.goodtiming.Utils.NewEventFragment;


public class EventFragment extends Fragment {
    private OnEventFragmentInteractionListener mListener;
    private Bundle mBundle;

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        // If you want to find a view in here, use rootView.findViewById instead of getActivity().findViewById

        return rootView;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mBundle = getArguments();
        super.onCreate(savedInstanceState);
    }

    // Initializes mListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventFragmentInteractionListener) {
            mListener = (OnEventFragmentInteractionListener) context;
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

    public interface OnEventFragmentInteractionListener {
        void ShowSessionFragment(Bundle bundle);
        void ShowCalendarFragment(Bundle bundle);
        void ShowUserFragment(Bundle bundle);
    }



}
