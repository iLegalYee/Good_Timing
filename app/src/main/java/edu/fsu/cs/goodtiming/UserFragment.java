package edu.fsu.cs.goodtiming;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserFragment extends Fragment {
    private OnUserFragmentInteractionListener mListener;
    private Bundle mBundle;

    public UserFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        // If you want to find a view in here, use rootView.findViewById instead of getActivity().findViewById

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
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
}
