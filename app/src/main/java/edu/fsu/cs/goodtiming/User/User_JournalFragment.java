package edu.fsu.cs.goodtiming.User;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashSet;

import edu.fsu.cs.goodtiming.R;


public class User_JournalFragment extends Fragment {

    private UserFragment.OnUserFragmentInteractionListener mListener;
    private Bundle mBundle;
    public static ArrayList<String> entries = new ArrayList<>();
    public static ArrayAdapter<String> arrayAdapter;

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
        final View rootView = inflater.inflate(R.layout.fragment_user_journal, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list_entries);
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences("edu.fsu.cs.goodtiming.entry", Context.MODE_PRIVATE);

        HashSet<String> entries_set = (HashSet<String>) sharedPreferences.getStringSet("entries", null);

        // If there are entries in the sharedpreferences, load them
        if (entries_set != null)
            entries = new ArrayList<>(entries_set);

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, entries);
        listView.setAdapter(arrayAdapter);

        // Open entry in new activity if clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), User_JournalEntry.class);
                intent.putExtra("entryID", i);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Entry");
                builder.setMessage("Would you like to delete this entry?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                deleteEntry(i);
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
        });

        return rootView;
    }

    // Method to delete an entry from the listview and sharedPreferences
    void deleteEntry(int i)
    {
        entries.remove(i);
        arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences("edu.fsu.cs.goodtiming.entry", Context.MODE_PRIVATE);

        HashSet<String> entries_set = new HashSet<>(User_JournalFragment.entries);
        sharedPreferences.edit()
                .putStringSet("entries", entries_set)
                .apply();
    }
}