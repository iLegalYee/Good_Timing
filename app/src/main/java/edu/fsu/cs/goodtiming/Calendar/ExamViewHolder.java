package edu.fsu.cs.goodtiming.Calendar;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import edu.fsu.cs.goodtiming.R;

// Holds the information for each selection in the recycler view shown in the show day fragment
// Accessed by the RecyclerAdapter
public class ExamViewHolder
        extends RecyclerView.ViewHolder {
    TextView examName;
    TextView examMessage;
    TextView examDate;
    View view;

    ExamViewHolder(View itemView)
    {
        super(itemView);
        examName
                = (TextView)itemView
                .findViewById(R.id.examName);
        examDate
                = (TextView)itemView
                .findViewById(R.id.examDate);
        examMessage
                = (TextView)itemView
                .findViewById(R.id.examMessage);
        view  = itemView;
    }
}