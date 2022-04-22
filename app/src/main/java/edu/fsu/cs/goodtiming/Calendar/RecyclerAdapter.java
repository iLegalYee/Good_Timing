package edu.fsu.cs.goodtiming.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
import edu.fsu.cs.goodtiming.R;

public class RecyclerAdapter
        extends RecyclerView.Adapter<ExamViewHolder> {

    List<ExamData> list = Collections.emptyList();

    Context context;
    ClickListener listiner;

    public RecyclerAdapter(List<ExamData> list,
                                Context context,ClickListener listiner)
    {
        this.list = list;
        this.context = context;
        this.listiner = listiner;
    }

    // Initializes the viewholder
    @Override
    public ExamViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View photoView = inflater.inflate(R.layout.exam_card, parent, false);

        ExamViewHolder viewHolder = new ExamViewHolder(photoView);
        return viewHolder;
    }

    // Messes with viewholder values so that the specified names and messages are shown in
    // the Recycler View
    @Override
    public void
    onBindViewHolder(final ExamViewHolder viewHolder,
                     final int position)
    {
        final int index = viewHolder.getAdapterPosition();
        final int id = list.get(position).id;
        final String calendar = list.get(position).calendar;
        viewHolder.examName.setText(list.get(position).name);
        viewHolder.examDate.setText(list.get(position).date);
        viewHolder.examMessage.setText(list.get(position).message);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listiner.click(index, id, calendar);
            }
        });
    }

    // Get number of items shown
    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}