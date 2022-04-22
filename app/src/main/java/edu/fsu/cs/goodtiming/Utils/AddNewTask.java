package edu.fsu.cs.goodtiming.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import edu.fsu.cs.goodtiming.R;

public class AddNewTask extends BottomSheetDialogFragment {

    // we call the database because here we will record when the user saves a task and gets into the database.

    public static final String TAG = "AddNewTask";

    private EditText mEditText;
    private Button mSaveButton;

    private DataBaseHelper myDB;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.edittext);
        mSaveButton = view.findViewById(R.id.button_save);

        myDB = new DataBaseHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);

            if (task.length() > 0){
                mSaveButton.setEnabled(false);
            }
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSaveButton.setEnabled(false);

                }else{
                    mSaveButton.setEnabled(true);
                    //mSaveButton.setBackgroundColor(getResources().getColor(R.color.design_default_color_on_primary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // setting up the save button to update the database and save the tasks the user made to display them later on.
        boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEditText.getText().toString();

                if (finalIsUpdate){
                    myDB.updateTask(bundle.getInt("id"), text);
                }else{
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    myDB.insertTask(item);
                }
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
