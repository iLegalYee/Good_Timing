package edu.fsu.cs.goodtiming;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.HashSet;

public class User_JournalEntry extends AppCompatActivity {
    private EditText body;
    private int entryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_journal_entry);

        // If entry already exists, get its ID
        Intent intent = getIntent();
        entryID = intent.getIntExtra("entryID", -1);

        // Get Edit texts
        body = (EditText) findViewById(R.id.entry_body);

        // Set Edit text if entry already exists
        if (entryID != -1)
        {
            body.setText(User_JournalFragment.entries.get(entryID));
        }
        else {
            User_JournalFragment.entries.add("");
            entryID = User_JournalFragment.entries.size() - 1;
            User_JournalFragment.arrayAdapter.notifyDataSetChanged();
        }

        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Intentionally empty
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                User_JournalFragment.entries.set(entryID, String.valueOf(charSequence));
                User_JournalFragment.arrayAdapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences =
                        getSharedPreferences("edu.fsu.cs.goodtiming.entry", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet(User_JournalFragment.entries);
                sharedPreferences.edit().putStringSet("entries", set).apply();
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // Intentionally empty
            }
        });
    }

}