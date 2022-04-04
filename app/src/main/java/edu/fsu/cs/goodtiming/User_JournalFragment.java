package edu.fsu.cs.goodtiming;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class User_JournalFragment extends Fragment {


    private UserFragment.OnUserFragmentInteractionListener mListener;
    private Bundle mBundle;

    public User_JournalFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_user__journal, container, false);

        Button addEntry = rootView.findViewById(R.id.new_journalEntry_button);
        addEntry.setOnClickListener(view -> {
            //TODO Launch new Activity that will create and save entry
            Toast.makeText(getContext(), "Add Entry Clicked", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }


}